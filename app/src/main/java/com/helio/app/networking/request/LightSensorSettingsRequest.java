package com.helio.app.networking.request;

import com.helio.app.networking.IPAddress;

import java.util.ArrayList;
import java.util.List;

public class LightSensorSettingsRequest extends SensorSettingsRequest {

    public LightSensorSettingsRequest(List<Integer> motorIds, String name, String ip, boolean active, int battery, String style) {
        super(motorIds, name, ip, active, battery, style);
    }

    public static LightSensorSettingsRequest newSensorRequest() {
        return new LightSensorSettingsRequest(new ArrayList<>(), "", IPAddress.DEFAULT, true, 0, "");
    }
}
