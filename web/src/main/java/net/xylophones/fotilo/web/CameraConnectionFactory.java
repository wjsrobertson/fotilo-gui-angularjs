package net.xylophones.fotilo.web;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.xylophones.fotilo.CameraControl;
import net.xylophones.fotilo.io.JPT3815W2CameraControl;
import net.xylophones.fotilo.io.JPT3815WCameraControl;
import net.xylophones.fotilo.web.configfile.model.CameraConfig;
import net.xylophones.fotilo.web.configfile.model.ConfigFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Component
public class CameraConnectionFactory {

    @Autowired
    private ConfigFile configFile;

    private final Map<String, CameraConfig> availableCameras = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        for (CameraConfig cameraConfig : configFile.getCameras()) {
            availableCameras.put(cameraConfig.getId(), cameraConfig);
        }
    }

    public CameraControl getCameraConnection(String cameraId) {
        try {
            return connections.get(cameraId);
        } catch (ExecutionException e) {
            return null;
        }
    }

    private LoadingCache<String, CameraControl> connections = CacheBuilder.newBuilder()
            .build(
                    new CacheLoader<String, CameraControl>() {
                        public CameraControl load(String key) {
                            return createCameraConnection(key);
                        }
                    }
            );

    private CameraControl createCameraConnection(String cameraId) {
        CameraConfig cameraConfig = availableCameras.get(cameraId);

        if (cameraConfig != null) {
            // TODO - replace with factory
            if ("JPT3815W".equals(cameraConfig.getType())) {
                return new JPT3815WCameraControl(
                        cameraConfig.getHost(),
                        cameraConfig.getPort(),
                        cameraConfig.getUsername(),
                        cameraConfig.getPassword()
                );
            } else if ("JPT3815W2".equals(cameraConfig.getType())) {
                return new JPT3815W2CameraControl(
                        cameraConfig.getHost(),
                        cameraConfig.getPort(),
                        cameraConfig.getUsername(),
                        cameraConfig.getPassword()
                );
            }
        }

        return null;
    }
}
