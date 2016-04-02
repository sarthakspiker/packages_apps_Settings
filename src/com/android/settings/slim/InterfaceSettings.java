/*
 * Copyright (C) 2013 SlimRoms
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.slim;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class InterfaceSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String MOTO_DOZE_PACKAGE_NAME="com.cyanogenmod.settings.device";
    private PreferenceScreen gesture_settings;
    private PreferenceScreen notification_slider;
    
    @Override
    protected int getMetricsCategory() {
        // todo add a constant in MetricsLogger.java
        return MetricsLogger.MAIN_SETTINGS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.slim_interface_settings);
        
        PreferenceScreen prefSet = getPreferenceScreen();
        
        //check for Moto doze
        gesture_settings = (PreferenceScreen) findPreference("device_specific_gesture_settings");
        
        boolean showMotoDozeGestures = getResources().getBoolean(
                com.android.internal.R.bool.config_showMotoDozeGestures);
        
        if (!showMotoDozeGestures) {
            prefSet.removePreference(gesture_settings);
        }
        
        //check for Notification slider
        notification_slider = (PreferenceScreen) findPreference("notification_slider");
        
        boolean showNotificationSlider = getResources().getBoolean(
                com.android.internal.R.bool.config_showNotificationSlider);
                
        if(!showNotificationSlider) {
        	prefSet.removePreference(notification_slider);
		}
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
