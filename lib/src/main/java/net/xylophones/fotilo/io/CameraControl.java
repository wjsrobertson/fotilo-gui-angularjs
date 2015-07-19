package net.xylophones.fotilo.io;

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

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.http.client.utils.HttpClientUtils.closeQuietly;

/**
 * TODO - use response handling instead
 * <p/>
 * http://hc.apache.org/httpcomponents-client-4.4.x/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java
 * <p/>
 * JPT3815W
 * TR3818
 */
public class CameraControl implements AutoCloseable {

    private static Map<Direction, Integer> DIRECTION_COMMANDS = newHashMap();

    static {
        DIRECTION_COMMANDS.put(Direction.UP, 0);
        DIRECTION_COMMANDS.put(Direction.UP_LEFT, 92);
        DIRECTION_COMMANDS.put(Direction.UP_RIGHT, 91);
        DIRECTION_COMMANDS.put(Direction.LEFT, 5);
        DIRECTION_COMMANDS.put(Direction.DOWN, 2);
        DIRECTION_COMMANDS.put(Direction.DOWN_RIGHT, 93);
        DIRECTION_COMMANDS.put(Direction.DOWN_LEFT, 92);
        DIRECTION_COMMANDS.put(Direction.RIGHT, 6);
    }

    private static final int COMMAND_STOP = 1;
    private static final int STATUS_CODE_SUCCESS = 200;

    private final CloseableHttpClient httpclient;
    private final String host;
    private final int port;
    private final String user;
    private final String pass;

    public CameraControl(CameraInfo cameraInfo) {
        this(cameraInfo.getHost(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPassword());
    }

    public CameraControl(String host, int port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials(user, pass));

        httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
    }

    public boolean move(Direction direction) throws IOException {
        Integer command = DIRECTION_COMMANDS.get(direction);
        return executeCommand(command);
    }

    public boolean stopMovement() throws IOException {
        return executeCommand(COMMAND_STOP);
    }

    // TODO - change to stream instead of CloseableHttpResponse
    public CloseableHttpResponse getVideoStream() throws IOException {
        String uri = "http://" + host + ":" + port + "/videostream.cgi?loginuse=" + user + "&loginpas=" + pass;
        HttpGet httpget = new HttpGet(uri);

        return httpclient.execute(httpget);
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


    private boolean executeCommand(int command) throws IOException {
        String url = createDecoderControlCommandUrl(command);
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpget);
        closeQuietly(response);
        return response.getStatusLine().getStatusCode() == STATUS_CODE_SUCCESS;
    }

    private String createDecoderControlCommandUrl(int command) {
        return "http://" + host + ":" + port + "/decoder_control.cgi?loginuse=" + user + "&loginpas=" + pass + "&command=" + command + "&onestep=0";
    }


    @Override
    public void close() {
        closeQuietly(httpclient);
    }
}
