package net.xylophones.fotilo.io;

import net.xylophones.fotilo.CameraControl;
import net.xylophones.fotilo.common.CameraInfo;
import net.xylophones.fotilo.common.Direction;
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

    private static final int COMMAND_START = 2;

    private static final int COMMAND_STOP = 3;

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
