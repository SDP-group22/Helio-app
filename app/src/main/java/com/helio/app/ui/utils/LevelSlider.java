package com.helio.app.ui.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.slider.Slider;

/**
 * The custom version of the material slider used for controlling blinds
 */
public class LevelSlider extends Slider {
    public LevelSlider(@NonNull Context context) {
        super(context);
        setMyParams();
    }

    public LevelSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setMyParams();
    }

    public LevelSlider(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMyParams();
    }

    private void setMyParams() {
        setValueFrom(0);
        setValueTo(100);
        setStepSize(1);
        setTickVisible(false);
        // Get the accent colour from the theme
        int highlightColour = ContextColourProvider.getColour(getContext(), android.R.attr.colorAccent);
        setThumbTintList(ColorStateList.valueOf(highlightColour));

        setLabelFormatter(new LevelLabelFormatter(getContext()));
    }
}
