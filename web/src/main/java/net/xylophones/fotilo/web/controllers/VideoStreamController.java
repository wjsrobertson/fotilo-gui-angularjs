package net.xylophones.fotilo.web.controllers;

import net.xylophones.fotilo.CameraControl;
import net.xylophones.fotilo.io.TR3818CameraControl;
import net.xylophones.fotilo.web.CameraConnectionFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.http.client.utils.HttpClientUtils.closeQuietly;

@Controller
public class VideoStreamController {

    private static final String CONTENT_TYPE = "Content-Type";

    @Autowired
    private CameraConnectionFactory cameraConnectionFactory;

    @RequestMapping("/stream")
    public void streamVideo(@RequestParam("camera") String cameraId, OutputStream outputStream, HttpServletResponse response) throws IOException {
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
}
