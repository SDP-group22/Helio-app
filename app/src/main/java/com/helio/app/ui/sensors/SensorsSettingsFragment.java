package com.helio.app.ui.sensors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import com.helio.app.model.Sensor;
import com.helio.app.networking.NetworkStatus;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SensorsSettingsFragment extends Fragment {
    private UserDataViewModel model;
    private SensorsRecViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensors_settings, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        adapter = new SensorsRecViewAdapter(getContext(), model);

        model.fetchLightSensors().observe(
                getViewLifecycleOwner(),
                sensors -> adapter.setLightSensors(sensors.values())
        );

        model.fetchMotionSensors().observe(
                getViewLifecycleOwner(),
                sensors -> adapter.setMotionSensors(sensors.values())
        );

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.sensorsRCView);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        // On plus button press, open the two sensor type buttons
        ViewGroup addButtonsLayout = view.findViewById(R.id.add_sensor_button_layout);
        FloatingActionButton plusButton = view.findViewById(R.id.add_button);
        plusButton.setOnClickListener(v -> {
            plusButton.setVisibility(View.GONE);
            addButtonsLayout.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in));
            addButtonsLayout.setVisibility(View.VISIBLE);
        });

        view.<FloatingActionButton>findViewById(R.id.add_motion_button).setOnClickListener(this::addButtonOnClickMotion);
        view.<FloatingActionButton>findViewById(R.id.add_light_button).setOnClickListener(this::addButtonOnClickLight);

        provideHubConnectionHint();

        return view;
    }

    private void addButtonOnClickMotion(View v) {
        Set<Integer> oldIds = adapter.getMotionSensors().stream().map(IdComponent::getId).collect(Collectors.toSet());

        model.addMotionSensor().observe(
                getViewLifecycleOwner(),
                sensors -> {
                    adapter.setMotionSensors(sensors.values());
                    navigateToNewComponent(oldIds, sensors);
                }
        );
    }

    private void addButtonOnClickLight(View v) {
        Set<Integer> oldIds = adapter.getLightSensors().stream().map(IdComponent::getId).collect(Collectors.toSet());

        model.addLightSensor().observe(
                getViewLifecycleOwner(),
                sensors -> {
                    adapter.setLightSensors(sensors.values());
                    navigateToNewComponent(oldIds, sensors);
                }
        );
    }

    private void provideHubConnectionHint() {
        model.getNetworkStatus().observe(
                getViewLifecycleOwner(),
                networkStatus -> {
                    System.out.println("connection status: " + networkStatus);
                    if(networkStatus == NetworkStatus.DISCONNECTED) {
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

    private void navigateToNewComponent(Set<Integer> oldIds, Map<Integer, ? extends Sensor> sensors) {
        // Find the new component and navigate to it
        for (Sensor s : sensors.values()) {
            if (!oldIds.contains(s.getId())) {
                SensorsSettingsFragmentDirections.ActionSensorsSettingFragmentToSingleSensorSettingFragment action =
                        SensorsSettingsFragmentDirections.actionSensorsSettingFragmentToSingleSensorSettingFragment();
                action.setCurrentSensorId(s.getId());
                action.setSensorType(s.getType());
                Navigation.findNavController(requireView()).navigate(action);
            }
        }
    }
}
