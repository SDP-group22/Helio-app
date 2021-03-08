package com.helio.app.ui.settings;

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

import com.helio.app.R;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Spinner themeSpinner = (Spinner) view.findViewById(R.id.theme_selection_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                view.getContext(), R.array.theme_options, android.R.layout.simple_spinner_item
        );
        themeSpinner.setAdapter(adapter);
        // listen for updates
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                final String newThemeName = parent.getItemAtPosition(position).toString();
                int newTheme = R.style.Theme_HelioApp;
                if(newThemeName.equals("Night")) {
                    System.out.println("Switching app theme to \"" + newThemeName + "\"...");
                    newTheme = R.style.Theme_HelioApp_Night;
                } else {
                    System.out.println("Unknown theme \"" + newThemeName + "\". " +
                            "Switching to default theme...");
                }
                getActivity().setTheme(newTheme);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}