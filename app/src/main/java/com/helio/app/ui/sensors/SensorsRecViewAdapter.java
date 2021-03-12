package com.helio.app.ui.sensors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.LightSensor;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Sensor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SensorsRecViewAdapter extends RecyclerView.Adapter<SensorsRecViewAdapter.ViewHolder> {

    private final Context context;
    private final UserDataViewModel model;
    private List<Sensor> sensors = new ArrayList<>();
    private Sensor sensor;

    public SensorsRecViewAdapter(Context context, UserDataViewModel model) {
        this.context = context;
        this.model = model;
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
        sensor = sensors.get(position);
        holder.txtName.setText(sensor.getName());
        holder.sensorIcon.setImageResource(sensor.getIcon());
        holder.activateSwitch.setChecked(sensor.isActive());
    }

    public void setLightSensors(Collection<LightSensor> sensors) {
        // Remove sensors of this type before adding them back to avoid duplication
        this.sensors = this.sensors.stream().filter(s -> s.getType() != LightSensor.TYPE).collect(Collectors.toList());
        this.sensors.addAll(sensors);
        sortSensors();
        notifyDataSetChanged();
    }

    public void setMotionSensors(Collection<MotionSensor> sensors) {
        // Remove sensors of this type before adding them back to avoid duplication
        this.sensors = this.sensors.stream().filter(s -> s.getType() != MotionSensor.TYPE).collect(Collectors.toList());
        this.sensors.addAll(sensors);
        sortSensors();
        notifyDataSetChanged();
    }

    private void sortSensors() {
        // Sort for consistent order
        Collections.sort(sensors, Comparator.comparingInt(sensor ->
                (sensor.getId() + 1) * sensor.getType() == MotionSensor.TYPE ? -1 : 1));
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final ImageView sensorIcon;
        private final SwitchMaterial activateSwitch;
        private final CardView parent;

        public ViewHolder(@NonNull View view) {
            super(view);
            txtName = view.findViewById(R.id.sensorName);
            sensorIcon = view.findViewById(R.id.sensorIcon);
            activateSwitch = view.findViewById(R.id.activate_switch);
            parent = view.findViewById(R.id.parent);

            // Click to open individual settings page
            parent.setOnClickListener(v -> {
                SensorsSettingsFragmentDirections.ActionSensorsSettingFragmentToSingleSensorSettingFragment action =
                        SensorsSettingsFragmentDirections.actionSensorsSettingFragmentToSingleSensorSettingFragment();
                action.setCurrentSensorId(sensor.getId());
                action.setSensorType(sensor.getType());
                Navigation.findNavController(view).navigate(action);
            });

            // Click listener for enable
            activateSwitch.setOnClickListener(v -> {
                SwitchMaterial switchView = (SwitchMaterial) v;

                // Update the view
                switchView.toggle();

                // Toast to explain what happened
                String message;
                if (switchView.isChecked()) {
                    message = parent.getResources().getString(R.string.deactivated);
                } else {
                    message = parent.getResources().getString(R.string.activated);
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                // Send update to model
                model.toggleSensorActive(sensors.get(getAdapterPosition()));
            });
        }
    }
}