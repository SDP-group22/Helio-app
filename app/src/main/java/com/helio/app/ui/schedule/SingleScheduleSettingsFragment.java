package com.helio.app.ui.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Day;
import com.helio.app.model.Schedule;
import com.helio.app.ui.utils.TextChangedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingleScheduleSettingsFragment extends Fragment {
    private Schedule schedule;
    private UserDataViewModel model;
    private int fillColour;
    private int backgroundColour;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_schedule_settings, container, false);
        assert getArguments() != null;
        int scheduleId = getArguments().getInt("currentScheduleId");
        fillColour = getFillColour();
        backgroundColour = getBackgroundColour();

        TextInputLayout name = view.findViewById(R.id.schedule_name);
        EditText nameEditText = name.getEditText();
        MaterialButton timeButton = view.findViewById(R.id.time_button);
        Slider levelSlider = view.findViewById(R.id.level_slider);


        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        model.setCurrentSchedule(scheduleId);
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                schedules -> {
                    schedule = schedules.get(scheduleId);

                    Objects.requireNonNull(nameEditText).setInputType(InputType.TYPE_CLASS_TEXT);
                    nameEditText.setText(schedule.getName());
                    timeButton.setText(schedule.getFormattedTime());
                    levelSlider.setValue(schedule.getTargetLevel());

                    nameEditText.addTextChangedListener(new TextChangedListener() {
                        @Override
                        public void onTextChanged(CharSequence s) {
                            schedule.setName(s.toString());
                        }
                    });

                    timeButton.setOnClickListener(v -> {
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
                            timeButton.setText(schedule.getFormattedTime());
                        });
                    });

                    levelSlider.addOnChangeListener((slider, value, fromUser) -> schedule.setTargetLevel((int) value));

                    prepareDays(view.findViewById(R.id.days_layout));
                }
        );

        return view;
    }

    private void prepareDays(ConstraintLayout daysLayout) {
        // Get days in local order
        Day firstLocalDay = Day.getEnumFromName(requireContext().getString(R.string.first_day));
        String[] shortDaysFromResource = requireContext().getResources().getStringArray(R.array.weekdaysShort);
        List<String> shortWeekdays = Day.getShortDaysLocalOrder(shortDaysFromResource, firstLocalDay);
        List<Day> daysLocalOrder = Day.getValuesLocalOrder(firstLocalDay);

        // Get all the day buttons
        List<MaterialButton> dayButtonList = new ArrayList<>();
        for (int i = 0; i < daysLayout.getChildCount(); i++) {
            dayButtonList.add((MaterialButton) daysLayout.getChildAt(i));
        }
        for (int i = 0; i < dayButtonList.size(); i++) {
            dayButtonList.get(i).setText(shortWeekdays.get(i));
        }

        for (int i = 0; i < dayButtonList.size(); i++) {
            Day day = daysLocalOrder.get(i);
            final MaterialButton button = dayButtonList.get(i);

            // Change the style depending on whether it is in the schedule or not
            styleButton(button, schedule.containsDay(day));

            // Change the style and update the schedule on click
            button.setOnClickListener(v -> {
                if (schedule.containsDay(day)) {
                    schedule.getDays().remove(day);
                } else {
                    schedule.getDays().add(day);
                }
                styleButton(button, schedule.containsDay(day));
            });
        }
    }

    private void styleButton(MaterialButton button, boolean dayActive) {
        if (dayActive) {
            button.setTextAppearance(getContext(), R.style.dayButtonStyle);
            button.setBackgroundColor(fillColour);
        } else {
            button.setTextAppearance(getContext(), R.style.dayButtonStyleOutline);
            button.setBackgroundColor(backgroundColour);
        }
    }

    private int getBackgroundColour() {
        TypedArray themeArray = requireContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int secondaryColour;
        try {
            secondaryColour = themeArray.getColor(0, 0);
        } finally {
            themeArray.recycle();
        }
        return secondaryColour;
    }

    private int getFillColour() {
        TypedArray themeArray = requireContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorPrimary});
        int secondaryColour;
        try {
            secondaryColour = themeArray.getColor(0, 0);
        } finally {
            themeArray.recycle();
        }
        return secondaryColour;
    }

    @Override
    public void onStop() {
        if (schedule != null) {
            model.pushScheduleState(schedule);
        }
        super.onStop();
    }
}
