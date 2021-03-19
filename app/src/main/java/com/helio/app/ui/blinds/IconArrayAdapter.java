package com.helio.app.ui.blinds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.helio.app.R;

import java.util.ArrayList;

public class IconArrayAdapter extends ArrayAdapter<CustomBlindsItem> {
    public IconArrayAdapter(@NonNull Context context, ArrayList<CustomBlindsItem> customBlindsItemArrayList) {
        super(context, R.layout.dropdown_blinds_list_item, customBlindsItemArrayList);
        System.out.println(customBlindsItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_blinds_list_item, parent, false);
        }
        CustomBlindsItem item = getItem(position);
        ImageView spinnerImage = convertView.findViewById(R.id.icon_image);
        if (item != null) {
            spinnerImage.setImageResource(item.getSpinnerItemImage());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_blinds_list_item, parent, false);
        }
        CustomBlindsItem item = getItem(position);
        ImageView dropDownImage = convertView.findViewById(R.id.icon_image);
        if (item != null) {
            // Set the drawable
            dropDownImage.setImageDrawable(ContextCompat.getDrawable(convertView.getContext(), item.getSpinnerItemImage()));
        }
        return convertView;
    }


}
