package net.xylophones.fotilo.common;

public class CameraSettings {

    private Integer frameRate;

    private Integer brightness;

    private Integer contrast;

    private String resolution;

    private Integer panTiltSpeed;

    private Boolean infrRedCutEnabled;

    public Integer getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(Integer frameRate) {
        this.frameRate = frameRate;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    public Integer getContrast() {
        return contrast;
    }

    public void setContrast(Integer contrast) {
        this.contrast = contrast;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Integer getPanTiltSpeed() {
        return panTiltSpeed;
    }

    public void setPanTiltSpeed(Integer panTiltSpeed) {
        this.panTiltSpeed = panTiltSpeed;
    }

    public Boolean getInfrRedCutEnabled() {
        return infrRedCutEnabled;
    }

    public void setInfrRedCutEnabled(Boolean infrRedCutEnabled) {
        this.infrRedCutEnabled = infrRedCutEnabled;
    }
}
