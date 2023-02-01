package com.chartboost.sdk.sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ads.Banner;
import com.chartboost.sdk.callbacks.BannerCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;

public class BannerSample extends BaseSample implements BannerCallback {

    private Banner chartboostBanner = null;
    private RelativeLayout bannerHolder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartboost_banner_sample);
        bannerHolder = findViewById(R.id.example_banner_holder);

        impressionType = ImpressionType.BANNER;
        chartboostBanner = new Banner(this, "start", Banner.BannerSize.STANDARD, this, null);

        title = (TextView) findViewById(R.id.title);
        title.setText("Banner");

        logTextView = (TextView) findViewById(R.id.logText);
        logTextView.setText(logTextView.getText(), TextView.BufferType.EDITABLE);
        logTextView.setMovementMethod(new ScrollingMovementMethod());

        displayCounter = findViewById(R.id.displayCounter);
        failClickCounter = findViewById(R.id.failClickCounter);
        clickCounter = findViewById(R.id.clickCounter);
        cacheCounter = findViewById(R.id.cacheCounter);
        dismissCounter = findViewById(R.id.dismissCounter);
        impressionCounter = findViewById(R.id.impressionCounter);
        failLoadCounter = findViewById(R.id.failLoadCounter);
        failDisplayCounter = findViewById(R.id.failDisplayCounter);
        rewardCounter = findViewById(R.id.rewardCounter);
        hasLocation = findViewById(R.id.hasText);

        Button cacheButton = findViewById(R.id.cacheButton);
        Button showButton = findViewById(R.id.showButton);
        Button clearButton = findViewById(R.id.clearButton);
        ImageButton settingsButton = findViewById(R.id.settingsButton);

        cacheButton.setOnClickListener(v -> cacheBanner());
        showButton.setOnClickListener(v -> showBanner());
        clearButton.setOnClickListener(v -> clearUI());
        settingsButton.setOnClickListener(v -> openSettings());

        locationSpinner = findViewById(R.id.locationSpinner);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                onLocationAdapterChange(parentView, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bannerHolder.addView(chartboostBanner);
        addToUILog("Chartboost version: "+ Chartboost.getSDKVersion());
        addToUILog("Bidder token: "+ Chartboost.getBidderToken());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(chartboostBanner != null) {
            chartboostBanner.detach();
        }
    }

    private void onLocationAdapterChange(AdapterView<?> parentView, int position) {
        location = parentView.getItemAtPosition(position).toString();
       // hasLocation.setText(isAdReadyToDisplay(location) ? "Yes" : "No");
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
    public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {
        if(clickError != null) {
            addToUILog("Banner clicked error: " + clickError.getCode().name());
            incrementCounter(failClickCounter);
        } else {
            addToUILog("Banner clicked");
            incrementCounter(clickCounter);
        }
    }

    @Override
    public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
        if(cacheError != null) {
            addToUILog("Banner cached error: " + cacheError.getCode().name());
            incrementCounter(failLoadCounter);
        } else {
            hasLocation.setText("Yes");
            addToUILog("Banner cached");
            incrementCounter(cacheCounter);
        }
    }

    @Override
    public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {
        addToUILog("Banner onAdRequestedToShow: " + showEvent.getAd().getLocation());
    }

    @Override
    public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {
        hasLocation.setText("No");
        if(showError != null) {
            addToUILog("Banner shown error: " + showError.getCode().name());
            incrementCounter(failDisplayCounter);
        } else {
            addToUILog("Banner shown for location: " + showEvent.getAd().getLocation());
            incrementCounter(displayCounter);
        }
    }

    @Override
    public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {
        incrementCounter(impressionCounter);
        addToUILog("Banner impressionEvent: " + impressionEvent.getAd().getLocation());
    }
}
