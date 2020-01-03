package com.chartboost.sdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import com.chartboost.sdk.Chartboost;

public class SelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        Chartboost.startWithAppId(getApplicationContext(), getResources().getString(R.string.appId), getResources().getString(R.string.appSignature));
        setupSdkWithCustomSettings();
        createOnClickListener(R.id.interstitialButton, BaseSample.ImpressionType.INTERSTITIAL);
        createOnClickListener(R.id.rewardedButton, BaseSample.ImpressionType.REWARDED);
        createOnClickListener(R.id.bannerButton, BaseSample.ImpressionType.BANNER);
        createOnClickListener(R.id.inPlayButton, BaseSample.ImpressionType.IN_PLAY);
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> startActivity(new Intent(SelectionActivity.this, SettingsActivity.class)));
    }

    private void setupSdkWithCustomSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Chartboost.setShouldPrefetchVideoContent(
                sharedPreferences.getBoolean(getString(R.string.key_enable_video_prefetch), true));
        Chartboost.setShouldRequestInterstitialsInFirstSession(
                sharedPreferences.getBoolean(getString(R.string.key_request_interstitial_in_first_session), true));
        Chartboost.setAutoCacheAds(
                sharedPreferences.getBoolean(getString(R.string.key_enable_autocache), true));

    }

    private void createOnClickListener(int buttonID, BaseSample.ImpressionType type) {
        findViewById(buttonID).setOnClickListener(new ImpressionClickListener(type));
    }

    private class ImpressionClickListener implements View.OnClickListener {

        private final BaseSample.ImpressionType type;

        ImpressionClickListener(BaseSample.ImpressionType type) {
             this.type = type;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            if(this.type == BaseSample.ImpressionType.BANNER) {
                intent = new Intent(getBaseContext(), BannerSample.class);
            } else {
                intent = new Intent(getBaseContext(), ChartboostSample.class);
                intent.putExtra(ChartboostSample.impressionTypeKey, this.type);
            }
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        // If an interstitial is on screen, close it.
        if (!Chartboost.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
