/*
 * Copyright (C) 2013 SlimRoms Project
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StatusBar extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String TAG = "StatusBar";

    private static final String PREF_CUSTOM_HEADER_DEFAULT = "status_bar_custom_header_default";
    private static final String PREF_ENABLE_TASK_MANAGER = "enable_task_manager";
 
    private ListPreference mCustomHeaderDefault;
    private SwitchPreference mEnableTaskManager;

    private boolean mCheckPreferences;
    
    
    @Override
    protected int getMetricsCategory() {
        // todo add a constant in MetricsLogger.java
        return MetricsLogger.MAIN_SETTINGS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createCustomView();
        
    }

 private PreferenceScreen createCustomView() {
       
        mCheckPreferences = false;
        addPreferencesFromResource(R.xml.status_bar_settings);        
        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();
 
        PackageManager pm = getPackageManager();
        Resources systemUiResources;
        try {
             systemUiResources = pm.getResourcesForApplication("com.android.systemui");
        } catch (Exception e) {
            Log.e(TAG, "can't access systemui resources",e);
            return null;
        }
        
        // Status bar custom header default
        mCustomHeaderDefault = (ListPreference) findPreference(PREF_CUSTOM_HEADER_DEFAULT);
        mCustomHeaderDefault.setOnPreferenceChangeListener(this);
         
        int customHeaderDefault = Settings.System.getInt(getActivity()
                 .getContentResolver(), Settings.System.STATUS_BAR_CUSTOM_HEADER_DEFAULT, 0);
         mCustomHeaderDefault.setValue(String.valueOf(customHeaderDefault));
         mCustomHeaderDefault.setSummary(mCustomHeaderDefault.getEntry());

        // Task manager
        mEnableTaskManager = (SwitchPreference) prefSet.findPreference(PREF_ENABLE_TASK_MANAGER);
        mEnableTaskManager.setChecked((Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.ENABLE_TASK_MANAGER, 0) == 1));

         setHasOptionsMenu(true);
         mCheckPreferences = true;
         return prefSet;
     }
     
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    
     if (!mCheckPreferences) {
             return false;
         }
         AlertDialog dialog;
 
         ContentResolver resolver = getActivity().getContentResolver();
         
         if (preference == mCustomHeaderDefault) {

         int customHeaderDefault = Integer.valueOf((String) newValue);
         int index = mCustomHeaderDefault.findIndexOfValue((String) newValue);
         Settings.System.putInt(getActivity().getContentResolver(), 
         Settings.System.STATUS_BAR_CUSTOM_HEADER_DEFAULT, customHeaderDefault);
         mCustomHeaderDefault.setSummary(mCustomHeaderDefault.getEntries()[index]);
         createCustomView();
             return true;
          }
          
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
       if  (preference == mEnableTaskManager) {
            boolean enabled = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.ENABLE_TASK_MANAGER, enabled ? 1:0);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
