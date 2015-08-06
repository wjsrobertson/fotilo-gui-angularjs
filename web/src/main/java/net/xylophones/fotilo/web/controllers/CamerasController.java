package net.xylophones.fotilo.web.controllers;

import net.xylophones.fotilo.common.Direction;
import net.xylophones.fotilo.web.CameraConnectionFactory;
import net.xylophones.fotilo.web.model.Camera;
import net.xylophones.fotilo.web.model.Cameras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping(value = "/cameras", produces = "application/json")
public class CamerasController {

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<Camera> listCameras() {
        return newArrayList(new Camera("front"), new Camera("side"), new Camera("inside"));
    }

}
