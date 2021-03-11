package com.helio.app.ui.schedules;

import android.content.Context;
import android.content.res.TypedArray;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        // Get the list of all days and the list of shortened day names (M, T, W...)
        // Rotate the arrays around for local specific first day as specified in strings.xml
        Day firstLocalDay = Day.getEnumFromName(context.getResources().getString(R.string.first_day));
        String[] shortDaysFromResource = context.getResources().getStringArray(R.array.weekdaysShort);
        List<String> shortWeekdays = Day.getShortDaysLocalOrder(shortDaysFromResource, firstLocalDay);

        List<Day> allDays = Day.getValuesLocalOrder(firstLocalDay);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (int i = 0; i < shortWeekdays.size(); i++) {
            if (i > 0) {
                // Do not put spacing in before the first day
                spannableStringBuilder.append(' ');
            }
            // Append the appropriate letter
            spannableStringBuilder.append(shortWeekdays.get(i));

            // If in the schedule emphasise the day with colour, bold, and underline
            if (schedule.getDays().contains(allDays.get(i))) {
                int start = i * 2;
                // The length is supposed to be 1 character, but in case it isn't
                int end = start + shortWeekdays.get(i).length();
                spannableStringBuilder.setSpan(new ForegroundColorSpan(highlightColour), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        holder.days.setText(spannableStringBuilder);

        // Set the name of the schedule, but if there isn't one remove it from the layout
        if (schedule.getName() == null || schedule.getName().equals("")) {
            holder.layout.removeView(holder.nameLayout);
        } else {
            holder.scheduleName.setText(schedule.getName());
        }
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
        private final CardView parent;
        private final RelativeLayout layout;
        private final RelativeLayout nameLayout;

        public ViewHolder(@NonNull View view) {
            super(view);
            layout = view.findViewById(R.id.parent_layout);
            nameLayout = view.findViewById(R.id.name_layout);
            time = view.findViewById(R.id.time);
            days = view.findViewById(R.id.days);
            activateSwitch = view.findViewById(R.id.activate_switch);
            parent = view.findViewById(R.id.parent);
            scheduleName = view.findViewById(R.id.schedule_name);

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

