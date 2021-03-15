package com.helio.app.ui.blinds;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;

public class SingleBlindSettingsFragment extends Fragment {

    private Motor motor;
    private UserDataViewModel model;

    private void setActionListeners(View view) {
        // "open" button
        view.findViewById(R.id.btn_open).setOnClickListener(v -> {
            System.out.println("OPEN button pressed for " + motor);
            model.moveCurrentMotor(100);
        });
        // "close" button
        view.findViewById(R.id.btn_close).setOnClickListener(v -> {
            System.out.println("CLOSE button pressed for " + motor);
            model.moveCurrentMotor(0);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_blind_settings, container, false);
        assert getArguments() != null;
        int motorId = getArguments().getInt("currentMotorId");

        SingleBlindSettingsPreferencesFragment preferenceFragment = (SingleBlindSettingsPreferencesFragment)
                getChildFragmentManager().findFragmentById(R.id.fragment_container_preferences);
        assert preferenceFragment != null;
        EditTextPreference namePreference = preferenceFragment.findPreference("name");
        EditTextPreference ipPreference = preferenceFragment.findPreference("ip");
        ListPreference iconPreference = preferenceFragment.findPreference("icon");
        Preference calibrationPreference = preferenceFragment.findPreference("calibration");

        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        model.setCurrentMotor(motorId);
        model.fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> {
                    motor = motors.get(motorId);

                    assert namePreference != null;
                    assert ipPreference != null;
                    assert calibrationPreference != null;
                    assert motor != null;
                    namePreference.setText(motor.getName());
                    ipPreference.setText(motor.getIp());

                    assert iconPreference != null;
                    if (motor.getIcon() == null) {
                        // If motor has no icon then set to None
                        iconPreference.setValueIndex(iconPreference.getEntryValues().length - 1);
                    } else {
                        iconPreference.setValue(motor.getIcon().name);
                    }

                    namePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                        motor.setName((String) newValue);
                        return true;
                    });
                    ipPreference.setOnPreferenceChangeListener(((preference, newValue) -> {
                        motor.setIp((String) newValue);
                        return true;
                    }));
                    iconPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                        motor.setStyle((String) newValue);
                        return true;
                    });
                    calibrationPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            SingleBlindSettingsFragmentDirections.ActionSingleBlindSettingsFragmentToCalibrationFragment action =
                                    SingleBlindSettingsFragmentDirections.actionSingleBlindSettingsFragmentToCalibrationFragment();
                            action.setCurrentMotorId(motor.getId());
                            Navigation.findNavController(view).navigate(action);
                            return false;
                        }
                    });
                }
        );
        setActionListeners(view);
        return view;
    }

    @Override
    public void onStop() {
        // Send changes to the motor state when the settings are closed
        // Check if null in case something is wrong or it hasn't loaded in yet
        if (motor != null) {
            model.pushCurrentMotorState(motor);
            Toast.makeText(requireContext(), requireContext().getString(R.string.component_updated), Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }
}