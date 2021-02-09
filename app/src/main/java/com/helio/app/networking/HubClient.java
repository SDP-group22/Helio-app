package com.helio.app.networking;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HubClient {
    private final HubService service;

    public HubClient(String baseAddress) {
        service = ServiceGenerator.createService(HubService.class, baseAddress);
    }

    public void addMotor(Map<Integer, Motor> motors, RegisterMotorRequest registerMotorRequest) {
        int motorId = registerMotorRequest.getId();
        Call<Motor> call = service.addMotor(registerMotorRequest);
        call.enqueue(new MotorCallback(motors, motorId));
    }

    public void activateMotor(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.activateMotor(motorId);
        call.enqueue(new MotorCallback(motors, motorId));
    }

    public void deactivateMotor(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.deactivateMotor(motorId);
        call.enqueue(new MotorCallback(motors, motorId));
    }

    public void renameMotor(Map<Integer, Motor> motors, RenameMotorRequest renameMotorRequest) {
        int motorId = renameMotorRequest.getId();
        Call<Motor> call = service.renameMotor(renameMotorRequest);
        call.enqueue(new MotorCallback(motors, motorId));
    }

    public void getMotor(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.getMotor(motorId);
        call.enqueue(new MotorCallback(motors, motorId));
    }

    public void moveMotor(Map<Integer, Motor> motors, MoveMotorRequest moveMotorRequest) {
        int motorId = moveMotorRequest.getId();
        Call<Motor> call = service.moveMotor(moveMotorRequest);
        call.enqueue(new MotorCallback(motors, motorId));
    }

    public void startMotorCalibration(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.startMotorCalibration(motorId);
        call.enqueue(new MotorCallback(motors, motorId));
    }

    public void stopMotorCalibration(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.stopMotorCalibration(motorId);
        call.enqueue(new MotorCallback(motors, motorId));
    }

    public void deleteMotor(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.deleteMotor(motorId);
        call.enqueue(new MotorDeletionCallback(motors, motorId));
    }
}

class MotorCallback implements Callback<Motor> {
    protected final Map<Integer, Motor> motors;
    protected final int motorId;

    MotorCallback(Map<Integer, Motor> motors, int motorId) {
        this.motors = motors;
        this.motorId = motorId;
    }

    protected void updateLocalMotorState(Motor m) {
        motors.put(motorId, m);
        System.out.println("Updated local state for " + m);
    }

    protected void unregisterFromLocalMotorState() {
        motors.remove(motorId);
        System.out.println(motorId + " was removed from local state");
    }

    @Override
    public void onResponse(Call<Motor> call, Response<Motor> response) {
        Motor m = response.body();
        if(m != null) {
            System.out.println(call + " succeeded: " + m);
            updateLocalMotorState(m);
        }
    }

    @Override
    public void onFailure(Call<Motor> call, Throwable t) {
        System.out.println(call + " failed: " + t);
    }
}

class MotorDeletionCallback extends MotorCallback {

    MotorDeletionCallback(Map<Integer, Motor> motors, int motorId) {
        super(motors, motorId);
    }

    @Override
    public void onResponse(Call<Motor> call, Response<Motor> response) {
        unregisterFromLocalMotorState();
    }
}
