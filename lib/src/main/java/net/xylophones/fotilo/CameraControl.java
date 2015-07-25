package net.xylophones.fotilo;

import net.xylophones.fotilo.common.Direction;
import net.xylophones.fotilo.common.Rotation;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.nio.file.Path;

public interface CameraControl {

    boolean move(Direction direction) throws IOException;

    boolean stopMovement() throws IOException;

    void saveSnapshot(Path path) throws IOException;

    CloseableHttpResponse getVideoStream() throws IOException;

    void setPanTiltSpeed(int speed) throws IOException;

    void setBrightness(int brightness) throws IOException;

    void setContrast(int contrast) throws IOException;

    void setResolution(String resolution) throws IOException;

    void flip(Rotation rotation) throws IOException;

    void storePreset(int location) throws IOException;

    void gotoPreset(int location) throws IOException;

    void setFrameRate(int fps) throws IOException;

}
