package com.helio.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

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
import com.helio.app.networking.CalibrationIntervalManager;
import com.helio.app.networking.HubClient;
import com.helio.app.networking.IPAddress;
import com.helio.app.networking.NetworkStatus;
import com.helio.app.networking.request.LightSensorSettingsRequest;
import com.helio.app.networking.request.MotionSensorSettingsRequest;
import com.helio.app.networking.request.MotorSettingsRequest;
import com.helio.app.networking.request.ScheduleSettingsRequest;

import java.text.NumberFormat;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDataViewModel extends AndroidViewModel {
    private final SharedPreferences sharedPrefs;
    private HubClient client;
    private MutableLiveData<Map<Integer, Motor>> motors;
    private MutableLiveData<Map<Integer, Schedule>> schedules;
    private MutableLiveData<Map<Integer, LightSensor>> lightSensors;
    private MutableLiveData<Map<Integer, MotionSensor>> motionSensors;
    private final CalibrationIntervalManager calibrationIntervalManager;

    public UserDataViewModel(@NonNull Application application) {
        super(application);
        sharedPrefs = getApplication().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        client = createClient(getHubIp());
        calibrationIntervalManager = new CalibrationIntervalManager(client);
    }

    public static String removeTrailingSpaces(String param) {
        if (param == null)
            return null;
        int len = param.length();
        for (; len > 0; len--) {
            if (!Character.isWhitespace(param.charAt(len - 1)))
                break;
        }
        return param.substring(0, len);
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

    public LiveData<Map<Integer, Motor>> addMotor() {
        MotorSettingsRequest motorSettingsRequest = MotorSettingsRequest.newMotorRequest();
        client.addMotor(motors, motorSettingsRequest);
        return motors;
    }

    public void toggleScheduleActive(Schedule s) {
        s.setActive(!s.isActive());
        pushScheduleState(s);
    }

    public LiveData<Map<Integer, Schedule>> addSchedule() {
        client.addSchedule(schedules, ScheduleSettingsRequest.newScheduleRequest());
        return schedules;
    }

    public LiveData<Map<Integer, MotionSensor>> addMotionSensor() {
        client.addMotionSensor(motionSensors, MotionSensorSettingsRequest.newSensorRequest());
        return motionSensors;
    }

    public LiveData<Map<Integer, LightSensor>> addLightSensor() {
        client.addLightSensor(lightSensors, LightSensorSettingsRequest.newSensorRequest());
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
        client.updateMotor(motors, m.getId(), motorSettingsRequest);
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

    private String getIpKey() {
        return getApplication().getString(R.string.ip_key);
    }

    public void startCalibration(Motor motor) {
        client.startCalibration(motor);
    }

    public void stopCalibration(Motor motor) {
        client.stopCalibration(motor);
    }

    public void moveUp(Motor motor) {
        calibrationIntervalManager.startMoveUpRequestLoop(motor);
    }

    public void moveDown(Motor motor) {
        calibrationIntervalManager.startMoveDownRequestLoop(motor);
    }

    public void stopMoving(Motor motor) {
        calibrationIntervalManager.stopRequestLoop();
    }

    public void setHighestPoint(Motor motor) {
        client.setHighestPoint(motor);
    }

    public void setLowestPoint(Motor motor) {
        client.setLowestPoint(motor);
    }

    public MutableLiveData<NetworkStatus> getNetworkStatus() {
        MutableLiveData<NetworkStatus> status = new MutableLiveData<>();
        client.getNetworkStatus(status);
        return status;
    }

    /**
     * Attempts to interpret the given voice command from speech recognition, and take actions as specified.
     *
     * @param voiceCommand the String from voice recognition
     * @param tts          text to speech engine
     * @return the motors data for updating the GUI
     */
    public MutableLiveData<Map<Integer, Motor>> interpretVoiceCommand(String voiceCommand, TextToSpeech tts) {
        voiceCommand = voiceCommand.toLowerCase();

        Resources res = getApplication().getApplicationContext().getResources();
        String[] openWords = res.getStringArray(R.array.open);
        String[] closeWords = res.getStringArray(R.array.close);

        boolean hasOpen = false;
        boolean hasClosed = false;
        boolean hasNumInRange = false;
        boolean hasName = false;
        String numberString = "";

        // Check if voiceCommand contains open or synonym of open
        for (String s : openWords) {
            if (voiceCommand.contains(s)) {
                hasOpen = true;
                break;
            }
        }

        // Check if voiceCommand contains close or synonym of close
        for (String s : closeWords) {
            if (voiceCommand.contains(s)) {
                hasClosed = true;
                break;
            }
        }

        // Extract number
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(voiceCommand);
        while (matcher.find()) {
            numberString = matcher.group(0);
        }
        assert numberString != null;
        if (!numberString.equals("")) {
            if (Integer.parseInt(numberString) <= 100 && Integer.parseInt(numberString) >= 0) {
                hasNumInRange = true;
            }
        }

        String returnString = null;
        Motor targetMotor = null;
        // Check if voiceCommand contains a blind's name
        for (Motor m : Objects.requireNonNull(motors.getValue()).values()) {
            String motorName = removeTrailingSpaces(m.getName().toLowerCase());
            if (!m.getName().equals("") && voiceCommand.contains(motorName)) {
                hasName = true;
                targetMotor = m;

                if (hasNumInRange) {
                    // Set blind to specific level
                    int level = Integer.parseInt(numberString);
                    m.setLevel(level);
                    returnString = res.getString(R.string.setting_level_message, m.getName(), NumberFormat.getPercentInstance().format(((float) level) / 100));
                } else if (hasOpen) {
                    // Open the blind
                    m.setLevel(0);
                    returnString = res.getString(R.string.open_message, m.getName());
                } else if (hasClosed) {
                    // Close the blind
                    m.setLevel(100);
                    returnString = res.getString(R.string.close_message, m.getName());
                }
                break;
            }
        }

        if (!hasName) {
            returnString = res.getString(R.string.blinds_not_found_message);
        } else if (returnString == null) {
            returnString = res.getString(R.string.command_not_recognised_message);
        } else {
            pushMotorState(targetMotor);
        }
        Toast.makeText(getApplication(), returnString, Toast.LENGTH_LONG).show();
        tts.speak(returnString, TextToSpeech.QUEUE_FLUSH, null, "");
        return motors;
    }
}
