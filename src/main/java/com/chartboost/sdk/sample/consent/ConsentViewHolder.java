package com.chartboost.sdk.sample.consent;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chartboost.sdk.sample.R;

public class ConsentViewHolder extends RecyclerView.ViewHolder {

    private final TextView standardNameText;
    private final TextView consentTypeText;
    private final ImageButton removeBtn;

    public ConsentViewHolder(@NonNull View consentSettingView) {
        super(consentSettingView);
        standardNameText = consentSettingView.findViewById(R.id.standardName);
        consentTypeText = consentSettingView.findViewById(R.id.consentType);
        removeBtn = consentSettingView.findViewById(R.id.consent_remove_btn);
    }

    public TextView getStandardNameText() {
        return standardNameText;
    }

    public TextView getConsentTypeText() {
        return consentTypeText;
    }

    public ImageButton getRemoveBtn() {
        return removeBtn;
    }
}
