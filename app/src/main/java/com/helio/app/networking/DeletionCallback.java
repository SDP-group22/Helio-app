package com.helio.app.networking;

import com.helio.app.model.IdComponent;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A MyDeletionCallback instance allows for code to be run after deleting on the Hub.
 * To remain in sync with the Hub, we also remove it from our local state.
 *
 * @see IdComponentCallback
 * @see HubClient
 */
class DeletionCallback<T extends IdComponent> implements Callback<ResponseBody> {
    private final Map<Integer, T> map;
    private final int id;

    DeletionCallback(Map<Integer, T> map, int id) {
        this.map = map;
        this.id = id;
    }

    @Override
    public void onResponse(
            @NotNull Call<ResponseBody> call,
            @NotNull Response<ResponseBody> response
    ) {
        map.remove(id);
        System.out.println(id + " was removed from local state");
    }

    @Override
    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
        System.out.println(call + " failed: " + t);
    }
}