package com.helio.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.helio.app.model.LightSensor;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Motor;
import com.helio.app.model.Schedule;
import com.helio.app.model.Sensor;
import com.helio.app.networking.HubClient;
import com.helio.app.networking.request.MotorSettingsRequest;
import com.helio.app.networking.request.ScheduleSettingsRequest;

import java.util.Map;
import java.util.Objects;

public class UserDataViewModel extends ViewModel {
    private final HubClient client = new HubClient("http://10.0.2.2:4310/");
    private MutableLiveData<Map<Integer, Motor>> motors;
    private MutableLiveData<Map<Integer, Schedule>> schedules;
    private MutableLiveData<Map<Integer, LightSensor>> lightsensors;
    private MutableLiveData<Map<Integer, MotionSensor>> motionsensors;
    private MutableLiveData<Map<Integer, Sensor>> sensors;
    private int currentMotorId = -1;
    private int currentScheduleID = -1;
    private int currentLightSensor = -1;
    private int currentMotionSensor = -1;
    private int currentSensor = -1;

    public LiveData<Map<Integer, Motor>> fetchMotors() {
        if(motors == null) {
            motors = new MutableLiveData<>();
            client.getAllMotors(motors);
        }
        return motors;
    }
    public LiveData<Map<Integer, Schedule>> fetchSchedules() {
        if(schedules == null) {
            schedules = new MutableLiveData<>();
            client.getAllSchedules(schedules);
        }
        return schedules;
    }
    public LiveData<Map<Integer, LightSensor>> fetchLightSensors(){
        if (lightsensors == null){
            lightsensors = new MutableLiveData<>();
            client.getAllLightSensors(lightsensors);
        }
        return lightsensors;
    }
    public LiveData<Map<Integer, MotionSensor>> fetchMotionSensors(){
        if (motionsensors == null){
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
    public void setCurrentSchedule(int id){
        currentScheduleID = id;
    }

    public void moveCurrentMotor(int level) {
        client.moveMotor(getCurrentMotor(), level);
    }

    public Motor getCurrentMotor() {
        return Objects.requireNonNull(motors.getValue()).get(currentMotorId);
    }
    public Schedule getCurrentSchedule(){
        return Objects.requireNonNull(schedules.getValue()).get(currentScheduleID);
    }

    public void pushCurrentMotorState(Motor m) {
        motors.getValue().put(currentMotorId, m);
        MotorSettingsRequest motorSettingsRequest = new MotorSettingsRequest(
                m.getName(), m.getIp(), m.isActive(), m.getBattery(), m.getLength(),
                m.getLevel(), m.getStyle()
        );
        client.updateMotor(motors.getValue(), currentMotorId, motorSettingsRequest);
    }
    public void pushCurrentScheduleState(Schedule s){
        schedules.getValue().put(currentMotorId,s);
        ScheduleSettingsRequest scheduleSettingsRequest = new ScheduleSettingsRequest(
                s.getScheduleEventName(),s.getScheduleMotorName(),s.isActive(),s.getDays(),s.getTargetLevel(),s.getGradient(),s.getMotorIds(),s.getTime()
        );
        client.updateSchedule(schedules.getValue(),currentMotorId,scheduleSettingsRequest);
    }


}
