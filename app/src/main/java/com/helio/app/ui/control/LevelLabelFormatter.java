package com.helio.app.ui.control;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.slider.LabelFormatter;
import com.helio.app.R;

import java.text.NumberFormat;

public class LevelLabelFormatter implements LabelFormatter {
    private final Context context;

    public LevelLabelFormatter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public String getFormattedValue(float value) {
        // Format the number as a percentage (possibly sensitive to Locale)
        // Display Closed or Open if completely closed or open
        if (value == 0) {
            return context.getString(R.string.closed_state);
        } else if (value == 100) {
            return context.getString(R.string.open_state);
        } else {
            return NumberFormat.getPercentInstance().format(value / 100);
        }
    }
}
