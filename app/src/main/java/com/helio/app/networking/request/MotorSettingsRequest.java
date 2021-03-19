package com.helio.app.networking.request;

import com.helio.app.R;

/**
 * retrofit serialises these objects to be passed to the Hub for /motor/register/ requests
 */
@SuppressWarnings({"FieldCanBeLocal", "unused", "RedundantSuppression"})
public class MotorSettingsRequest {
    private final boolean active;
    private final int battery;
    private final String ip;
    private final int length;
    private final int level;
    private final String name;
    private final String style;

    public MotorSettingsRequest(String name, String ip, boolean active, int battery, int length, int level, String style) {
        this.name = name;
        this.ip = ip;
        this.active = active;
        this.battery = battery;
        this.length = length;
        this.level = level;
        this.style = style;
    }

    public static MotorSettingsRequest newMotorRequest() {
        return new MotorSettingsRequest("", "0.0.0.0", true, 0, 0, 0, String.valueOf(R.drawable.ic_tv));
    }
}
