package net.xylophones.fotilo.web.configfile.model;

import java.util.List;

public class ConfigFile {

    private List<CameraConfig> cameras;

    public List<CameraConfig> getCameras() {
        return cameras;
    }

    public void setCameras(List<CameraConfig> cameras) {
        this.cameras = cameras;
    }
}
