package com.helio.app.networking;

import androidx.lifecycle.MutableLiveData;

import com.helio.app.model.IdComponent;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An instance of this class allows for code to be run after deleting on the Hub.
 * To remain in sync with the Hub, we also remove it from our local state.
 *
 * @see IdComponentCallback
 * @see HubClient
 */
class DeletionCallback<T extends IdComponent> implements Callback<ResponseBody> {
    private final MutableLiveData<Map<Integer, T>> liveData;
    private final int id;

    DeletionCallback(MutableLiveData<Map<Integer, T>> liveData, int id) {
        this.liveData = liveData;
        this.id = id;
    }

    @Override
    public void onResponse(
            @NotNull Call<ResponseBody> call,
            @NotNull Response<ResponseBody> response
    ) {
        System.out.println(id + " is being removed from local state");
        Map<Integer, T> map = liveData.getValue();
        assert map != null;
        map.remove(id);
        liveData.setValue(map);
    }

    @Override
    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
        System.out.println(call + " failed: " + t);
    }
}