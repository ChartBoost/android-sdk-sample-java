package com.chartboost.sdk.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.privacy.model.CCPA;
import com.chartboost.sdk.privacy.model.COPPA;
import com.chartboost.sdk.privacy.model.Custom;
import com.chartboost.sdk.privacy.model.GDPR;
import com.chartboost.sdk.privacy.model.GenericDataUseConsent;
import com.chartboost.sdk.sample.consent.AddConsentDialog;
import com.google.android.material.snackbar.Snackbar;

public class SelectionActivity extends AppCompatActivity  {

    private static final String DISCLOSURE_SHOWN_KEY = "disclosure_shown";

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
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> startActivity(new Intent(SelectionActivity.this, SettingsActivity.class)));

        displayAdIdConsentIdNeeded();
    }

    private void initSDK(String[] ids) {
        String appId = ids[0];
        String appSignature = ids[1];
        Chartboost.startWithAppId(getApplicationContext(), appId, appSignature, startError -> {
            if (startError == null) {
                Toast.makeText(SelectionActivity.this.getApplicationContext(), "SDK is initialized", Toast.LENGTH_SHORT).show();
                checkKnownConsentStatus();
            } else {
                Toast.makeText(SelectionActivity.this.getApplicationContext(), "SDK initialized with error: "+startError.getCode().name(), Toast.LENGTH_SHORT).show();
            }
        });
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

    }

    private void createOnClickListener(int buttonID, BaseSample.ImpressionType type) {
        findViewById(buttonID).setOnClickListener(new ImpressionClickListener(type));
    }

    private void checkKnownConsentStatus() {
        displayConsentInLogs(AddConsentDialog.CONSENT_TYPE.GDPR);
        displayConsentInLogs(AddConsentDialog.CONSENT_TYPE.CCPA);
        displayConsentInLogs(AddConsentDialog.CONSENT_TYPE.COPPA);
    }

    private void displayConsentInLogs(AddConsentDialog.CONSENT_TYPE consentType) {
        GenericDataUseConsent dataUseConsent = null;
        if (consentType == AddConsentDialog.CONSENT_TYPE.GDPR) {
            try {
                dataUseConsent = (GDPR) Chartboost.getDataUseConsent(SelectionActivity.this, GDPR.GDPR_STANDARD);
            } catch (Exception e) {
                Log.e("Chartboost", "Cannot parse consent to GDPR: "+ e);
            }
        }

        if (consentType == AddConsentDialog.CONSENT_TYPE.CCPA) {
            try {
                dataUseConsent = (CCPA) Chartboost.getDataUseConsent(SelectionActivity.this, CCPA.CCPA_STANDARD);
            } catch (Exception e) {
                Log.e("Chartboost", "Cannot parse consent to CCPA: "+ e);
            }
        }

        if (consentType == AddConsentDialog.CONSENT_TYPE.COPPA) {
            try {
                dataUseConsent = (COPPA) Chartboost.getDataUseConsent(SelectionActivity.this, COPPA.COPPA_STANDARD);
            } catch (Exception e) {
                Log.e("Chartboost", "Cannot parse consent to COPPA: "+ e);
            }
        }

        if(dataUseConsent != null) {
            Log.e("Chartboost", dataUseConsent.getPrivacyStandard()+" is "+ dataUseConsent.getConsent());
        } else {
            Log.e("Chartboost", consentType+" is not set");
        }
    }

    /*
    For further information plese read document
    https://support.google.com/googleplay/android-developer/answer/10144311?visit_id=637824342745618041-164143577&rd=1
     */
    public void displayAdIdConsentIdNeeded() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (!sharedPreferences.getBoolean(DISCLOSURE_SHOWN_KEY, false)) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.rootView), getString(R.string.disclosure_message), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences.edit().putBoolean(DISCLOSURE_SHOWN_KEY, true).apply();
                }
            });
            snackbar.show();
        }
    }

    private class ImpressionClickListener implements View.OnClickListener {

        private final BaseSample.ImpressionType type;

        ImpressionClickListener(BaseSample.ImpressionType type) {
             this.type = type;
        }

        @Override
        public void onClick(View v) {
            Intent intent = null;
            if (this.type == BaseSample.ImpressionType.BANNER) {
                intent = new Intent(getBaseContext(), BannerSample.class);
            } else if (this.type == BaseSample.ImpressionType.REWARDED){
                intent = new Intent(getBaseContext(), RewardedSample.class);
            } else if (this.type == BaseSample.ImpressionType.INTERSTITIAL) {
                intent = new Intent(getBaseContext(), InterstitialSample.class);
            }

            if (intent != null) {
                startActivity(intent);
            }
        }
    }
}
