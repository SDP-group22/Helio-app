package com.helio.app.networking;

import android.os.Handler;

import com.helio.app.model.Motor;

public class CalibrationIntervalManager {
    private static final int CALIBRATION_INTERVAL_DELAY = 1000;
    private final Handler calibrationIntervalHandler;
    private final HubClient client;
    private Runnable pendingRunnable = null;
    private Motor targetMotor = null;

    public CalibrationIntervalManager(HubClient client) {
        this.client = client;
        this.calibrationIntervalHandler = new Handler();
    }

    private void sendSingleMoveUpRequest(Motor motor) {
        System.out.println("CALIBRATION: \tsending one move_up request...");
    }

    public void startRequestLoop(Motor motor) {
        System.out.println("CALIBRATION: setting up move_up request loop...");
        // set up a handler that calls itself
        pendingRunnable = new Runnable() {
            @Override
            public void run() {
                sendSingleMoveUpRequest(motor);
                calibrationIntervalHandler.postDelayed(this, CALIBRATION_INTERVAL_DELAY);
            }
        };
        calibrationIntervalHandler.postDelayed(pendingRunnable, 0);
    }

    public void stopRequestLoop() {
        if(targetMotor == null || pendingRunnable == null) {
            throw new IllegalStateException("Cannot stop calibration loop when either targetMotor" +
                    " or pendingRunnable is null.");
        }
        System.out.println("CALIBRATION: stopping calibration request loop...");
        calibrationIntervalHandler.removeCallbacks(pendingRunnable);
        pendingRunnable = null;
    }
}
