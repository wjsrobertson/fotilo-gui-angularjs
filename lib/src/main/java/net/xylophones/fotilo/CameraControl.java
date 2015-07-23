package net.xylophones.fotilo;

import net.xylophones.fotilo.common.Direction;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.nio.file.Path;

public interface CameraControl {

    boolean move(Direction direction) throws IOException;

    boolean stopMovement() throws IOException;

    void saveSnapshot(Path path) throws IOException;

    CloseableHttpResponse getVideoStream() throws IOException;

}
