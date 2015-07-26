package net.xylophones.fotilo.common;

import static org.apache.http.util.TextUtils.isEmpty;

public enum Direction {

    UP,
    UP_LEFT,
    UP_RIGHT,
    LEFT,
    DOWN,
    DOWN_RIGHT,
    DOWN_LEFT,
    RIGHT;

    public static Direction fromString(String string) {
        if (isEmpty(string)) {
            return null;
        }

        boolean hasLeft = string.toLowerCase().matches(".*left.*");
        boolean hasUp = string.toLowerCase().matches(".*up.*");
        boolean hasDown = string.toLowerCase().matches(".*down.*");
        boolean hasRight = string.toLowerCase().matches(".*right.*");

        if (hasLeft) {
            if (hasUp) {
                return UP_LEFT;
            } else if (hasDown) {
                return DOWN_LEFT;
            } else {
                return LEFT;
            }
        } else if (hasRight) {
            if (hasUp) {
                return UP_RIGHT;
            } else if (hasDown) {
                return DOWN_RIGHT;
            } else {
                return RIGHT;
            }
        } else if (hasUp) {
            return UP;
        } else if (hasDown) {
            return DOWN;
        }

        return null;
    }
}
