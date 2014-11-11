package com.pkg.remotecontrol2;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class RemoteControlSettingsActivity extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_app);
        PreferenceManager.setDefaultValues(this, R.xml.settings_app, false);
    }
}
