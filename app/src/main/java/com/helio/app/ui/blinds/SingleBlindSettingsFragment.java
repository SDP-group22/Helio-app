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
import androidx.navigation.Navigation;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;
import com.helio.app.ui.SingleComponentSettingsFragment;

public class SingleBlindSettingsFragment extends SingleComponentSettingsFragment<Motor> {

    private void setActionListeners(View view) {
        // "open" button
        view.findViewById(R.id.btn_open).setOnClickListener(v -> {
            System.out.println("OPEN button pressed for " + component);
            getModel().moveCurrentMotor(100);
        });
        // "close" button
        view.findViewById(R.id.btn_close).setOnClickListener(v -> {
            System.out.println("CLOSE button pressed for " + component);
            getModel().moveCurrentMotor(0);
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

        getModel().setCurrentMotor(motorId);
        getModel().fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> {
                    component = motors.get(motorId);

                    assert namePreference != null;
                    assert ipPreference != null;
                    assert calibrationPreference != null;
                    assert component != null;
                    namePreference.setText(component.getName());
                    ipPreference.setText(component.getIp());

                    assert iconPreference != null;

                    // It has crashed before because this was null after deleting.
                    // It is hard to reproduce and I think it might be a race condition, so solving by checking for null here
                    // If it is null it will only be as this page is closing so it won't matter
                    if (component != null) {
                        namePreference.setText(component.getName());
                        ipPreference.setText(component.getIp());
                        if (component.getIcon() == null) {
                            // If motor has no icon then set to None
                            iconPreference.setValueIndex(iconPreference.getEntryValues().length - 1);
                        } else {
                            iconPreference.setValue(component.getIcon().name);
                        }

                        namePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                            component.setName((String) newValue);
                            return true;
                        });
                        ipPreference.setOnPreferenceChangeListener(((preference, newValue) -> {
                            component.setIp((String) newValue);
                            return true;
                        }));
                        iconPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                            component.setStyle((String) newValue);
                            return true;
                        });
                        calibrationPreference.setOnPreferenceClickListener(preference -> {
                            // Start calibration before opening page
                            getModel().startCalibration(component);

                            SingleBlindSettingsFragmentDirections.ActionSingleBlindSettingsFragmentToCalibrationFragment action =
                                    SingleBlindSettingsFragmentDirections.actionSingleBlindSettingsFragmentToCalibrationFragment();
                            action.setCurrentMotorId(component.getId());
                            Navigation.findNavController(view).navigate(action);
                            return false;
                        });
                    }
                }
        );
        setActionListeners(view);
        return view;
    }

    @Override
    public void onStop() {
        // Send changes to the motor state when the settings are closed
        // Check if null in case something is wrong or it hasn't loaded in yet
        if (component != null) {
            getModel().pushCurrentMotorState(component);
            Toast.makeText(requireContext(), requireContext().getString(R.string.component_updated), Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }
}