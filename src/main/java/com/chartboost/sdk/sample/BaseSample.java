package com.chartboost.sdk.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.LoggingLevel;

public class BaseSample extends Activity {

    protected enum ImpressionType {
        INTERSTITIAL,
        REWARDED,
        BANNER
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
    public TextView impressionCounter;
    public TextView failLoadCounter;
    public TextView failDisplayCounter;
    public TextView rewardCounter;

    public TextView hasLocation;
    public Spinner locationSpinner;

    public String location = "default";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Chartboost.setLoggingLevel(LoggingLevel.ALL);
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
            Log.i("BaseSample", message);
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
                textView.scrollTo(0, Math.max(scrollAmount, 0));
            }
        });
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

        if(impressionCounter != null) {
            impressionCounter.setText("0");
        }

        if(failLoadCounter != null) {
            failLoadCounter.setText("0");
        }

        if(failDisplayCounter != null) {
            failDisplayCounter.setText("0");
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
}
