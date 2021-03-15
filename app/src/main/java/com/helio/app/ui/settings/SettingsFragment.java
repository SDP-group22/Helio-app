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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.MainActivity;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.IPAddress;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    UserDataViewModel model;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        setupThemeDropdown(view);
        setupIpAddress(view);
        return view;
    }

    private void setupIpAddress(View view) {
        TextInputLayout ipEditLayout = view.findViewById(R.id.ip_address);
        EditText ipEditText = ipEditLayout.getEditText();
        Button connectButton = view.findViewById(R.id.connect_button);
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

                // Reset the IP in the API connection
                model.setHubIp(ip);

                Toast.makeText(requireContext(), requireContext().getString(R.string.ip_address_set), Toast.LENGTH_SHORT).show();
            } else {
                ipEditLayout.setError("Incorrect format");
            }
        });
    }

    private void setupThemeDropdown(View view) {

        AutoCompleteTextView themeMenu = (AutoCompleteTextView) view.<TextInputLayout>findViewById(R.id.theme_menu).getEditText();
        assert themeMenu != null;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_list_item, view.getResources().getStringArray(R.array.theme_options));
        themeMenu.setAdapter(adapter);
        selectCurrentTheme(themeMenu);
        themeMenu.setOnItemClickListener((parent, v, position, id) -> {
            final String newThemeName = parent.getItemAtPosition(position).toString();
            ((MainActivity) requireActivity()).updateTheme(newThemeName);
        });
    }

    private void selectCurrentTheme(AutoCompleteTextView themeMenu) {
        ListAdapter adapter = Objects.requireNonNull(themeMenu).getAdapter();
        String currentThemeName = ((MainActivity) requireActivity()).getCurrentThemeName();
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            String itemValue = (String) adapter.getItem(i);
            if (itemValue.equals(currentThemeName)) {
                System.out.println("Setting spinner to \"" + itemValue + "\"...");
                themeMenu.setText(currentThemeName, false);
                break;
            }
        }
    }
}