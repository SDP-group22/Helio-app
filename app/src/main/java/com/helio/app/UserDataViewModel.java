package com.helio.app;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.helio.app.model.IdComponent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDataViewModel extends AndroidViewModel {
    private final HubClient client = new HubClient("http://10.0.2.2:4310/");
    private MutableLiveData<Map<Integer, Motor>> motors;
    private MutableLiveData<Map<Integer, Schedule>> schedules;
    private MutableLiveData<Map<Integer, LightSensor>> lightSensors;
    private MutableLiveData<Map<Integer, MotionSensor>> motionSensors;
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
                "", "0.0.0.0", true, 0, 0, 0, "");
        client.addMotor(motors, motorSettingsRequest);
        return motors;
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
                new ArrayList<>(), getApplication().getString(R.string.new_motion_sensor), "0.0.0.0", true, 0, "", "00:15");
        client.addMotionSensor(motionSensors, request);
        return motionSensors;
    }

    public LiveData<Map<Integer, LightSensor>> addLightSensor() {
        LightSensorSettingsRequest request = new LightSensorSettingsRequest(
                new ArrayList<>(), getApplication().getString(R.string.new_light_sensor), "0.0.0.0", true, 0, "");
        client.addLightSensor(lightSensors, request);
        return lightSensors;
    }

    public void toggleSensorActive(Sensor s) {
        s.setActive(!s.isActive());
        pushSensorState(s);
    }

    public void pushSensorState(Sensor s) {
        if (s.getClass() == MotionSensor.class) {
            pushMotionSensorState((MotionSensor) s);
        } else if (s.getClass() == LightSensor.class) {
            pushLightSensorState((LightSensor) s);
        }
    }

    public void pushComponentState(IdComponent component) {
        if (component.getClass() == MotionSensor.class) {
            pushMotionSensorState((MotionSensor) component);

        } else if (component.getClass() == LightSensor.class) {
            pushLightSensorState((LightSensor) component);

        } else if (component.getClass() == Motor.class) {
            pushMotorState((Motor) component);

        } else if (component.getClass() == Schedule.class) {
            pushScheduleState((Schedule) component);

        }
    }

    private void pushMotorState(Motor m) {
        Objects.requireNonNull(motors.getValue()).put(m.getId(), m);
        MotorSettingsRequest motorSettingsRequest = new MotorSettingsRequest(
                m.getName(), m.getIp(), m.isActive(), m.getBattery(), m.getLength(),
                m.getLevel(), m.getStyle()
        );
        client.updateMotor(motors, currentMotorId, motorSettingsRequest);
    }

    private void pushScheduleState(Schedule s) {
        if (schedules.getValue() != null) {
            schedules.getValue().put(s.getId(), s);
            ScheduleSettingsRequest scheduleSettingsRequest = new ScheduleSettingsRequest(
                    s.getName(), s.isActive(), s.getDays(), s.getTargetLevel(), s.getGradient(), s.getMotorIds(), s.getTime()
            );
            client.updateSchedule(schedules, s.getId(), scheduleSettingsRequest);
        }
    }

    private void pushMotionSensorState(MotionSensor s) {
        if (motionSensors.getValue() != null) {
            motionSensors.getValue().put(s.getId(), s);
            MotionSensorSettingsRequest request = new MotionSensorSettingsRequest(
                    s.getMotorIds(), s.getName(), s.getIp(), s.isActive(), s.getBattery(), s.getStyle(), s.getDurationSensitivity());
            client.updateMotionSensor(motionSensors, s.getId(), request);
        }
    }

    private void pushLightSensorState(LightSensor s) {
        if (lightSensors.getValue() != null) {
            lightSensors.getValue().put(s.getId(), s);
            LightSensorSettingsRequest request = new LightSensorSettingsRequest(
                    s.getMotorIds(), s.getName(), s.getIp(), s.isActive(), s.getBattery(), s.getStyle());
            client.updateLightSensor(lightSensors, s.getId(), request);
        }
    }

    public void deleteComponent(IdComponent component) {
        if (component.getClass() == MotionSensor.class) {
            client.deleteMotionSensor(motionSensors, (Sensor) component);

        } else if (component.getClass() == LightSensor.class) {
            client.deleteLightSensor(lightSensors, (Sensor) component);

        } else if (component.getClass() == Motor.class) {
            client.deleteMotor(motors, (Motor) component);

        } else if (component.getClass() == Schedule.class) {
            client.deleteSchedule(schedules, (Schedule) component);

        }
    }

    /**
     * Attempts to interpret the given voice command from speech recognition, and take actions as specified.
     *
     * @param voiceCommand the String from voice recognition
     */
    public String interpretVoiceCommand(String voiceCommand) {
        voiceCommand = voiceCommand.toLowerCase();
        Resources res = getApplication().getApplicationContext().getResources();
        String[] openArr = res.getStringArray(R.array.open);
        String[] closeArr = res.getStringArray(R.array.close);
//        String[] openArr = new String[2];
//        String[] closeArr = new String[2];
//        openArr [0] = "open";
//        openArr [1] = "up";
//        closeArr [0] = "close";
//        closeArr [1] = "down";
        boolean ifHasOpen = false;
        boolean ifHasClose = false;
        boolean ifHasNumInRange = false;
        String Num = "";
        boolean ifHasName = false;

        // Check if voiceCommand contains open or synonym of open
        for (String s : openArr) {
            if (voiceCommand.contains(s)) {
                ifHasOpen = true;
                break;
            }
        }

        // Check if voiceCommand contains close or synonym of close
        for (String s : closeArr) {
            if (voiceCommand.contains(s)) {
                ifHasClose = true;
                break;
            }
        }

        // Extract number
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(voiceCommand);
        while (matcher.find()) {
            Num = matcher.group(0);
        }
        if(Integer.parseInt(Num) <= 100 && Integer.parseInt(Num) >= 0){
            ifHasNumInRange = true;
        }

        // Check if voiceCommand contains a blind's name
        for (Motor m : motors.getValue().values()) {
            if (voiceCommand.contains(m.getName())) {
                ifHasName = true;

                if(ifHasNumInRange){
                    // Set blind to specific level
                    setCurrentMotor(m.getId());
                    moveCurrentMotor(Integer.parseInt(Num));
                    return String.format("Set %s to level %s", m.getName(),Num);
                }

                if (ifHasOpen) {
                    // Open the blind
                    setCurrentMotor(m.getId());
                    moveCurrentMotor(0);
                    return String.format("Open %s", m.getName());
                }

                if (ifHasClose) {
                    // Close the blind
                    setCurrentMotor(m.getId());
                    moveCurrentMotor(100);
                    return String.format("Close %s", m.getName());
                }

            }
        }

        if(!ifHasName){
            return "Please include a name of blind";
        }
        return "Please include a command for this blind";
    }
}
