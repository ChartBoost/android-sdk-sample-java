package com.chartboost.sdk.sample.privacy.presentation;

import com.chartboost.sdk.sample.privacy.domain.DisclosureRepository;

public class DisclosurePresenter {
    private DisclosureRepository disclosureRepository;
    private View view;

    public DisclosurePresenter(DisclosureRepository disclosureRepository) {
        this.disclosureRepository = disclosureRepository;
    }

    public void attach(View view) {
        this.view = view;
        showDialogIfNeverShown();
    }

    public void detach() {
        this.view = null;
    }

    private void showDialogIfNeverShown() {
        if (!disclosureRepository.getDisclosureDialogWasShown()) {
            if (view != null) {
                view.showDisclosureDialog();
            }
        }
    }

    public void disclosureDialogShown() {
        disclosureRepository.saveDisclosureDialogWasShown();
    }

    public interface View {
        void showDisclosureDialog();
    }
}
