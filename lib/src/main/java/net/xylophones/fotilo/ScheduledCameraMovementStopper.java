package net.xylophones.fotilo;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledCameraMovementStopper {

    private static final int ONE_MINUTE_IN_SECONDS = 60;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void stopMovementAfterTime(final CameraControl cameraControl, final int seconds) {
        if (seconds > ONE_MINUTE_IN_SECONDS) {
            throw new IllegalArgumentException("time can't more than " + ONE_MINUTE_IN_SECONDS + " seconds");
        }

        scheduler.schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cameraControl.stopMovement();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , seconds, TimeUnit.SECONDS);
    }

}
