package com.helio.app.ui.blinds;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;
import com.helio.app.ui.MotorIcon;
import com.helio.app.ui.utils.ContextColourProvider;
import com.helio.app.ui.utils.LevelLabelFormatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BlindsRecViewAdapter extends RecyclerView.Adapter<BlindsRecViewAdapter.ViewHolder> {
    private final Context context;
    private final UserDataViewModel model;
    private ArrayList<Motor> motors = new ArrayList<>();
    private int highlightColour;

    public BlindsRecViewAdapter(Context context, UserDataViewModel model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blinds_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Motor motor = motors.get(position);
        holder.activateSwitch.setChecked(motor.isActive());
        // Parse the date from the blinds String and convert to local format
        //holder.motorName.setText(motor.getName());
        // Get the emphasis colour from the theme
        highlightColour = ContextColourProvider.getColour(context, android.R.attr.colorAccent);
        MotorIcon icon = motor.getIcon();
        if (icon != null) {
            holder.blindIcon.setImageResource(icon.id);
        }
        // Set the target level, and emphasise the level itself
        SpannableStringBuilder levelStringBuilder = getStyledLevelString(motor.getLevel());

        holder.level.setText(levelStringBuilder);
    }

    @Override
    public int getItemCount() {
        return motors.size();
    }

    public void setMotors(ArrayList<Motor> motors) {
        this.motors = motors;
        notifyDataSetChanged();
    }

    @NotNull
    private SpannableStringBuilder getStyledLevelString(int targetLevel) {
        SpannableStringBuilder levelStringBuilder = new SpannableStringBuilder();
        String targetBaseString = context.getString(R.string.target_motor_value_prefix);
        String targetAsString = new LevelLabelFormatter(context).getFormattedValue(targetLevel);
        levelStringBuilder.append(targetBaseString);
        levelStringBuilder.append(targetAsString);

        int start = targetBaseString.length();
        int end = start + targetAsString.length();
        levelStringBuilder.setSpan(new ForegroundColorSpan(highlightColour), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        levelStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return levelStringBuilder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView motorName;
        private final TextView level;
        private final ImageView blindIcon;
        private final CardView parent;
        private final SwitchMaterial activateSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            motorName = itemView.findViewById(R.id.blinds_name);
            blindIcon = itemView.findViewById(R.id.blindIcon);
            level = itemView.findViewById(R.id.blinds_level);
            activateSwitch = itemView.findViewById(R.id.activate_blinds_switch);
            parent = itemView.findViewById(R.id.parent);


            // Click listener for enable
            activateSwitch.setOnClickListener(v -> {
                SwitchMaterial switchView = (SwitchMaterial) v;

                // Update the view
                switchView.toggle();

                // Toast to explain what happened
                String message;
                if (switchView.isChecked()) {
                    message = parent.getResources().getString(R.string.closed_state);
                } else {
                    message = parent.getResources().getString(R.string.open_state);
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                // Send update to model
                model.toggleMotorActive(motors.get(getAdapterPosition()));
            });


            parent.setOnClickListener(v ->
            {
                BlindsSettingsFragmentDirections.ActionBlindsSettingsFragmentToSingleBlindSettingsFragment action =
                        BlindsSettingsFragmentDirections.actionBlindsSettingsFragmentToSingleBlindSettingsFragment();
                Motor m = motors.get(getAdapterPosition());
                action.setCurrentMotorId(m.getId());
                Navigation.findNavController(itemView).navigate(action);
            });

        }
    }

}
