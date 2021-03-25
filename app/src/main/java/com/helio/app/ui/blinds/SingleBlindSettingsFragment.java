package com.helio.app.ui.blinds;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.R;
import com.helio.app.model.Motor;
import com.helio.app.networking.IPAddress;
import com.helio.app.ui.SingleComponentSettingsFragment;
import com.helio.app.ui.utils.TextChangedListener;

import java.util.ArrayList;

public class SingleBlindSettingsFragment extends SingleComponentSettingsFragment<Motor> implements AdapterView.OnItemSelectedListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_blind_settings, container, false);
        assert getArguments() != null;
        int motorId = getArguments().getInt("currentMotorId");

        EditText nameEditText = view.<TextInputLayout>findViewById(R.id.blinds_name).getEditText();
        TextInputLayout ipEditLayout = view.findViewById(R.id.blinds_ip_address);
        EditText ipEditText = ipEditLayout.getEditText();
        Button calibrationButton = view.findViewById(R.id.calibration);
        assert nameEditText != null;
        assert ipEditText != null;
        assert calibrationButton != null;

        // Spinner
        Spinner customSpinner = view.findViewById(R.id.blinds_icon_spinner);
        assert customSpinner != null;
        ArrayList<Integer> iconItemList = new ArrayList<>();

        // Get array of resource ids (this annoying way is necessary)
        TypedArray ar = view.getResources().obtainTypedArray(R.array.icon_ids);
        int[] iconIds = new int[ar.length()];
        for (int i = 0; i < ar.length(); i++) {
            iconIds[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();

        for (int iconId : iconIds) {
            iconItemList.add(iconId);
        }

        IconArrayAdapter adapter = new IconArrayAdapter(requireContext(), iconItemList);
        customSpinner.setAdapter(adapter);
        customSpinner.setOnItemSelectedListener(this);

        getModel().fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> {
                    component = motors.get(motorId);
                    if (component != null) {

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


                        calibrationButton.setOnClickListener(v -> {
                            // Start calibration before opening page
                            getModel().startCalibration(component);

                            SingleBlindSettingsFragmentDirections.ActionSingleBlindSettingsFragmentToCalibrationFragment action =
                                    SingleBlindSettingsFragmentDirections.actionSingleBlindSettingsFragmentToCalibrationFragment();
                            action.setCurrentMotorId(component.getId());
                            Navigation.findNavController(view).navigate(action);
                        });

                        // Set the icon
                        for (int i = 0; i < iconIds.length; i++) {
                            if (iconIds[i] == component.getIconId()) {
                                customSpinner.setSelection(i);
                            }
                        }
                    }
                }
        );

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (component != null) {
            component.setIconId((Integer) adapterView.getSelectedItem());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}