package com.helio.app.networking;

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
    private final Map<Integer, T> map;

    IdComponentCallback(Map<Integer, T> map) {
        this.map = map;
    }

    @Override
    public void onResponse(@NotNull Call<T> call, Response<T> response) {
        T t = response.body();
        if (t != null) {
            System.out.println(call + " succeeded: " + t);
            map.put(t.getId(), t);
            System.out.println("Updated local state for " + t);
        } else {
            System.out.println("Communication error");
        }
    }

    @Override
    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
        System.out.println(call + " failed: " + t);
    }
}
