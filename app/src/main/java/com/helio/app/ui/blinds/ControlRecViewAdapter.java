package com.helio.app.ui.blinds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;

import java.util.ArrayList;

public class ControlRecViewAdapter extends RecyclerView.Adapter<ControlRecViewAdapter.ViewHolder> {
    private final UserDataViewModel model;
    private ArrayList<Motor> motors = new ArrayList<>();

    public ControlRecViewAdapter(UserDataViewModel model) {
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
        if (motor.getIcon() != null) {
            holder.blindIcon.setImageResource(motor.getIcon().id);
        }
        holder.slider.setValue(motor.getLevel());
    }

    @Override
    public int getItemCount() {
        return motors.size();
    }

    public void setMotors(ArrayList<Motor> motors) {
        this.motors = motors;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;
        private final ImageView blindIcon;
        private final Slider slider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.scheduleName);
            blindIcon = itemView.findViewById(R.id.blindIcon);
            slider = itemView.findViewById(R.id.controlSlider);
            CardView parent = itemView.findViewById(R.id.parent);

            // Update the level of the motor when the user lets go of the slider
            slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(@NonNull Slider slider) {
                }

                @Override
                public void onStopTrackingTouch(@NonNull Slider slider) {
                    Motor motor = motors.get(getAdapterPosition());
                    // send update to Hub
                    motor.setLevel((int) slider.getValue());
                    System.out.println("Updated level using slider: " + motor);
                    model.pushComponentState(motor);
                }
            });

            parent.setOnClickListener(v -> {
                ControlFragmentDirections.ActionNavigationControlToSingleBlindSettingsFragment action =
                        ControlFragmentDirections.actionNavigationControlToSingleBlindSettingsFragment();
                Motor m = motors.get(getAdapterPosition());
                action.setCurrentMotorId(m.getId());
                Navigation.findNavController(itemView).navigate(action);
            });
        }
    }
}
