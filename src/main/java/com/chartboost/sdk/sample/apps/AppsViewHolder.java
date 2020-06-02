package com.chartboost.sdk.sample.apps;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chartboost.sdk.sample.R;

class AppsViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView id;
    private TextView signature;
    private int backgroundColor;
    private SharedPreferences sharedPreferences;
    private String selectedAppIdKey;
    private boolean isSelected = false;

    AppsViewHolder(View itemView, int backgroundColor, SharedPreferences sharedPreferences, String selectedAppIdKey) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        id = itemView.findViewById(R.id.id);
        signature = itemView.findViewById(R.id.signature);
        this.backgroundColor = backgroundColor;
        this.sharedPreferences = sharedPreferences;
        this.selectedAppIdKey = selectedAppIdKey;
    }

    void bind(AppConfig item) {
        name.setText(item.getName());
        id.setText(item.getAppId());
        signature.setText(item.getAppSignature());
        String selected = sharedPreferences.getString(selectedAppIdKey, "");
        Log.e("test", "selected: "+selected);
        if(selected.equals(item.getAppId())) {
            selectView();
        } else {
            deselectView();
        }
    }

    boolean isSelected() {
        return isSelected;
    }

    void selectView() {
        isSelected = true;
        itemView.setBackgroundColor(backgroundColor);
    }

    void deselectView() {
        isSelected = false;
        itemView.setBackgroundColor(Color.TRANSPARENT);
    }
}
