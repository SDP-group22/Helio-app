package com.helio.app.networking;

import com.helio.app.model.IdComponent;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllCallback<T extends IdComponent> implements Callback<List<T>> {
    private final Map<Integer, T> map;

    public GetAllCallback(Map<Integer, T> map) {
        this.map = map;
    }

    @Override
    public void onResponse(@NotNull Call<List<T>> call, @NotNull Response<List<T>> response) {
        List<T> responseList = response.body();
        if (responseList != null) {
            System.out.println(call + " succeeded: " + responseList);
            // Clear the map and replace with the new one
            map.clear();
            for (T t : responseList) {
                map.put(t.getId(), t);
            }
            System.out.println("Updated local state for " + responseList);
        } else {
            System.out.println("Communication error");
        }
    }

    @Override
    public void onFailure(@NotNull Call<List<T>> call, @NotNull Throwable t) {
        System.out.println(call + " failed: " + t);
    }
}
