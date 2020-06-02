package com.chartboost.sdk.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * Created by Gonzalo Alsina on 4/28/16.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{
                    Manifest.permission.READ_PHONE_STATE},
                    123);
        } else {
            startSelectionActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startSelectionActivity();
    }

    private void startSelectionActivity() {
        finish();
        startActivity(new Intent(SplashActivity.this, SelectionActivity.class));
    }
}