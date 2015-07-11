package net.xylophones.fotilo;

import net.xylophones.fotilo.io.CameraConnection;

/**
 */
public class CameraConnectionTest {

    public static void main(String[] args) throws Exception {
        CameraConnection connection = new CameraConnection("192.168.1.6", 443, "admin", "admin123");
        connection.y();
    }
}
