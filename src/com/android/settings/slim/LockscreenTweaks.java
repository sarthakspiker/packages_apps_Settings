 /*
 * Copyright (C) 2015 OrionLP
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

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import com.android.settings.widget.SeekBarPreferenceCham;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class LockscreenTweaks extends SettingsPreferenceFragment 
   implements OnPreferenceChangeListener {

    private static final String LOCKSCREEN_BOTTOM_SHORTCUTS = "lockscreen_bottom_shortcuts";
    
	private static final String KEY_LOCKSCREEN_BLUR_RADIUS = "lockscreen_blur_radius";

    private SwitchPreference mLockscreenBottomShortcuts;
	private SeekBarPreferenceCham mBlurRadius;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.orion_lockscreen_tweaks);
        
    ContentResolver resolver = getActivity().getContentResolver();

    mBlurRadius = (SeekBarPreferenceCham) findPreference(KEY_LOCKSCREEN_BLUR_RADIUS);
        mBlurRadius.setValue(Settings.System.getInt(resolver,
                Settings.System.LOCKSCREEN_BLUR_RADIUS, 14));
        mBlurRadius.setOnPreferenceChangeListener(this);

        // Lockscreen bottom shortcuts
        mLockscreenBottomShortcuts = (SwitchPreference) findPreference(LOCKSCREEN_BOTTOM_SHORTCUTS);
        if (mLockscreenBottomShortcuts != null) {
            boolean lockScreenBottomShortcutsEnabled = Settings.Secure.getInt(getContentResolver(),
                    Settings.Secure.LOCKSCREEN_BOTTOM_SHORTCUTS, 1) == 1;
            mLockscreenBottomShortcuts.setChecked(lockScreenBottomShortcutsEnabled);
            mLockscreenBottomShortcuts.setSummary(lockScreenBottomShortcutsEnabled
                    ? R.string.lockscreen_bottom_shortcuts_enabled :
                      R.string.lockscreen_bottom_shortcuts_disabled);
            mLockscreenBottomShortcuts.setOnPreferenceChangeListener(this);
        }
        
  }

   @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
    		ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mLockscreenBottomShortcuts) {
            Settings.Secure.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.Secure.LOCKSCREEN_BOTTOM_SHORTCUTS,
                    (Boolean) value ? 1 : 0);
        }
        else if(preference ==mBlurRadius) {
                    int width = ((Integer)value).intValue();
            Settings.System.putInt(resolver,
                    Settings.System.LOCKSCREEN_BLUR_RADIUS, width);
            return true;
		}
        return false;
    }
   
   @Override
    public void onResume() {
    	super.onResume();
    	}
   
   @Override
   	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
    return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

}
