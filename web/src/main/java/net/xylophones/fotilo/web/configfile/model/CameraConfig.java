package net.xylophones.fotilo.web.configfile.model;

import java.util.Objects;

public class CameraConfig {

    private String id;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof CameraConfig)) {
            return false;
        }

        CameraConfig other = (CameraConfig) o;

        return Objects.equals(getHost(), other.getHost())
                && Objects.equals(getPort(), other.getPort())
                && Objects.equals(getUsername(), other.getUsername())
                && Objects.equals(getPassword(), other.getPassword())
                && Objects.equals(getType(), other.getType())
                && Objects.equals(getId(), other.getId())
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, username, password, type, id);
    }
}
