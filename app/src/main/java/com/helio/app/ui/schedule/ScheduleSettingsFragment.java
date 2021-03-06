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

import com.helio.app.MainActivity;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleSettingsFragment extends Fragment {
    private UserDataViewModel model;
    private Schedule schedule;
    private TextView timer1;
    private int t1Hour,t1Minute;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_settings, container, false);
        int scheduleId = getArguments().getInt("currentScheduleId");

        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        model.setCurrentMotor(scheduleId);
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                schedules -> {
                    schedule = schedules.get(scheduleId);
                    // Set values of the settings UI with view.findViewById() and the values of schedule

                    // set the timer1 by time clicker
                    timer1 = view.findViewById(R.id.timer1);
                    timer1.setOnClickListener(new View.OnClickListener() {
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
                                        timer1.setText(f12Hours.format(date));
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
                }
        );
        return view;
    }
}
