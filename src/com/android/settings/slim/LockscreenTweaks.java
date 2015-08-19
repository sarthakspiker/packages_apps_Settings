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

 import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class LockscreenTweaks extends SettingsPreferenceFragment {

    private static final String LOCKSCREEN_BOTTOM_SHORTCUTS = "lockscreen_bottom_shortcuts";
    private static final String KEY_LOCKSCREEN_CATEGORY = "lockscreen_category";
    private static final String KEY_LOCKSCREEN_WALLPAPER = "lockscreen_wallpaper";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.orion_lockscreen_tweaks);
  }

	private PreferenceScreen createPreferenceHierarchy() {
     PreferenceScreen root = getPreferenceScreen();
        if (root != null) {
            root.removeAll();
        }
        addPreferencesFromResource(R.xml.orion_lockscreen_tweaks);
        root = getPreferenceScreen();
        // Add package manager to check if features are available
        PackageManager pm = getActivity().getPackageManager();

     // Lockscreen wallpaper
        PreferenceCategory lockscreenCategory = (PreferenceCategory)
            root.findPreference(KEY_LOCKSCREEN_CATEGORY);
		if (lockscreenCategory !=null) {
        	PreferenceScreen lockscreenWallpaper = (PreferenceScreen)
            lockscreenCategory.findPreference(KEY_LOCKSCREEN_WALLPAPER);
	  		if(lockscreenWallpaper != null) {
        		try {
            	getActivity().getPackageManager().getPackageInfo("com.slim.wallpaperpicker", 0);
        		}catch (PackageManager.NameNotFoundException e) {
            		lockscreenCategory.removePreference(lockscreenWallpaper);
        		}
	         	return root;
			}
		}
	}	
}

