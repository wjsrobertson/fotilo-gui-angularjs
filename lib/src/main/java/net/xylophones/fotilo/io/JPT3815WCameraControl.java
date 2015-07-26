package net.xylophones.fotilo.io;

import net.xylophones.fotilo.CameraControl;
import net.xylophones.fotilo.common.CameraInfo;
import net.xylophones.fotilo.common.Direction;
import net.xylophones.fotilo.common.Rotation;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static net.xylophones.fotilo.InputValidationUtil.checkWithinRange;
import static org.apache.http.client.utils.HttpClientUtils.closeQuietly;

public class JPT3815WCameraControl implements CameraControl, AutoCloseable {

    private static final String SNAPSHOT_URL = "http://%s:%s/cgi-bin/snapshot.cgi";

    private static final String ACTION_CONTROL_URL = "http://%s:%s/cgi-bin/control.cgi?action=cmd&code=%s&value=%s";

    private static final String MJPEG_URL = "http://%s:%s/vjpeg.v";

    private static Map<Direction, Integer> DIRECTION_VALUES = new HashMap<>();

    static {
        DIRECTION_VALUES.put(Direction.UP, 1);
        DIRECTION_VALUES.put(Direction.UP_LEFT, 6);
        DIRECTION_VALUES.put(Direction.UP_RIGHT, 5);
        DIRECTION_VALUES.put(Direction.LEFT, 4);
        DIRECTION_VALUES.put(Direction.DOWN, 2);
        DIRECTION_VALUES.put(Direction.DOWN_RIGHT, 7);
        DIRECTION_VALUES.put(Direction.DOWN_LEFT, 8);
        DIRECTION_VALUES.put(Direction.RIGHT, 3);
    }

    private static final int COMMAND_SPEED = 1;
    private static final int COMMAND_START = 2;
    private static final int COMMAND_STOP = 3;
    private static final int COMMAND_SET_CONTRAST = 5;
    private static final int COMMAND_SET_BRIGHTNESS = 6;
    private static final int COMMAND_RESOLUTION = 7;
    private static final int COMMAND_FRAME_RATE = 8;
    private static final int COMMAND_ROTATION = 9;
    private static final int COMMAND_SET_LOCATION = 11;
    private static final int COMMAND_GOTO_LOCATION = 13;

    private static final int VALUE_ROTATION_VERTICAL = 23;
    private static final int VALUE_ROTATION_HORIZONTAL = 13;

    private static final int STATUS_CODE_SUCCESS = 200;

    private CameraInfo cameraInfo;

    private Lock movementLock = new ReentrantLock();

    // guarded by movementLock
    private Direction lastDirection;

    private final CloseableHttpClient httpclient;

    public JPT3815WCameraControl(CameraInfo cameraInfo) {
        this.cameraInfo = cameraInfo;

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(cameraInfo.getHost(), cameraInfo.getPort()),
                new UsernamePasswordCredentials(cameraInfo.getUsername(), cameraInfo.getPassword())
        );

        httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
    }

    public boolean move(Direction direction) throws IOException {
        movementLock.lock();
        try {
            Integer directionValue = DIRECTION_VALUES.get(direction);
            lastDirection = direction;
            return executeCommand(COMMAND_START, directionValue);
        } finally {
            movementLock.unlock();
        }
    }

    public boolean stopMovement() throws IOException {
        movementLock.lock();
        try {
            Integer directionValue = DIRECTION_VALUES.get(lastDirection);
            return executeCommand(COMMAND_STOP, directionValue);
        } finally {
            movementLock.unlock();
        }
    }

    // TODO - change to InputStream instead of CloseableHttpResponse
    public CloseableHttpResponse getVideoStream() throws IOException {
        String mJpegUrl = String.format(MJPEG_URL, cameraInfo.getHost(), cameraInfo.getPort());
        HttpGet httpGet = new HttpGet(mJpegUrl);

        return httpclient.execute(httpGet);
    }

    @Override
    public void setPanTiltSpeed(int speed) throws IOException {
        checkWithinRange("speed not within range", speed, 1, 10);
        executeCommand(COMMAND_SPEED, speed);
    }

    @Override
    public void setBrightness(int brightness) throws IOException {
        checkWithinRange("brightness not within range", brightness, 0, 255);
        executeCommand(COMMAND_SET_BRIGHTNESS, brightness);
    }

    @Override
    public void setContrast(int contrast) throws IOException {
        checkWithinRange("contrast not within range", contrast, 0, 5);
        executeCommand(COMMAND_SET_CONTRAST, contrast);
    }

    @Override
    public void setResolution(String resolution) throws IOException {
        int resolutionValue = 10485880; // "160x120"
        switch (resolution) {
            case "320x240":
                resolutionValue = 20971760;
                break;
            case "640x480":
                resolutionValue = 41943520;
                break;
        }

        executeCommand(COMMAND_RESOLUTION, resolutionValue);
    }

    @Override
    public void flip(Rotation rotation) throws IOException {
        if (Rotation.HORIZONTAL.equals(rotation)) {
            executeCommand(COMMAND_ROTATION, VALUE_ROTATION_VERTICAL);
        } else if (Rotation.HORIZONTAL.equals(rotation)) {
            executeCommand(COMMAND_ROTATION, VALUE_ROTATION_HORIZONTAL);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void storePreset(int location) throws IOException {
        checkWithinRange("location not within range", location, 1, 16);
        executeCommand(COMMAND_SET_LOCATION, location);
    }

    @Override
    public void gotoPreset(int location) throws IOException {
        checkWithinRange("frame rate not within range", location, 1, 10);
        executeCommand(COMMAND_GOTO_LOCATION, location);
    }

    @Override
    public void setFrameRate(int fps) throws IOException {
        checkWithinRange("frame rate not within range", fps, 1, 30);
        executeCommand(COMMAND_FRAME_RATE, fps);
    }

    @Override
    public void saveSnapshot(Path path) throws IOException {
        String snapshotUrl = String.format(SNAPSHOT_URL, cameraInfo.getHost(), cameraInfo.getPort());
        HttpGet httpget = new HttpGet(snapshotUrl);

        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            if (response.getStatusLine().getStatusCode() == STATUS_CODE_SUCCESS) {
                FileOutputStream fos = new FileOutputStream(path.toFile());
                IOUtils.copy(response.getEntity().getContent(), fos);
            } else {
                // TODO - throw exception with error code
            }
        } finally {
            response.close();
        }
    }

    private boolean executeCommand(int command, int value) throws IOException {
        String url = createActionControlUrl(command, value);
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpget);
        closeQuietly(response);
        return response.getStatusLine().getStatusCode() == STATUS_CODE_SUCCESS;
    }

    private String createActionControlUrl(int command, int value) {
        return String.format(ACTION_CONTROL_URL,
                cameraInfo.getHost(), cameraInfo.getPort(), command, value);
    }

    @Override
    public void close() {
        closeQuietly(httpclient);
    }


}
