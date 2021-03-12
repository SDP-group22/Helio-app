package com.helio.app.ui.sensors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    public void setLightSensors(Collection<LightSensor> sensors) {
        // Remove sensors of this type before adding them back to avoid duplication
        this.sensors = this.sensors.stream().filter(s -> s.getType() != LightSensor.TYPE).collect(Collectors.toList());
        this.sensors.addAll(sensors);
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
                sensor.getId() + sensor.getType() == MotionSensor.TYPE ? -1000 : 1000));
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final ImageView sensorIcon;
        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.sensorName);
            sensorIcon = itemView.findViewById(R.id.sensorIcon);
            parent = itemView.findViewById(R.id.parent);

            // Click to open individual settings page
            parent.setOnClickListener(v -> {
                SensorsSettingsFragmentDirections.ActionSensorsSettingFragmentToSingleSensorSettingFragment action =
                        SensorsSettingsFragmentDirections.actionSensorsSettingFragmentToSingleSensorSettingFragment();
                action.setCurrentSensorId(sensor.getId());
                action.setSensorType(sensor.getType());
                Navigation.findNavController(itemView).navigate(action);
            });
        }
    }
}