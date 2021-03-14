package com.helio.app.ui.settings;

import java.util.HashMap;
import java.util.Map;

public enum AppTheme {
    DEFAULT("Default"),
    NIGHT("Night"),
    HIGH_CONTRAST("High Contrast");

    public static final String DEFAULT_THEME_NAME = "Default";
    // Lookup map for getting an AppTheme from an integer
    private static final Map<String, AppTheme> lookup = new HashMap<>();

    static {
        for (AppTheme theme : AppTheme.values()) {
            lookup.put(theme.name, theme);
        }
    }

    private final String name;

    AppTheme(String name) {
        this.name = name;
    }

    public static AppTheme getEnumFromName(String name) {
        if (!lookup.containsKey(name)) {
            throw new IllegalArgumentException("\"" + name + "\" is not a valid theme name");
        }
        return lookup.get(name);
    }

    public String getName() {
        return name;
    }
}
