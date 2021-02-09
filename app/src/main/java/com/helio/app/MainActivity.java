package com.helio.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.helio.app.networking.MoveMotorRequest;
import com.helio.app.networking.RegisterMotorRequest;
import com.helio.app.networking.HubClient;
import com.helio.app.networking.Motor;
import com.helio.app.networking.RenameMotorRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<Integer, Motor> motors;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_schedule, R.id.navigation_hub)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // This makes the bar at the top change when you use the navigation (changes the title text)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // This makes the fragment change when you press the navigation buttons
        NavigationUI.setupWithNavController(navView, navController);

        fetchState();
    }

    private void fetchState() {
        motors = new HashMap<>();
        HubClient client = new HubClient("http://10.0.2.2:4310/");
        RegisterMotorRequest registerMotorRequest = new RegisterMotorRequest(
                42,
                "bedroom",
                "1.2.3.4",
                false
        );
        RenameMotorRequest renameMotorRequest = new RenameMotorRequest(42, "kitchen");
        MoveMotorRequest moveMotorRequest = new MoveMotorRequest(42, 25);
        // use Handler to force the actions to happen in order
        Handler handler = new Handler();
        handler.postDelayed(() -> client.addMotor(motors, registerMotorRequest), 0);
        handler.postDelayed(() -> client.activateMotor(motors, 42), 250);
        handler.postDelayed(() -> client.renameMotor(motors, renameMotorRequest), 500);
        handler.postDelayed(() -> client.getMotor(motors, 42), 750);
        handler.postDelayed(() -> client.moveMotor(motors, moveMotorRequest), 1000);
        handler.postDelayed(() -> client.startMotorCalibration(motors, 42), 1250);
        handler.postDelayed(() -> client.stopMotorCalibration(motors, 42), 1500);
        handler.postDelayed(() -> client.deactivateMotor(motors, 42), 1750);
        handler.postDelayed(() -> client.deleteMotor(motors, 42), 2000);
    }
}