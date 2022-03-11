package com.chartboost.sdk.sample.privacy.data;

import android.content.SharedPreferences;

import com.chartboost.sdk.sample.privacy.domain.DisclosureRepository;

public class DisclosureRepositoryPreferences implements DisclosureRepository {
    private static String DISCLOSURE_SHOWN_KEY = "disclosure_shown";
    private SharedPreferences sharedPreferences;

    public DisclosureRepositoryPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Boolean getDisclosureDialogWasShown() {
       return sharedPreferences.getBoolean(DISCLOSURE_SHOWN_KEY,false);
    }

    @Override
    public void saveDisclosureDialogWasShown() {
        sharedPreferences.edit().putBoolean(DISCLOSURE_SHOWN_KEY, true).apply();
    }
}
