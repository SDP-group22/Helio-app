package com.helio.app.ui.control;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ControlFragment extends Fragment {

    private UserDataViewModel model;
    protected static final int RESULT_SPEECH = 100;
    private FloatingActionButton fab_voiceIntegration;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        ControlRecViewAdapter adapter = new ControlRecViewAdapter(getContext(), model);
        model.fetchMotors().observe(
                getViewLifecycleOwner(),
                motors -> adapter.setMotors(new ArrayList<>(motors.values()))
        );

        // Start RecognizerIntent
        fab_voiceIntegration = view.findViewById(R.id.fab_voice_integration);
        fab_voiceIntegration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please start your voice");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getActivity(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });


        // Insert into the recycler view
        RecyclerView recView = view.findViewById(R.id.control_rc_view);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    // Receiving the speech response
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // Take action
                    model.voiceIntegrationAction(text.get(0));
//                    Toast t = Toast.makeText(getActivity(), text.get(0), Toast.LENGTH_LONG);
//                    t.show();
                }
                break;
            }

        }
    }
}


