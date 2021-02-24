package com.helio.app.networking.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused", "RedundantSuppression"})
public abstract class SensorSettingsRequest {
    @SerializedName("motor_ids")
    private final List<Integer> motorIds;
    private final String name;
    private final String ip;
    private final boolean active;
    private final int battery;
    private final String style;

    public SensorSettingsRequest(List<Integer> motorIds, String name, String ip, boolean active, int battery, String style) {
        this.motorIds = motorIds;
        this.name = name;
        this.ip = ip;
        this.active = active;
        this.battery = battery;
        this.style = style;
    }
}
