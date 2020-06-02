package com.chartboost.sdk.sample.apps;

public class AppConfig {

    private String mName;
    private String mAppId;
    private String mAppSignature;

    public AppConfig(String name, String appId, String appSignature) {
        mName = name;
        mAppId = appId;
        mAppSignature = appSignature;
    }

    public String getName() {
        return mName;
    }

    public String getAppId() {
        return mAppId;
    }

    public String getAppSignature() {
        return mAppSignature;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setAppId(String mAppId) {
        this.mAppId = mAppId;
    }

    public void setAppSignature(String mAppSignature) {
        this.mAppSignature = mAppSignature;
    }
}