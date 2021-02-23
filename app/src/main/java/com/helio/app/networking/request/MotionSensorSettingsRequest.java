package com.helio.app.networking.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused", "RedundantSuppression"})
public class MotionSensorSettingsRequest extends SensorSettingsRequest {
    @SerializedName("duration_sensitivity")
    private final String durationSensitivity;

    public MotionSensorSettingsRequest(List<Integer> motorIds, String name, String ip, boolean active, int battery, String style, String durationSensitivity) {
        super(motorIds, name, ip, active, battery, style);
        this.durationSensitivity = durationSensitivity;
    }
}
