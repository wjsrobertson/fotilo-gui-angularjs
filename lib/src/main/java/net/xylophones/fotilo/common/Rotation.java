package net.xylophones.fotilo.common;

import static org.apache.http.util.TextUtils.isEmpty;

public enum Rotation {

    VERTICAL,
    HORIZONTAL;

    public static Rotation fromString(String from) {
        if (isEmpty(from)) {
            return null;
        }

        if (from.toLowerCase().contains("vert")) {
            return VERTICAL;
        } else if (from.toLowerCase().contains("hor")) {
            return HORIZONTAL;
        }

        return null;
    }

}
