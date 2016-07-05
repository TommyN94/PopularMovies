package com.example.android.popularmovies;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add preferences defined in preferences.xml
        addPreferencesFromResource(R.xml.preferences);
    }
}
