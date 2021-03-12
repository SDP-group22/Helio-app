package com.helio.app.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.helio.app.MainActivity;
import com.helio.app.R;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setupThemeDropdown(view);
        return view;
    }

    private void setupThemeDropdown(View view) {

        AutoCompleteTextView themeMenu = (AutoCompleteTextView) view.<TextInputLayout>findViewById(R.id.theme_menu).getEditText();
        assert themeMenu != null;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.theme_list_item, view.getResources().getStringArray(R.array.theme_options));
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