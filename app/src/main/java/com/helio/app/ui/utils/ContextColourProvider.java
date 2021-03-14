package com.helio.app.ui.utils;

import android.content.Context;
import android.content.res.TypedArray;

public class ContextColourProvider {

    public static int getColour(Context context, int colorAccent) {
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(new int[]{colorAccent});
        int highlightColour;
        try {
            highlightColour = themeArray.getColor(0, 0);
        } finally {
            themeArray.recycle();
        }
        return highlightColour;
    }
}
