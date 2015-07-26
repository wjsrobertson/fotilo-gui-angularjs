package net.xylophones.fotilo.cli;

import net.xylophones.fotilo.common.Direction;
import net.xylophones.fotilo.common.Rotation;

public class CommandLineOptions {

    private boolean help;
    private boolean interactive;
    private String file;
    private Direction direction;
    private Integer time;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String image;
    private Integer speed;
    private Integer brightness;
    private Integer contrast;
    private String resolution;
    private Rotation flipRotation;
    private Integer store;
    private Integer goTo;
    private Integer fps;

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
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

    public Rotation getFlipRotation() {
        return flipRotation;
    }

    public void setFlipRotation(Rotation flipRotation) {
        this.flipRotation = flipRotation;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getGoTo() {
        return goTo;
    }

    public void setGoTo(Integer goTo) {
        this.goTo = goTo;
    }

    public Integer getFps() {
        return fps;
    }

    public void setFps(Integer fps) {
        this.fps = fps;
    }
}
