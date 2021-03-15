package com.helio.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.helio.app.model.IPAddress;
import com.helio.app.model.LightSensor;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Motor;
import com.helio.app.model.Schedule;
import com.helio.app.model.Sensor;
import com.helio.app.networking.HubClient;
import com.helio.app.networking.request.LightSensorSettingsRequest;
import com.helio.app.networking.request.MotionSensorSettingsRequest;
import com.helio.app.networking.request.MotorSettingsRequest;
import com.helio.app.networking.request.ScheduleSettingsRequest;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class UserDataViewModel extends AndroidViewModel {
    private HubClient client;
    private MutableLiveData<Map<Integer, Motor>> motors;
    private MutableLiveData<Map<Integer, Schedule>> schedules;
    private MutableLiveData<Map<Integer, LightSensor>> lightSensors;
    private MutableLiveData<Map<Integer, MotionSensor>> motionSensors;
    private int currentMotorId = -1;
    private final SharedPreferences sharedPrefs;

    public UserDataViewModel(@NonNull Application application) {
        super(application);
        sharedPrefs = getApplication().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        client = createClient(getHubIp());
    }

    private HubClient createClient(String ip) {
        // IP of local machine when using emulator is 10.0.2.2
        return new HubClient(IPAddress.getBaseAddressUrl(ip));
    }

    public String getHubIp() {
        return sharedPrefs.getString(getIpKey(), IPAddress.DEFAULT);
    }

    public void setHubIp(String ip) {
        if (IPAddress.correctFormat(ip)) {
            // Set the preference
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(getIpKey(), ip);
            editor.apply();
            System.out.println("New IP: " + sharedPrefs.getString(getIpKey(), IPAddress.DEFAULT));

            // Create the new client and reset the data so that it is reloaded
            client = createClient(ip);
            motors = null;
            schedules = null;
            lightSensors = null;
            motionSensors = null;
        } else {
            // If this is being thrown then you need to input validation before it gets to this point
            throw new IllegalArgumentException("IP address in incorrect format: " + ip);
        }
    }

    public LiveData<Map<Integer, Motor>> fetchMotors() {
        if (motors == null) {
            motors = new MutableLiveData<>();
            client.getAllMotors(motors);
        }
        return motors;
    }

    public LiveData<Map<Integer, Schedule>> fetchSchedules() {
        if (schedules == null) {
            schedules = new MutableLiveData<>();
            client.getAllSchedules(schedules);
        }
        return schedules;
    }

    public LiveData<Map<Integer, LightSensor>> fetchLightSensors() {
        if (lightSensors == null) {
            lightSensors = new MutableLiveData<>();
            client.getAllLightSensors(lightSensors);
        }
        return lightSensors;
    }

    public LiveData<Map<Integer, MotionSensor>> fetchMotionSensors() {
        if (motionSensors == null) {
            motionSensors = new MutableLiveData<>();
            client.getAllMotionSensors(motionSensors);
        }
        return motionSensors;
    }

    public void moveCurrentMotor(int level) {
        client.moveMotor(getCurrentMotor(), level);
    }

    public Motor getCurrentMotor() {
        return Objects.requireNonNull(motors.getValue()).get(currentMotorId);
    }

    public void setCurrentMotor(int id) {
        currentMotorId = id;
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
                "", IPAddress.DEFAULT, true, 0, 0, 0, "");
        client.addMotor(motors, motorSettingsRequest);
        return motors;
    }

    public void pushScheduleState(Schedule s) {
        if (schedules.getValue() != null) {
            schedules.getValue().put(s.getId(), s);
            ScheduleSettingsRequest scheduleSettingsRequest = new ScheduleSettingsRequest(
                    s.getName(), s.isActive(), s.getDays(), s.getTargetLevel(), s.getGradient(), s.getMotorIds(), s.getTime()
            );
            client.updateSchedule(schedules, s.getId(), scheduleSettingsRequest);
        }
    }

    public void toggleScheduleActive(Schedule s) {
        s.setActive(!s.isActive());
        pushScheduleState(s);
    }

    public LiveData<Map<Integer, Schedule>> addSchedule() {
        ScheduleSettingsRequest request = new ScheduleSettingsRequest(
                "", true, new ArrayList<>(), 0, 0, new ArrayList<>(), "12:00");
        client.addSchedule(schedules, request);
        return schedules;
    }

    public LiveData<Map<Integer, MotionSensor>> addMotionSensor() {
        MotionSensorSettingsRequest request = new MotionSensorSettingsRequest(
                new ArrayList<>(), getApplication().getString(R.string.new_motion_sensor), IPAddress.DEFAULT, true, 0, "", "00:15");
        client.addMotionSensor(motionSensors, request);
        return motionSensors;
    }

    public LiveData<Map<Integer, LightSensor>> addLightSensor() {
        LightSensorSettingsRequest request = new LightSensorSettingsRequest(
                new ArrayList<>(), getApplication().getString(R.string.new_light_sensor), IPAddress.DEFAULT, true, 0, "");
        client.addLightSensor(lightSensors, request);
        return lightSensors;
    }

    public void pushSensorState(MotionSensor s) {
        if (motionSensors.getValue() != null) {
            motionSensors.getValue().put(s.getId(), s);
            MotionSensorSettingsRequest request = new MotionSensorSettingsRequest(
                    s.getMotorIds(), s.getName(), s.getIp(), s.isActive(), s.getBattery(), s.getStyle(), s.getDurationSensitivity());
            client.updateMotionSensor(motionSensors, s.getId(), request);
        }
    }

    public void pushSensorState(LightSensor s) {
        if (lightSensors.getValue() != null) {
            lightSensors.getValue().put(s.getId(), s);
            LightSensorSettingsRequest request = new LightSensorSettingsRequest(
                    s.getMotorIds(), s.getName(), s.getIp(), s.isActive(), s.getBattery(), s.getStyle());
            client.updateLightSensor(lightSensors, s.getId(), request);
        }
    }

    public void toggleSensorActive(Sensor s) {
        s.setActive(!s.isActive());
        pushSensorState(s);
    }

    public void pushSensorState(Sensor s) {
        if (s.getClass() == MotionSensor.class) {
            pushSensorState((MotionSensor) s);
        } else if (s.getClass() == LightSensor.class) {
            pushSensorState((LightSensor) s);
        }
    }

    private String getIpKey() {
        return getApplication().getString(R.string.ip_key);
    }
}
