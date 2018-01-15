package net.xylophones.fotilo.web.controllers;

import net.xylophones.fotilo.web.configfile.model.CameraConfig;
import net.xylophones.fotilo.web.model.Camera;
import net.xylophones.fotilo.web.configfile.model.ConfigFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping(value = "/api/cameras", produces = "application/json")
public class CamerasController {

    @Autowired
    private ConfigFile configFile;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<Camera> listCameras() {

        List<Camera> cameras = newArrayList();
        for (CameraConfig camera : configFile.getCameras()) {
            cameras.add(new Camera(camera.getId()));
        }

        return cameras;
    }
}
