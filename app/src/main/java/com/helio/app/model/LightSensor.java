package com.helio.app.model;

import com.helio.app.R;

import java.util.List;

public class LightSensor extends Sensor {
    public static final int TYPE = 1;

    public LightSensor(int id, String name, String ip, boolean active, int battery,
                       List<Integer> motorIds, String style) {
        super(id, name, ip, active, battery, motorIds, style);
    }

    public LightSensor(int id) {
        super(id);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_light_sensor;
    }

    @Override
    public int getContentDescription() {
        return R.string.light_sensor;
    }
}
