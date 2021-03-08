package com.helio.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.helio.app.ui.settings.AppTheme;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the theme that is supposed to be active and activate if necessary
        String currentThemeName = getCurrentThemeName();
        int currentTheme = getTheme(currentThemeName);
        setTheme(currentTheme);
        setContentView(R.layout.activity_main);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_control,
                R.id.navigation_blinds,
                R.id.navigation_hub
        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // This makes the bar at the top change when you use the navigation (changes the title text)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // This makes the fragment change when you press the navigation buttons
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Navigate backwards when pressing back button in top app bar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentThemeName() {
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        return sharedPrefs.getString(getString(R.string.user_settings_theme_key), "Default");
    }
    public void updateTheme(String themeName) {
        AppTheme newTheme = AppTheme.getEnumFromName(themeName);
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        String currentThemeName = sharedPrefs.getString(getString(R.string.user_settings_theme_key),
                "Default");
        AppTheme currentTheme = AppTheme.getEnumFromName(currentThemeName);
        if(currentTheme == newTheme) {
            System.out.println("New theme \"" + themeName + "\" is already active.");
        } else {
            System.out.println("Updating theme to \"" + themeName + "\"...");
            saveNewThemeName(sharedPrefs, themeName);
            activateNewTheme(newTheme);
        }
    }

    private int getTheme(String themeName) {
        AppTheme theme = AppTheme.getEnumFromName(themeName);
        return getTheme(theme);
    }

    private int getTheme(AppTheme theme) {
        int appTheme = R.style.Theme_HelioApp;
        switch(theme) {
            case NIGHT:
                appTheme = R.style.Theme_HelioApp_Night;
        }
        return appTheme;
    }

    private void activateNewTheme(AppTheme theme) {
        int appTheme = getTheme(theme);
        setTheme(appTheme);
        // reload activity
        finish();
        startActivity(getIntent());
    }

    private void saveNewThemeName(SharedPreferences sharedPrefs, String themeName) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(getString(R.string.user_settings_theme_key), themeName);
        editor.apply();
        System.out.println("New theme name: " + sharedPrefs.getString(getString(R.string.user_settings_theme_key), "DEFAULT"));
    }
}

