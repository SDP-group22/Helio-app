package com.helio.app.ui.blinds;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.R;
import com.helio.app.model.Motor;
import com.helio.app.networking.IPAddress;
import com.helio.app.ui.SingleComponentSettingsFragment;
import com.helio.app.ui.utils.TextChangedListener;

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

//        SingleBlindSettingsPreferencesFragment preferenceFragment = (SingleBlindSettingsPreferencesFragment)
//                getChildFragmentManager().findFragmentById(R.id.fragment_container_preferences);
//        assert preferenceFragment != null;
//        EditTextPreference namePreference = preferenceFragment.findPreference("name");
//        EditTextPreference ipPreference = preferenceFragment.findPreference("ip");
//        ListPreference iconPreference = preferenceFragment.findPreference("icon");
//        Preference calibrationPreference = preferenceFragment.findPreference("calibration");

        getModel().fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> {
                    component = motors.get(motorId);
                    assert component != null;

//                    nameEditText.setText(component.getName());
//                    nameEditText.addTextChangedListener(new TextChangedListener() {
//                        @Override
//                        public void onTextChanged(CharSequence s) {
//                            component.setName(s.toString());
//                        }
//                    });
//
//                    assert namePreference != null;
//                    assert ipPreference != null;
//                    assert calibrationPreference != null;
//                    assert component != null;
//                    namePreference.setText(component.getName());
//                    ipPreference.setText(component.getIp());
//
//                    assert iconPreference != null;
//                    ipEditText.setText(component.getIp());
//                    ipEditText.addTextChangedListener(new TextChangedListener() {
//                        @Override
//                        public void onTextChanged(CharSequence s) {
//
//                            if (IPAddress.correctFormat(s.toString())) {
//                                // Clear the error message if there is one
//                                ipEditLayout.setError(null);
//
//                                component.setIp(s.toString());
//
//                                Toast.makeText(requireContext(), requireContext().getString(R.string.ip_address_set), Toast.LENGTH_SHORT).show();
//                            } else {
//                                ipEditLayout.setError(requireContext().getString(R.string.ip_incorrect_format));
//                            }
//                        }
//                    });
//                }
//        );




    }
        );
        return view;
}}