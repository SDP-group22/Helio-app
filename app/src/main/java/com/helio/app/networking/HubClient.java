package com.helio.app.networking;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.helio.app.model.Day;
import com.helio.app.model.LightSensor;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Motor;
import com.helio.app.model.Schedule;
import com.helio.app.networking.request.LightSensorSettingsRequest;
import com.helio.app.networking.request.MotionSensorSettingsRequest;
import com.helio.app.networking.request.MotorSettingsRequest;
import com.helio.app.networking.request.ScheduleSettingsRequest;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.helio.app.model.Sensor;

/**
 * HubClient is the client-side code we use to communicate with the Hub.
 * Using retrofit and an interface that matches our OpenAPI specification, sending requests becomes
 * quite easy to do.<br/>
 * The following methods abstract this away further, by automatically updating our local state after
 * we receive a response.
 *
 * @see HubService
 */
public class HubClient {
    private final HubService service;

    public HubClient(String baseAddress) {
        // Logging: https://stackoverflow.com/a/33328524/
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Logging level
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(logging);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseAddress)
                .addConverterFactory(buildCustomConverter())
                .client(httpClient.build());
        Retrofit retrofit = builder.build();
        service = retrofit.create(HubService.class);
    }

    private static GsonConverterFactory buildCustomConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Adding custom deserializers
        gsonBuilder.registerTypeAdapter(Schedule.class, new ScheduleDeserializer());
        Gson gson = gsonBuilder.create();

        return GsonConverterFactory.create(gson);
    }

    public void addMotor(MutableLiveData<Map<Integer, Motor>> motors, MotorSettingsRequest motorSettingsRequest) {
        Call<Motor> call = service.addMotor(motorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void activateMotor(MutableLiveData<Map<Integer, Motor>> motors, int motorId) {
        Call<Motor> call = service.activateMotor(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void deactivateMotor(MutableLiveData<Map<Integer, Motor>> motors, int motorId) {
        Call<Motor> call = service.deactivateMotor(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void updateMotor(MutableLiveData<Map<Integer, Motor>> motors, int id, MotorSettingsRequest motorSettingsRequest) {
        Call<Motor> call = service.updateMotor(id, motorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void renameMotor(MutableLiveData<Map<Integer, Motor>> motors, int id, String name) {
        Call<Motor> call = service.renameMotor(id, name);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void getMotor(MutableLiveData<Map<Integer, Motor>> motors, int motorId) {
        Call<Motor> call = service.getMotor(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void moveMotor(Motor motor, int level) {
        Call<Motor> call = service.moveMotor(motor.getId(), level);
        call.enqueue(new MoveMotorCallback(motor));
    }

    public void startMotorCalibration(MutableLiveData<Map<Integer, Motor>> motors, int motorId) {
        Call<Motor> call = service.startMotorCalibration(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void stopMotorCalibration(MutableLiveData<Map<Integer, Motor>> motors, int motorId) {
        Call<Motor> call = service.stopMotorCalibration(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void deleteMotor(MutableLiveData<Map<Integer, Motor>> motors, int motorId) {
        Call<ResponseBody> call = service.deleteMotor(motorId);
        call.enqueue(new DeletionCallback<>(motors, motorId));
    }

    public void getAllMotors(MutableLiveData<Map<Integer, Motor>> motors) {
        Call<List<Motor>> call = service.getAllMotors();
        call.enqueue(new GetAllCallback<>(motors));
    }

    public void getAllSchedules(MutableLiveData<Map<Integer, Schedule>> schedules) {
        Call<List<Schedule>> call = service.getAllSchedules();
        call.enqueue(new GetAllCallback<>(schedules));
    }

    public void addSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, ScheduleSettingsRequest scheduleSettingsRequest) {
        Call<Schedule> call = service.addSchedule(scheduleSettingsRequest);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void deleteSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, int id) {
        Call<ResponseBody> call = service.deleteSchedule(id);
        call.enqueue(new DeletionCallback<>(schedules, id));
    }

    public void updateSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, int id, ScheduleSettingsRequest scheduleSettingsRequest) {
        Call<Schedule> call = service.updateSchedule(id, scheduleSettingsRequest);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void changeDaysSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, int id, List<Day> days) {
        Call<Schedule> call = service.changeDaysSchedule(id, days);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void changeTimeSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, int id, String time) {
        Call<Schedule> call = service.changeTimeSchedule(id, time);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void changeGradientSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, int id, int gradient) {
        Call<Schedule> call = service.changeGradientSchedule(id, gradient);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void renameSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, int id, String name) {
        Call<Schedule> call = service.renameSchedule(id, name);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void activateSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, int id) {
        Call<Schedule> call = service.activateSchedule(id);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void deactivateSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, int id) {
        Call<Schedule> call = service.deactivateSchedule(id);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void getAllLightSensors(MutableLiveData<Map<Integer, LightSensor>> sensors) {
        Call<List<LightSensor>> call = service.getAllLightSensors();
        call.enqueue(new GetAllCallback<>(sensors));
    }

    public void addLightSensor(MutableLiveData<Map<Integer, LightSensor>> sensors, LightSensorSettingsRequest lightSensorSettingsRequest) {
        Call<LightSensor> call = service.addLightSensor(lightSensorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(sensors));
    }

    public void deleteLightSensor(MutableLiveData<Map<Integer, LightSensor>> sensors, int id) {
        Call<ResponseBody> call = service.deleteLightSensor(id);
        call.enqueue(new DeletionCallback<>(sensors, id));
    }

    public void updateLightSensor(MutableLiveData<Map<Integer, LightSensor>> sensors, int id, LightSensorSettingsRequest lightSensorSettingsRequest) {
        Call<LightSensor> call = service.updateLightSensor(id, lightSensorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(sensors));
    }

    public void getAllMotionSensors(MutableLiveData<Map<Integer, MotionSensor>> sensors) {
        Call<List<MotionSensor>> call = service.getAllMotionSensors();
        call.enqueue(new GetAllCallback<>(sensors));
    }

    public void addMotionSensor(MutableLiveData<Map<Integer, MotionSensor>> sensors, MotionSensorSettingsRequest motionSensorSettingsRequest) {
        Call<MotionSensor> call = service.addMotionSensor(motionSensorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(sensors));
    }

    public void deleteMotionSensor(MutableLiveData<Map<Integer, MotionSensor>> sensors, int id) {
        Call<ResponseBody> call = service.deleteMotionSensor(id);
        call.enqueue(new DeletionCallback<>(sensors, id));
    }

    public void updateMotionSensor(MutableLiveData<Map<Integer, MotionSensor>> sensors, int id, MotionSensorSettingsRequest motionSensorSettingsRequest) {
        Call<MotionSensor> call = service.updateMotionSensor(id, motionSensorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(sensors));
    }
}
