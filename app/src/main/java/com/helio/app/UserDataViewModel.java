package com.helio.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.helio.app.model.Motor;
import com.helio.app.model.Schedule;
import com.helio.app.networking.HubClient;
import com.helio.app.networking.request.MotorSettingsRequest;
import com.helio.app.networking.request.ScheduleSettingsRequest;

import java.util.Map;
import java.util.Objects;

public class UserDataViewModel extends AndroidViewModel {
    private final HubClient client = new HubClient("http://10.0.2.2:4310/");
    private MutableLiveData<Map<Integer, Motor>> motors;
    private MutableLiveData<Map<Integer, Schedule>> schedules;
    private int currentMotorId = -1;
    private int currentScheduleID = -1;

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

    public LiveData<Map<Integer, Schedule>> fetchSchedules() {
        if (schedules == null) {
            schedules = new MutableLiveData<>();
            client.getAllSchedules(schedules);
        }
        return schedules;
    }


    public void setCurrentMotor(int id) {
        currentMotorId = id;
    }

    public void setCurrentSchedule(int id) {
        currentScheduleID = id;
    }

    public void moveCurrentMotor(int level) {
        client.moveMotor(getCurrentMotor(), level);
    }

    public Motor getCurrentMotor() {
        return Objects.requireNonNull(motors.getValue()).get(currentMotorId);
    }

    public Schedule getCurrentSchedule() {
        return Objects.requireNonNull(schedules.getValue()).get(currentScheduleID);
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

    public void pushCurrentScheduleState(Schedule s) {
        schedules.getValue().put(currentMotorId, s);
        ScheduleSettingsRequest scheduleSettingsRequest = new ScheduleSettingsRequest(
                s.getName(), s.isActive(), s.getDays(), s.getTargetLevel(), s.getGradient(), s.getMotorIds(), s.getTime()
        );
        client.updateSchedule(schedules, currentScheduleID, scheduleSettingsRequest);
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
        if (s.isActive()) {
            client.deactivateSchedule(schedules, s.getId());
        } else {
            client.activateSchedule(schedules, s.getId());
        }
    }
}
