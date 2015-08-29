package net.xylophones.fotilo.io;

import net.xylophones.fotilo.common.CameraDefinition;
import net.xylophones.fotilo.common.OrientationControlType;
import net.xylophones.fotilo.common.SettingsRange;

import java.util.ArrayList;
import java.util.List;

public class JPT3815W2DefinitionProvider implements CameraDefinitionProvider {

    @Override
    public CameraDefinition getCameraDefinition() {
        // TODO - range values should be set from constants
        CameraDefinition cameraDefinition = new CameraDefinition();

        cameraDefinition.setCameraType("JPT3815W2");
        cameraDefinition.setCameraManufacturer("Tenvis");
        cameraDefinition.setBrightnessRange(new SettingsRange(0, 255));
        cameraDefinition.setContrastRange(new SettingsRange(0, 5));
        cameraDefinition.setFrameRateRange(new SettingsRange(1, 30));
        cameraDefinition.setLocationRange(new SettingsRange(1, 10));
        cameraDefinition.setPanTiltSpeedRange(new SettingsRange(0, 10));
        cameraDefinition.setSupportsInfraRedCut(false);
        cameraDefinition.setOrientationControlType(OrientationControlType.VERTICAL_AND_HORIZONTAL_FLIP);

        List<String> resolutions = new ArrayList<>();
        resolutions.add("160x120");
        resolutions.add("320x240");
        resolutions.add("640x480");
        cameraDefinition.setSupportedResolutions(resolutions);


        return cameraDefinition;
    }
}
