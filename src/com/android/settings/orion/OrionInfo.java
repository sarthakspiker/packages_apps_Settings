 /*
 * Copyright (C) 2016 OrionOs
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
package com.android.settings.orion;

import android.os.Bundle;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemProperties;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.SearchIndexableResource;
import com.android.internal.logging.MetricsLogger;

import android.provider.SearchIndexableResource;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Index;
import com.android.settings.search.Indexable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class OrionInfo extends SettingsPreferenceFragment implements Indexable{

    private static final String KEY_ORION_VERSION = "orion_version";
    private static final String KEY_ORION_OTA = "slimota";
    private static final String KEY_DEVICE_MAINTAINER = "device_maintainer";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.orion_info);
        
        setValueSummary(KEY_ORION_VERSION, "ro.orion.version");
        findPreference(KEY_ORION_VERSION).setEnabled(true);
        setMaintainerSummary(KEY_DEVICE_MAINTAINER, "ro.orion.maintainer");
  }
  
  	@Override
    protected int getMetricsCategory() {
        return MetricsLogger.APPLICATION;
    }

    private void setValueSummary(String preference, String property) {
        try {
            findPreference(preference).setSummary(
                    SystemProperties.get(property,
                            getResources().getString(R.string.device_info_default)));
        } catch (RuntimeException e) {
            // No recovery
        }
    }
    
    private void setMaintainerSummary(String preference, String property) {
        try {
            String maintainers = SystemProperties.get(property,
                    getResources().getString(R.string.device_info_default));
            findPreference(preference).setSummary(maintainers);
            if (maintainers.contains(",")) {
                findPreference(preference).setTitle(
                        getResources().getString(R.string.device_maintainers));
            }
        } catch (RuntimeException e) {
            // No recovery
        }
    }
    
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
    	  if (preference.getKey().equals(KEY_ORION_OTA)) {
                     boolean supported = false;
                     try {
                         supported = (getPackageManager().getPackageInfo("com.fusionjack.slimota", 0).versionCode > 0);
                     } catch (PackageManager.NameNotFoundException e) {
                     }
        }
        
          return super.onPreferenceTreeClick(preferenceScreen, preference);       
    }
    
     /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {

            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(
                    Context context, boolean enabled) {
                final SearchIndexableResource sir = new SearchIndexableResource(context);
                sir.xmlResId = R.xml.orion_info;
                return Arrays.asList(sir);
            }

            @Override
            public List<String> getNonIndexableKeys(Context context) {
                final List<String> keys = new ArrayList<String>();
               
                keys.add(KEY_ORION_OTA);
                return keys;
            }

            private boolean isPropertyMissing(String property) {
                return SystemProperties.get(property).equals("");
            }
        };

}
