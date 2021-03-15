package com.helio.app.networking;

import com.helio.app.model.LightSensor;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Motor;
import com.helio.app.model.Schedule;
import com.helio.app.networking.request.LightSensorSettingsRequest;
import com.helio.app.networking.request.MotionSensorSettingsRequest;
import com.helio.app.networking.request.MotorSettingsRequest;
import com.helio.app.networking.request.ScheduleSettingsRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * This interface matches the OpenAPI specification we came up with as a group.
 *
 * @see HubClient
 */
public interface HubService {
    @POST("/motor/register")
    Call<Motor> addMotor(@Body MotorSettingsRequest motorSettingsRequest);

    @PATCH("/motor/update/{motor_id}")
    Call<Motor> updateMotor(@Path("motor_id") int id, @Body MotorSettingsRequest motorSettingsRequest);

    @PATCH("/motor/move/{motor_id}")
    Call<Motor> moveMotor(@Path("motor_id") int id, @Body int level);

    @PATCH("/motor/start_calibrate/{motor_id}")
    Call<Motor> startMotorCalibration(@Path("motor_id") int id);

    @PATCH("/motor/stop_calibrate/{motor_id}")
    Call<Motor> stopMotorCalibration(@Path("motor_id") int id);

    @GET("/motor/get_all")
    Call<List<Motor>> getAllMotors();

    @GET("/schedule/get_all")
    Call<List<Schedule>> getAllSchedules();

    @POST("/schedule/register")
    Call<Schedule> addSchedule(@Body ScheduleSettingsRequest scheduleSettingsRequest);

    @PATCH("/schedule/update/{schedule_id}")
    Call<Schedule> updateSchedule(@Path("schedule_id") int id, @Body ScheduleSettingsRequest scheduleSettingsRequest);

    @POST("/light/register")
    Call<LightSensor> addLightSensor(@Body LightSensorSettingsRequest registerLightSensorRequest);

    @GET("/light/get_all")
    Call<List<LightSensor>> getAllLightSensors();

    @PATCH("/light/update/{light_sensor_id}")
    Call<LightSensor> updateLightSensor(@Path("light_sensor_id") int id, @Body LightSensorSettingsRequest lightSensorSettingsRequest);

    @POST("/motion/register")
    Call<MotionSensor> addMotionSensor(@Body MotionSensorSettingsRequest motionSensorSettingsRequest);

    @GET("/motion/get_all")
    Call<List<MotionSensor>> getAllMotionSensors();

    @PATCH("/motion/update/{motion_sensor_id}")
    Call<MotionSensor> updateMotionSensor(@Path("motion_sensor_id") int id, @Body MotionSensorSettingsRequest motionSensorSettingsRequest);

    @GET("/motor/get_all")
    Call<List<Motor>> getNetworkStatus();
}
