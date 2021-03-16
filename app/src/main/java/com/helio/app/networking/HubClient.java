package com.helio.app.networking;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.helio.app.model.IdComponent;
import com.helio.app.model.LightSensor;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Motor;
import com.helio.app.model.Schedule;
import com.helio.app.model.Sensor;
import com.helio.app.networking.request.LightSensorSettingsRequest;
import com.helio.app.networking.request.MotionSensorSettingsRequest;
import com.helio.app.networking.request.MotorSettingsRequest;
import com.helio.app.networking.request.ScheduleSettingsRequest;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public void addMotor(MutableLiveData<Map<Integer, Motor>> motors, MotorSettingsRequest motorSettingsRequest) {
        Call<Motor> call = service.addMotor(motorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(motors));
    }

    public void updateMotor(MutableLiveData<Map<Integer, Motor>> motors, int id, MotorSettingsRequest motorSettingsRequest) {
        Call<Motor> call = service.updateMotor(id, motorSettingsRequest);
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

    public void updateSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, int id, ScheduleSettingsRequest scheduleSettingsRequest) {
        Call<Schedule> call = service.updateSchedule(id, scheduleSettingsRequest);
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

    public void updateMotionSensor(MutableLiveData<Map<Integer, MotionSensor>> sensors, int id, MotionSensorSettingsRequest motionSensorSettingsRequest) {
        Call<MotionSensor> call = service.updateMotionSensor(id, motionSensorSettingsRequest);
        call.enqueue(new IdComponentCallback<>(sensors));
    }

    public void deleteMotor(MutableLiveData<Map<Integer, Motor>> motors, Motor m) {
        Call<ResponseBody> call = service.deleteMotor(m.getId());
        call.enqueue(new DeletionCallback<>(motors, m.getId()));
    }

    public void deleteSchedule(MutableLiveData<Map<Integer, Schedule>> schedules, Schedule s) {
        Call<ResponseBody> call = service.deleteSchedule(s.getId());
        call.enqueue(new DeletionCallback<>(schedules, s.getId()));
    }

    public void deleteMotionSensor(MutableLiveData<Map<Integer, MotionSensor>> sensors, Sensor s) {
        Call<ResponseBody> call = service.deleteMotionSensor(s.getId());
        call.enqueue(new DeletionCallback<>(sensors, s.getId()));
    }

    public void deleteLightSensor(MutableLiveData<Map<Integer, LightSensor>> sensors, Sensor s) {
        Call<ResponseBody> call = service.deleteLightSensor(s.getId());
        call.enqueue(new DeletionCallback<>(sensors, s.getId()));
    }

    public void getNetworkStatus(MutableLiveData<NetworkStatus> status) {
        Call<List<Motor>> call = service.getNetworkStatus();
        call.enqueue(new NetworkStatusCallback(status));
    }

}
