package com.helio.app.model;

import java.util.List;

public class MotionSensor extends Sensor {
    private int durationSensitivity;

    public MotionSensor(int id, String name, String ip, boolean active, int battery,
                        List<Integer> motorIds, String style, int durationSensitivity) {
        super(id, name, ip, active, battery, motorIds, style);
        this.durationSensitivity = durationSensitivity;
    }

    public MotionSensor(int id) {
        super(id);
    }

    public int getDurationSensitivity() {
        return durationSensitivity;
    }

    public void setDurationSensitivity(int durationSensitivity) {
        this.durationSensitivity = durationSensitivity;
    }
}