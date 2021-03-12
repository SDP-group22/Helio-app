package com.helio.app.model;

import java.util.List;

public class LightSensor extends Sensor {
    public LightSensor(int id, String name, String ip, boolean active, int battery,
                       List<Integer> motorIds, String style) {
        super(id, name, ip, active, battery, motorIds, style);
    }

    public LightSensor(int id) {
        super(id);
    }

    @Override
    public String getType() {
        return "Light";
    }
}
