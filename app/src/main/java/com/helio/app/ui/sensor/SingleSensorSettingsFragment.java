package com.helio.app.ui.sensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.MainActivity;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Sensor;

public class SingleSensorSettingsFragment extends Fragment {
    private UserDataViewModel model;
    private Sensor sensor;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView dropDownText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_sensor_settings, container, false);
        UserDataViewModel model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        assert getArguments() != null;

        textInputLayout = view.findViewById(R.id.motion_sensor_sensitivity);
        dropDownText = view.findViewById(R.id.dropdown_sensitivity);
        String [] sensitivityItem = new String[]{
                "1 seconds",
                "3 seconds",
                "5 seconds",
                "10 seconds",
                "30 seconds",
                "60 seconds" };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(SingleSensorSettingsFragment.this,R.layout.dropdown_motion_sensitivity,sensitivityItem);
        dropDownText.setAdapter(adapter);

        return view;
    }

}
