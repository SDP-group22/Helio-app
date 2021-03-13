package com.helio.app.model;

import com.google.gson.annotations.SerializedName;
import com.helio.app.R;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MotionSensor extends Sensor {
    public static final int TYPE = 0;
    @SerializedName("duration_sensitivity")
    private String durationSensitivity;

    public MotionSensor(int id, String name, String ip, boolean active, int battery,
                        List<Integer> motorIds, String style, String durationSensitivity) {
        super(id, name, ip, active, battery, motorIds, style);
        this.durationSensitivity = durationSensitivity;
    }

    public MotionSensor(int id) {
        super(id);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_motion_sensor;
    }

    @Override
    public int getContentDescription() {
        return R.string.motion_sensor;
    }

    public String getDurationSensitivity() {
        return durationSensitivity;
    }

    public void setDurationSensitivity(int hour, int minute) {
        // Locale is set because it complains about that sometimes causing bugs
        this.durationSensitivity = String.format(Locale.US, "%02d:%02d", hour, minute);
    }

    public int getDurationSensitivityHour() {
        return Integer.parseInt(Objects.requireNonNull(durationSensitivity).split(":")[0]);
    }

    public int getDurationSensitivityMinute() {
        return Integer.parseInt(Objects.requireNonNull(durationSensitivity).split(":")[1]);
    }
}
