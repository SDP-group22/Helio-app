package com.helio.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.helio.app.model.LightSensor;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Motor;
import com.helio.app.networking.HubClient;
import com.helio.app.networking.request.MotorSettingsRequest;
import java.util.Map;
import java.util.Objects;

public class UserDataViewModel extends AndroidViewModel {
    private final HubClient client = new HubClient("http://10.0.2.2:4310/");
    private MutableLiveData<Map<Integer, Motor>> motors;
    private MutableLiveData<Map<Integer, LightSensor>> lightsensors;
    private MutableLiveData<Map<Integer, MotionSensor>> motionsensors;
    //private MutableLiveData<Map<Integer, Sensor>> sensors;
    private int currentMotorId = -1;
    private int currentScheduleID = -1;
    private int currentLightSensor = -1;
    private int currentMotionSensor = -1;
    private int currentSensor = -1;


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

    public LiveData<Map<Integer, LightSensor>> fetchLightSensors() {
        if (lightsensors == null) {
            lightsensors = new MutableLiveData<>();
            client.getAllLightSensors(lightsensors);
        }
        return lightsensors;
    }

    public LiveData<Map<Integer, MotionSensor>> fetchMotionSensors() {
        if (motionsensors == null) {
            motionsensors = new MutableLiveData<>();
            client.getAllMotionSensors(motionsensors);
        }
        return motionsensors;
    }
//    public LiveData<Map<Integer, Sensor>> fetchSensors(){
//        if (sensors == null){
//            sensors = new MutableLiveData<>();
//            client.getAllSensors(sensors);
//        }
//        return sensors;
//    }

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
}