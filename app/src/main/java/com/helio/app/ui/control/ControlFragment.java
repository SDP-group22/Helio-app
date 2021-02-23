package com.helio.app.ui.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helio.app.R;
import com.helio.app.model.Motor;
import com.helio.app.ui.MotorIcon;

import java.util.ArrayList;

public class ControlFragment extends Fragment {

    private ControlViewModel controlViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controlViewModel =
                new ViewModelProvider(this).get(ControlViewModel.class);
        View view = inflater.inflate(R.layout.fragment_control, container, false);

        // Prepare the list of motors TODO (temporary)
        ArrayList<Motor> motors = new ArrayList<>();
        motors.add(new Motor("Bedroom", MotorIcon.BEDROOM));
        motors.add(new Motor("Kitchen", MotorIcon.KITCHEN));
        motors.add(new Motor("Next to TV", MotorIcon.TV));
        motors.add(new Motor("Living room", MotorIcon.HOUSE));

        // Setup the adapter with the motors
        ControlRecViewAdapter adapter = new ControlRecViewAdapter(getContext());
        adapter.setMotors(motors);

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.control_rc_view);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
}