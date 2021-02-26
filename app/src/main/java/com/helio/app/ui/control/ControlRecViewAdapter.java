package com.helio.app.ui.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ControlRecViewAdapter extends RecyclerView.Adapter<ControlRecViewAdapter.ViewHolder> {
    private final Context context;
    private final UserDataViewModel model;
    private ArrayList<Motor> motors = new ArrayList<>();

    public ControlRecViewAdapter(Context context, UserDataViewModel model) {
        this.context = context;
        this.model = model;
    }


    @NonNull
    @Override
    public ControlRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.control_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ControlRecViewAdapter.ViewHolder holder, int position) {
        // Set the name and icon of the motor and put the current level in the slider
        Motor motor = motors.get(position);
        holder.txtName.setText(motor.getName());
        if(motor.getIcon() != null) {
            holder.blindIcon.setImageResource(motor.getIcon().id);
        }
        holder.slider.setValue(motor.getLevel());

        // Format the number as a percentage (possibly sensitive to Locale)
        // Display Closed or Open if completely closed or open
        holder.slider.setLabelFormatter(value -> {
            if (value == 0) {
                return context.getResources().getString(R.string.closed_state);
            } else if (value == 100) {
                return context.getResources().getString(R.string.open_state);
            } else {
                return NumberFormat.getPercentInstance().format(value / 100);
            }
        });

        // Update the level of the motor when the user lets go of the slider
        holder.slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Motor motor = motors.get(position);
                // disable dragging? TODO
                // send update to Hub
                motor.setLevel((int) slider.getValue());
                System.out.println("Updated level using slider: " + motor);
                model.setCurrentMotor(motor.getId());
                model.pushCurrentMotorState(motor);
                // asynchronously enable dragging? TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        return motors.size();
    }

    public void setMotors(ArrayList<Motor> motors) {
        this.motors = motors;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;
        private final ImageView blindIcon;
        private final Slider slider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.scheduleName);
            blindIcon = itemView.findViewById(R.id.blindIcon);
            slider = itemView.findViewById(R.id.controlSlider);
        }
    }
}
