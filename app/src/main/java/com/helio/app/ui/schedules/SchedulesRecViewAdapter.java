package com.helio.app.ui.schedules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;
import com.helio.app.model.Schedule;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;

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
        holder.scheduleEventName.setText(schedule.getScheduleEventName());
        holder.scheduleMotorName.setText(schedule.getScheduleMotorName());
        holder.scheduleSlider.setValue(schedule.getTargetLevel());
        // Format the number as a percentage (possibly sensitive to Locale)
        // Display Closed or Open if completely closed or open
        holder.scheduleSlider.setLabelFormatter(value -> {
            if (value == 0) {
                return context.getResources().getString(R.string.closed_state);
            } else if (value == 100) {
                return context.getResources().getString(R.string.open_state);
            } else {
                return NumberFormat.getPercentInstance().format(value / 100);
            }
        });
        // Update the level of the schedule motor when the user lets go of the slider
        holder.scheduleSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Schedule schedule = schedules.get(position);
                // disable dragging? TODO
                // send update to Hub
                schedule.setTargetLevel((int) schedule.getTargetLevel());
                System.out.println("Updated level using slider: " + schedule);
                model.setCurrentMotor(schedule.getId());
                model.pushCurrentScheduleState(schedule);
                // asynchronously enable dragging? TODO
            }
        });

        holder.parent.setOnClickListener(v ->
        {
            SchedulesFragmentDirections.ActionScheduleFragmentToScheduleSettingsFragment action =
                    SchedulesFragmentDirections.actionScheduleFragmentToScheduleSettingsFragment();
            action.setCurrentScheduleId(schedule.getId());
            Navigation.findNavController(holder.itemView).navigate(action);
        });
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

        private final TextView scheduleEventName;
        private final TextView scheduleTime;
        private final TextView scheduleMotorName;
        private final Slider scheduleSlider;
        private final SwitchMaterial scheduleSwtich;
        private final TextView schedule_DaysOfWeek;
        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleEventName = itemView.findViewById(R.id.schedule_eventName);
            scheduleTime = itemView.findViewById(R.id.schedule_timeSelect);
            scheduleMotorName = itemView.findViewById(R.id.schedule_motorName);
            scheduleSlider = itemView.findViewById(R.id.schedule_controlSlider);
            scheduleSwtich = itemView.findViewById(R.id.schedule_swtich);
            schedule_DaysOfWeek = itemView.findViewById(R.id.schedule_calendar_DaysOfWeek);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
