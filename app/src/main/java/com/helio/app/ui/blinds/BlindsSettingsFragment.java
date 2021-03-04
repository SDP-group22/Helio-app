package com.helio.app.ui.blinds;

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

import java.util.ArrayList;

public class BlindsSettingsFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blinds_settings, container, false);
        UserDataViewModel model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        BlindsRecViewAdapter adapter = new BlindsRecViewAdapter(getContext(), model);
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
}
