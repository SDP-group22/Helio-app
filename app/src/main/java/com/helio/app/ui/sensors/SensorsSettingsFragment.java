package com.helio.app.ui.sensors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Sensor;

public class SensorsSettingsFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors_settings, container, false);
        UserDataViewModel model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        // Currently I didn't know which sensor will be chosen.
        Sensor sensor = null;
        SensorsRecViewAdapter adapter = new SensorsRecViewAdapter(getContext(), model,sensor);
        
        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.sensorsRCView);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }



//    // Adam Sorry I just note your code because currently I am using different method to write about this part.from Jeremy
//    @Override
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        setPreferencesFromResource(R.xml.sensor_preferences, rootKey);
//
//        EditTextPreference namePreference = findPreference("name");
//        EditTextPreference ipPreference = findPreference("ip");
//        EditTextPreference inactivityDurationPreference = findPreference("inactivityDuration");
//        ListPreference changeIconPreference = findPreference("changeIcon");
//        ListPreference sensorTypePreference = findPreference("sensorType");
//
//
//        assert ipPreference != null;
//        ipPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_PHONE));
//
//        assert namePreference != null;
//        namePreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_TEXT));
//
//        assert inactivityDurationPreference != null;
//        inactivityDurationPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
//
//        assert sensorTypePreference != null;
//        String motionSensorString = getResources().getString(R.string.motion_sensor);
//        // Make duration visible if is motion sensor
//        inactivityDurationPreference.setVisible(sensorTypePreference.getValue().equals(motionSensorString));
//
//        // Update when sensor type changes
//        sensorTypePreference.setOnPreferenceChangeListener((preference, newValue) -> {
//            inactivityDurationPreference.setVisible(newValue.equals(motionSensorString));
//            return true;
//        });
//    }
}