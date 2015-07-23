package net.xylophones.fotilo;

import net.xylophones.fotilo.io.TR3818CameraControl;

import java.nio.file.Paths;

/**
 */
public class CameraConnectionTest {

    public static void main(String[] args) throws Exception {
        TR3818CameraControl connection = new TR3818CameraControl("192.168.1.6", 443, "admin", "admin123");
        connection.saveSnapshot(Paths.get("/tmp/out.jpg"));
    }

}
