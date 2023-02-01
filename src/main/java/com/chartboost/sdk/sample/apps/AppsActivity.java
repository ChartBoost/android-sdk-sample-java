package com.chartboost.sdk.sample.apps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chartboost.sdk.sample.R;
import com.chartboost.sdk.sample.SplashActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppsActivity extends AppCompatActivity implements AppsAdapterListener {

    private static final String PREFS_KEY_APPS_SETTINGS = "apps_setting";
    private AppsAdapter adapter;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_activity);
        init();
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AppsAdapter(createListOfApps(), this, this);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(view -> showNewAppConfigDialog());
    }

    private List<AppConfig> createListOfApps() {
        List<AppConfig> appList = new ArrayList<>();
        Set<String> apps = loadAppsSetFromPrefs();

        if(apps!=null && !apps.isEmpty()) {
            createConfigList(apps, appList);
        } else {
            createDefaultConfig(appList);
        }
        return appList;
    }

    private void createConfigList(Set<String> apps, List<AppConfig> appList) {
        for (String app : apps) {
            appList.add(gson.fromJson(app, AppConfig.class));
        }
    }

    private void createDefaultConfig(List<AppConfig> appList) {
        appList.add(new AppConfig("Default", getString(R.string.appId), getString(R.string.appSignature)));
        appList.add(new AppConfig("Kids content", getString(R.string.appId_kids_content), getString(R.string.appSignature_kids_content)));
    }

    private Set<String> loadAppsSetFromPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return prefs.getStringSet(PREFS_KEY_APPS_SETTINGS, Collections.emptySet());
    }

    private void showNewAppConfigDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_app_dialog);
        dialog.setTitle(R.string.add_app);
        dialog.findViewById(R.id.dialog_add_app_confirm).setOnClickListener(v -> onAddAppConfigConfirmed(dialog));
        dialog.findViewById(R.id.dialog_add_app_cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void onAddAppConfigConfirmed(Dialog dialog) {
        AppConfig app = createNewAppConfigDataFromDialog(dialog);
        addConfigToAdapterAndSave(app);
        dialog.dismiss();
    }

    private AppConfig createNewAppConfigDataFromDialog(Dialog dialog) {
        String appName = ((EditText) dialog.findViewById(R.id.name)).getText().toString();
        String appId = ((EditText) dialog.findViewById(R.id.appId)).getText().toString();
        String appSignature = ((EditText) dialog.findViewById(R.id.signature)).getText().toString();
        return new AppConfig(appName, appId, appSignature);
    }

    private void addConfigToAdapterAndSave(AppConfig config) {
        if (config.getAppId().isEmpty() || config.getAppSignature().isEmpty()) return;

        adapter.add(config);
        Set<String> set = getAppsListAsSet();
        set.add(gson.toJson(config));
        saveAppsConfigInSharedPrefs(set);
    }

    @Override
    public void onItemRemoved() {
        Set<String> set = getAppsListAsSet();
        saveAppsConfigInSharedPrefs(set);
    }

    private Set<String> getAppsListAsSet() {
        Set<String> set = new HashSet<>();
        List<AppConfig> apps = adapter.getAppsList();
        for (AppConfig config : apps) {
            set.add(gson.toJson(config));
        }
        return set;
    }

    private void saveAppsConfigInSharedPrefs(Set<String> dataSet) {
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putStringSet(PREFS_KEY_APPS_SETTINGS, dataSet).apply();
    }

    @Override
    public void onItemSelected(String appId, String appSignature) {
        confirmAppIdChange(appId, appSignature);
    }

    private void confirmAppIdChange(String appId, String appSignature) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.dialog_restart_title))
                .setMessage(getString(R.string.dialog_restart_msg))
                .setPositiveButton(getString(R.string.dialog_restart_confirm), (dialogInterface, i) -> restartAppWithDelay(appId, appSignature))
                .setNegativeButton(getString(R.string.dialog_restart_cancel), (dialogInterface, i) -> adapter.notifyDataSetChanged())
                .show();
    }

    private void restartAppWithDelay(String appId, String appSignature) {
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit()
                .putString(getString(R.string.key_app_id_selected), appId)
                .putString(getString(R.string.key_app_signature_selected), appSignature)
                .apply();
        new Handler().postDelayed(() -> restartApp(getApplicationContext()), 500);
    }

    private void restartApp(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Runtime.getRuntime().exit(0);
    }
}
