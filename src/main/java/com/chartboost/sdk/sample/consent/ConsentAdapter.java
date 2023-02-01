package com.chartboost.sdk.sample.consent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chartboost.sdk.privacy.model.CCPA;
import com.chartboost.sdk.privacy.model.DataUseConsent;
import com.chartboost.sdk.privacy.model.GDPR;
import com.chartboost.sdk.sample.R;

import java.util.List;

public class ConsentAdapter extends RecyclerView.Adapter<ConsentViewHolder> {
    private List<DataUseConsent> consentsData;
    private ConsentRemoveListener removeCallback;

    public ConsentAdapter(List<DataUseConsent> consentsData, ConsentRemoveListener removeCallback) {
        this.consentsData = consentsData;
        this.removeCallback = removeCallback;
    }

    @NonNull
    @Override
    public ConsentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.consent_row, parent, false);
        return new ConsentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsentViewHolder holder, int position) {
        DataUseConsent data = consentsData.get(position);
        holder.getStandardNameText().setText(data.getPrivacyStandard());
        String dataConsent = data.getConsent().toString();
        String consentTypeString = "";
        String privacyStandard = data.getPrivacyStandard();

        switch (privacyStandard) {
            case GDPR.GDPR_STANDARD:
                GDPR.GDPR_CONSENT gdprConsent = GDPR.GDPR_CONSENT.fromValue(dataConsent);
                if (gdprConsent != null) {
                    consentTypeString = ""+gdprConsent.name()+" value: "+data.getConsent();
                }
                break;
            case CCPA.CCPA_STANDARD:
                CCPA.CCPA_CONSENT ccpaConsent = CCPA.CCPA_CONSENT.fromValue(dataConsent);
                if (ccpaConsent != null) {
                    consentTypeString = ""+ccpaConsent.name()+" value: "+data.getConsent();
                }
                break;
            default:
                consentTypeString = data.getConsent().toString();
                break;
        }

        holder.getConsentTypeText().setText(consentTypeString);
        holder.getRemoveBtn().setOnClickListener(v -> removeCallback.removeConsent(data));
    }

    @Override
    public int getItemCount() {
        return consentsData.size();
    }

    public void updateData(List<DataUseConsent> consentsDataUpdate) {
        consentsData = consentsDataUpdate;
        notifyDataSetChanged();
    }
}
