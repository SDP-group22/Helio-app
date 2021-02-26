package com.helio.app.ui.schedule;

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
import com.helio.app.UserDataViewModel;
import com.helio.app.ui.blinds.BlindsRecViewAdapter;

import java.util.ArrayList;

public class SchedulesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        UserDataViewModel model = new ViewModelProvider(this).get(UserDataViewModel.class);
        SchedulesRecViewAdapter adapter = new SchedulesRecViewAdapter(getContext());
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                schedules -> adapter.setSchedules(new ArrayList<>(schedules.values()))
        );

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.schedulesRCView);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }


}
