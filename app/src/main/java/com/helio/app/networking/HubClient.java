package com.helio.app.networking;

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
        httpClient.addInterceptor(logging);

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

    public void addMotor(Map<Integer, Motor> motors, MotorSettingsRequest motorSettingsRequest) {
        Call<Motor> call = service.addMotor(motorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void activateMotor(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.activateMotor(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void deactivateMotor(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.deactivateMotor(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void updateMotor(Map<Integer, Motor> motors, int id, MotorSettingsRequest motorSettingsRequest) {
        Call<Motor> call = service.updateMotor(id, motorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void renameMotor(Map<Integer, Motor> motors, int id, String name) {
        Call<Motor> call = service.renameMotor(id, name);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void getMotor(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.getMotor(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void moveMotor(Map<Integer, Motor> motors, int id, int level) {
        Call<Motor> call = service.moveMotor(id, level);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void startMotorCalibration(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.startMotorCalibration(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void stopMotorCalibration(Map<Integer, Motor> motors, int motorId) {
        Call<Motor> call = service.stopMotorCalibration(motorId);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void deleteMotor(Map<Integer, Motor> motors, int motorId) {
        Call<ResponseBody> call = service.deleteMotor(motorId);
        call.enqueue(new DeletionCallback<>(motors, motorId));
    }

    public void getAllMotors(Map<Integer, Motor> motors) {
        Call<List<Motor>> call = service.getAllMotors();
        call.enqueue(new GetAllCallback<>(motors));
    }

    public void getAllSchedules(Map<Integer, Schedule> schedules) {
        Call<List<Schedule>> call = service.getAllSchedules();
        call.enqueue(new GetAllCallback<>(schedules));
    }

    public void addSchedule(Map<Integer, Schedule> schedules, ScheduleSettingsRequest scheduleSettingsRequest) {
        Call<Schedule> call = service.addSchedule(scheduleSettingsRequest);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void deleteSchedule(Map<Integer, Schedule> schedules, int id) {
        Call<ResponseBody> call = service.deleteSchedule(id);
        call.enqueue(new DeletionCallback<>(schedules, id));
    }

    public void updateSchedule(Map<Integer, Schedule> schedules, int id, ScheduleSettingsRequest scheduleSettingsRequest) {
        Call<Schedule> call = service.updateSchedule(id, scheduleSettingsRequest);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void changeDaysSchedule(Map<Integer, Schedule> schedules, int id, List<Day> days) {
        Call<Schedule> call = service.changeDaysSchedule(id, days);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void changeTimeSchedule(Map<Integer, Schedule> schedules, int id, String time) {
        Call<Schedule> call = service.changeTimeSchedule(id, time);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void changeGradientSchedule(Map<Integer, Schedule> schedules, int id, int gradient) {
        Call<Schedule> call = service.changeGradientSchedule(id, gradient);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void renameSchedule(Map<Integer, Schedule> schedules, int id, String name) {
        Call<Schedule> call = service.renameSchedule(id, name);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void activateSchedule(Map<Integer, Schedule> schedules, int id) {
        Call<Schedule> call = service.activateSchedule(id);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void deactivateSchedule(Map<Integer, Schedule> schedules, int id) {
        Call<Schedule> call = service.deactivateSchedule(id);
        call.enqueue(new IdComponentCallback<>(schedules));
    }

    public void getAllLightSensors(Map<Integer, LightSensor> sensors) {
        Call<List<LightSensor>> call = service.getAllLightSensors();
        call.enqueue(new GetAllCallback<>(sensors));
    }

    public void addLightSensor(Map<Integer, LightSensor> sensors, LightSensorSettingsRequest lightSensorSettingsRequest) {
        Call<LightSensor> call = service.addLightSensor(lightSensorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(sensors));
    }

    public void deleteLightSensor(Map<Integer, LightSensor> sensors, int id) {
        Call<ResponseBody> call = service.deleteLightSensor(id);
        call.enqueue(new DeletionCallback<>(sensors, id));
    }

    public void updateLightSensor(Map<Integer, LightSensor> sensors, int id, LightSensorSettingsRequest lightSensorSettingsRequest) {
        Call<LightSensor> call = service.updateLightSensor(id, lightSensorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(sensors));
    }

    public void getAllMotionSensors(Map<Integer, MotionSensor> sensors) {
        Call<List<MotionSensor>> call = service.getAllMotionSensors();
        call.enqueue(new GetAllCallback<>(sensors));
    }

    public void addMotionSensor(Map<Integer, MotionSensor> sensors, MotionSensorSettingsRequest motionSensorSettingsRequest) {
        Call<MotionSensor> call = service.addMotionSensor(motionSensorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(sensors));
    }

    public void deleteMotionSensor(Map<Integer, MotionSensor> sensors, int id) {
        Call<ResponseBody> call = service.deleteMotionSensor(id);
        call.enqueue(new DeletionCallback<>(sensors, id));
    }

    public void updateMotionSensor(Map<Integer, MotionSensor> sensors, int id, MotionSensorSettingsRequest motionSensorSettingsRequest) {
        Call<MotionSensor> call = service.updateMotionSensor(id, motionSensorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(sensors));
    }
}
