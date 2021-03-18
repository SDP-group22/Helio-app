package com.helio.app.ui.blinds;

public class CustomBlindsItem {

    private final String spinnerItemName;
    private final int spinnerItemImage;

    public CustomBlindsItem(String spinnerItemName, int spinnerItemImage) {
        this.spinnerItemName = spinnerItemName;
        this.spinnerItemImage = spinnerItemImage;
    }

    public String getSpinnerItemName() {
        return spinnerItemName;
    }

    public int getSpinnerItemImage() {
        return spinnerItemImage;
    }
}
