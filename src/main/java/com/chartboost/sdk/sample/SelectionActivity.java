package com.chartboost.sdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Privacy.model.CCPA;
import com.chartboost.sdk.Privacy.model.GDPR;

public class SelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // Needs to be set before SDK init
        Chartboost.addDataUseConsent(this, new GDPR(GDPR.GDPR_CONSENT.BEHAVIORAL));
        Chartboost.addDataUseConsent(this, new CCPA(CCPA.CCPA_CONSENT.OPT_IN_SALE));

        //start SDK with app id and app signature
        String[] ids = loadSelectedAppId(sharedPreferences);
        initSDK(ids);

        //set sdk settings
        setupSdkWithCustomSettings(sharedPreferences);
        createOnClickListener(R.id.interstitialButton, BaseSample.ImpressionType.INTERSTITIAL);
        createOnClickListener(R.id.rewardedButton, BaseSample.ImpressionType.REWARDED);
        createOnClickListener(R.id.bannerButton, BaseSample.ImpressionType.BANNER);
        createOnClickListener(R.id.inPlayButton, BaseSample.ImpressionType.IN_PLAY);
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> startActivity(new Intent(SelectionActivity.this, SettingsActivity.class)));
    }

    private void initSDK(String[] ids) {
        String appId = ids[0];
        String appSignature = ids[1];
        Chartboost.setDelegate(delegateInit);
        Chartboost.startWithAppId(getApplicationContext(), appId, appSignature);
    }

    private String[] loadSelectedAppId(SharedPreferences sharedPreferences) {
        String[] ids = new String[2];
        String appId = sharedPreferences.getString(getString(R.string.key_app_id_selected), "");
        String appSignature = sharedPreferences.getString(getString(R.string.key_app_signature_selected), "");

        //in case there were no selected values, use default and save them as selected
        if(appId.isEmpty() || appSignature.isEmpty()) {
            appId = getResources().getString(R.string.appId);
            appSignature = getResources().getString(R.string.appSignature);
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit()
                    .putString(getString(R.string.key_app_id_selected), appId)
                    .putString(getString(R.string.key_app_signature_selected), appSignature)
                    .apply();
        }
        ids[0] = appId;
        ids[1] = appSignature;
        return ids;
    }

    private void setupSdkWithCustomSettings(SharedPreferences sharedPreferences) {
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

    private ChartboostDelegate delegateInit = new ChartboostDelegate() {
        @Override
        public void didInitialize() {
            super.didInitialize();
            Toast.makeText(SelectionActivity.this.getApplicationContext(), "SDK is initialized", Toast.LENGTH_SHORT).show();
            displayGDPRConsentInLogs();
        }
    };

    private void displayGDPRConsentInLogs() {
        GDPR gdpr = null;
        try {
            gdpr = (GDPR) Chartboost.getDataUseConsent(SelectionActivity.this, GDPR.GDPR_STANDARD);
        } catch (Exception e) {
            Log.e("Chartboost", "Cannot parse consent to GDPR: "+e.toString());
        }

        if(gdpr != null) {
            String consentValue = gdpr.getConsent();
            if(GDPR.GDPR_CONSENT.BEHAVIORAL.getValue().equals(consentValue)) {
                Log.e("Chartboost", "GDPR is BEHAVIORAL");
            } else if(GDPR.GDPR_CONSENT.NON_BEHAVIORAL.getValue().equals(consentValue)){
                Log.e("Chartboost", "GDPR is NON_BEHAVIORAL");
            } else {
                Log.e("Chartboost", "GDPR is INVALID CONSENT");
            }
        } else {
            Log.e("Chartboost", "GDPR is not set");
        }
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
