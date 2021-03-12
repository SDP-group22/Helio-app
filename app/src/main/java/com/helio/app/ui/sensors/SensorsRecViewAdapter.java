package com.helio.app.ui.sensors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Sensor;

public class SensorsRecViewAdapter extends RecyclerView.Adapter<SensorsRecViewAdapter.ViewHolder> {
    private final Context context;
    private final UserDataViewModel model;
    private final Sensor sensor;
    public SensorsRecViewAdapter(Context context, UserDataViewModel model, Sensor sensor) {
        this.context = context;
        this.model = model;
        this.sensor = sensor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensors_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    public Sensor getSensor() {
        return sensor;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final ImageView blindIcon;
        private final CardView parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.sensorName);
            blindIcon = itemView.findViewById(R.id.sensorIcon);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}