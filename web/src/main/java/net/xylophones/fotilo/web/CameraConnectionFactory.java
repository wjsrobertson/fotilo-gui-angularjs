package net.xylophones.fotilo.web;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.xylophones.fotilo.io.CameraControl;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class CameraConnectionFactory {

    private LoadingCache<String, CameraControl> connections = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<String, CameraControl>() {
                        public CameraControl load(String key) {
                            return createCameraConnection(key);
                        }
                    }
            );

    private CameraControl createCameraConnection(String cameraId) {
        switch (cameraId) {
            case "side":
                return new CameraControl("192.168.1.139", 81, "admin", "");
            case "front":
                return new CameraControl("192.168.1.6", 81, "admin", "admin123");
            case "inside":
                return new CameraControl("192.168.1.7", 7777, "admin", "admin");
        }

        return null;
    }

    public CameraControl getCameraConnection(String cameraId) {
        try {
            return connections.get(cameraId);
        } catch (ExecutionException e) {
            return null;
        }
    }

}
