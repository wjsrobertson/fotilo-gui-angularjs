package net.xylophones.fotilo.common;

import static org.apache.http.util.TextUtils.isEmpty;

public enum OnOrOff {

    ON(true),
    OFF(false);

    private final boolean isOn;

    OnOrOff(boolean isOn) {
        this.isOn = isOn;
    }

    public static OnOrOff fromString(String from) {
        if (isEmpty(from)) {
            return null;
        }

        if (from.toLowerCase().equals("on")) {
            return ON;
        } else {
            return OFF;
        }
    }

    public boolean asBoolean() {
        return isOn;
    }

}
