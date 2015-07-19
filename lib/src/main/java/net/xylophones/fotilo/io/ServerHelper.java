package net.xylophones.fotilo.io;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

/**
 */
public class ServerHelper {

    public static SSLServerSocketFactory getSslServerSocketFactory(int port) throws KeyStoreException, IOException,
            NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {

        SSLServerSocketFactory sf = null;
        if (port == 8443) {
            // Initialize SSL context
            ClassLoader classLoader = ElementalHttpServer.class.getClassLoader();
            URL url = classLoader.getResource("my.keystore");
            if (url == null) {
                System.out.println("Keystore not found");
                System.exit(1);
            }
            KeyStore keystore = KeyStore.getInstance("jks");
            keystore.load(url.openStream(), "secret".toCharArray());
            KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
            kmfactory.init(keystore, "secret".toCharArray());
            KeyManager[] keymanagers = kmfactory.getKeyManagers();
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(keymanagers, null, null);
            sf = sslcontext.getServerSocketFactory();
        }
        return sf;
    }

}
