package com.chartboost.sdk.sample.apps;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chartboost.sdk.sample.R;

import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsViewHolder> {

    private List<AppConfig> appsList;
    private final int accentColor;
    private AppsViewHolder selectedView;
    private AppsAdapterListener callback;
    private SharedPreferences sharedPreferences;
    private String selectedAppIdKey;

    AppsAdapter(List<AppConfig> appsList, Context context, AppsAdapterListener callback) {
        this.appsList = appsList;
        this.accentColor = context.getResources().getColor(R.color.green80);
        this.callback = callback;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.selectedAppIdKey = context.getResources().getString(R.string.key_app_id_selected);
    }

    @Override
    public @NonNull AppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_row, parent, false);
        return new AppsViewHolder(v, accentColor, sharedPreferences, selectedAppIdKey);
    }

    @Override
    public void onBindViewHolder(@NonNull AppsViewHolder holder, int position) {
        final AppConfig item = appsList.get(position);
        if(item!=null) {
            updateView(holder, item);
            holder.itemView.setOnLongClickListener(v -> removeItem(position));
        }
    }

    private void updateView(@NonNull AppsViewHolder holder, @NonNull AppConfig item) {
        holder.bind(item);
        saveInitialSelectedItem(holder);

        holder.itemView.setOnClickListener(v -> {
            if(!holder.isSelected()) {
                updateSelectedItemBackground(holder);
                this.callback.onItemSelected(item.getAppId(), item.getAppSignature());
            }
        });
    }

    private void saveInitialSelectedItem(AppsViewHolder holder) {
        if(holder.isSelected()) {
            selectedView = holder;
        }
    }

    private void updateSelectedItemBackground(AppsViewHolder clickedView) {
        if(selectedView!=null) {
            selectedView.deselectView();
            selectedView = clickedView;
            selectedView.selectView();
        }
    }

    private boolean removeItem(int position) {
        appsList.remove(position);
        notifyItemRemoved(position);
        this.callback.onItemRemoved();
        notifyDataSetChanged();
        return false;
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    public void add(AppConfig item) {
        appsList.add(item);
        notifyDataSetChanged();
    }

    List<AppConfig> getAppsList() {
        return appsList;
    }
}