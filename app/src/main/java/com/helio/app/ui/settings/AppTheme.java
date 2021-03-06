package com.helio.app.ui.settings;

import com.helio.app.R;

import java.util.HashMap;
import java.util.Map;

public enum AppTheme {
    DEFAULT(R.string.default_theme),
    NIGHT(R.string.night_theme),
    HIGH_CONTRAST(R.string.high_contrast_theme);

    public static final int DEFAULT_THEME_ID = R.string.default_theme;
    // Lookup map for getting an AppTheme from an integer
    private static final Map<Integer, AppTheme> lookup = new HashMap<>();

    static {
        for (AppTheme theme : AppTheme.values()) {
            lookup.put(theme.resourceId, theme);
        }
    }

    private final int resourceId;

    AppTheme(int resourceId) {
        this.resourceId = resourceId;
    }

    public static AppTheme getEnumFromId(int id) {
        if (!lookup.containsKey(id)) {
            // Use default instead of throw here since I don't want it to crash if the saved preferences are wrong
            System.out.println("\"" + id + "\" is not a valid theme id, loading default");
            return DEFAULT;
        }
        return lookup.get(id);
    }
}
