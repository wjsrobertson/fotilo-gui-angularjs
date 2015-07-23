package net.xylophones.fotilo.io;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

import static net.xylophones.fotilo.io.ServerHelper.getSslServerSocketFactory;

/**
 * Basic, yet fully functional and spec compliant, HTTP/1.1 file server.
 */
public class ElementalHttpServer {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Please specify document root directory");
            System.exit(1);
        }

        String docRoot = args[0];
        int port = 8080;

        HttpProcessor httpproc = HttpProcessorBuilder.create()
                .add(new ResponseDate())
                .add(new ResponseServer("Test/1.1"))
                .add(new ResponseContent())
                .add(new ResponseConnControl()).build();

        UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
        reqistry.register("*", new HttpFileHandler(docRoot));

        HttpService httpService = new HttpService(httpproc, reqistry);

        SSLServerSocketFactory sf = getSslServerSocketFactory(port);

        Thread t = new RequestListenerThread(port, httpService, sf);
        t.setDaemon(false);
        t.start();
    }

    static class HttpFileHandler implements HttpRequestHandler {
        private final String docRoot;

        public HttpFileHandler(String docRoot) {
            this.docRoot = docRoot;
        }

        public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context)
                throws HttpException, IOException {

            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
            if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
                throw new MethodNotSupportedException(method + " method not supported");
            }
            String target = request.getRequestLine().getUri();

            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                byte[] entityContent = EntityUtils.toByteArray(entity);
                System.out.println("Incoming entity content (bytes): " + entityContent.length);
            }

            // InputStreamEntity

            TR3818CameraControl connection = new TR3818CameraControl("192.168.1.6", 443, "admin", "admin123");
            CloseableHttpResponse cameraResponse = connection.getVideoStream();

            response.setStatusCode(HttpStatus.SC_OK);
            //response.addHeader(cameraResponse.getFirstHeader("Content-Type"));
            //response.addHeader(cameraResponse.getFirstHeader("Connection"));

            String contentTypeValue = cameraResponse.getFirstHeader("Content-Type").getValue();
            ContentType contentType = ContentType.parse(contentTypeValue);
            InputStreamEntity responseEntity = new InputStreamEntity(cameraResponse.getEntity().getContent(), contentType);
            response.setEntity(responseEntity);
        }

    }

    /**
     * Socket listening loop
     */
    static class RequestListenerThread extends Thread {
        private final HttpConnectionFactory<DefaultBHttpServerConnection> connFactory;
        private final ServerSocket serversocket;
        private final HttpService httpService;

        public RequestListenerThread(int port, HttpService httpService, SSLServerSocketFactory sf)
                throws IOException {
            this.connFactory = DefaultBHttpServerConnectionFactory.INSTANCE;
            this.serversocket = sf != null ? sf.createServerSocket(port) : new ServerSocket(port);
            this.httpService = httpService;
        }

        @Override
        public void run() {
            System.out.println("Listening on port " + this.serversocket.getLocalPort());
            while (!Thread.interrupted()) {
                try {
                    Socket socket = this.serversocket.accept();
                    System.out.println("Incoming connection from " + socket.getInetAddress());
                    HttpServerConnection conn = this.connFactory.createConnection(socket);

                    Thread t = new WorkerThread(this.httpService, conn);
                    t.setDaemon(true);
                    t.start();
                } catch (InterruptedIOException ex) {
                    break;
                } catch (IOException e) {
                    System.err.println("I/O error initialising connection thread: "
                            + e.getMessage());
                    break;
                }
            }
        }
    }

    static class WorkerThread extends Thread {
        private final HttpService httpservice;
        private final HttpServerConnection conn;

        public WorkerThread(HttpService httpservice, HttpServerConnection conn) {
            this.httpservice = httpservice;
            this.conn = conn;
        }

        @Override
        public void run() {
            HttpContext context = new BasicHttpContext(null);
            try {
                while (!Thread.interrupted() && this.conn.isOpen()) {
                    this.httpservice.handleRequest(this.conn, context);
                }
            } catch (ConnectionClosedException ex) {
                System.err.println("Client closed connection");
            } catch (IOException ex) {
                System.err.println("I/O error: " + ex.getMessage());
            } catch (HttpException ex) {
                System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
            } finally {
                try {
                    this.conn.shutdown();
                } catch (IOException ignore) {
                }
            }
        }

    }

}