package com.helio.app.ui.utils;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * A string resource ID that returns its string value in toString().
 */
public class IdString {
    private final int id;
    private final Context context;

    public IdString(int id, Context context) {
        this.id = id;
        this.context = context;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return context.getString(id);
    }
}
