package com.helio.app.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.IdComponent;

public abstract class SingleComponentSettingsFragment<T extends IdComponent> extends Fragment {
    protected T component;
    private UserDataViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        model = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // On delete button pressed
        if (item.getItemId() == R.id.delete) {
            // Send the message to the back to delete
            model.deleteComponent(component);

            // Set to null so that it doesn't try and update it on close
            component = null;

            // Notify user and go back
            Toast.makeText(requireContext(), requireContext().getString(R.string.deleted_message), Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        // Send changes to the motor state when the settings are closed
        // Check if null in case something is wrong or it hasn't loaded in yet
        if (component != null) {
            model.pushComponentState(component);
            Toast.makeText(requireContext(), requireContext().getString(R.string.component_updated), Toast.LENGTH_SHORT).show();
        }
        super.onStop();
    }

    public UserDataViewModel getModel() {
        return model;
    }
}
