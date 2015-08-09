package net.xylophones.fotilo;

import net.xylophones.fotilo.common.*;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.nio.file.Path;

public interface CameraControl {

    boolean move(Direction direction) throws IOException;

    void move(Direction direction, int duration) throws IOException;

    boolean stopMovement() throws IOException;

    void saveSnapshot(Path path) throws IOException;

    CloseableHttpResponse getVideoStream() throws IOException;

    void setPanTiltSpeed(int speed) throws IOException;

    void setBrightness(int brightness) throws IOException;

    void setContrast(int contrast) throws IOException;

    void setResolution(String resolution) throws IOException;

    void flip(Rotation rotation) throws IOException;

    void storeLocation(int location) throws IOException;

    void gotoLocation(int location) throws IOException;

    void setFrameRate(int fps) throws IOException;

    CameraSettings getCameraSettings() throws IOException;

    CameraDefinition getCameraDefinition() throws IOException;

    CameraOverview getCameraOverview() throws IOException;

}
