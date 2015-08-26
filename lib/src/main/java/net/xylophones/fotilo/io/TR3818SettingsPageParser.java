package net.xylophones.fotilo.io;

import net.xylophones.fotilo.common.CameraSettings;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TR3818SettingsPageParser {

    private static final String FRAMERATE_KEY = "enc_framerate";
    private static final String BRIGHTNESS_KEY = "vbright";
    private static final String CONTRAST_KEY = "vcontrast";
    private static final String PAN_TILT_SPEED_KEY = "speed";
    private static final String RESOLUTION_KEY = "resolution";
    private static final String INFRA_RED_CUT_KEY = "ircut";

    private static final Integer IR_CUT_ON = 1;

    public CameraSettings parseFromPage(InputStream page) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(page, StandardCharsets.UTF_8));
        List<String> lines = getConfigPageLines(reader);

        Map<String, Integer> settingsMap = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split("=");
            if (parts.length == 2) {
                String key = stripPrefix(parts[0]);
                String value = stripSuffix(parts[1]);
                if (value.matches("-*\\d+")) {
                    settingsMap.put(key, Integer.valueOf(value));
                }
            }
        }

        CameraSettings settings = new CameraSettings();
        settings.setBrightness(settingsMap.get(BRIGHTNESS_KEY));
        settings.setContrast(settingsMap.get(CONTRAST_KEY));
        settings.setFrameRate(settingsMap.get(FRAMERATE_KEY));
        settings.setPanTiltSpeed(settingsMap.get(PAN_TILT_SPEED_KEY));
        settings.setInfrRedCutEnabled(IR_CUT_ON.equals(settingsMap.get(INFRA_RED_CUT_KEY)));

        String resolution = convertResolutionToString(settingsMap.get(RESOLUTION_KEY));
        settings.setResolution(resolution);

        return settings;
    }

    private String stripSuffix(String part) {
        return part.substring(0, part.length() - 1);
    }

    private String stripPrefix(String part) {
        return part.substring(4);
    }

    private List<String> getConfigPageLines(BufferedReader reader) throws IOException {
        return IOUtils.readLines(reader);
    }

    private String convertResolutionToString(Integer resolutionId) {
        String resolution = null;

        if (resolutionId == 0) {
            resolution = "640x480";
        } else if (resolutionId == 1) {
            resolution = "320x240";
        }

        return resolution;
    }
}
