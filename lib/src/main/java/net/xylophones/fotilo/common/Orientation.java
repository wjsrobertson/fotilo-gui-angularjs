package net.xylophones.fotilo.common;

import static org.apache.http.util.TextUtils.isEmpty;

public enum Orientation {

    NORMAL,
    FLIP,
    MIRROR,
    FLIP_AND_MIRROR;

    public static Orientation fromString(String orientation) {
        if (isEmpty(orientation)) {
            return null;
        }

        orientation = orientation.toLowerCase();

        if (orientation.startsWith("normal")) {
            return NORMAL;
        } else if (orientation.contains("flip") && ! orientation.contains("mirror")) {
            return FLIP;
        } else if (orientation.contains("mirror") && ! orientation.contains("flip")) {
            return MIRROR;
        } else if (orientation.contains("mirror") && orientation.contains("flip")) {
            return FLIP_AND_MIRROR;
        }

        return null;
    }

}
