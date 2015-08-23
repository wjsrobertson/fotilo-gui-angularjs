package net.xylophones.fotilo.web.controllers;

import net.xylophones.fotilo.web.model.Camera;
import net.xylophones.fotilo.web.configfile.model.ConfigFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/cameras", produces = "application/json")
public class CamerasController {

    @Autowired
    private ConfigFile configFile;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<Camera> listCameras() {
        return configFile.getCameras().stream().map(x -> new Camera(x.getId())).collect(Collectors.toList());
    }
}
