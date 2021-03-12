package com.helio.app.ui.schedule;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
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

        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        model.setCurrentSchedule(scheduleId);
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                schedules -> {
                    schedule = schedules.get(scheduleId);
                    TextInputLayout name = view.findViewById(R.id.schedule_name);
                    EditText nameEditText = name.getEditText();
                    Objects.requireNonNull(nameEditText).setInputType(InputType.TYPE_CLASS_TEXT);
                    nameEditText.setText(schedule.getName());

                    nameEditText.addTextChangedListener(new TextChangedListener() {
                        @Override
                        public void onTextChanged(CharSequence s) {
                            schedule.setName(s.toString());
                        }
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
