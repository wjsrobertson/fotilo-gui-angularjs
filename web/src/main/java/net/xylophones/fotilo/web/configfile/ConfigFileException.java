package net.xylophones.fotilo.web.configfile;

public class ConfigFileException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConfigFileException(String message) {
        super(message);
    }

    public ConfigFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigFileException(Throwable cause) {
        super(cause);
    }
}
