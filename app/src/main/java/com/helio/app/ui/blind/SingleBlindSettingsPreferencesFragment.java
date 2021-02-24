package com.helio.app.ui.blind;

import android.os.Bundle;
import android.text.InputType;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.helio.app.R;

public class SingleBlindSettingsPreferencesFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.single_blind_preferences, rootKey);

        EditTextPreference namePreference = findPreference("name");
        EditTextPreference ipPreference = findPreference("ip");
        ListPreference changeIconPreference = findPreference("changeIcon");
        Preference calibrationPreference = findPreference("calibration");
        Preference openNowPreference = findPreference("openNow");
        Preference closeNowPreference = findPreference("closeNow");
        Preference createSchedulePreference = findPreference("createSchedule");
        Preference seeSchedulePreference = findPreference("seeSchedule");
        Preference motionSensorPreference = findPreference("motionSensor");
        Preference lightSensorPreference = findPreference("lightSensor");

        assert ipPreference != null;
        ipPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_PHONE));

        assert namePreference != null;
        namePreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_TEXT));


    }
}