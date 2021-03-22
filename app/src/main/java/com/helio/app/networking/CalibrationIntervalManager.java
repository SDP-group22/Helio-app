package com.helio.app.networking;

import android.os.Handler;

import com.helio.app.model.Motor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CalibrationIntervalManager {
    private CalibrationIntervalManagerState state;
    private static final int CALIBRATION_INTERVAL_DELAY = 1000;
    private final Handler calibrationIntervalHandler;
    private final HubClient client;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> requestsLoopHandle = null;
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
        if (state != CalibrationIntervalManagerState.IDLE) {
            throw new IllegalStateException("Cannot start move up unless current state is IDLE.");
        }
        state = CalibrationIntervalManagerState.MOVING_UP;
        targetMotor = motor;
        // schedule move_up request at fixed interval
        requestsLoopHandle = scheduler.scheduleAtFixedRate(
                this::sendSingleMoveUpRequest,
                0,
                CALIBRATION_INTERVAL_DELAY,
                TimeUnit.MILLISECONDS
        );
    }

    public void startMoveDownRequestLoop(Motor motor) {
        System.out.println("CALIBRATION: setting up move_down request loop...");
        if (state != CalibrationIntervalManagerState.IDLE) {
            return;
        }
        state = CalibrationIntervalManagerState.MOVING_DOWN;
        targetMotor = motor;
        // schedule move_down request at fixed interval
        requestsLoopHandle = scheduler.scheduleAtFixedRate(
                this::sendSingleMoveDownRequest,
                0,
                CALIBRATION_INTERVAL_DELAY,
                TimeUnit.MILLISECONDS
        );
    }

    public void stopRequestLoop() {
        System.out.println("CALIBRATION: stopping calibration request loop...");
        if (!(state == CalibrationIntervalManagerState.MOVING_UP ||
                state == CalibrationIntervalManagerState.MOVING_DOWN)) {
            return;
        }
        requestsLoopHandle.cancel(true);
        client.stopMoving(targetMotor);
        requestsLoopHandle = null;
        targetMotor = null;
        state = CalibrationIntervalManagerState.IDLE;
    }
}
