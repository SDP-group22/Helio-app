package com.helio.app.ui.blinds;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ControlFragment extends Fragment {

    protected static final int RESULT_SPEECH = 100;
    ControlRecViewAdapter adapter;
    TextToSpeech tts;
    private UserDataViewModel model;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        adapter = new ControlRecViewAdapter(model);
        model.fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> adapter.setMotors(new ArrayList<>(motors.values()))
        );

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
        addButton.setOnClickListener(
                v -> model.addMotor().observe(
                        getViewLifecycleOwner(),
                        motors -> adapter.setMotors(new ArrayList<>(motors.values()))
                )
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

}