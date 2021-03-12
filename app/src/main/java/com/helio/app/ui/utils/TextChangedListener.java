package com.helio.app.ui.utils;

import android.text.Editable;
import android.text.TextWatcher;


public abstract class TextChangedListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.onTextChanged(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public abstract void onTextChanged(CharSequence s);
}
