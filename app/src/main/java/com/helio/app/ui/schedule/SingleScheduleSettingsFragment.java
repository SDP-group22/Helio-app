package com.helio.app.ui.schedule;

import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Schedule;
import com.helio.app.ui.utils.TextChangedListener;

import java.util.Objects;

public class SingleScheduleSettingsFragment extends Fragment {
    private Schedule schedule;
    private UserDataViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_schedule_settings, container, false);
        assert getArguments() != null;
        int scheduleId = getArguments().getInt("currentScheduleId");

        TextInputLayout name = view.findViewById(R.id.schedule_name);
        EditText nameEditText = name.getEditText();
        MaterialButton button = view.findViewById(R.id.time_button);

        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        model.setCurrentSchedule(scheduleId);
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                schedules -> {
                    schedule = schedules.get(scheduleId);

                    Objects.requireNonNull(nameEditText).setInputType(InputType.TYPE_CLASS_TEXT);
                    nameEditText.setText(schedule.getName());
                    button.setText(schedule.getFormattedTime());

                    nameEditText.addTextChangedListener(new TextChangedListener() {
                        @Override
                        public void onTextChanged(CharSequence s) {
                            schedule.setName(s.toString());
                        }
                    });

                    button.setOnClickListener(v -> {
                        // Show a time picker, with the appropriate time format, and setting it to the current schedule time
                        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                                .setTimeFormat(DateFormat.is24HourFormat(getContext()) ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H)
                                .setHour(schedule.getTimeHour())
                                .setMinute(schedule.getTimeMinute())
                                .build();
                        picker.show(getParentFragmentManager(), "schedule_time_select");

                        // Unlike with the text and sliders elsewhere, the button text needs to be updated
                        picker.addOnDismissListener(dialog -> {
                            schedule.setTime(picker.getHour(), picker.getMinute());
                            button.setText(schedule.getFormattedTime());
                        });
                    });
                }
        );

        return view;
    }

    @Override
    public void onStop() {
        if (schedule != null) {
            model.pushScheduleState(schedule);
        }
        super.onStop();
    }
}
