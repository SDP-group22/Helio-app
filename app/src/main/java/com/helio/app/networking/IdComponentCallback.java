package com.helio.app.networking;

import androidx.lifecycle.MutableLiveData;

import com.helio.app.model.IdComponent;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An instance of this class allows for code to be run when a response is received from the Hub.
 * It will update our local state to match that of the Hub after performing an action.
 *
 * @see DeletionCallback
 * @see HubClient
 */
class IdComponentCallback<T extends IdComponent> implements Callback<T> {
    private final MutableLiveData<Map<Integer, T>> liveData;

    IdComponentCallback(MutableLiveData<Map<Integer, T>> liveData) {
        this.liveData = liveData;
    }

    @Override
    public void onResponse(@NotNull Call<T> call, Response<T> response) {
        T t = response.body();
        if (t != null) {
            System.out.println(call + " succeeded: " + t);
            System.out.println("Updating local state for " + t);
            Map<Integer, T> map = liveData.getValue();
            assert map != null;
            map.put(t.getId(), t);
            liveData.setValue(map);
        } else {
            System.out.println("Communication error");
        }
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
        System.out.println(call + " failed: " + t);
    }
}
