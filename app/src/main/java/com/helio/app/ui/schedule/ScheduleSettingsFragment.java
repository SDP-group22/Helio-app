package com.helio.app.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Schedule;

public class ScheduleSettingsFragment extends Fragment {
    private UserDataViewModel model;
    private Schedule schedule;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        int scheduleId = getArguments().getInt("currentScheduleId");

        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        model.setCurrentMotor(scheduleId);
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                schedules -> {
                    schedule = schedules.get(scheduleId);
                    // Set values of the settings UI with view.findViewById() and the values of schedule
                }
        );
        return view;
    }
}
