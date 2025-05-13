package com.hfad.flashcardapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Switch soundSwitch;
    private Switch cardDirectionSwitch;
    private Switch nightmodeSwitch;
    private EditText fontSizeEditText;
    private EditText customDatabaseEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);

        // Initialize UI components
        soundSwitch = findViewById(R.id.soundSwitch);
        cardDirectionSwitch = findViewById(R.id.cardDirectionSwitch);
        nightmodeSwitch = findViewById(R.id.nightmodeSwitch);
        fontSizeEditText = findViewById(R.id.settings_edit);
        customDatabaseEditText = findViewById(R.id.customDatabaseEditText);

        // Initialize SharedPreferences and Editor
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Load saved settings
        loadSettings();

        // Add listener for night mode switch
        nightmodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Apply night mode immediately when switch is toggled
                applyNightMode(isChecked);

                // Save the change to SharedPreferences
                editor.putBoolean("night_mode", isChecked);
                editor.apply();
            }
        });
        cardDirectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("card_direction", isChecked);
                editor.apply();
            }
        });
    }

    public void goBack(View view) {
        finish(); // Close SettingsActivity and return to the previous activity
    }

    public void resetSettings(View view) {
        // Reset settings to default values
        soundSwitch.setChecked(true); // Default value for sound switch
        cardDirectionSwitch.setChecked(false); // Default value for card direction switch
        nightmodeSwitch.setChecked(false); // Default value for night mode switch
        fontSizeEditText.setText("16"); // Default font size
        customDatabaseEditText.setText(""); // Default database path

        // Apply night mode change immediately
        applyNightMode(false);

        // Clear stored preferences
        editor.clear(); // Clear all stored preferences
        editor.apply(); // Apply the changes
    }

    public void saveSettings(View view) {
        // Save current settings to SharedPreferences
        boolean soundEnabled = soundSwitch.isChecked();
        boolean cardDirectionEnabled = cardDirectionSwitch.isChecked();
        boolean nightModeEnabled = nightmodeSwitch.isChecked();
        String fontSize = fontSizeEditText.getText().toString();
        String customDatabase = customDatabaseEditText.getText().toString();

        // Store the values in SharedPreferences
        editor.putBoolean("sound_enabled", soundEnabled);
        editor.putBoolean("card_direction", cardDirectionEnabled);
        editor.putBoolean("night_mode", nightModeEnabled);
        editor.putString("font_size", fontSize);
        editor.putString("custom_database", customDatabase);
        editor.apply(); // Apply the changes

        // Apply night mode change immediately
        applyNightMode(nightModeEnabled);

        // Show confirmation feedback (optional)
        // Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
    }

    private void loadSettings() {
        // Load stored settings from SharedPreferences and apply them to views
        boolean soundEnabled = sharedPreferences.getBoolean("sound_enabled", true); // Default is true
        boolean cardDirectionEnabled = sharedPreferences.getBoolean("card_direction", false); // Default is false
        boolean nightModeEnabled = sharedPreferences.getBoolean("night_mode", false); // Default is false
        String fontSize = sharedPreferences.getString("font_size", "16"); // Default is 16
        String customDatabase = sharedPreferences.getString("custom_database", ""); // Default is empty

        // Apply the settings to the views
        soundSwitch.setChecked(soundEnabled);
        cardDirectionSwitch.setChecked(cardDirectionEnabled);
        nightmodeSwitch.setChecked(nightModeEnabled);
        fontSizeEditText.setText(fontSize);
        customDatabaseEditText.setText(customDatabase);

        // Apply the night mode based on the saved preference
        applyNightMode(nightModeEnabled);
    }

    private void applyNightMode(boolean isNightMode) {
        if (isNightMode) {
            // Set night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // Set light mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }



}