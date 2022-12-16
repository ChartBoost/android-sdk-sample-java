package com.chartboost.sdk.sample.consent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.chartboost.sdk.privacy.model.CCPA;
import com.chartboost.sdk.privacy.model.COPPA;
import com.chartboost.sdk.privacy.model.Custom;
import com.chartboost.sdk.privacy.model.DataUseConsent;
import com.chartboost.sdk.privacy.model.GDPR;
import com.chartboost.sdk.sample.R;
import java.util.Arrays;
import java.util.List;

public class AddConsentDialog extends DialogFragment {
    private EditText editStandard;
    private EditText editConsent;
    private AutoCompleteTextView autocompleteConsent;

    private CONSENT_TYPE consentType = CONSENT_TYPE.CUSTOM;

    private final List<String> gdprConsents = Arrays.asList(GDPR.GDPR_CONSENT.BEHAVIORAL.name(), GDPR.GDPR_CONSENT.NON_BEHAVIORAL.name());
    private final List<String> ccpaConsents = Arrays.asList(CCPA.CCPA_CONSENT.OPT_IN_SALE.name(), CCPA.CCPA_CONSENT.OPT_OUT_SALE.name());
    private final List<String> coppaConsents = Arrays.asList( "true", "false");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.consent_dialog, container, false);
        TextView title = v.findViewById(R.id.consent_dialog_title);
        Button createButton = v.findViewById(R.id.consent_dialog_add_btn);
        Button cancelButton = v.findViewById(R.id.consent_dialog_cancel_btn);
        editStandard = v.findViewById(R.id.consent_dialog_edit_standard);
        editConsent = v.findViewById(R.id.consent_dialog_edit_consent);
        autocompleteConsent = v.findViewById(R.id.consent_dialog_autocomplete_edit);
        RadioButton gdprRadio = v.findViewById(R.id.consent_dialog_gdpr_radio);
        RadioButton ccpaRadio = v.findViewById(R.id.consent_dialog_ccpa_radio);
        RadioButton coppaRadio = v.findViewById(R.id.consent_dialog_coppa_radio);
        RadioButton customRadio = v.findViewById(R.id.consent_dialog_custom_radio);

        gdprRadio.setOnClickListener(v1 -> {
            ccpaRadio.setChecked(false);
            coppaRadio.setChecked(false);
            customRadio.setChecked(false);
            consentType = CONSENT_TYPE.GDPR;
            switchView();
        });

        ccpaRadio.setOnClickListener(v1 -> {
            gdprRadio.setChecked(false);
            coppaRadio.setChecked(false);
            customRadio.setChecked(false);
            consentType = CONSENT_TYPE.CCPA;
            switchView();
        });

        coppaRadio.setOnClickListener(v1 -> {
            gdprRadio.setChecked(false);
            ccpaRadio.setChecked(false);
            customRadio.setChecked(false);
            consentType = CONSENT_TYPE.COPPA;
            switchView();
        });

        customRadio.setOnClickListener(v1 -> {
            gdprRadio.setChecked(false);
            ccpaRadio.setChecked(false);
            coppaRadio.setChecked(false);
            consentType = CONSENT_TYPE.CUSTOM;
            switchView();
        });

        createButton.setOnClickListener(v1 -> createConsent());
        cancelButton.setOnClickListener(v1 -> dismiss());
        return v;
    }

    private void createConsent() {
        String standard = editStandard.getText().toString();
        String consent = autocompleteConsent.getText().toString();
        DataUseConsent dataUseConsent = null;

        if (consentType == CONSENT_TYPE.GDPR) {
            if (GDPR.GDPR_CONSENT.BEHAVIORAL.name().equals(consent)) {
                dataUseConsent = new GDPR(GDPR.GDPR_CONSENT.BEHAVIORAL);
            } else if (GDPR.GDPR_CONSENT.NON_BEHAVIORAL.name().equals(consent)) {
                dataUseConsent = new GDPR(GDPR.GDPR_CONSENT.NON_BEHAVIORAL);
            }
        } else if (consentType == CONSENT_TYPE.CCPA) {
            if (CCPA.CCPA_CONSENT.OPT_IN_SALE.name().equals(consent)) {
                dataUseConsent = new CCPA(CCPA.CCPA_CONSENT.OPT_IN_SALE);
            } else if (CCPA.CCPA_CONSENT.OPT_OUT_SALE.name().equals(consent)) {
                dataUseConsent = new CCPA(CCPA.CCPA_CONSENT.OPT_OUT_SALE);
            }
        } else if (consentType == CONSENT_TYPE.COPPA) {
            if ("true".equals(consent)) {
                dataUseConsent = new COPPA(true);
            } else if ("false".equals(consent)) {
                dataUseConsent = new COPPA(false);
            }
        } else {
            consent = editConsent.getText().toString();
            dataUseConsent = new Custom(standard, consent);
        }

        if (dataUseConsent == null) {
            Toast.makeText(getContext(), "Consent creation failed. Wrong standard or consent name.", Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(dataUseConsent.getPrivacyStandard()) && !TextUtils.isEmpty(dataUseConsent.getConsent().toString())) {
                ConsentDialogListener listener = ((ConsentDialogListener) getActivity());
                if(listener != null) {
                    listener.createConsent(dataUseConsent);
                } else {
                    Toast.makeText(getContext(), "Consent creation failed.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Consent creation failed. Wrong standard or consent name.", Toast.LENGTH_SHORT).show();
            }
        }

        dismiss();
    }

    private void switchView() {
        if (consentType == CONSENT_TYPE.GDPR) {
            editStandard.setVisibility(View.GONE);
            editConsent.setVisibility(View.GONE);
            autocompleteConsent.setVisibility(View.VISIBLE);
            setAutoCompleteAdapter(gdprConsents);
        } else if (consentType == CONSENT_TYPE.CCPA) {
            editStandard.setVisibility(View.GONE);
            editConsent.setVisibility(View.GONE);
            autocompleteConsent.setVisibility(View.VISIBLE);
            setAutoCompleteAdapter(ccpaConsents);
        } else if (consentType == CONSENT_TYPE.COPPA) {
            editStandard.setVisibility(View.GONE);
            editConsent.setVisibility(View.GONE);
            autocompleteConsent.setVisibility(View.VISIBLE);
            setAutoCompleteAdapter(coppaConsents);
        } else {
            editStandard.setVisibility(View.VISIBLE);
            editConsent.setVisibility(View.VISIBLE);
            autocompleteConsent.setVisibility(View.GONE);
        }
    }

    private void setAutoCompleteAdapter(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
        autocompleteConsent.setAdapter(adapter);
        autocompleteConsent.showDropDown();
    }


    public static AddConsentDialog newInstance() {
        return new AddConsentDialog();
    }

    public enum CONSENT_TYPE {
        GDPR, CCPA, COPPA, CUSTOM
    }
}


