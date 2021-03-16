package com.helio.app.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.MainActivity;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.networking.IPAddress;
import com.helio.app.networking.NetworkStatus;
import com.helio.app.ui.utils.IdString;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private UserDataViewModel model;
    private TextInputLayout ipEditLayout;
    private Button connectButton;
    private AutoCompleteTextView themeMenu;
    private TextView connectionStatus;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        ipEditLayout = view.findViewById(R.id.ip_address);
        connectButton = view.findViewById(R.id.connect_button);
        themeMenu = (AutoCompleteTextView) view.<TextInputLayout>findViewById(R.id.theme_menu).getEditText();
        connectionStatus = view.findViewById(R.id.connection_status);

        setupThemeDropdown();
        setupIpAddress();
        updateConnectionStatus();
        return view;
    }

    private void updateConnectionStatus() {
        // Set to disconnected while we wait for the update to come in.
        // This avoids a delay of it still saying connected after putting in an incorrect IP address.
        connectionStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(android.R.drawable.ic_delete, 0, 0, 0);
        connectionStatus.setText(R.string.not_connected);

        // Observe the connection and update to connected when available
        model.getNetworkStatus().observe(
                getViewLifecycleOwner(),
                status -> {
                    if (status == NetworkStatus.CONNECTED) {
                        connectionStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_wifi_24, 0, 0, 0);
                        connectionStatus.setText(R.string.connected);
                    }
                }
        );

    }

    private void setupIpAddress() {
        EditText ipEditText = ipEditLayout.getEditText();
        assert ipEditText != null;
        ipEditText.setText(model.getHubIp());

        // Click the connect button automatically if the user presses enter
        ipEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                connectButton.performClick();
                return false;
            }
            return false;
        });

        connectButton.setOnClickListener(v -> {
            String ip = ipEditText.getText().toString();
            if (IPAddress.correctFormat(ip)) {
                // Clear the error message if there is one
                ipEditLayout.setError(null);

                // Close the keyboard
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ipEditText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                // Reset the IP in the API connection and refresh connection status
                model.setHubIp(ip);
                updateConnectionStatus();

                Toast.makeText(requireContext(), requireContext().getString(R.string.ip_address_set), Toast.LENGTH_SHORT).show();
            } else {
                ipEditLayout.setError(requireContext().getString(R.string.ip_incorrect_format));
            }
        });
    }

    private void setupThemeDropdown() {
        assert themeMenu != null;

        IdString[] themes = new IdString[]{new IdString(R.string.default_theme, requireContext()),
                new IdString(R.string.night_theme, requireContext()),
                new IdString(R.string.high_contrast_theme, requireContext())};

        ArrayAdapter<IdString> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_list_item, themes);
        themeMenu.setAdapter(adapter);
        selectCurrentTheme(themeMenu);
        themeMenu.setOnItemClickListener((parent, v, position, id) -> {
            final int newTheme = ((IdString) parent.getItemAtPosition(position)).getId();
            ((MainActivity) requireActivity()).updateTheme(newTheme);
        });
    }

    private void selectCurrentTheme(AutoCompleteTextView themeMenu) {
        ListAdapter adapter = Objects.requireNonNull(themeMenu).getAdapter();
        int currentThemeId = ((MainActivity) requireActivity()).getCurrentThemeId();
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            int itemValue = ((IdString) adapter.getItem(i)).getId();
            if (itemValue == currentThemeId) {
                System.out.println("Setting spinner to \"" + itemValue + "\"...");
                themeMenu.setText(getString(itemValue), false);
                break;
            }
        }
    }
}