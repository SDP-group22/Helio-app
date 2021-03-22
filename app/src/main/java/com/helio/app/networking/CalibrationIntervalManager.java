package com.helio.app.networking;

import android.os.Handler;

import com.helio.app.model.Motor;

public class CalibrationIntervalManager {
    private CalibrationIntervalManagerState state;
    private static final int CALIBRATION_INTERVAL_DELAY = 1000;
    private final Handler calibrationIntervalHandler;
    private final HubClient client;
    private Runnable pendingRunnable = null;
    private Motor targetMotor = null;

    public CalibrationIntervalManager(HubClient client) {
        this.client = client;
        this.calibrationIntervalHandler = new Handler();
        state = CalibrationIntervalManagerState.IDLE;
    }

    private void sendSingleMoveUpRequest() {
        System.out.println("CALIBRATION: \tsending one move_up request...");
        client.moveUp(targetMotor);
    }

    private void sendSingleMoveDownRequest() {
        System.out.println("CALIBRATION: \tsending one move_down request...");
        client.moveDown(targetMotor);
    }

    public void startMoveUpRequestLoop(Motor motor) {
        System.out.println("CALIBRATION: setting up move_up request loop...");
        if(state != CalibrationIntervalManagerState.IDLE) {
            throw new IllegalStateException("Cannot start move up unless current state is IDLE.");
        }
        state = CalibrationIntervalManagerState.MOVING_UP;
        targetMotor = motor;
        // set up a handler that calls itself
        pendingRunnable = new Runnable() {
            @Override
            public void run() {
                sendSingleMoveUpRequest();
                calibrationIntervalHandler.postDelayed(this, CALIBRATION_INTERVAL_DELAY);
            }
        };
        calibrationIntervalHandler.postDelayed(pendingRunnable, 0);
    }

    public void startMoveDownRequestLoop(Motor motor) {
        System.out.println("CALIBRATION: setting up move_down request loop...");
        if(state != CalibrationIntervalManagerState.IDLE) {
            return;
        }
        state = CalibrationIntervalManagerState.MOVING_DOWN;
        targetMotor = motor;
        // set up a handler that calls itself
        pendingRunnable = new Runnable() {
            @Override
            public void run() {
                sendSingleMoveDownRequest();
                calibrationIntervalHandler.postDelayed(this, CALIBRATION_INTERVAL_DELAY);
            }
        };
        calibrationIntervalHandler.postDelayed(pendingRunnable, 0);
    }

    public void stopRequestLoop() {
        System.out.println("CALIBRATION: stopping calibration request loop...");
        if(!(state == CalibrationIntervalManagerState.MOVING_UP ||
                state == CalibrationIntervalManagerState.MOVING_DOWN)) {
            return;
        }
        calibrationIntervalHandler.removeCallbacks(pendingRunnable);
        client.stopMoving(targetMotor);
        pendingRunnable = null;
        targetMotor = null;
        state = CalibrationIntervalManagerState.IDLE;
    }
}
