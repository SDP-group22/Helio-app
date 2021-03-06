package com.helio.app.ui.schedules;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.IdComponent;
import com.helio.app.model.Schedule;
import com.helio.app.networking.NetworkStatus;
import com.helio.app.ui.NoComponentHintBackground;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class SchedulesSettingsFragment extends Fragment {
    private SchedulesRecViewAdapter adapter;
    private UserDataViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_settings, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        adapter = new SchedulesRecViewAdapter(getContext(), model);
        model.fetchSchedules().observe(
                getViewLifecycleOwner(),
                Schedules -> {
                    adapter.setSchedules(new ArrayList<>(Schedules.values()));
                    toggleNoComponentsHint();
                }
        );

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.schedulesRCView);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(this::addButtonOnClick);

        // Make hint displayed when empty act as an add button
        View hintImage = view.findViewById(R.id.add_component_hint_layout);
        hintImage.setOnClickListener(this::addButtonOnClick);

        provideHubConnectionHint();
        Handler handler = new Handler();
        handler.postDelayed(this::toggleNoComponentsHint, 100);

        return view;
    }

    private void addButtonOnClick(View v) {
        Set<Integer> oldIds = adapter.getSchedules().stream().map(IdComponent::getId).collect(Collectors.toSet());
        model.addSchedule().observe(
                getViewLifecycleOwner(),
                schedules -> {
                    adapter.setSchedules(new ArrayList<>(schedules.values()));

                    // Find the new component and navigate to it
                    for (Schedule s : schedules.values()) {
                        if (!oldIds.contains(s.getId())) {
                            SchedulesSettingsFragmentDirections.ActionScheduleFragmentToScheduleSettingsFragment action =
                                    SchedulesSettingsFragmentDirections.actionScheduleFragmentToScheduleSettingsFragment();
                            action.setCurrentScheduleId(s.getId());
                            Navigation.findNavController(requireView()).navigate(action);
                        }
                    }
                }
        );
    }

    private void toggleNoComponentsHint() {
        // provide a hint to the user if there are no components
        NoComponentHintBackground hintInterface = (NoComponentHintBackground) getActivity();
        assert hintInterface != null;
        if (adapter.getItemCount() == 0) {
            hintInterface.showNoComponentHint();
        } else {
            hintInterface.hideNoComponentHint();
        }
    }

    private void provideHubConnectionHint() {
        model.getNetworkStatus().observe(
                getViewLifecycleOwner(),
                networkStatus -> {
                    System.out.println("connection status: " + networkStatus);
                    if (networkStatus == NetworkStatus.DISCONNECTED) {
                        // hint the user to set up a connection to their hub
                        Toast.makeText(
                                getContext(),
                                getResources().getString(R.string.connect_to_your_helio_hub_device),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
}
