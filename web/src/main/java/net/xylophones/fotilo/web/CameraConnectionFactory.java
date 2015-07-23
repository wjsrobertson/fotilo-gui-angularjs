package net.xylophones.fotilo.web;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.xylophones.fotilo.io.TR3818CameraControl;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class CameraConnectionFactory {

    private LoadingCache<String, TR3818CameraControl> connections = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<String, TR3818CameraControl>() {
                        public TR3818CameraControl load(String key) {
                            return createCameraConnection(key);
                        }
                    }
            );

    private TR3818CameraControl createCameraConnection(String cameraId) {
        switch (cameraId) {
            case "side":
                return new TR3818CameraControl("192.168.1.139", 81, "admin", "");
            case "front":
                return new TR3818CameraControl("192.168.1.6", 81, "admin", "admin123");
            case "inside":
                return new TR3818CameraControl("192.168.1.7", 7777, "admin", "admin");
        }

        return null;
    }

    public TR3818CameraControl getCameraConnection(String cameraId) {
        try {
            return connections.get(cameraId);
        } catch (ExecutionException e) {
            return null;
        }
    }

}
