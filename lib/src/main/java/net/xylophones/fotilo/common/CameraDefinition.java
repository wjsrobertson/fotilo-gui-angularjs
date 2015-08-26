package net.xylophones.fotilo.common;

import java.util.List;

public class CameraDefinition {

    private String cameraManufacturer;

    private String cameraType;

    private SettingsRange brightnessRange;

    private SettingsRange contrastRange;

    private SettingsRange panTiltSpeedRange;

    private SettingsRange frameRateRange;

    private SettingsRange locationRange;

    private List<String> supportedResolutions;

    private Boolean supportsInfraRedCut;

    public String getCameraManufacturer() {
        return cameraManufacturer;
    }

    public void setCameraManufacturer(String cameraManufacturer) {
        this.cameraManufacturer = cameraManufacturer;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public SettingsRange getBrightnessRange() {
        return brightnessRange;
    }

    public void setBrightnessRange(SettingsRange brightnessRange) {
        this.brightnessRange = brightnessRange;
    }

    public SettingsRange getContrastRange() {
        return contrastRange;
    }

    public void setContrastRange(SettingsRange contrastRange) {
        this.contrastRange = contrastRange;
    }

    public SettingsRange getPanTiltSpeedRange() {
        return panTiltSpeedRange;
    }

    public void setPanTiltSpeedRange(SettingsRange panTiltSpeedRange) {
        this.panTiltSpeedRange = panTiltSpeedRange;
    }

    public SettingsRange getFrameRateRange() {
        return frameRateRange;
    }

    public void setFrameRateRange(SettingsRange frameRateRange) {
        this.frameRateRange = frameRateRange;
    }

    public SettingsRange getLocationRange() {
        return locationRange;
    }

    public void setLocationRange(SettingsRange locationRange) {
        this.locationRange = locationRange;
    }

    public List<String> getSupportedResolutions() {
        return supportedResolutions;
    }

    public void setSupportedResolutions(List<String> supportedResolutions) {
        this.supportedResolutions = supportedResolutions;
    }

    public Boolean getSupportsInfraRedCut() {
        return supportsInfraRedCut;
    }

    public void setSupportsInfraRedCut(Boolean supportsInfraRedCut) {
        this.supportsInfraRedCut = supportsInfraRedCut;
    }
}
