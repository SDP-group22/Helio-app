package com.helio.app.ui.schedules;

import android.content.Context;
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
import com.helio.app.model.Schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        holder.activateSwitch.setChecked(schedule.isActive());
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
        private final SwitchMaterial activateSwitch;
        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            activateSwitch = itemView.findViewById(R.id.activate_switch);
            parent = itemView.findViewById(R.id.parent);

            // Click listener for enable
            activateSwitch.setOnClickListener(v -> {
                SwitchMaterial switchView = (SwitchMaterial) v;

                // Update the view
                switchView.toggle();

                // Toast to explain what happened
                String message;
                if (switchView.isChecked()) {
                    message = parent.getResources().getString(R.string.scheduleDeactivated);
                } else {
                    message = parent.getResources().getString(R.string.scheduleActivated);
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                // Send update to model
                model.toggleScheduleActive(schedules.get(getAdapterPosition()));
            });
        }
    }
}

