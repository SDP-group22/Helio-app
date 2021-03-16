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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @DELETE("/motor/unregister/{motor_id}")
    Call<ResponseBody> deleteMotor(@Path("motor_id") int id);

    @DELETE("/schedule/unregister/{schedule_id}")
    Call<ResponseBody> deleteSchedule(@Path("schedule_id") int id);

    @DELETE("/motion/unregister/{motion_sensor_id}")
    Call<ResponseBody> deleteMotionSensor(@Path("motion_sensor_id") int id);

    @DELETE("/light/unregister/{light_sensor_id}")
    Call<ResponseBody> deleteLightSensor(@Path("light_sensor_id") int id);

    @GET("/motor/get_all")
    Call<List<Motor>> getNetworkStatus();

    @PATCH("/motor/calibrate/start/{motor_id}")
    Call<ResponseBody> startCalibration(@Path("motor_id") int id);

    @PATCH("/motor/calibrate/stop/{motor_id}")
    Call<ResponseBody> stopCalibration(@Path("motor_id") int id);

    @PATCH("/motor/calibrate/move_up/{motor_id}")
    Call<ResponseBody> moveUp(@Path("motor_id") int id);

    @PATCH("/motor/calibrate/move_down/{motor_id}")
    Call<ResponseBody> moveDown(@Path("motor_id") int id);

    @PATCH("/motor/calibrate/stop_moving/{motor_id}")
    Call<ResponseBody> stopMoving(@Path("motor_id") int id);

    @PATCH("/motor/calibrate/set_highest/{motor_id}")
    Call<ResponseBody> setHighestPoint(@Path("motor_id") int id);

    @PATCH("/motor/calibrate/set_lowest/{motor_id}")
    Call<ResponseBody> setLowestPoint(@Path("motor_id") int id);
}
