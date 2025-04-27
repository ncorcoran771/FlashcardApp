package com.hfad.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Study_Screen extends AppCompatActivity {
    int card_index = 0;
    int deck_size = 25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_study_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            updateView();
            return insets;
        });
    }

    public void next_card(View view){
        card_index = (card_index + 1) % deck_size;
        updateView();
    }

    public void prev_card(View view){
        card_index = (card_index - 1);
        if(card_index < 0){
            card_index = deck_size - 1;
        }
        updateView();
    }



    public void goToMenu(View view){
        Intent intent = new Intent(Study_Screen.this, MainActivity.class);
        startActivity(intent);
    }

    public void updateView(){
        TextView index_view = findViewById(R.id.card_indexer);
        String index_string = (card_index + 1) + " / " + deck_size;
        index_view.setText(index_string);
    }
}