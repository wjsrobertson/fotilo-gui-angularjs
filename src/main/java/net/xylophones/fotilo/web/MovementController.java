package net.xylophones.fotilo.web;

import net.xylophones.fotilo.common.Direction;
import net.xylophones.fotilo.io.CameraConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.Writer;

@Controller
public class MovementController {

    @Autowired
    private CameraConnectionFactory cameraConnectionFactory;

    @RequestMapping("/move")
    public void move(@RequestParam("camera") String cameraId, @RequestParam("direction") String direction, Writer writer) throws IOException {
        Direction moveInDirection = Direction.fromString(direction);
        cameraConnectionFactory.getCameraConnection(cameraId).move(moveInDirection);
        writer.append("OK");
    }

    @RequestMapping("/stop")
    public void stop(@RequestParam("camera") String cameraId, Writer writer) throws IOException {
        cameraConnectionFactory.getCameraConnection(cameraId).stopMovement();
        writer.append("OK");
    }

}
