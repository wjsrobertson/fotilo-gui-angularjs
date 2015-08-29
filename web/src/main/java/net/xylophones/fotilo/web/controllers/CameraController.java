package net.xylophones.fotilo.web.controllers;

import net.xylophones.fotilo.CameraControl;
import net.xylophones.fotilo.common.*;
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

    @RequestMapping(value = "/{cameraId}", method = RequestMethod.GET)
    public CameraOverview getSettings(@PathVariable("cameraId") String cameraId) throws IOException {
        return getCameraConnection(cameraId).getCameraOverview();
    }

    @RequestMapping(value = "/{cameraId}/settings/frame-rate/{fps}", method = RequestMethod.POST)
    public void setFrameRate(@PathVariable("cameraId") String cameraId, @PathVariable("fps") Integer fps) throws IOException {
        getCameraConnection(cameraId).setFrameRate(fps);
    }

    @RequestMapping(value = "/{cameraId}/settings/brightness/{brightness}", method = RequestMethod.POST)
    public void setBrightness(@PathVariable("cameraId") String cameraId, @PathVariable("brightness") Integer brightness)
            throws IOException {
        getCameraConnection(cameraId).setBrightness(brightness);
    }

    @RequestMapping(value = "/{cameraId}/settings/contrast/{contrast}", method = RequestMethod.POST)
    public void setContrast(@PathVariable("cameraId") String cameraId, @PathVariable("contrast") Integer contrast)
            throws IOException {
        getCameraConnection(cameraId).setContrast(contrast);
    }

    @RequestMapping(value = "/{cameraId}/settings/resolution/{resolution}", method = RequestMethod.POST)
    public void setResolution(@PathVariable("cameraId") String cameraId, @PathVariable("resolution") String resolution)
            throws IOException {
        getCameraConnection(cameraId).setResolution(resolution);
    }

    @RequestMapping(value = "/{cameraId}/settings/pan-tilt-speed/{panTiltSpeed}", method = RequestMethod.POST)
    public void setPanTiltSpeed(@PathVariable("cameraId") String cameraId, @PathVariable("panTiltSpeed") Integer panTiltSpeed)
            throws IOException {
        getCameraConnection(cameraId).setPanTiltSpeed(panTiltSpeed);
    }

    @RequestMapping("/{cameraId}/control/move/{direction}")
    public String move(@PathVariable("cameraId") String cameraId,
                       @PathVariable("direction") String direction,
                       @RequestParam(value = "duration", required = false) Integer duration) throws IOException {

        Direction moveInDirection = Direction.fromString(direction);

        if (duration != null) {
            getCameraConnection(cameraId).move(moveInDirection, duration);
        } else {
            getCameraConnection(cameraId).move(moveInDirection);
        }

        return "OK";
    }

    @RequestMapping("/{cameraId}/control/stop")
    public String stop(@PathVariable("cameraId") String cameraId) throws IOException {
        getCameraConnection(cameraId).stopMovement();
        return "OK";
    }

    @RequestMapping(value="/{cameraId}/settings/flip/{rotation}", method = RequestMethod.POST)
    public String flip(@PathVariable("cameraId") String cameraId, @PathVariable("rotation") String rotation) throws IOException {
        getCameraConnection(cameraId).flip(Rotation.fromString(rotation));
        return "OK";
    }

    @RequestMapping(value="/{cameraId}/settings/orientation/{orientation}", method = RequestMethod.POST)
    public String setOrientation(@PathVariable("cameraId") String cameraId, @PathVariable("orientation") String orientation) throws IOException {
        getCameraConnection(cameraId).oritentation(Orientation.fromString(orientation));
        return "OK";
    }

    @RequestMapping(value="/{cameraId}/settings/infra-red-light-on/{onOrOff}", method = RequestMethod.POST)
    public String setInfraRedLightOnOrOff(@PathVariable("cameraId") String cameraId, @PathVariable("onOrOff") String onOrOff) throws IOException {
        getCameraConnection(cameraId).setInfraRedLightOn(OnOrOff.fromString(onOrOff).asBoolean());
        return "OK";
    }

    @RequestMapping("/{cameraId}/stream")
    public void streamVideo(OutputStream outputStream, HttpServletResponse response,
                            @PathVariable("cameraId") String cameraId) throws IOException {

        CameraControl TR3818CameraControl = getCameraConnection(cameraId);
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

    private CameraControl getCameraConnection(String cameraId) {
        return cameraConnectionFactory.getCameraConnection(cameraId);
    }
}
