package net.xylophones.fotilo.io;

import net.xylophones.fotilo.common.CameraDefinition;
import net.xylophones.fotilo.common.SettingsRange;

import java.util.ArrayList;
import java.util.List;

public class TR3818DefinitionProvider implements CameraDefinitionProvider {

    @Override
    public CameraDefinition getCameraDefinition() {
        // TODO - range values should be set from constants
        CameraDefinition cameraDefinition = new CameraDefinition();

        cameraDefinition.setCameraType("TR3818");
        cameraDefinition.setCameraManufacturer("Tenvis");
        cameraDefinition.setBrightnessRange(new SettingsRange(-128, 128));
        cameraDefinition.setContrastRange(new SettingsRange(1, 254));
        cameraDefinition.setFrameRateRange(new SettingsRange(1, 30));
        cameraDefinition.setLocationRange(new SettingsRange(1, 8));
        cameraDefinition.setPanTiltSpeedRange(new SettingsRange(1, 10));
        cameraDefinition.setSupportsInfraRedCut(true);

        List<String> resolutions = new ArrayList<>();
        resolutions.add("320x240");
        resolutions.add("640x480");
        cameraDefinition.setSupportedResolutions(resolutions);


        return cameraDefinition;
    }
}
