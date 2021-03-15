package com.helio.app.ui.blinds;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helio.app.R;


public class CalibrationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calibration, container, false);
        assert getArguments() != null;
        int motorId = getArguments().getInt("currentMotorId");

        return view;
    }
}