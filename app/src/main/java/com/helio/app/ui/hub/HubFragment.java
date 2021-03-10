package com.helio.app.ui.hub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.helio.app.R;

public class HubFragment extends Fragment {

    private HubViewModel hubViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hubViewModel =
                new ViewModelProvider(this).get(HubViewModel.class);
        return inflater.inflate(R.layout.fragment_hub, container, false);
    }
}