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
import com.chartboost.sdk.ChartboostBanner;
import com.chartboost.sdk.ChartboostBannerListener;
import com.chartboost.sdk.Events.ChartboostCacheError;
import com.chartboost.sdk.Events.ChartboostCacheEvent;
import com.chartboost.sdk.Events.ChartboostClickError;
import com.chartboost.sdk.Events.ChartboostClickEvent;
import com.chartboost.sdk.Events.ChartboostShowError;
import com.chartboost.sdk.Events.ChartboostShowEvent;

public class BannerSample extends BaseSample implements ChartboostBannerListener {

    private ChartboostBanner chartboostBanner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartboost_banner_sample);
        impressionType = ImpressionType.BANNER;
        chartboostBanner = findViewById(R.id.example_banner);
        chartboostBanner.setListener(this);

        title = (TextView) findViewById(R.id.title);
        title.setText("Banner");

        logTextView = (TextView) findViewById(R.id.logText);
        logTextView.setText(logTextView.getText(), TextView.BufferType.EDITABLE);
        logTextView.setMovementMethod(new ScrollingMovementMethod());

        displayCounter = (TextView) findViewById(R.id.displayCounter);
        failClickCounter = (TextView) findViewById(R.id.failClickCounter);
        clickCounter = (TextView) findViewById(R.id.clickCounter);
        cacheCounter = (TextView) findViewById(R.id.cacheCounter);
        dismissCounter = (TextView) findViewById(R.id.dismissCounter);
        completeCounter = (TextView) findViewById(R.id.completeCounter);
        failLoadCounter = (TextView) findViewById(R.id.failLoadCounter);
        closeCounter = (TextView) findViewById(R.id.closeCounter);
        rewardCounter = (TextView) findViewById(R.id.rewardCounter);
        hasLocation = (TextView) findViewById(R.id.hasText);

        Button cacheButton = (Button) findViewById(R.id.cacheButton);
        Button showButton = (Button) findViewById(R.id.showButton);
        Button clearButton = (Button) findViewById(R.id.clearButton);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);

        cacheButton.setOnClickListener(v -> cacheBanner());
        showButton.setOnClickListener(v -> showBanner());
        clearButton.setOnClickListener(v -> clearUI());
        settingsButton.setOnClickListener(v -> openSettings());

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                onLocationAdapterChange(parentView, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onLocationAdapterChange(AdapterView<?> parentView, int position) {
        Chartboost.setActivityAttrs(BannerSample.this);
        location = parentView.getItemAtPosition(position).toString();
        hasLocation.setText(isAdReadyToDisplay(location) ? "Yes" : "No");
        addToUILog("Location changed to " + location);
    }

    private void openSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void cacheBanner() {
        if(chartboostBanner != null) {
            chartboostBanner.cache();
        }
    }

    private void showBanner() {
        if(chartboostBanner != null) {
            chartboostBanner.show();
        }
    }

    @Override
    public void onAdCached(ChartboostCacheEvent chartboostCacheEvent, ChartboostCacheError chartboostCacheError) {
        if(chartboostCacheError != null) {
            addToUILog("Banner cached error: " + chartboostCacheError.code);
        } else {
            addToUILog("Banner cached");
        }
    }

    @Override
    public void onAdShown(ChartboostShowEvent chartboostShowEvent, ChartboostShowError chartboostShowError) {
        if(chartboostShowError != null) {
            addToUILog("Banner shown error: " + chartboostShowError.code);
        } else {
            addToUILog("Banner shown for location: " + chartboostShowEvent.location);
        }
    }

    @Override
    public void onAdClicked(ChartboostClickEvent chartboostClickEvent, ChartboostClickError chartboostClickError) {
        if(chartboostClickError != null) {
            addToUILog("Banner clicked error: " + chartboostClickError.code);
        } else {
            addToUILog("Banner clicked");
        }
    }
}
