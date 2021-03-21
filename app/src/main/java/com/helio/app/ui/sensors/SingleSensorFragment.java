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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.R;
import com.helio.app.model.LightSensor;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Sensor;
import com.helio.app.networking.IPAddress;
import com.helio.app.ui.SingleComponentFragment;
import com.helio.app.ui.utils.MotorIdsBlindsCheckboxRecViewAdapter;
import com.helio.app.ui.utils.TextChangedListener;

import java.util.ArrayList;

public class SingleSensorFragment extends SingleComponentFragment<Sensor> {
    private EditText nameEditText;
    private TextInputLayout ipEditLayout;
    private EditText ipEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_sensor_settings, container, false);
        assert getArguments() != null;
        int sensorId = getArguments().getInt("currentSensorId");
        int sensorType = getArguments().getInt("sensorType");


        ipEditLayout = view.findViewById(R.id.name);
        nameEditText = ipEditLayout.getEditText();
        ipEditText = view.<TextInputLayout>findViewById(R.id.ip_address).getEditText();

        MotorIdsBlindsCheckboxRecViewAdapter checkBoxRCAdapter = new MotorIdsBlindsCheckboxRecViewAdapter();
        getModel().fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> checkBoxRCAdapter.setMotors(new ArrayList<>(motors.values()))
        );

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.blindsRCView);
        recView.setAdapter(checkBoxRCAdapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextInputLayout sensitivityMenuLayout = view.findViewById(R.id.dropdown_sensitivity);

        // Only motion sensors have duration sensitivity
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        if (sensorType == MotionSensor.TYPE) {
            actionBar.setTitle(R.string.motion_sensor);

            AutoCompleteTextView sensitivityMenu = (AutoCompleteTextView) sensitivityMenuLayout.getEditText();
            assert sensitivityMenu != null;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_list_item,
                    view.getResources().getStringArray(R.array.duration_sensitivity_options));
            sensitivityMenu.setAdapter(adapter);

            getModel().fetchMotionSensors().observe(
                    getViewLifecycleOwner(),
                    sensors -> {
                        component = sensors.get(sensorId);
                        if (component != null) {
                            checkBoxRCAdapter.setComponent(component);
                            setupNameIp();
                            setupSensitivity(sensitivityMenu);
                        }
                    });
        } else if (sensorType == LightSensor.TYPE) {
            actionBar.setTitle(R.string.light_sensor);
            ((ViewGroup) sensitivityMenuLayout.getParent()).removeView(sensitivityMenuLayout);

            getModel().fetchLightSensors().observe(
                    getViewLifecycleOwner(),
                    sensors -> {
                        component = sensors.get(sensorId);
                        if (component != null) {
                            checkBoxRCAdapter.setComponent(component);
                            setupNameIp();
                        }
                    });
        }

        return view;
    }

    private void setupSensitivity(AutoCompleteTextView sensitivityMenu) {
        MotionSensor motionSensor = (MotionSensor) component;
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
    }

    private void setupNameIp() {
        nameEditText.setText(component.getName());
        nameEditText.addTextChangedListener(new TextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s) {
                component.setName(s.toString());
            }
        });

        ipEditText.setText(component.getIp());
        ipEditText.addTextChangedListener(new TextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s) {
                if (IPAddress.correctFormat(s.toString())) {
                    // Clear the error message if there is one
                    ipEditLayout.setError(null);

                    component.setIp(s.toString());

                    Toast.makeText(requireContext(), requireContext().getString(R.string.ip_address_set), Toast.LENGTH_SHORT).show();
                } else {
                    ipEditLayout.setError(requireContext().getString(R.string.ip_incorrect_format));
                }
            }
        });
    }
}