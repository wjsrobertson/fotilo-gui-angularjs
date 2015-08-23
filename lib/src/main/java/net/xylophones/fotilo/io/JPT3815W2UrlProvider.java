package net.xylophones.fotilo.io;

import net.xylophones.fotilo.common.CameraInfo;

public class JPT3815W2UrlProvider implements CameraUrlProvider {

    private static final String SNAPSHOT_URL = "http://%s:%s/media/?action=snapshot";

    private static final String ACTION_CONTROL_URL = "http://%s:%s/media/?action=cmd&code=%s&value=%s";

    private static final String MJPEG_URL = "http://%s:%s/media/?action=stream";

    private static final String SETTINGS_PAGE_URL = "http://%s:%s/video/livesp.asp";

    @Override
    public String getSettingsPageUrl(CameraInfo cameraInfo) {
        return String.format(SETTINGS_PAGE_URL, cameraInfo.getHost(), cameraInfo.getPort());
    }

    @Override
    public String getMjpegUrl(CameraInfo cameraInfo) {
        return String.format(MJPEG_URL, cameraInfo.getHost(), cameraInfo.getPort());
    }

    @Override
    public String createActionControlUrl(CameraInfo cameraInfo, int command, int value) {
        return String.format(ACTION_CONTROL_URL,
                cameraInfo.getHost(), cameraInfo.getPort(), command, value);
    }

    @Override
    public String getSnapshotUrl(CameraInfo cameraInfo) {
        return String.format(SNAPSHOT_URL, cameraInfo.getHost(), cameraInfo.getPort());
    }

}
