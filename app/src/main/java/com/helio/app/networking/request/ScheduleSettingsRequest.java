package com.helio.app.networking.request;

import com.google.gson.annotations.SerializedName;
import com.helio.app.model.Day;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused", "RedundantSuppression", "MismatchedQueryAndUpdateOfCollection"})
public class ScheduleSettingsRequest {
    private String ScheduleEventName;
    private String ScheduleMotorName;
    private final boolean active;
    private final List<String> days;
    @SerializedName("target_level")
    private final int targetLevel;
    private final int gradient;
    @SerializedName("motor_ids")
    private final List<Integer> motorIds;
    private final String time;

    public ScheduleSettingsRequest(String ScheduleEventName, String ScheduleMotorName, boolean active, List<Day> days, int targetLevel,
                                   int gradient, List<Integer> motorIds, String time) {
        this.ScheduleEventName = ScheduleEventName;
        this.ScheduleMotorName = ScheduleMotorName;
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
}
