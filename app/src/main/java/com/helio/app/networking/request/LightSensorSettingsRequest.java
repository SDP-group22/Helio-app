package com.helio.app.networking.request;

import java.util.List;

public class LightSensorSettingsRequest extends SensorSettingsRequest {

    public LightSensorSettingsRequest(List<Integer> motorIds, String name, String ip, boolean active, int battery, String style) {
        super(motorIds, name, ip, active, battery, style);
    }
}
