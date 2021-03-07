package com.helio.app.ui.schedule;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleScheduleSettingsFragment extends Fragment {
    private Schedule schedule;
    private UserDataViewModel model;
    private TextView timer;
    int t1Hour,t1Minute;

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
                schedules ->{
                    schedule = schedules.get(scheduleId);
                }
        );
        // set the timer1 by time clicker
        timer = view.findViewById(R.id.schedule_timeSelect);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Initialize hour and minutes
                                t1Hour = hourOfDay;
                                t1Minute = minute;
                                // Store hour and minute in string
                                String time = t1Hour + ":" + t1Minute;
                                // Initialize 24 hours time format
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try{
                                    Date date = f24Hours.parse(time);
                                    // Initialize 12 hours time format
                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    //Set selected time on text view
                                    timer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },12,0,false);
                //Set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //Displayed previous selected time
                timePickerDialog.updateTime(t1Hour,t1Minute);
                //Show dialog
                timePickerDialog.show();
            }
        });

        return view;
    }



}
