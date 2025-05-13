package com.hfad.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            this.build_buttons();
            return insets;
        });
    }

    public void goToCreation(View HOMESCREEN){
        Intent intent = new Intent(MainActivity.this, Card_Creation_Screen.class);
        startActivity(intent);
    }

    private void set_flashcard_settings(Button new_button){
        new_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        new_button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 205, getResources().getDisplayMetrics())
        );
        params.setMargins(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()),
                0
        );
        new_button.setLayoutParams(params);
    }
    private Button create_flashcard_button(String subject) {
        Button new_button = new Button(this);
        new_button.setText(subject);
        set_flashcard_settings(new_button);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStudy(v);
            }
        });

        return new_button;
    }
    private void build_buttons(){
        String[] card_subjects = getResources().getStringArray(R.array.flashcard_subjects);
        LinearLayout card_segment = findViewById(R.id.flashcard_buttons);
        for(String subject : card_subjects) {
            Button new_button = this.create_flashcard_button(subject);
            card_segment.addView(new_button);
        }
    }

    public void goToStudy(View view){
        Intent intent = new Intent(MainActivity.this, Study_Screen.class);
        startActivity(intent);
    }

    public void goToSettings(View view){
        System.out.println("Settings button pressed");
        // Start the SettingsActivity
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}