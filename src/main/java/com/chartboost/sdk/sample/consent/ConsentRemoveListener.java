package com.chartboost.sdk.sample.consent;

import com.chartboost.sdk.privacy.model.DataUseConsent;

public interface ConsentRemoveListener {
    void removeConsent(DataUseConsent consent);
}
