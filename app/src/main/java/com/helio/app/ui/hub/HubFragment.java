package com.helio.app.ui.hub;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.helio.app.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class HubFragment extends Fragment {

    protected static final int RESULT_SPEECH1 = 1;
    protected static final int RESULT_SPEECH2 = 2;
    protected static final int RESULT_SPEECH3 = 3;
    protected static final int RESULT_SPEECH4 = 4;
    private ImageButton btnSpeak1;
    private ImageButton btnSpeak2;
    private ImageButton btnSpeak3;
    private ImageButton btnSpeak4;
    private EditText hubAddress1;
    private EditText hubAddress2;
    private EditText hubAddress3;
    private EditText hubAddress4;

    private HubViewModel hubViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hub, container, false);
        hubViewModel = new ViewModelProvider(this).get(HubViewModel.class);

        btnSpeak1 = (ImageButton) view.findViewById(R.id.btnSpeak1);
        btnSpeak2 = (ImageButton) view.findViewById(R.id.btnSpeak2);
        btnSpeak3 = (ImageButton) view.findViewById(R.id.btnSpeak3);
        btnSpeak4 = (ImageButton) view.findViewById(R.id.btnSpeak4);
        hubAddress1 = view.findViewById(R.id.hubAddress1);
        hubAddress2 = view.findViewById(R.id.hubAddress2);
        hubAddress3 = view.findViewById(R.id.hubAddress3);
        hubAddress4 = view.findViewById(R.id.hubAddress4);


        btnSpeak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please start your voice");
                try {
                    startActivityForResult(intent, RESULT_SPEECH1);
                    hubAddress1.setText("");
                } catch (ActivityNotFoundException a) {

                }
            }
        });
        btnSpeak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please start your voice");
                try {
                    startActivityForResult(intent, RESULT_SPEECH2);
                    hubAddress2.setText("");
                } catch (ActivityNotFoundException a) {

                }
            }
        });
        btnSpeak3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please start your voice");
                try {
                    startActivityForResult(intent, RESULT_SPEECH3);
                    hubAddress3.setText("");
                } catch (ActivityNotFoundException a) {

                }
            }
        });
        btnSpeak4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please start your voice");
                try {
                    startActivityForResult(intent, RESULT_SPEECH4);
                    hubAddress4.setText("");
                } catch (ActivityNotFoundException a) {

                }
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH1: {
                if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    hubAddress1.setText(text.get(0));
                }
                break;
            }
            case RESULT_SPEECH2: {
                if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    hubAddress2.setText(text.get(0));
                }
                break;
            }
            case RESULT_SPEECH3: {
                if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    hubAddress3.setText(text.get(0));
                }
                break;
            }
            case RESULT_SPEECH4: {
                if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    hubAddress4.setText(text.get(0));
                }
                break;
            }
        }
    }


}