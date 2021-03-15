package com.helio.app.ui.blinds;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;


public class CalibrationFragment extends Fragment {

    private Motor motor;
    private UserDataViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calibration, container, false);
        assert getArguments() != null;
        int motorId = getArguments().getInt("currentMotorId");

        MaterialButton upButton = view.findViewById(R.id.move_up);
        MaterialButton downButton = view.findViewById(R.id.move_down);
        MaterialButton setHighButton = view.findViewById(R.id.set_high);
        MaterialButton setLowButton = view.findViewById(R.id.set_low);

        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        model.setCurrentMotor(motorId);
        model.fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> {
                    motor = motors.get(motorId);

                    // Start calibration upon page opening
                    model.startCalibration(motor);

                    upButton.setOnTouchListener((v, event) -> {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            model.moveUp(motor);
                        } else if (event.getAction() == MotionEvent.ACTION_UP){
                            model.stopMoving(motor);
                        }
                        v.performClick();
                        return false;
                    });
                    downButton.setOnTouchListener((v, event) -> {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            model.moveDown(motor);
                        } else if (event.getAction() == MotionEvent.ACTION_UP){
                            model.stopMoving(motor);
                        }
                        v.performClick();
                        return false;
                    });
                    setHighButton.setOnClickListener(v -> model.setHighestPoint(motor));
                    setLowButton.setOnClickListener(v -> model.setLowestPoint(motor));
                }
        );

        return view;
    }

    @Override
    public void onStop() {
        if (model != null) {
            // Stop calibration upon page closing
            model.stopMoving(motor);
            model.stopCalibration(motor);
            Toast.makeText(requireContext(), requireContext().getString(R.string.calibrated), Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }
}