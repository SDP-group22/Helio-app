package com.helio.app.model;

import org.jetbrains.annotations.NotNull;


/**
 * Motor objects represent motor controllers in the user's home environment.
 */
public class Motor implements IdComponent {
    private final int id;
    private String name;
    private String ip;
    private boolean active;
    private int level;
    private final int battery;
    private final int length;
    private String style;

    public Motor(
            int id,
            String name,
            String ip,
            boolean active,
            int level,
            int battery,
            int length,
            String style) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.active = active;
        this.level = level;
        this.battery = battery;
        this.length = length;
        this.style = style;
    }

    /**
     * Create a motor with the ID only, leaving everything else as default.
     */
    public Motor(int id) {
        this(id, "", "", false, 0, 0, 0, "");
    }

    /**
     * Create a motor with name and icon only (DEBUG)
     */
    public Motor(String name) {
        this(0, name, "", false, 0, 0, 0, "");
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

    public int getLength() {
        return length;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getIconId() {
        return Integer.parseInt(style);
    }

    public void setIconId(int iconId) {
        style = String.valueOf(iconId);
    }

    @Override
    public int getId() {
        return id;
    }
}
