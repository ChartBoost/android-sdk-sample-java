package com.chartboost.sdk.sample.privacy.domain;

public interface DisclosureRepository {
    Boolean getDisclosureDialogWasShown();

    void saveDisclosureDialogWasShown();
}
