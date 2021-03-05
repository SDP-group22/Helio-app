package com.helio.app.networking;

import com.helio.app.model.Motor;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoveMotorCallback implements Callback<Motor> {
    private final Motor motor;

    public MoveMotorCallback(Motor motor) {
        this.motor = motor;
    }

    @Override
    public void onResponse(@NotNull Call<Motor> call, Response<Motor> response) {
        if (response.body() != null) {
            System.out.println(call + " succeeded: " + response);
            motor.setLevel(response.body().getLevel());
        } else {
            System.out.println("Communication error");
        }
    }

    @Override
    public void onFailure(@NotNull Call<Motor> call, @NotNull Throwable t) {
        System.out.println(call + " failed: " + t);
    }
}
