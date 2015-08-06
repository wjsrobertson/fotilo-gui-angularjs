package net.xylophones.fotilo.web.model;

import javafx.scene.Camera;

import java.io.Serializable;
import java.util.List;

public class Cameras implements Serializable {

        private List<Camera> cameras;

        public List<Camera> getCameras () {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }
}
