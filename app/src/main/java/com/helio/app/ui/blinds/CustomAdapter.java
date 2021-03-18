package com.helio.app.ui.blinds;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.helio.app.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<CustomBlindsItem> {
    public CustomAdapter(@NonNull Context context, ArrayList<CustomBlindsItem> customBlindsItemArrayList) {
        super(context, 0, customBlindsItemArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_blinds_list,parent,false);
        }
        CustomBlindsItem item = getItem(position);
        ImageView spinnerImage = convertView.findViewById(R.id.blindsImagesList);
        TextView spinnerName = convertView.findViewById(R.id.blindsNameList);
        if (item != null) {
            spinnerImage.setImageResource(item.getSpinnerItemImage());
            spinnerName.setText(item.getSpinnerItemName());
        }
        return convertView;
    }
    @Override
    public View getDropDownView(int position,@Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_blinds_list_item,parent,false);
        }
        CustomBlindsItem item = getItem(position);
        ImageView dropDownImage = convertView.findViewById(R.id.blindsImagesItems);
        TextView dropDownName = convertView.findViewById(R.id.blindsNameItems);
        if (item != null) {

            // This is to set the drawable actually.
            dropDownImage.setImageDrawable((Drawable) Array.get(R.array.iconsValues,item.getSpinnerItemImage()));
            dropDownName.setText(item.getSpinnerItemName());
        }
        return convertView;
    }




}
