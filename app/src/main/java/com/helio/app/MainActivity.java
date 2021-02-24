package com.helio.app;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.helio.app.model.Day;
import com.helio.app.model.LightSensor;
import com.helio.app.model.MotionSensor;
import com.helio.app.model.Motor;
import com.helio.app.model.Schedule;
import com.helio.app.networking.HubClient;
import com.helio.app.networking.request.LightSensorSettingsRequest;
import com.helio.app.networking.request.MotionSensorSettingsRequest;
import com.helio.app.networking.request.MotorSettingsRequest;
import com.helio.app.networking.request.ScheduleSettingsRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    public static final int DELAY = 500;
    private int postTime = 0;
    private Map<Integer, Motor> motors;
    private Map<Integer, Schedule> schedules;
    private Map<Integer, LightSensor> lightSensors;
    private Map<Integer, MotionSensor> motionSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_control, R.id.navigation_blinds, R.id.navigation_hub)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // This makes the bar at the top change when you use the navigation (changes the title text)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // This makes the fragment change when you press the navigation buttons
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navView, navController);
        fetchState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Navigate backwards when pressing back button in top app bar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchState() {
        motors = new HashMap<>();
        HubClient client = new HubClient("http://10.0.2.2:4310/");
        testMotor(client);
        testSchedule(client);
        testLightSensors(client);
        testMotionSensors(client);
    }

    private void testMotor(HubClient client) {
        MotorSettingsRequest motorSettingsRequest = new MotorSettingsRequest(
                "bedroom",
                "1.2.3.4",
                false,
                0, 0, 0, "style");
        // use Handler to force the actions to happen in order
        Handler handler = new Handler();

        postTime += DELAY;
        handler.postDelayed(() -> client.getAllMotors(motors), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.addMotor(motors, motorSettingsRequest), postTime);

        // This is extremely bad
        AtomicInteger id = new AtomicInteger(-1);

        postTime += DELAY;
        handler.postDelayed(() -> id.set(new ArrayList<>(motors.keySet()).get(0)), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.activateMotor(motors, id.get()), postTime);

        postTime += DELAY;
        MotorSettingsRequest newMotorSettingsRequest = new MotorSettingsRequest("Mars", "5.6.7.8",
                false, 0, 0, 0, "fancy");
        handler.postDelayed(() -> client.updateMotor(motors, id.get(), newMotorSettingsRequest), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.renameMotor(motors, id.get(), "kitchen"), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.getMotor(motors, id.get()), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.moveMotor(motors, id.get(), 25), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.startMotorCalibration(motors, id.get()), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.stopMotorCalibration(motors, id.get()), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.deactivateMotor(motors, id.get()), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.deleteMotor(motors, id.get()), postTime);
    }

    private void testSchedule(HubClient client) {
        schedules = new HashMap<>();
        List<Integer> motorIds = new ArrayList<>();
        motorIds.add(999);
        List<Day> days = new ArrayList<>();
        days.add(Day.MONDAY);
        ScheduleSettingsRequest scheduleSettingsRequest = new ScheduleSettingsRequest(
                "name",
                false,
                days,
                0, 0, motorIds, "16:20");

        // use Handler to force the actions to happen in order
        Handler handler = new Handler();

        postTime += DELAY;
        handler.postDelayed(() -> client.getAllSchedules(schedules), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.addSchedule(schedules, scheduleSettingsRequest), postTime);

        // This is extremely bad
        postTime += DELAY;
        AtomicInteger id = new AtomicInteger(-1);
        handler.postDelayed(() -> id.set(new ArrayList<>(schedules.keySet()).get(0)), postTime);

        postTime += DELAY;
        ScheduleSettingsRequest newScheduleSettingsRequest = new ScheduleSettingsRequest(
                "new name", false, days, 0, 12, motorIds, "00:00");
        handler.postDelayed(() -> client.updateSchedule(schedules, id.get(), newScheduleSettingsRequest), postTime);

        postTime += DELAY;
        days.remove(0);
        days.add(Day.SATURDAY);
        days.add(Day.SUNDAY);
        handler.postDelayed(() -> client.changeDaysSchedule(schedules, id.get(), days), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.changeTimeSchedule(schedules, id.get(), "a"), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.changeGradientSchedule(schedules, id.get(), 7216), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.renameSchedule(schedules, id.get(), "newName"), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.activateSchedule(schedules, id.get()), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.deactivateSchedule(schedules, id.get()), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.deleteSchedule(schedules, id.get()), postTime);
    }

    private void testLightSensors(HubClient client) {
        lightSensors = new HashMap<>();
        List<Integer> motorIds = new ArrayList<>();
        motorIds.add(999);
        LightSensorSettingsRequest lightSensorSettingsRequest = new LightSensorSettingsRequest(
                motorIds, "name", "1.2.3.4", false, 0, "style");

        // use Handler to force the actions to happen in order
        Handler handler = new Handler();

        postTime += DELAY;
        handler.postDelayed(() -> client.getAllLightSensors(lightSensors), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.addLightSensor(lightSensors, lightSensorSettingsRequest), postTime);

        // This is extremely bad
        postTime += DELAY;
        AtomicInteger id = new AtomicInteger(-1);
        handler.postDelayed(() -> id.set(new ArrayList<>(lightSensors.keySet()).get(0)), postTime);

        postTime += DELAY;
        LightSensorSettingsRequest newLightSensorSettingsRequest = new LightSensorSettingsRequest(
                motorIds, "a", "0.0.0.0", false, 0, "style");
        handler.postDelayed(() -> client.updateLightSensor(lightSensors, id.get(), newLightSensorSettingsRequest), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.deleteLightSensor(lightSensors, id.get()), postTime);
    }

    private void testMotionSensors(HubClient client) {
        motionSensors = new HashMap<>();
        List<Integer> motorIds = new ArrayList<>();
        motorIds.add(999);
        MotionSensorSettingsRequest motionSensorSettingsRequest = new MotionSensorSettingsRequest(
                motorIds, "name", "1.2.3.4", false, 0, "style", "30");

        // use Handler to force the actions to happen in order
        Handler handler = new Handler();

        postTime += DELAY;
        handler.postDelayed(() -> client.getAllMotionSensors(motionSensors), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.addMotionSensor(motionSensors, motionSensorSettingsRequest), postTime);

        // This is extremely bad
        postTime += DELAY;
        AtomicInteger id = new AtomicInteger(-1);
        handler.postDelayed(() -> id.set(new ArrayList<>(motionSensors.keySet()).get(0)), postTime);

        postTime += DELAY;
        MotionSensorSettingsRequest newMotionSensorSettingsRequest = new MotionSensorSettingsRequest(
                motorIds, "new", "1.1.1.1", false, 0, "style", "40");
        handler.postDelayed(() -> client.updateMotionSensor(motionSensors, id.get(), newMotionSensorSettingsRequest), postTime);

        postTime += DELAY;
        handler.postDelayed(() -> client.deleteMotionSensor(motionSensors, id.get()), postTime);
    }

}

