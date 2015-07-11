package net.xylophones.fotilo.io;

import net.xylophones.fotilo.common.Direction;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static org.apache.http.client.utils.HttpClientUtils.closeQuietly;

/**
 * TODO - use response handling instead
 * <p>
 * http://hc.apache.org/httpcomponents-client-4.4.x/httpclient/examples/org/apache/http/examples/client/ClientWithResponseHandler.java
 *
 * JPT3815W
 * TR3818
 */
public class CameraConnection {

    private static Map<Direction, Integer> directionsCommands = newHashMap();

    static {
        directionsCommands.put(Direction.UP, 0);
        directionsCommands.put(Direction.UP_LEFT, 92);
        directionsCommands.put(Direction.UP_RIGHT, 91);
        directionsCommands.put(Direction.LEFT, 5);
        directionsCommands.put(Direction.DOWN, 2);
        directionsCommands.put(Direction.DOWN_RIGHT, 93);
        directionsCommands.put(Direction.DOWN_LEFT, 92);
        directionsCommands.put(Direction.RIGHT, 6);
    }

    private static final int COMMAND_STOP = 1;
    private static final int STATUS_CODE_SUCCESS = 200;

    private final CloseableHttpClient httpclient;
    private final String host;
    private final int port;
    private final String user;
    private final String pass;

    public CameraConnection(String host, int port, String user, String pass) {
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
        Integer command = directionsCommands.get(direction);
        return executeCommand(command);
    }

    public boolean stopMovement() throws IOException {
        return executeCommand(COMMAND_STOP);
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

    public CloseableHttpResponse getVideoStream() throws IOException {
        String uri = "http://" + host + ":" + port + "/videostream.cgi?loginuse=" + user + "&loginpas=" + pass;
        HttpGet httpget = new HttpGet(uri);

        return httpclient.execute(httpget);
    }

    public void y() throws IOException {
        HttpGet httpget = new HttpGet("http://192.168.1.6:81/snapshot.cgi?user=admin&pwd=admin123");

        System.out.println("Executing request " + httpget.getRequestLine());
        CloseableHttpResponse response = httpclient.execute(httpget);
        FileOutputStream fos = new FileOutputStream("/tmp/out.jpg");
        IOUtils.copy(response.getEntity().getContent(), fos);

        try {
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
        } finally {
            response.close();
        }
    }

    public void destroy() {
        closeQuietly(httpclient);
    }
}
