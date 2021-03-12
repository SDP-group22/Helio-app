package com.helio.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public abstract class Sensor implements IdComponent {
    private final int id;
    @SerializedName("motor_ids")
    private final List<Integer> motorIds;
    private String name;
    private String ip;
    private boolean active;
    private int battery;
    private String style;

    public Sensor(int id, String name, String ip, boolean active, int battery,
                  List<Integer> motorIds, String style) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.active = active;
        this.battery = battery;
        this.motorIds = motorIds;
        this.style = style;
    }

    /**
     * Create a Sensor with the ID only, leaving everything else as default.
     */
    public Sensor(int id) {
        this(id, "", "", false, 0, new ArrayList<>(), "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public List<Integer> getMotorIds() {
        return motorIds;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public int getId() {
        return id;
    }

    public abstract int getType();

    public abstract int getIcon();
}
