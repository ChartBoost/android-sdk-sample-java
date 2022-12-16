package com.chartboost.sdk.sample.consent;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.privacy.model.DataUseConsent;
import com.chartboost.sdk.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConsentSettings extends AppCompatActivity implements ConsentDialogListener, ConsentRemoveListener {

  private ConsentNameRepository consentNameRepository;
  private ConsentAdapter consentAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.apps_activity);

      RecyclerView consentsList = findViewById(R.id.consents_list);
      Button addConsentBtn = findViewById(R.id.add_consent_btn);

      consentNameRepository = new ConsentNameRepository(this);
      consentAdapter = new ConsentAdapter(prepareConsentData(), this);
      consentsList.setLayoutManager(new LinearLayoutManager(this));
      consentsList.setAdapter(consentAdapter);
      addConsentBtn.setOnClickListener(v -> addConsent());
  }

  private void addConsent() {
      AddConsentDialog.newInstance().show(getSupportFragmentManager(), "consent_dialog");
  }

  @Override
  public void createConsent(DataUseConsent consent) {
      addDataUseConsent(getApplicationContext(), consent);
      consentNameRepository.save(consent);
      consentAdapter.updateData(prepareConsentData());
  }

  @Override
  public void removeConsent(DataUseConsent consent) {
      clearDataUseConsent(getApplicationContext(), consent.getPrivacyStandard());
      consentNameRepository.remove(consent);
      consentAdapter.updateData(prepareConsentData());
  }

  private List<DataUseConsent> prepareConsentData() {
    Set<String> names = consentNameRepository.load();
    ArrayList<DataUseConsent> data = new ArrayList<>();
    for(String privacyStandard : names) {
       DataUseConsent dataUseConsent = getDataUseConsent(getApplicationContext(), privacyStandard);
       if (dataUseConsent != null) {
         data.add(dataUseConsent);
       }
    }
    return data;
  }

  private void addDataUseConsent(Context context, DataUseConsent consent) {
      Chartboost.addDataUseConsent(context, consent);
  }

  private void clearDataUseConsent(Context context, String privacyStandard) {
      Chartboost.clearDataUseConsent(context, privacyStandard);
  }

  private DataUseConsent getDataUseConsent(Context context, String privacyStandard) {
      return Chartboost.getDataUseConsent(context, privacyStandard);
  }
}
