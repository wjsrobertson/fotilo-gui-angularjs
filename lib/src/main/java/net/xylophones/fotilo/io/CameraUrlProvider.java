package net.xylophones.fotilo.io;

import net.xylophones.fotilo.common.CameraInfo;

public interface CameraUrlProvider {

    String getSettingsPageUrl(CameraInfo cameraInfo);

    String getMjpegUrl(CameraInfo cameraInfo);

    String createActionControlUrl(CameraInfo cameraInfo, int command, int value);

    String getSnapshotUrl(CameraInfo cameraInfo);

}
