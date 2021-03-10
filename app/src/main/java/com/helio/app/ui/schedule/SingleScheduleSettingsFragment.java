package com.helio.app.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Schedule;

public class SingleScheduleSettingsFragment extends Fragment {
    private Schedule schedule;
    private UserDataViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_schedule_settings, container, false);
        assert getArguments() != null;
        int scheduleId = getArguments().getInt("currentScheduleId");

        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        model.setCurrentSchedule(scheduleId);
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                schedules -> schedule = schedules.get(scheduleId)
        );


        return view;
    }


}
