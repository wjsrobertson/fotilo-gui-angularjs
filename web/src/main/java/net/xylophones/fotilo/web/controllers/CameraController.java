package net.xylophones.fotilo.web.controllers;

import net.xylophones.fotilo.CameraControl;
import net.xylophones.fotilo.common.CameraOverview;
import net.xylophones.fotilo.common.Direction;
import net.xylophones.fotilo.web.CameraConnectionFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.http.client.utils.HttpClientUtils.closeQuietly;

@RestController
@RequestMapping(value = "/api/camera", produces = "application/json")
public class CameraController {

    private static final String CONTENT_TYPE = "Content-Type";

    @Autowired
    private CameraConnectionFactory cameraConnectionFactory;

    @RequestMapping("/{cameraId}/control/move/{direction}")
    public String move(@PathVariable("cameraId") String cameraId,
                       @PathVariable("direction") String direction,
                       @RequestParam(value = "duration", required = false) Integer duration) throws IOException {

        Direction moveInDirection = Direction.fromString(direction);

        if (duration != null) {
            cameraConnectionFactory.getCameraConnection(cameraId).move(moveInDirection, duration);
        } else {
            cameraConnectionFactory.getCameraConnection(cameraId).move(moveInDirection);
        }

        return "OK";
    }

    @RequestMapping("/{cameraId}/control/stop")
    public String stop(@PathVariable("cameraId") String cameraId) throws IOException {
        cameraConnectionFactory.getCameraConnection(cameraId).stopMovement();
        return "OK";
    }

    @RequestMapping("/{cameraId}/stream")
    public void streamVideo(OutputStream outputStream, HttpServletResponse response,
                            @PathVariable("cameraId") String cameraId) throws IOException {

        CameraControl TR3818CameraControl = cameraConnectionFactory.getCameraConnection(cameraId);
        CloseableHttpResponse cameraResponse = TR3818CameraControl.getVideoStream();

        try {
            String contentTypeValue = cameraResponse.getFirstHeader(CONTENT_TYPE).getValue();
            response.setHeader(CONTENT_TYPE, contentTypeValue);

            InputStreamEntity responseEntity = new InputStreamEntity(cameraResponse.getEntity().getContent());
            responseEntity.writeTo(outputStream);
        } finally {
            closeQuietly(cameraResponse);
        }
    }

    @RequestMapping(value = {"/{cameraId}"}, method = RequestMethod.GET)
    public CameraOverview getSettings(@PathVariable("cameraId") String cameraId) throws IOException {
        return cameraConnectionFactory.getCameraConnection(cameraId).getCameraOverview();
    }

}
