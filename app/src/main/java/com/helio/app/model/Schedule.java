package com.helio.app.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Schedule implements IdComponent {
    private final int id;
    @SerializedName("motor_ids")
    private final List<Integer> motorIds;
    private String ScheduleEventName;
    private String ScheduleMotorName;
    private boolean active;
    private List<Day> days;
    @SerializedName("target_level")
    private int targetLevel;
    private int gradient;
    private String time;

    public Schedule(int id, String ScheduleEventName,String ScheduleMotorName, boolean active, List<Day> days, int targetLevel,
                    int gradient, List<Integer> motorIds, String time) {
        this.id = id;
        this.ScheduleEventName = ScheduleEventName;
        this.ScheduleMotorName = ScheduleMotorName;
        this.active = active;
        this.days = days;
        this.targetLevel = targetLevel;
        this.gradient = gradient;
        this.motorIds = motorIds;
        this.time = time;
    }

    /**
     * Create a schedule with the ID only, leaving everything else as default.
     */
    public Schedule(int id) {
        this(id, "","", false, new ArrayList<>(), 0, 0, new ArrayList<>(), "");
    }

    public String getScheduleEventName() {
        return ScheduleEventName;
    }

    public void setScheduleEventName(String name) {
        this.ScheduleEventName = name;
    }

    public String getScheduleMotorName() {
        return ScheduleMotorName;
    }

    public void setScheduleMotorName(String name) {
        this.ScheduleMotorName = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(int targetLevel) {
        this.targetLevel = targetLevel;
    }

    public int getGradient() {
        return gradient;
    }

    public void setGradient(int gradient) {
        this.gradient = gradient;
    }

    public List<Integer> getMotorIds() {
        return motorIds;
    }

    public String getTime() {
        return time;
    }

    /**
     * Set the time of this schedule
     *
     * @param hour   the hour of the day (24-hour)
     * @param minute the minute
     */
    public void setTime(int hour, int minute) {
        // Locale is set because it complains about that sometimes causing bugs
        this.time = String.format(Locale.US, "%02d:%02d", hour, minute);
    }

    public int getTimeHour() {
        return Integer.parseInt(Objects.requireNonNull(time).split(":")[0]);
    }

    public int getTimeMinute() {
        return Integer.parseInt(Objects.requireNonNull(time).split(":")[1]);
    }

    @Override
    public int getId() {
        return id;
    }


    @NotNull
    @Override
    public String toString() {
        return "Schedule{" +
                "id='" + getId() + '\'' +
                ", schedule event name='" + getScheduleEventName() + '\'' +
                ", schedule motor name='" + getScheduleMotorName() + '\'' +
                ", active=" + active +
                ", days=" + days +
                ", targetLevel=" + targetLevel +
                ", gradient=" + gradient +
                ", motorIds=" + motorIds +
                ", time='" + time + '\'' +
                '}';
    }
}
