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

import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.R;
import com.helio.app.model.Motor;
import com.helio.app.networking.IPAddress;
import com.helio.app.ui.SingleComponentSettingsFragment;
import com.helio.app.ui.utils.TextChangedListener;

public class SingleBlindSettingsFragment extends SingleComponentSettingsFragment<Motor> {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_blind_settings, container, false);
        assert getArguments() != null;
        int motorId = getArguments().getInt("currentMotorId");

        EditText nameEditText = view.<TextInputLayout>findViewById(R.id.blinds_name).getEditText();
        TextInputLayout ipEditLayout = view.findViewById(R.id.blinds_ip_address);
        EditText ipEditText = ipEditLayout.getEditText();
        assert nameEditText != null;
        assert ipEditText != null;

        TextInputLayout iconMenuLayoutView = view.findViewById(R.id.dropdown_icons);

        // Set the blinds icon
        AutoCompleteTextView iconMenu = (AutoCompleteTextView) iconMenuLayoutView.getEditText();
        assert iconMenu != null;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_list_item,
                view.getResources().getStringArray(R.array.icons));
        iconMenu.setAdapter(adapter);

        getModel().fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> {
                    component = motors.get(motorId);
                    assert component != null;

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
        );

        return view;
    }
}