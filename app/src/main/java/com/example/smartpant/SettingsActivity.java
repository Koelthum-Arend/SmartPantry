package com.example.smartpant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SmartPantPrefs";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    private static final String KEY_UNITS = "units"; // "metric" or "imperial"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        SwitchMaterial switchExpiry = findViewById(R.id.switchExpiryAlerts);
        RadioGroup rgUnits = findViewById(R.id.rgUnits);
        RadioButton rbMetric = findViewById(R.id.rbMetric);
        RadioButton rbImperial = findViewById(R.id.rbImperial);

        toolbar.setNavigationOnClickListener(v -> finish());

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load saved settings
        switchExpiry.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, false));

        String units = prefs.getString(KEY_UNITS, "metric");
        if ("imperial".equals(units)) {
            rbImperial.setChecked(true);
        } else {
            rbMetric.setChecked(true);
        }

        // Save when changed
        switchExpiry.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply();
        });

        rgUnits.setOnCheckedChangeListener((group, checkedId) -> {
            String selected = (checkedId == R.id.rbImperial) ? "imperial" : "metric";
            prefs.edit().putString(KEY_UNITS, selected).apply();
        });
    }
}