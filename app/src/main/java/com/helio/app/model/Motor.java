package com.helio.app.model;

import com.helio.app.ui.MotorIcon;

import org.jetbrains.annotations.NotNull;


/**
 * Motor objects represent motor controllers in the user's home environment.
 */
public class Motor extends IdComponent {
    private String name;
    private String ip;
    private boolean active;
    private int level;
    private int battery;
    private int length;
    private String style;
    private MotorIcon icon;

    public Motor(
            int id,
            String name,
            String ip,
            boolean active,
            int level,
            int battery,
            int length,
            String style,
            MotorIcon icon) {
        super(id);
        this.name = name;
        this.ip = ip;
        this.active = active;
        this.level = level;
        this.battery = battery;
        this.length = length;
        this.style = style;
        this.icon = icon;
    }

    /**
     * Create a motor with the ID only, leaving everything else as default.
     */
    public Motor(int id) {
        this(id, "", "", false, 0, 0, 0, "", null);
    }

    /**
     * Create a motor with name and icon only (DEBUG)
     */
    public Motor(String name, MotorIcon icon) {
        this(0, name, "", false, 0, 0, 0, "", icon);
    }

    @NotNull
    @Override
    public String toString() {
        return "Motor{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", active=" + active +
                ", level=" + level +
                ", battery=" + battery +
                ", length=" + length +
                ", style='" + style + '\'' +
                ", icon=" + icon +
                '}';
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public MotorIcon getIcon() {
        return icon;
    }

    public void setIcon(MotorIcon icon) {
        this.icon = icon;
    }
}
