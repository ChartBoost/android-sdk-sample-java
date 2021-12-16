package com.chartboost.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.chartboost.sdk.Chartboost;

public class ChartboostSample extends BaseSample {

    protected final static String impressionTypeKey = "IMPRESSION_TYPE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chartboost_sample);

        title = (TextView) findViewById(R.id.title);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            impressionType = (BaseSample.ImpressionType) extras.get(impressionTypeKey);
        }

        switch (impressionType) {
            case INTERSTITIAL:
                title.setText("Interstitial");
                break;
            case REWARDED:
                title.setText("Rewarded");
                break;
        }

        // Log
        logTextView = (TextView) findViewById(R.id.logText);
        logTextView.setText(logTextView.getText(), TextView.BufferType.EDITABLE);
        logTextView.setMovementMethod(new ScrollingMovementMethod());

        // Counters
        displayCounter = (TextView) findViewById(R.id.displayCounter);
        failClickCounter = (TextView) findViewById(R.id.failClickCounter);
        clickCounter = (TextView) findViewById(R.id.clickCounter);
        cacheCounter = (TextView) findViewById(R.id.cacheCounter);
        dismissCounter = (TextView) findViewById(R.id.dismissCounter);
        completeCounter = (TextView) findViewById(R.id.completeCounter);
        failLoadCounter = (TextView) findViewById(R.id.failLoadCounter);
        closeCounter = (TextView) findViewById(R.id.closeCounter);
        rewardCounter = (TextView) findViewById(R.id.rewardCounter);

        // Location
        hasLocation = (TextView) findViewById(R.id.hasText);

        // Buttons
        // Button
        Button backButton = (Button) findViewById(R.id.backButton);
        Button cacheButton = (Button) findViewById(R.id.cacheButton);
        Button showButton = (Button) findViewById(R.id.showButton);
        Button clearButton = (Button) findViewById(R.id.clearButton);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                location = parentView.getItemAtPosition(position).toString();
                hasLocation.setText(isAdReadyToDisplay(location) ? "Yes" : "No");
                addToUILog("Location changed to " + location);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addToUILog("Using Chartboost v" + Chartboost.getSDKVersion());

        backButton.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ChartboostSample.this, SelectionActivity.class));
        });

        settingsButton.setOnClickListener(v -> startActivity(new Intent(ChartboostSample.this, SettingsActivity.class)));

        cacheButton.setOnClickListener(v -> {
            switch (impressionType) {
                case INTERSTITIAL:
                    Chartboost.cacheInterstitial(location);
                    break;
                case REWARDED:
                    Chartboost.cacheRewardedVideo(location);
                    break;
            }
        });

        showButton.setOnClickListener(v -> {
            switch (impressionType) {
                case INTERSTITIAL:
                    Chartboost.showInterstitial(location);
                    break;

                case REWARDED:
                    Chartboost.showRewardedVideo(location);
                    break;
            }
        });

        clearButton.setOnClickListener(v -> clearUI());

    }
}
