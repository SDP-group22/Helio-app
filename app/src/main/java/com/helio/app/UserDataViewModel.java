package com.helio.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.helio.app.model.Motor;
import com.helio.app.networking.HubClient;
import com.helio.app.networking.request.MotorSettingsRequest;

import java.util.Map;
import java.util.Objects;

public class UserDataViewModel extends AndroidViewModel {
    private final HubClient client = new HubClient("http://10.0.2.2:4310/");
    private MutableLiveData<Map<Integer, Motor>> motors;
    private int currentMotorId = -1;

    public UserDataViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Map<Integer, Motor>> fetchMotors() {
        if (motors == null) {
            motors = new MutableLiveData<>();
            client.getAllMotors(motors);
        }
        return motors;
    }

    public void setCurrentMotor(int id) {
        currentMotorId = id;
    }

    public void moveCurrentMotor(int level) {
        client.moveMotor(getCurrentMotor(), level);
    }

    public Motor getCurrentMotor() {
        return Objects.requireNonNull(motors.getValue()).get(currentMotorId);
    }

    public void pushCurrentMotorState(Motor m) {
        Objects.requireNonNull(motors.getValue()).put(currentMotorId, m);
        MotorSettingsRequest motorSettingsRequest = new MotorSettingsRequest(
                m.getName(), m.getIp(), m.isActive(), m.getBattery(), m.getLength(),
                m.getLevel(), m.getStyle()
        );
        client.updateMotor(motors, currentMotorId, motorSettingsRequest);
    }

    public LiveData<Map<Integer, Motor>> addMotor() {
        MotorSettingsRequest motorSettingsRequest = new MotorSettingsRequest(
                getApplication().getString(R.string.new_blinds), "0.0.0.0", true, 0, 0, 0, "");
        client.addMotor(motors, motorSettingsRequest);
        return motors;
    }

    public void voiceIntegrationAction(String s) {
        // Go through each of the motors
        for (Motor m : motors.getValue().values()) {
            // Check if s contains a blind's name
            if (s.contains(m.getName())) {
                if(s.contains("open") || s.contains("up")|| s.contains("raise")){
                    // Open the blind
                    setCurrentMotor(m.getId());
                    moveCurrentMotor(100);
//                    System.out.println(String.format("Open %s", m.getName()));
                }
                if(s.contains("close") || s.contains("down")|| s.contains("lower")){
                    // Close the blind
                    setCurrentMotor(m.getId());
                    moveCurrentMotor(0);
//                    System.out.println(String.format("Close %s", m.getName()));
                }
            }
        }
    }
}
