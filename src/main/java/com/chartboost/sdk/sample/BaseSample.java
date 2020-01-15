package com.chartboost.sdk.sample;

import android.app.Activity;
import android.os.Bundle;

import android.text.Layout;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.InPlay.CBInPlay;
import com.chartboost.sdk.Libraries.CBLogging;
import com.chartboost.sdk.Model.CBError;

public class BaseSample extends Activity {

    private final String TAG = "BaseSample";

    protected enum ImpressionType {
        INTERSTITIAL,
        REWARDED,
        BANNER,
        IN_PLAY
    }

    public BaseSample.ImpressionType impressionType;

    public TextView title;
    public TextView logTextView;

    // Counters
    public TextView displayCounter;
    public TextView failClickCounter;
    public TextView clickCounter;
    public TextView cacheCounter;
    public TextView dismissCounter;
    public TextView completeCounter;
    public TextView failLoadCounter;
    public TextView closeCounter;
    public TextView rewardCounter;

    public TextView hasLocation;
    public Spinner locationSpinner;

    public String location = "default";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Chartboost.setDelegate(delegate);
        Chartboost.setLoggingLevel(CBLogging.Level.ALL);
    }

    @Override
    public void onBackPressed() {
        if (!Chartboost.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void incrementCounter(TextView counter) {
        addToCounter(counter, 1);
    }

    private void addToCounter(TextView counter, int amount) {
        setTextSafely(counter, String.valueOf(Integer.parseInt(counter.getText().toString()) + amount));
    }

    private void setTextSafely(final TextView textView, final String text) {
        if(textView != null && text != null) {
            this.runOnUiThread(() -> textView.setText(text));
        }
    }

    public void addToUILog(final String message) {
        if(logTextView != null) {
            String stringBuilder = logTextView.getText() + message + "\n";
            setTextSafely(logTextView, stringBuilder);
            scrollToBottom(logTextView, true);
            Log.i(TAG, message);
        }
    }

    public void scrollToBottom(final TextView textView, final boolean bottom) {
        runOnUiThread(() -> {
            if(textView == null) {
                return;
            }

            if (textView.getLayout() != null) {
                Layout layout = textView.getLayout();

                int scrollAmount = 0;
                if(bottom) {
                    scrollAmount = layout.getLineTop(textView.getLineCount()) - textView.getHeight();
                }
                // if there is no need to scroll, scrollAmount will be <=0
                if (scrollAmount > 0) {
                    textView.scrollTo(0, scrollAmount);
                } else {
                    textView.scrollTo(0, 0);
                }
            }
        });
    }

    private void setHasAdForLocation(String location) {
        setTextSafely(hasLocation, isAdReadyToDisplay(location) ? "Yes" : "No");
    }

    public boolean isAdReadyToDisplay(String location){
        switch (impressionType) {
            case INTERSTITIAL:
                return Chartboost.hasInterstitial(location);
            case REWARDED:
                return Chartboost.hasRewardedVideo(location);
            case IN_PLAY:
                return CBInPlay.hasInPlay(location);
            default:
                return false;
        }
    }

    public void clearUI() {
        if(displayCounter != null) {
            displayCounter.setText("0");
        }

        if(failClickCounter != null) {
            failClickCounter.setText("0");
        }

        if(clickCounter != null) {
            clickCounter.setText("0");
        }

        if(cacheCounter != null) {
            cacheCounter.setText("0");
        }

        if(dismissCounter != null) {
            dismissCounter.setText("0");
        }

        if(completeCounter != null) {
            completeCounter.setText("0");
        }

        if(failLoadCounter != null) {
            failLoadCounter.setText("0");
        }

        if(closeCounter != null) {
            closeCounter.setText("0");
        }

        if(rewardCounter != null) {
            rewardCounter.setText("0");
        }

        if(hasLocation != null) {
            hasLocation.setText("No");
        }

        if(logTextView != null) {
            logTextView.setText("");
        }
    }

    public ChartboostDelegate delegate = new ChartboostDelegate() {

        @Override
        public boolean shouldRequestInterstitial(String location) {
            addToUILog("Should request interstitial at " + location + "?");
            return true;
        }

        @Override
        public boolean shouldDisplayInterstitial(String location) {
            addToUILog("Should display interstitial at " + location + "?");
            return true;
        }

        @Override
        public void didCacheInterstitial(String location) {
            addToUILog("Interstitial cached at " + location);
            setHasAdForLocation(location);
            incrementCounter(cacheCounter);
        }

        @Override
        public void didFailToLoadInterstitial(String location, CBError.CBImpressionError error) {
            addToUILog("Interstitial failed to load at " + location + " with error: " + error.name());
            setHasAdForLocation(location);
            incrementCounter(failLoadCounter);
        }

        @Override
        public void willDisplayInterstitial(String location) {
            addToUILog("Will display interstitial at " + location);
        }

        @Override
        public void didDismissInterstitial(String location) {
            addToUILog("Interstitial dismissed at " + location);
            incrementCounter(dismissCounter);
        }

        @Override
        public void didCloseInterstitial(String location) {
            addToUILog("Interstitial closed at " + location);
            incrementCounter(closeCounter);
        }

        @Override
        public void didClickInterstitial(String location) {
            addToUILog("Interstitial clicked at " + location );
            incrementCounter(clickCounter);
        }

        @Override
        public void didDisplayInterstitial(String location) {
            addToUILog("Interstitial displayed at " + location);
            setHasAdForLocation(location);
            incrementCounter(displayCounter);
        }

        @Override
        public void didFailToRecordClick(String uri, CBError.CBClickError error) {
            addToUILog("Failed to record click " + (uri != null ? uri : "null") + ", error: " + error.name());
            incrementCounter(failClickCounter);
        }

        @Override
        public boolean shouldDisplayRewardedVideo(String location) {
            addToUILog("Should display rewarded video at " + location + "?");
            return true;
        }

        @Override
        public void didCacheRewardedVideo(String location) {
            addToUILog("Did cache rewarded video " + location);
            setHasAdForLocation(location);
            incrementCounter(cacheCounter);
        }

        @Override
        public void didFailToLoadRewardedVideo(String location,
                                               CBError.CBImpressionError error) {
            addToUILog("Rewarded Video failed to load at " + location + " with error: " + error.name());
            setHasAdForLocation(location);
            incrementCounter(failLoadCounter);
        }

        @Override
        public void didDismissRewardedVideo(String location) {
            addToUILog("Rewarded video dismissed at " + location);
            incrementCounter(dismissCounter);
        }

        @Override
        public void didCloseRewardedVideo(String location) {
            addToUILog("Rewarded video closed at " + location);
            incrementCounter(closeCounter);
        }

        @Override
        public void didClickRewardedVideo(String location) {
            addToUILog("Rewarded video clicked at " + location);
            incrementCounter(clickCounter);
        }

        @Override
        public void didCompleteRewardedVideo(String location, int reward) {
            addToUILog("Rewarded video completed at " + location + "for reward: " + reward);
            incrementCounter(completeCounter);
            addToCounter(rewardCounter, reward);
        }

        @Override
        public void didDisplayRewardedVideo(String location) {
            addToUILog("Rewarded video displayed at " + location);
            setHasAdForLocation(location);
            incrementCounter(displayCounter);
        }

        @Override
        public void willDisplayVideo(String location) {
            addToUILog("Will display video at " + location);
        }

        @Override
        public void didCacheInPlay(String location) {
            addToUILog("In Play loaded at " + location);
            setHasAdForLocation(location);
            incrementCounter(cacheCounter);
        }

        @Override
        public void didFailToLoadInPlay(String location, CBError.CBImpressionError error) {
            addToUILog("In play failed to load at " + location + ", with error: " + error);
            setHasAdForLocation(location);
            incrementCounter(failLoadCounter);
        }

        @Override
        public void didInitialize() {
            addToUILog("Chartboost SDK is initialized and ready!");
        }
    };
}
