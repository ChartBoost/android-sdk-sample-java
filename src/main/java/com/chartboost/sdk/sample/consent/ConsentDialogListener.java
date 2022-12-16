package com.chartboost.sdk.sample.consent;

import com.chartboost.sdk.privacy.model.DataUseConsent;

public interface ConsentDialogListener {
    void createConsent(DataUseConsent consent);
}
