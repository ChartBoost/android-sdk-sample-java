package com.chartboost.sdk.sample;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

import androidx.annotation.Nullable;

import com.chartboost.sdk.Chartboost;

/**
 * Created by Gonzalo on 5/13/16.
 */
public class SettingsActivity extends PreferenceActivity {

    private static final String TAG = "Settings Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            setUpSwitches();
        }

        private void setUpSwitches() {
            SwitchPreference setShouldRequestInterstitialsInFirstSessionSwitch = (SwitchPreference) findPreference(getString(R.string.key_request_interstitial_in_first_session));
            setShouldRequestInterstitialsInFirstSessionSwitch.setOnPreferenceChangeListener((preference, newValue) -> onSetShouldRequestInterstitialsInFirstSessionChanged(newValue));

            SwitchPreference setShouldPrefetchVideoContentSwitch = (SwitchPreference) findPreference(getString(R.string.key_enable_video_prefetch));
            setShouldPrefetchVideoContentSwitch.setOnPreferenceChangeListener((preference, newValue) -> onSetShouldPrefetchVideoContentChanged(newValue));

            SwitchPreference setAutoCacheAdsSwitch = (SwitchPreference) findPreference(getString(R.string.key_enable_autocache));
            setAutoCacheAdsSwitch.setOnPreferenceChangeListener((preference, newValue) -> onSetAutoCacheAdsChanged(newValue));
        }

        private boolean onSetAutoCacheAdsChanged(Object newValue) {
            Log.v(TAG, "Auto cache ads : " + newValue);
            Chartboost.setAutoCacheAds((Boolean) newValue);
            return true;
        }

        private boolean onSetShouldPrefetchVideoContentChanged(Object newValue) {
            Log.v(TAG, "Prefetch set to : " + newValue);
            Chartboost.setShouldPrefetchVideoContent((Boolean) newValue);
            return true;
        }

        private boolean onSetShouldRequestInterstitialsInFirstSessionChanged(Object newValue) {
            Log.v(TAG, "Request interstitials in first session : " + newValue);
            Chartboost.setShouldRequestInterstitialsInFirstSession((Boolean) newValue);
            return true;
        }
    }
}
