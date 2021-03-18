package com.helio.app.networking.request;

import com.google.gson.annotations.SerializedName;
import com.helio.app.model.Day;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused", "RedundantSuppression", "MismatchedQueryAndUpdateOfCollection"})
public class ScheduleSettingsRequest {
    private final String name;
    private final boolean active;
    private final List<String> days;
    @SerializedName("target_level")
    private final int targetLevel;
    private final int gradient;
    @SerializedName("motor_ids")
    private final List<Integer> motorIds;
    private final String time;

    public ScheduleSettingsRequest(String name, boolean active, List<Day> days, int targetLevel,
                                   int gradient, List<Integer> motorIds, String time) {
        this.name = name;
        this.active = active;
        this.days = new ArrayList<>();
        for (Day d : days) {
            this.days.add(d.dayName);
        }
        this.targetLevel = targetLevel;
        this.gradient = gradient;
        this.motorIds = motorIds;
        this.time = time;
    }

    public static ScheduleSettingsRequest newScheduleRequest() {
        return new ScheduleSettingsRequest(
                "", true, new ArrayList<>(), 0, 0, new ArrayList<>(), "12:00");
    }
}
