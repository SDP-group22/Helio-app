package com.helio.app.ui.blinds;

import android.content.Context;
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

import com.helio.app.R;
import com.helio.app.model.Motor;

import java.util.ArrayList;

public class BlindsRecViewAdapter extends RecyclerView.Adapter<BlindsRecViewAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Motor> motors = new ArrayList<>();

    public BlindsRecViewAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blinds_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(motors.get(position).getName());
        holder.blindIcon.setImageResource(motors.get(position).getIcon().id);
        holder.parent.setOnClickListener(v ->
        {
            Toast.makeText(context, motors.get(position).getName() + " Selected", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(holder.itemView).navigate(R.id.action_blindsSettingsFragment_to_singleBlindSettingsFragment);
        });
    }

    @Override
    public int getItemCount() {
        return motors.size();
    }

    public void setMotors(ArrayList<Motor> motors) {
        this.motors = motors;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;
        private final ImageView blindIcon;
        private final CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            blindIcon = itemView.findViewById(R.id.blindIcon);
            parent = itemView.findViewById(R.id.parent);
        }
    }

}
