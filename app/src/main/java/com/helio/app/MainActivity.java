package com.helio.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.helio.app.ui.NoComponentHintBackground;
import com.helio.app.ui.settings.AppTheme;

public class MainActivity extends AppCompatActivity implements NoComponentHintBackground {
    private static boolean restartOnSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the theme that is supposed to be active and activate if necessary
        int currentTheme = getTheme(getCurrentThemeId());
        setTheme(currentTheme);
        setContentView(R.layout.activity_main);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_control,
                R.id.navigation_schedule,
                R.id.navigation_sensors,
                R.id.navigation_settings
        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // This makes the bar at the top change when you use the navigation (changes the title text)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // This makes the fragment change when you press the navigation buttons
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navView, navController);

        if (restartOnSettings) {
            navController.navigate(R.id.navigation_settings);
            restartOnSettings = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Navigate backwards when pressing back button in top app bar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public int getCurrentThemeId() {
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        return sharedPrefs.getInt(getString(R.string.user_settings_theme_key), AppTheme.DEFAULT_THEME_ID);
    }

    public void updateTheme(int themeId) {
        AppTheme newTheme = AppTheme.getEnumFromId(themeId);
        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        int currentThemeId = sharedPrefs.getInt(getString(R.string.user_settings_theme_key), AppTheme.DEFAULT_THEME_ID);
        AppTheme currentTheme = AppTheme.getEnumFromId(currentThemeId);
        if (currentTheme == newTheme) {
            System.out.println("New theme \"" + themeId + "\" is already active.");
        } else {
            System.out.println("Updating theme to \"" + themeId + "\"...");
            saveNewTheme(sharedPrefs, themeId);
            restartOnSettings = true;
            activateNewTheme(newTheme);
        }
    }

    private int getTheme(int themeName) {
        AppTheme theme = AppTheme.getEnumFromId(themeName);
        return getTheme(theme);
    }

    private int getTheme(AppTheme theme) {
        int appTheme = R.style.Theme_HelioApp;
        switch (theme) {
            case NIGHT:
                appTheme = R.style.Theme_HelioApp_Night;
                break;
            case HIGH_CONTRAST:
                appTheme = R.style.Theme_HelioApp_HighContrast;
        }
        return appTheme;
    }

    private void activateNewTheme(AppTheme theme) {
        int appTheme = getTheme(theme);
        setTheme(appTheme);
        // reload activity
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void saveNewTheme(SharedPreferences sharedPrefs, int themeId) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(getString(R.string.user_settings_theme_key), themeId);
        editor.apply();
        System.out.println("New theme: " +
                sharedPrefs.getInt(getString(R.string.user_settings_theme_key), AppTheme.DEFAULT_THEME_ID));
    }

    @Override
    public void showNoComponentHint() {
        findViewById(R.id.add_component_hint_image).setVisibility(View.VISIBLE);
        findViewById(R.id.add_component_hint_text).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoComponentHint() {
        findViewById(R.id.add_component_hint_image).setVisibility(View.GONE);
        findViewById(R.id.add_component_hint_text).setVisibility(View.GONE);
    }
}

