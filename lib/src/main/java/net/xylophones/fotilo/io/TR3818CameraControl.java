package net.xylophones.fotilo.io;

import net.xylophones.fotilo.CameraControl;
import net.xylophones.fotilo.common.*;
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
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static net.xylophones.fotilo.InputValidationUtil.checkWithinRange;
import static org.apache.http.client.utils.HttpClientUtils.closeQuietly;

public class TR3818CameraControl implements CameraControl, AutoCloseable {

    private static Map<Direction, Integer> DIRECTION_COMMANDS = new HashMap<>();

    static {
        DIRECTION_COMMANDS.put(Direction.UP, 0);
        DIRECTION_COMMANDS.put(Direction.UP_LEFT, 92);
        DIRECTION_COMMANDS.put(Direction.UP_RIGHT, 91);
        DIRECTION_COMMANDS.put(Direction.LEFT, 4);
        DIRECTION_COMMANDS.put(Direction.DOWN, 2);
        DIRECTION_COMMANDS.put(Direction.DOWN_RIGHT, 93);
        DIRECTION_COMMANDS.put(Direction.DOWN_LEFT, 92);
        DIRECTION_COMMANDS.put(Direction.RIGHT, 6);
    }

    private static final int STATUS_CODE_SUCCESS = 200;

    private static final String COMMAND_SPEED = "ptz_patrol_rate";
    private static final int COMMAND_STOP = 1;

    private static final int PARAM_RESOLUTION = 0;
    private static final int PARAM_BRIGHTNESS = 1;
    private static final int PARAM_SET_CONTRAST = 2;
    private static final int PARAM_ORIENTATION = 5;
    private static final int PARAM_FRAME_RATE = 6;
    private static final int PARAM_INFRA_RED = 14;

    private static final int COMMAND_SET_LOCATION_OFFSET = 29;
    private static final int COMMAND_GOTO_LOCATION_OFFSET = 30;

    private static final int ORIENATION_NORMAL = 0;
    private static final int ORIENATION_FLIP = 1;
    private static final int ORIENATION_MIRROR = 2;
    private static final int ORIENATION_FLIP_AND_MIRROR = 3;

    private final CloseableHttpClient httpclient;
    private final String host;
    private final int port;
    private final String user;
    private final String pass;

    private TR3818DefinitionProvider definitionProvider = new TR3818DefinitionProvider();

    private TR3818SettingsPageParser settingsPageParser = new TR3818SettingsPageParser();

    private final CameraDefinition cameraDefinition;

    public TR3818CameraControl(CameraInfo cameraInfo) {
        this(cameraInfo.getHost(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPassword());
    }

    // TODO - get rid of this constructor
    public TR3818CameraControl(String host, int port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(user, pass));

        httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();

        cameraDefinition = definitionProvider.getCameraDefinition();
    }

    public boolean move(Direction direction) throws IOException {
        Integer command = DIRECTION_COMMANDS.get(direction);
        return executeDecoderCommand(command);
    }

    @Override
    public void move(Direction direction, int duration) throws IOException {
        throw new UnsupportedOperationException();
    }

    public boolean stopMovement() throws IOException {
        return executeDecoderCommand(COMMAND_STOP);
    }

    // TODO - change to stream instead of CloseableHttpResponse
    public CloseableHttpResponse getVideoStream() throws IOException {
        String uri = "http://" + host + ":" + port + "/videostream.cgi?loginuse=" + user + "&loginpas=" + pass;
        HttpGet httpget = new HttpGet(uri);

        return httpclient.execute(httpget);
    }

    @Override
    public void setPanTiltSpeed(int speed) throws IOException {
        int min = cameraDefinition.getPanTiltSpeedRange().getMin();
        int max = cameraDefinition.getPanTiltSpeedRange().getMax();
        checkWithinRange("speed not within range", speed, min, max);
        setMiscSetting(COMMAND_SPEED, speed);
    }

    @Override
    public void setBrightness(int brightness) throws IOException {
        int min = cameraDefinition.getBrightnessRange().getMin();
        int max = cameraDefinition.getBrightnessRange().getMax();
        checkWithinRange("brightness not within range", brightness, min, max);
        setCameraControlParam(PARAM_BRIGHTNESS, brightness);
    }

    @Override
    public void setContrast(int contrast) throws IOException {
        int min = cameraDefinition.getContrastRange().getMin();
        int max = cameraDefinition.getContrastRange().getMax();
        checkWithinRange("contrast not within range", contrast, min, max);
        setCameraControlParam(PARAM_SET_CONTRAST, contrast);
    }

    @Override
    public void setResolution(String resolution) throws IOException {
        Integer resolutionValue = null;

        if ("640x480".equals(resolution)) {
            resolutionValue = 0;
        } else if ("320x240".equals(resolution)) {
            resolutionValue = 1;
        }

        if (resolutionValue != null) {
            setCameraControlParam(PARAM_RESOLUTION, resolutionValue);
        } else {
            throw new IllegalArgumentException(resolution + " is an invalid resolution");
        }
    }

    @Override
    public void flip(Rotation rotation) throws IOException {
        // PARAM_ROTATION
    }

    @Override
    public void storeLocation(int location) throws IOException {
        int min = cameraDefinition.getLocationRange().getMin();
        int max = cameraDefinition.getLocationRange().getMax();
        checkWithinRange("location not within range", location, min, max);
        int locationCommand = ((2*location) - 1) + COMMAND_SET_LOCATION_OFFSET;
        executeDecoderCommand(locationCommand);
    }

    @Override
    public void gotoLocation(int location) throws IOException {
        int min = cameraDefinition.getLocationRange().getMin();
        int max = cameraDefinition.getLocationRange().getMax();
        checkWithinRange("location not within range", location, min, max);
        int locationCommand = ((2*location) - 1) + COMMAND_GOTO_LOCATION_OFFSET;
        executeDecoderCommand(locationCommand);
    }

    @Override
    public void setFrameRate(int fps) throws IOException {
        int min = cameraDefinition.getFrameRateRange().getMin();
        int max = cameraDefinition.getFrameRateRange().getMax();
        checkWithinRange("frame rate not within range", fps, min, max);
        setCameraControlParam(PARAM_FRAME_RATE, fps);
    }

    @Override
    public void setInfraRedLightOn(boolean on) throws IOException {
        int irValue = on ? 1 : 0;
        setCameraControlParam(PARAM_INFRA_RED, irValue);
    }

    @Override
    public void oritentation(Orientation orientation) throws IOException {
        if (Orientation.FLIP == orientation) {
            setCameraControlParam(PARAM_ORIENTATION, ORIENATION_FLIP);
        } else if (Orientation.FLIP_AND_MIRROR == orientation) {
            setCameraControlParam(PARAM_ORIENTATION, ORIENATION_NORMAL);
        } else if (Orientation.MIRROR == orientation) {
            setCameraControlParam(PARAM_ORIENTATION, ORIENATION_MIRROR);
        } else if (Orientation.NORMAL == orientation) {
            setCameraControlParam(PARAM_ORIENTATION, ORIENATION_FLIP_AND_MIRROR);
        }
    }

    @Override
    public CameraSettings getCameraSettings() throws IOException {
        throw new UnsupportedOperationException();
    }

    public void saveSnapshot(Path path) throws IOException {
        String uri = "http://" + host + ":" + port + "/snapshot.cgi?user=" + user + "&pwd=" + pass;
        HttpGet httpget = new HttpGet(uri);

        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            if (response.getStatusLine().getStatusCode() == STATUS_CODE_SUCCESS) {
                FileOutputStream fos = new FileOutputStream(path.toFile());
                IOUtils.copy(response.getEntity().getContent(), fos);
            } else {
                // throw exception with error code
            }
        } finally {
            response.close();
        }
    }


    private boolean executeDecoderCommand(int command) throws IOException {
        String url = createDecoderControlCommandUrl(command);
        return sendGetRequest(url);
    }

    private String createDecoderControlCommandUrl(int command) {
        return "http://" + host + ":" + port + "/decoder_control.cgi?loginuse=" + user + "&loginpas=" + pass + "&command=" + command + "&onestep=0";
    }

    private boolean setCameraControlParam(int param, int value) throws IOException {
        String url = createCameraControlCommandUrl(param, value);
        return sendGetRequest(url);
    }

    private String createCameraControlCommandUrl(int param, int value) {
        return "http://" + host + ":" + port + "/camera_control.cgi?loginuse=" + user + "&loginpas=" + pass + "&param=" + param + "&value=" + value;
    }

    private boolean setMiscSetting(String setting, int value) throws IOException {
        String url = createSetMiscUrl(setting, value);
        return sendGetRequest(url);
    }

    private String createSetMiscUrl(String setting, int value) {
        return "http://" + host + ":" + port + "/set_misc.cgi?loginuse=" + user + "&loginpas=" + pass + "&" + setting + "=" + value;
    }

    private String createSettingsPageUrl() {
        return "http://" + host + ":" + port + "/get_camera_params.cgi?loginuse=" + user + "&loginpas=" + pass;
    }

    private boolean sendGetRequest(String url) throws IOException {
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpget);
        closeQuietly(response);
        return response.getStatusLine().getStatusCode() == STATUS_CODE_SUCCESS;
    }


    @Override
    public void close() {
        closeQuietly(httpclient);
    }

    public CameraDefinition getCameraDefinition() {
        return definitionProvider.getCameraDefinition();
    }

    @Override
    public CameraOverview getCameraOverview() throws IOException {
        String settingsPageUrl = createSettingsPageUrl();
        HttpGet httpget = new HttpGet(settingsPageUrl);
        CloseableHttpResponse response = httpclient.execute(httpget);
        CameraSettings cameraSettings = null;
        try {
            InputStream responseStream = response.getEntity().getContent();
            cameraSettings = settingsPageParser.parseFromPage(responseStream);
        } finally {
            closeQuietly(response);
        }

        CameraOverview cameraOverview = new CameraOverview();

        cameraOverview.setDefinition(getCameraDefinition());
        cameraOverview.setSettings(cameraSettings);

        return cameraOverview;
    }
}
