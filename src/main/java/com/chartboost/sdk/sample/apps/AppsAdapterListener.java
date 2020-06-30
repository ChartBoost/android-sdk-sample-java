package com.chartboost.sdk.sample.apps;

interface AppsAdapterListener {
    void onItemSelected(String appId, String appSignature);
    void onItemRemoved();
}
