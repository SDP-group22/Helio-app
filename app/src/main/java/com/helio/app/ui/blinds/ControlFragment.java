package com.helio.app.ui.blinds;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
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
import com.helio.app.model.Motor;
import com.helio.app.networking.NetworkStatus;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static android.app.Activity.RESULT_OK;

public class ControlFragment extends Fragment {

    protected static final int RESULT_SPEECH = 100;
    private ControlRecViewAdapter adapter;
    private TextToSpeech tts;
    private UserDataViewModel model;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> requestsLoopHandle = null;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        adapter = new ControlRecViewAdapter(model, this);

        startPollingLoop();

        // Start RecognizerIntent
        FloatingActionButton fab_voiceIntegration = view.findViewById(R.id.fab_voice_integration);
        fab_voiceIntegration.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            try {
                startActivityForResult(intent, RESULT_SPEECH);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getActivity(),
                        view.getResources().getString(R.string.does_not_support_voice_recognition),
                        Toast.LENGTH_LONG).show();
            }
        });

        // Text to speech
        tts = new TextToSpeech(getContext(), status -> {
        });

        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.control_rc_view);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton addButton = view.findViewById(R.id.add_blinds_button);
        addButton.setOnClickListener(this::addButtonOnClick);

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

        return view;
    }

    // Receiving the speech response

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SPEECH) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                // Take action
                model.interpretVoiceCommand(text.get(0), tts).observe(
                        getViewLifecycleOwner(),
                        motors -> adapter.setMotors(new ArrayList<>(motors.values())));
            }
        }
    }

    private void addButtonOnClick(View v) {
        // Save the IDs before adding the component
        Set<Integer> oldIds = adapter.getMotors().stream().map(IdComponent::getId).collect(Collectors.toSet());
        model.addMotor().observe(
                getViewLifecycleOwner(),
                motors -> {
                    adapter.setMotors(new ArrayList<>(motors.values()));

                    // Find the new component and navigate to it
                    for (Motor m : motors.values()) {
                        if (!oldIds.contains(m.getId())) {
                            ControlFragmentDirections.ActionNavigationControlToSingleBlindSettingsFragment action =
                                    ControlFragmentDirections.actionNavigationControlToSingleBlindSettingsFragment();
                            action.setCurrentMotorId(m.getId());
                            Navigation.findNavController(requireView()).navigate(action);
                        }
                    }
                }
        );
    }

    @Override
    public void onStop() {
        // Stop polling when leaving the control page
        stopPollingLoop();
        super.onStop();
    }

    private void fetchMotors() {
        model.fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> adapter.setMotors(new ArrayList<>(motors.values()))
        );
    }

    public void startPollingLoop() {
        // Poll and update the motors repeatedly
        requestsLoopHandle = scheduler.scheduleAtFixedRate(() -> requireActivity().runOnUiThread(() -> {
            // Try/catch because it sometimes crashes when this runs after the view has been closed
            try {
                fetchMotors();
            } catch (IllegalStateException e) {
                System.out.println("Caught error in polling loop, ignoring it");
            }
        }), 500, 1000, TimeUnit.MILLISECONDS);
    }

    public void stopPollingLoop() {
        requestsLoopHandle.cancel(true);
    }
}