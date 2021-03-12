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

public class SensorsRecViewAdapter extends RecyclerView.Adapter<SensorsRecViewAdapter.ViewHolder> {

    private final Context context;
    private final UserDataViewModel model;
    private ArrayList<Sensor> sensors = new ArrayList<>();

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
        Sensor sensor = sensors.get(position);
        holder.txtName.setText(sensor.getName());
        holder.parent.setOnClickListener(v -> {
            SensorsSettingsFragmentDirections.ActionSensorsSettingFragmentToSingleSensorSettingFragment action =
                    SensorsSettingsFragmentDirections.actionSensorsSettingFragmentToSingleSensorSettingFragment();
            action.setCurrentSensorId(sensor.getId());
            action.setSensorType(sensor.getType());
            Navigation.findNavController(holder.itemView).navigate(action);
        });
    }

    public void setLightSensors(Collection<LightSensor> sensors) {
        this.sensors.addAll(sensors);
        notifyDataSetChanged();
    }

    public void setMotionSensors(Collection<MotionSensor> sensors) {
        this.sensors.addAll(sensors);
        notifyDataSetChanged();
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
        }
    }
}