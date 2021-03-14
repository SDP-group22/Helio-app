package com.helio.app.ui.schedules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;

import java.util.ArrayList;

public class SchedulesSettingsFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_settings, container, false);
        UserDataViewModel model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        SchedulesRecViewAdapter adapter = new SchedulesRecViewAdapter(getContext(), model);
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                Schedules -> adapter.setSchedules(new ArrayList<>(Schedules.values()))
        );

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.schedulesRCView);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(
                v -> model.addSchedule().observe(
                        getViewLifecycleOwner(),
                        schedules -> adapter.setSchedules(new ArrayList<>(schedules.values()))
                )
        );

        return view;
    }
}
