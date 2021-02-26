package com.helio.app.ui.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.helio.app.R;
import com.helio.app.model.Schedule;

import java.util.ArrayList;

public class SchedulesRecViewAdapter extends RecyclerView.Adapter<SchedulesRecViewAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Schedule> schedules = new ArrayList<>();

    public SchedulesRecViewAdapter(Context context) {
        this.context = context;
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
        holder.scheduleName.setText(schedule.getName());

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

        private final TextView scheduleName;
        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleName = itemView.findViewById(R.id.scheduleName);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
