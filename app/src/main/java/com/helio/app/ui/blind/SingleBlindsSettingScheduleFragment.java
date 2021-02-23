package com.helio.app.ui.blind;

import android.os.Bundle;
import android.text.InputType;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;


import com.helio.app.R;


public class SingleBlindsSettingScheduleFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.single_blind_schedule_preferences,rootKey);

        EditTextPreference time = findPreference("Time");
        ListPreference Day = findPreference("Day");
        ListPreference State = findPreference("State");

    }
}
