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

public class SensorsSettingsFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors_settings, container, false);
        UserDataViewModel model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        SensorsRecViewAdapter adapter = new SensorsRecViewAdapter(getContext(), model);

        model.fetchLightSensors().observe(
                getViewLifecycleOwner(),
                sensors -> adapter.setLightSensors(sensors.values())
        );

        model.fetchMotionSensors().observe(
                getViewLifecycleOwner(),
                sensors -> adapter.setMotionSensors(sensors.values())
        );

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.sensorsRCView);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}
