package com.helio.app.ui.schedules;

import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Day;
import com.helio.app.model.Schedule;
import com.helio.app.ui.utils.ContextColourProvider;
import com.helio.app.ui.utils.MotorIdsBlindsCheckboxRecViewAdapter;
import com.helio.app.ui.utils.TextChangedListener;

import org.jetbrains.annotations.NotNull;

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
        fillColour = ContextColourProvider.getColour(getContext(), android.R.attr.colorPrimary);
        backgroundColour = ContextColourProvider.getColour(getContext(), android.R.attr.windowBackground);

        TextInputLayout name = view.findViewById(R.id.schedule_name);
        EditText nameEditText = name.getEditText();
        MaterialButton timeButton = view.findViewById(R.id.time_button);
        Slider levelSlider = view.findViewById(R.id.level_slider);

        MotorIdsBlindsCheckboxRecViewAdapter adapter = new MotorIdsBlindsCheckboxRecViewAdapter();

        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                schedules -> {
                    schedule = schedules.get(scheduleId);

                    adapter.setComponent(schedule);

                    Objects.requireNonNull(nameEditText).setInputType(InputType.TYPE_CLASS_TEXT);
                    nameEditText.setText(schedule.getName());
                    nameEditText.addTextChangedListener(new TextChangedListener() {
                        @Override
                        public void onTextChanged(CharSequence s) {
                            schedule.setName(s.toString());
                        }
                    });

                    timeButton.setText(schedule.getFormattedTime());
                    timeButton.setOnClickListener(timeButtonClickListener(timeButton));

                    levelSlider.setValue(schedule.getTargetLevel());
                    levelSlider.addOnChangeListener((slider, value, fromUser) -> schedule.setTargetLevel((int) value));

                    prepareDays(view.findViewById(R.id.days_layout));
                }
        );

        model.fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> adapter.setMotors(new ArrayList<>(motors.values()))
        );

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.blindsRCView);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @NotNull
    private View.OnClickListener timeButtonClickListener(MaterialButton timeButton) {
        return v -> {
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
        };
    }

    private void prepareDays(ConstraintLayout daysLayout) {
        // Get days in local order
        Day firstLocalDay = Day.getFirstLocalDay(requireContext());
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

    @Override
    public void onStop() {
        if (schedule != null) {
            model.pushScheduleState(schedule);
            Toast.makeText(requireContext(), requireContext().getString(R.string.component_updated), Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }
}
