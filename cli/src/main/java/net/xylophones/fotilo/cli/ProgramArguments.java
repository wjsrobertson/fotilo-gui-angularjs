package net.xylophones.fotilo.cli;

public enum ProgramArguments {

    HELP("help", "print help and exit", false),
    INTERACTIVE("interactive", "run in interactive mode", false),
    FILE("file", "config properties file with camera connection information", true),
    DIRECTION("direction", "direction to move", true),
    TIME("time", "time to move for. Used in conjunction with direction argument", true),
    HOST("host", "host/IP of camera", true),
    PORT("port", "port of camera", true),
    USERNAME("username", "username for camera authentication", true),
    PASSWORD("password", "password for camera authentication", true),
    IMAGE("image", "file to save image as JPG format", true),
    SPEED("speed", "camera movement speed [1-10]", true),
    BRIGHTNESS("brightness", "camera brightness [0-255]", true),
    CONTRAST("contrast", "camera contrast [0-5]", true),
    RESOLUTION("resolution", "camera resolution [160x120, 320x240, 640x480]", true),
    FLIP("flip", "flip camera view [vertical, horizontal]", true),
    STORE_LOCATION("store", "store camera location [1-16]", true),
    GOTO_LOCATION("goto", "store camera location [1-16]", true),
    FRAMES_PER_SECOND("fps", "set camera frame rate (frames per second) [1-30]", true);

    private final boolean hasArgument;
    private final String name;
    private final String description;

    ProgramArguments(String name, String description, boolean hasArgument) {
        this.hasArgument = hasArgument;
        this.name = name;
        this.description = description;
    }

    public boolean hasArgument() {
        return hasArgument;
    }

    public String getArgName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
