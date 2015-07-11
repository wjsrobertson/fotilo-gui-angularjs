package net.xylophones.fotilo.io;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class CameraConnectionFactory {

    private LoadingCache<String, CameraConnection> connections = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<String, CameraConnection>() {
                        public CameraConnection load(String key) {
                            return createCameraConnection(key);
                        }
                    }
            );

    private CameraConnection createCameraConnection(String cameraId) {
        switch (cameraId) {
            case "side":
                return new CameraConnection("192.168.1.139", 81, "admin", "");
            case "front":
                return new CameraConnection("192.168.1.6", 81, "admin", "admin123");
            case "inside":
                return new CameraConnection("192.168.1.7", 7777, "admin", "admin");
        }

        return null;
    }

    public CameraConnection getCameraConnection(String cameraId) {
        try {
            return connections.get(cameraId);
        } catch (ExecutionException e) {
            return null;
        }
    }

}
