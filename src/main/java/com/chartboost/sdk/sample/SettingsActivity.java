package com.chartboost.sdk.sample;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

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
        addPreferencesFromResource(R.xml.preferences);
        setUpSwitches();
    }

    private void setUpSwitches() {
        SwitchPreference lockOrientationSwitch = (SwitchPreference) findPreference(getString(R.string.key_lock_orientation));
        lockOrientationSwitch.setOnPreferenceChangeListener((preference, newValue) -> onlockOrientationChanged((Boolean) newValue));
        lockOrientationSwitch.setEnabled(false);

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

    private boolean onlockOrientationChanged(Boolean newValue) {
        if (ChartboostSample.activity == null) {
            Toast.makeText(SettingsActivity.this, "Unable to lock orientation, Debug Activity not initiated", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newValue) {
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                ChartboostSample.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                ChartboostSample.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
        } else {
            ChartboostSample.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        return true;
    }
}
