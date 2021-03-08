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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.helio.app.R;
import com.helio.app.UserDataViewModel;
import com.helio.app.model.Motor;
import com.helio.app.ui.MotorIcon;

import java.util.ArrayList;

public class BlindsRecViewAdapter extends RecyclerView.Adapter<BlindsRecViewAdapter.ViewHolder> {

    private final Context context;
    private final UserDataViewModel model;
    private ArrayList<Motor> motors = new ArrayList<>();

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
        holder.txtName.setText(motor.getName());
        MotorIcon icon = motor.getIcon();
        if(icon != null) {
            holder.blindIcon.setImageResource(icon.id);
        }
        holder.parent.setOnClickListener(v ->
        {
            /*  The following line is horribly convoluted, but it follows the advice given here:
                https://developer.android.com/guide/navigation/navigation-pass-data#define_destination_arguments
                In short, this allows us to pass an argument (the id of the motor) to the single
                blind settings fragment.
             */
            BlindsSettingsFragmentDirections.ActionBlindsSettingsFragmentToSingleBlindSettingsFragment action =
                    BlindsSettingsFragmentDirections.actionBlindsSettingsFragmentToSingleBlindSettingsFragment();
            action.setCurrentMotorId(motor.getId());
            Toast.makeText(context, motor.getName() + " Selected",
                    Toast.LENGTH_SHORT).show();
            Navigation.findNavController(holder.itemView).navigate(action);
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
