package com.helio.app.ui.schedules;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Day;
import com.helio.app.model.Schedule;
import com.helio.app.ui.utils.ContextColourProvider;
import com.helio.app.ui.utils.LevelLabelFormatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SchedulesRecViewAdapter extends RecyclerView.Adapter<SchedulesRecViewAdapter.ViewHolder> {
    private final Context context;
    private final UserDataViewModel model;
    private ArrayList<Schedule> schedules = new ArrayList<>();
    private int highlightColour;

    public SchedulesRecViewAdapter(Context context, UserDataViewModel model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public SchedulesRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item,
                parent, false);

        return new SchedulesRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.activateSwitch.setChecked(schedule.isActive());

        // Parse the date from the schedule String and convert to local format
        holder.time.setText(schedule.getFormattedTime());

        // Get the emphasis colour from the theme
        highlightColour = ContextColourProvider.getColour(context, android.R.attr.colorAccent);

        holder.days.setText(getStyledDaysString(schedule.getDays()));

        // Set the name of the schedule, but if there isn't one remove it from the layout
        if (schedule.getName() == null || schedule.getName().equals("")) {
            holder.layout.removeView(holder.nameLayout);
        } else {
            holder.scheduleName.setText(schedule.getName());
        }

        // Set the target level, and emphasise the level itself
        SpannableStringBuilder levelStringBuilder = getStyledLevelString(schedule.getTargetLevel());

        holder.level.setText(levelStringBuilder);
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


    @NotNull
    private SpannableStringBuilder getStyledDaysString(List<Day> scheduleDays) {
        // Get the list of all days and the list of shortened day names (M, T, W...)
        // Rotate the arrays around for local specific first day
        Day firstLocalDay = Day.getFirstLocalDay(context);
        String[] shortDaysFromResource = context.getResources().getStringArray(R.array.weekdaysShort);
        List<String> shortWeekdays = Day.getShortDaysLocalOrder(shortDaysFromResource, firstLocalDay);

        List<Day> allDays = Day.getValuesLocalOrder(firstLocalDay);

        SpannableStringBuilder daysStringBuilder = new SpannableStringBuilder();
        for (int i = 0; i < shortWeekdays.size(); i++) {
            if (i > 0) {
                // Do not put spacing in before the first day
                daysStringBuilder.append(' ');
            }
            // Append the appropriate letter
            daysStringBuilder.append(shortWeekdays.get(i));

            // If in the schedule emphasise the day with colour, bold, and underline
            if (scheduleDays.contains(allDays.get(i))) {
                int start = i * 2;
                // The length is supposed to be 1 character, but in case it isn't
                int end = start + shortWeekdays.get(i).length();
                daysStringBuilder.setSpan(new ForegroundColorSpan(highlightColour), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                daysStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                daysStringBuilder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return daysStringBuilder;
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView time;
        private final TextView days;
        private final SwitchMaterial activateSwitch;
        private final TextView scheduleName;
        private final MaterialCardView parent;
        private final RelativeLayout layout;
        private final RelativeLayout nameLayout;
        private final TextView level;

        public ViewHolder(@NonNull View view) {
            super(view);
            layout = view.findViewById(R.id.parent_layout);
            nameLayout = view.findViewById(R.id.name_layout);
            time = view.findViewById(R.id.time);
            days = view.findViewById(R.id.days);
            activateSwitch = view.findViewById(R.id.activate_switch);
            parent = view.findViewById(R.id.parent);
            scheduleName = view.findViewById(R.id.schedule_name);
            level = view.findViewById(R.id.level);

            // Click listener for enable
            activateSwitch.setOnClickListener(v -> {
                SwitchMaterial switchView = (SwitchMaterial) v;

                // Update the view
                switchView.toggle();

                // Toast to explain what happened
                String message;
                if (switchView.isChecked()) {
                    message = parent.getResources().getString(R.string.component_deactivated);
                } else {
                    message = parent.getResources().getString(R.string.component_activated);
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                // Send update to model
                model.toggleScheduleActive(schedules.get(getAdapterPosition()));
            });

            parent.setOnClickListener(v -> {
                SchedulesSettingsFragmentDirections.ActionScheduleFragmentToScheduleSettingsFragment action =
                        SchedulesSettingsFragmentDirections.actionScheduleFragmentToScheduleSettingsFragment();
                Schedule s = schedules.get(getAdapterPosition());
                action.setCurrentScheduleId(s.getId());
                Navigation.findNavController(itemView).navigate(action);
            });
        }
    }
}

