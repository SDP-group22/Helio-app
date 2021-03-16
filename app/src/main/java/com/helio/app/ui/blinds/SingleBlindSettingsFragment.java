package com.helio.app.ui.blinds;

import android.os.Bundle;
import android.text.InputType;
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
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;
import com.helio.app.ui.utils.ContextColourProvider;
import com.helio.app.ui.utils.MotorIdsBlindsCheckboxRecViewAdapter;
import com.helio.app.ui.utils.TextChangedListener;

import java.util.ArrayList;
import java.util.Objects;

public class SingleBlindSettingsFragment extends Fragment {
    private Motor motor;
    private UserDataViewModel model;
    private EditText nameEditText;
    private EditText ipEditText;
    private int fillColour;
    private int backgroundColour;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_blind_settings, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        assert getArguments() != null;
        int motorId = getArguments().getInt("currentMotorId");
        fillColour = ContextColourProvider.getColour(getContext(), android.R.attr.colorPrimary);
        backgroundColour = ContextColourProvider.getColour(getContext(), android.R.attr.windowBackground);

        nameEditText = view.<TextInputLayout>findViewById(R.id.blinds_name).getEditText();
        ipEditText = view.<TextInputLayout>findViewById(R.id.blinds_ip_address).getEditText();
        Slider levelSlider = view.findViewById(R.id.blinds_level);

        MotorIdsBlindsCheckboxRecViewAdapter checkBoxRCAdapter = new MotorIdsBlindsCheckboxRecViewAdapter();
        TextInputLayout iconMenuLayoutview = view.findViewById(R.id.dropdown_icons);
        // Set the blinds icon
        AutoCompleteTextView iconMenu = (AutoCompleteTextView) iconMenuLayoutview.getEditText();
        assert iconMenu != null;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_list_item,
                view.getResources().getStringArray(R.array.icons));
        iconMenu.setAdapter(adapter);

        model.fetchMotors().observe(
                getViewLifecycleOwner(),
                motors-> {
                    motor = motors.get(motorId);
                    levelSlider.setValue(motor.getLevel());
                    levelSlider.addOnChangeListener((slider, value, fromUser) -> motor.setLevel((int) value));
                    nameEditText.setText(motor.getName());
                    nameEditText.addTextChangedListener(new TextChangedListener() {
                        @Override
                        public void onTextChanged(CharSequence s) {
                            motor.setName(s.toString());
                        }
                    });
                    ipEditText.setText(motor.getIp());
                    ipEditText.addTextChangedListener(new TextChangedListener() {
                        @Override
                        public void onTextChanged(CharSequence s) {
                            motor.setIp(s.toString());
                        }
                    });
                }
        );
        model.fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> checkBoxRCAdapter.setMotors(new ArrayList<>(motors.values()))
        );

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