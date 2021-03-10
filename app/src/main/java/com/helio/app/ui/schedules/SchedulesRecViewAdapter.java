package com.helio.app.ui.schedules;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Day;
import com.helio.app.model.Schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SchedulesRecViewAdapter extends RecyclerView.Adapter<SchedulesRecViewAdapter.ViewHolder> {
    private final Context context;
    private final UserDataViewModel model;
    private ArrayList<Schedule> schedules = new ArrayList<>();

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
    public void onBindViewHolder(@NonNull SchedulesRecViewAdapter.ViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.activateSwitch.setChecked(schedule.isActive());

        // Parse the date from the schedule String and convert to local format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        try {
            Date date = sdf.parse(schedule.getTime());
            if (date != null) {
                String formattedTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
                holder.time.setText(formattedTime);
            }
        } catch (ParseException e) {
            System.out.println("Could not parse date " + schedule.getTime());
            e.printStackTrace();
        }

        // Get the emphasis colour from the theme
        TypedArray themeArray = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorAccent});
        int highlightColour;
        try {
            highlightColour = themeArray.getColor(0, 0);
        } finally {
            themeArray.recycle();
        }

        String[] days = context.getResources().getStringArray(R.array.weekdaysShort);
        String daysString = buildDaysString(days);
        SpannableString spannableDaysString = new SpannableString(daysString);
        List<String> allDayStrings = Day.getStrings();
        List<String> scheduleDayStrings = schedule.getDays().stream().map(d -> d.dayName).collect(Collectors.toList());
        for (int i = 0; i < days.length; i++) {
            if (scheduleDayStrings.contains(allDayStrings.get(i))) {
                spannableDaysString.setSpan(new ForegroundColorSpan(highlightColour), i * 2, i * 2 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableDaysString.setSpan(new StyleSpan(Typeface.BOLD), i * 2, i * 2 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableDaysString.setSpan(new UnderlineSpan(), i * 2, i * 2 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        holder.days.setText(spannableDaysString);
    }

    private String buildDaysString(String[] days) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            if (i > 0) {
                builder.append(' ');
            }
            builder.append(days[i]);
        }
        return builder.toString();
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
        private final CardView parent;

        public ViewHolder(@NonNull View view) {
            super(view);
            time = view.findViewById(R.id.time);
            days = view.findViewById(R.id.days);
            activateSwitch = view.findViewById(R.id.activate_switch);
            parent = view.findViewById(R.id.parent);

            // Click listener for enable
            activateSwitch.setOnClickListener(v -> {
                SwitchMaterial switchView = (SwitchMaterial) v;

                // Update the view
                switchView.toggle();

                // Toast to explain what happened
                String message;
                if (switchView.isChecked()) {
                    message = parent.getResources().getString(R.string.schedule_deactivated);
                } else {
                    message = parent.getResources().getString(R.string.schedule_activated);
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                // Send update to model
                model.toggleScheduleActive(schedules.get(getAdapterPosition()));
            });
        }
    }
}

