package com.helio.app.networking;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MotorDeletionCallback implements Callback<ResponseBody> {
    private final Map<Integer, Motor> motors;
    private final int motorId;

    MotorDeletionCallback(Map<Integer, Motor> motors, int motorId) {
        this.motors = motors;
        this.motorId = motorId;
    }

    private void unregisterFromLocalMotorState() {
        motors.remove(motorId);
        System.out.println(motorId + " was removed from local state");
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        unregisterFromLocalMotorState();
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        System.out.println(call + " failed: " + t);
    }
}