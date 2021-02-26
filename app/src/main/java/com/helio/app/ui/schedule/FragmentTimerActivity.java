package com.helio.app.ui.schedule;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.helio.app.MainActivity;
import com.helio.app.R;

import java.util.Calendar;

public class FragmentTimerActivity extends AppCompatActivity {
    EditText timeHour;
    EditText timeMinute;
    Button setTime;
    Button setAlarm;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule);

        timeHour = findViewById(R.id.editHour);
        timeMinute = findViewById(R.id.editMinute);
        setTime = findViewById(R.id.btnSetTimer);
        setAlarm = findViewById(R.id.btnSetAlarm);


        setTime.setOnClickListener((v) -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            // The API didn't work properly. The timePicker didn't work with no reason. Maybe because it is not in the mainActivity? I don't know why it happened.
            timePickerDialog = new TimePickerDialog(FragmentTimerActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                    timeHour.setText(String.format("%02d",hourOfDay));
                    timeMinute.setText(String.format("%02d",minute));
                }
            },currentHour,currentMinute,false);
            timePickerDialog.show();

        });

        setAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!timeHour.getText().toString().isEmpty() && !timeMinute.getText().toString().isEmpty()){
                    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                    intent.putExtra(AlarmClock.EXTRA_HOUR,Integer.parseInt(timeHour.getText().toString()));
                    intent.putExtra(AlarmClock.EXTRA_MINUTES,Integer.parseInt(timeMinute.getText().toString()));
                    intent.putExtra(AlarmClock.EXTRA_MESSAGE,"Set alarm for morning walk");
                    if (intent.resolveActivity(getPackageManager())!=null){
                        startActivity(intent);
                    }else{
                        Toast.makeText(FragmentTimerActivity.this,"There is no app that support this action",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FragmentTimerActivity.this,"Please enter a time",Toast.LENGTH_SHORT).show();
                }

            }

        });

    }
}
