package com.helio.app.networking;

import androidx.lifecycle.MutableLiveData;

import com.helio.app.model.Motor;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class NetworkStatusCallback implements Callback<List<Motor>> {
    private final MutableLiveData<NetworkStatus> status;

    NetworkStatusCallback(MutableLiveData<NetworkStatus> status) {
        this.status = status;
    }

    @Override
    public void onResponse(@NotNull Call<List<Motor>> call, @NotNull Response<List<Motor>> response) {
        status.setValue(NetworkStatus.CONNECTED);
    }

    @Override
    public void onFailure(@NotNull Call<List<Motor>> call, @NotNull Throwable t) {
        status.setValue(NetworkStatus.DISCONNECTED);
    }
}
