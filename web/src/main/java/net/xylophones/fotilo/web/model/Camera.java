package net.xylophones.fotilo.web.model;

import java.io.Serializable;

public class Camera implements Serializable {

    private String name;

    public Camera(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
