package net.xylophones.fotilo.io;

import net.xylophones.fotilo.common.CameraSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JPT3815WSettingsPageParser {

    private static final String FRAMERATE_SRING = "document.getElementById(\"camFPS\").value = ";
    private static final String FRAMERATE_REGEX = ".*document\\.getElementById\\(\"camFPS\"\\)\\.value = (\\d+).*";
    private static final String BRIGHTNESS_SRING = "sbar_pos('pos_brig', ";
    private static final String BRIGHTNESS_REGEX = ".*sbar_pos\\('pos_brig', (\\d+).*";
    private static final String CONTRAST_SRING = "sbar_pos('pos_cntr', ";
    private static final String CONTRAST_REGEX = "^sbar_pos\\('pos_cntr', (\\d+).*";
    private static final String PAN_TILT_SPEED_STRING = "var ptz_speed=";
    private static final String PAN_TILT_SPEED_REGEX = "var ptz_speed=(\\d+).*";
    private static final String RESOLUTION_STRING = "switch";
    private static final String RESOLUTION_REGEX = ".*switch\\((\\d+).*";

    private static final Pattern FRAMERATE_PATTERN = Pattern.compile(FRAMERATE_REGEX);
    private static final Pattern BRIGHTNESS_PATTERN = Pattern.compile(BRIGHTNESS_REGEX);
    private static final Pattern CONTRAST_PATTERN = Pattern.compile(CONTRAST_REGEX);
    private static final Pattern PAN_TILT_SPEED_PATTERN = Pattern.compile(PAN_TILT_SPEED_REGEX);
    private static final Pattern RESOLUTION_PATTERN = Pattern.compile(RESOLUTION_REGEX);

    public CameraSettings parseFromPage(InputStream page) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(page, StandardCharsets.UTF_8));

        Integer frameRate = null;
        Integer brightness = null;
        Integer contrast = null;
        String resolution = null;
        Integer panTiltSpeed = null;

        String line;
        while((line = reader.readLine()) != null) {
            if (frameRate == null && line.contains(FRAMERATE_SRING)) {
                frameRate = getIntegerFromMatch(line, FRAMERATE_PATTERN);
            } else if (brightness == null && line.contains(BRIGHTNESS_SRING)) {
                brightness = getIntegerFromMatch(line, BRIGHTNESS_PATTERN);
            } else if (contrast == null && line.contains(CONTRAST_SRING)) {
                contrast = getIntegerFromMatch(line, CONTRAST_PATTERN);
            } else if (resolution == null && line.contains(RESOLUTION_STRING)) {
                Integer resolutionId = getIntegerFromMatch(line, RESOLUTION_PATTERN);
                resolution = convertResolutionToString(resolutionId);
            } else if (panTiltSpeed == null && line.contains(PAN_TILT_SPEED_STRING)) {
                panTiltSpeed = getIntegerFromMatch(line, PAN_TILT_SPEED_PATTERN);
            }
        }

        CameraSettings settings = new CameraSettings();
        settings.setBrightness(brightness);
        settings.setContrast(contrast);
        settings.setFrameRate(frameRate);
        settings.setPanTiltSpeed(panTiltSpeed);
        settings.setResolution(resolution);

        return settings;
    }

    private Integer getIntegerFromMatch(String line, Pattern regex) {
        Integer matchingInt = null;
        Matcher matcher = regex.matcher(line);
        if (matcher.matches()) {
            String matchingString = matcher.group(1);
            matchingInt = Integer.valueOf(matchingString);
        }
        return matchingInt;
    }


    private String convertResolutionToString(Integer resolutionId) {
        String resolution = null;

        if (resolutionId == 32) {
            resolution = "640x480";
        } else if (resolutionId == 8) {
            resolution = "320x240";
        }else if (resolutionId == 2) {
            resolution = "160x120";
        }

        return resolution;
    }
}
