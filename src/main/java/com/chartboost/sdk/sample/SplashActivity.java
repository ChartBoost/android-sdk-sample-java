package com.chartboost.sdk.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by Gonzalo Alsina on 4/28/16.
 */
public class SplashActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        if (Build.VERSION.SDK_INT >= 23) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},123);
        } else {
            SplashActivity.this.finish();
            startActivity(new Intent(SplashActivity.this, SelectionActivity.class));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        SplashActivity.this.finish();
        startActivity(new Intent(SplashActivity.this, SelectionActivity.class));
    }
}