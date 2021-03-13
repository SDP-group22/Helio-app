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

import com.google.android.material.checkbox.MaterialCheckBox;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;
import com.helio.app.model.Sensor;
import com.helio.app.ui.MotorIcon;

import java.util.ArrayList;

public class BlindsCheckboxRecViewAdapter extends RecyclerView.Adapter<BlindsCheckboxRecViewAdapter.ViewHolder> {

    private final Context context;
    private final UserDataViewModel model;
    private ArrayList<Motor> motors = new ArrayList<>();
    private Motor motor;
    private Sensor sensor;

    public BlindsCheckboxRecViewAdapter(Context context, UserDataViewModel model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blinds_checkbox_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        motor = motors.get(position);
        holder.txtName.setText(motor.getName());
        MotorIcon icon = motor.getIcon();
        if (icon != null) {
            holder.blindIcon.setImageResource(icon.id);
        }
        holder.checkBox.setChecked(sensor.getMotorIds().contains(motor.getId()));
    }

    @Override
    public int getItemCount() {
        return motors.size();
    }

    public void setMotors(ArrayList<Motor> motors) {
        this.motors = motors;
        notifyDataSetChanged();
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;
        private final ImageView blindIcon;
        private final MaterialCheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            blindIcon = itemView.findViewById(R.id.blindIcon);
            checkBox = itemView.findViewById(R.id.checkbox);
            CardView parent = itemView.findViewById(R.id.parent);

            parent.setOnClickListener(v -> {
                checkBox.setChecked(!checkBox.isChecked());
                updateMotorIds();
            });

            checkBox.setOnClickListener(v -> updateMotorIds());
        }

        private void updateMotorIds() {
            if (checkBox.isChecked()) {
                sensor.getMotorIds().add(motors.get(getAdapterPosition()).getId());
            } else {
                // It needs to be an Integer not an int so that it removes the ID and not the item at that position in the list
                sensor.getMotorIds().remove((Integer) motors.get(getAdapterPosition()).getId());
            }
        }
    }

}
