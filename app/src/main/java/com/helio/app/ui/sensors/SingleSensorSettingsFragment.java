package com.helio.app.ui.sensors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Sensor;
import com.helio.app.ui.utils.TextChangedListener;

import java.util.ArrayList;

public class SingleSensorSettingsFragment extends Fragment {
    private UserDataViewModel model;
    private Sensor sensor;
    private EditText nameEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_sensor_settings, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        assert getArguments() != null;
        int sensorId = getArguments().getInt("currentSensorId");
        int sensorType = getArguments().getInt("sensorType");

        nameEditText = view.<TextInputLayout>findViewById(R.id.name).getEditText();

        BlindsCheckboxRecViewAdapter checkBoxRCAdapter = new BlindsCheckboxRecViewAdapter(getContext(), model);
        model.fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> checkBoxRCAdapter.setMotors(new ArrayList<>(motors.values()))
        );

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.blindsRCView);
        recView.setAdapter(checkBoxRCAdapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextInputLayout sensitivityMenuLayout = view.findViewById(R.id.dropdown_sensitivity);

        // Only motion sensors have duration sensitivity
        if (sensorType == MotionSensor.TYPE) {
            AutoCompleteTextView sensitivityMenu = (AutoCompleteTextView) sensitivityMenuLayout.getEditText();
            assert sensitivityMenu != null;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.theme_list_item,
                    view.getResources().getStringArray(R.array.duration_sensitivity_options));
            sensitivityMenu.setAdapter(adapter);

            model.fetchMotionSensors().observe(
                    getViewLifecycleOwner(),
                    sensors -> {
                        sensor = sensors.get(sensorId);
                        checkBoxRCAdapter.setSensor(sensor);
                        setupName();

                        MotionSensor motionSensor = (MotionSensor) sensor;
                        assert motionSensor != null;
                        // Only try to set the text if it is not blank
                        if (motionSensor.getDurationSensitivity() != null && !motionSensor.getDurationSensitivity().equals("")) {
                            // Format is just the number of it is less than 60 minutes, and HH:mm otherwise
                            int minute = motionSensor.getDurationSensitivityMinute();
                            int hour = motionSensor.getDurationSensitivityHour();
                            String text;
                            if (hour == 0) {
                                text = String.valueOf(minute);
                            } else {
                                text = hour + ":" + minute;
                            }
                            sensitivityMenu.setText(text, false);
                        }

                        sensitivityMenu.setOnItemClickListener((parent, v, position, id) ->
                                motionSensor.setDurationSensitivity(0, Integer.parseInt(parent.getItemAtPosition(position).toString())));
                    });
        } else {
            ((ViewGroup) sensitivityMenuLayout.getParent()).removeView(sensitivityMenuLayout);

            model.fetchLightSensors().observe(
                    getViewLifecycleOwner(),
                    sensors -> {
                        sensor = sensors.get(sensorId);
                        checkBoxRCAdapter.setSensor(sensor);
                        setupName();
                    });
        }

        return view;
    }

    private void setupName() {
        nameEditText.setText(sensor.getName());
        nameEditText.addTextChangedListener(new TextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s) {
                sensor.setName(s.toString());
            }
        });
    }

    @Override
    public void onStop() {
        // Send changes to the motor state when the settings are closed
        // Check if null in case something is wrong or it hasn't loaded in yet
        if (sensor != null) {
            model.pushSensorState(sensor);
            Toast.makeText(requireContext(), requireContext().getString(R.string.updated), Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }
}