package com.helio.app.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.helio.app.MainActivity;
import com.helio.app.R;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setupThemeDropdown(view);
        return view;
    }

    private void setupThemeDropdown(View view) {
        Spinner themeSpinner = (Spinner) view.findViewById(R.id.theme_selection_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.theme_options, android.R.layout.simple_spinner_item
        );
        themeSpinner.setAdapter(adapter);
        selectCurrentTheme(themeSpinner);
        // listen for updates
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                final String newThemeName = parent.getItemAtPosition(position).toString();
                ((MainActivity) getActivity()).updateTheme(newThemeName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void selectCurrentTheme(Spinner spinner) {
        String currentThemeName = ((MainActivity) getActivity()).getCurrentThemeName();
        final int count = spinner.getCount();
        for(int i = 0; i < count; i++) {
            String itemValue = (String) spinner.getItemAtPosition(i);
            if(itemValue.equals(currentThemeName)) {
                System.out.println("Setting spinner to \"" + itemValue + "\"...");
                spinner.setSelection(i);
                break;
            }
        }
    }
}