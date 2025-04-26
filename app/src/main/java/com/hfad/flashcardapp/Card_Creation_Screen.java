package com.hfad.flashcardapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Card_Creation_Screen extends AppCompatActivity {

    int starting_number_of_cards = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card_creation_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            for(int i = 0; i < starting_number_of_cards; i++) {
                add_card(getWindow().getDecorView());
            }
            return insets;
        });
    }

    private RelativeLayout.LayoutParams _get_question_params(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        float density = getResources().getDisplayMetrics().density;
        int marginTop = (int) (15 * density); // 15sp converted to pixels
        params.setMargins(0, marginTop, 0, 0);
        return params;
    }
    private RelativeLayout.LayoutParams _get_bar_params() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) (2 * getResources().getDisplayMetrics().density));
        float density = getResources().getDisplayMetrics().density;
        int marginStart = (int) (1 * density);
        int marginEnd = (int) (40 * density);
        params.setMargins(marginStart, 0, marginEnd, 0);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        return params;
    }

    private RelativeLayout.LayoutParams _get_answer_params() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        float density = getResources().getDisplayMetrics().density;
        int marginBottom = (int) (70 * density);
        params.setMargins(0, 0, 0, marginBottom);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        return params;
    }
    private void _add_card_styling(RelativeLayout card) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) (290 * getResources().getDisplayMetrics().density));
        float density = getResources().getDisplayMetrics().density;
        int marginStart = (int) (30 * density);
        int marginEnd = (int) (30 * density);
        int marginTop = (int) (15 * density);
        params.setMargins(marginStart, marginTop, marginEnd, 0);
        card.setBackgroundResource(R.drawable.grey_border);
        card.setLayoutParams(params);
    }




    private void _add_card_attributes(RelativeLayout card){
        EditText question_section = new EditText(this);
        View white_bar = new View(this);
        EditText answer_section = new EditText(this);

        question_section.setHint("Enter Question:");
        white_bar.setBackgroundColor(Color.WHITE);
        answer_section.setHint("Enter Answer:");

        card.addView(question_section, _get_question_params());
        card.addView(white_bar, _get_bar_params());
        card.addView(answer_section, _get_answer_params());
    }


    private RelativeLayout _create_card(){
        RelativeLayout new_card = new RelativeLayout(this);
        _add_card_attributes(new_card);
        _add_card_styling(new_card);
        return new_card;
    }
    public void add_card(View CreationScreen) {
        LinearLayout card_container = findViewById(R.id.flashcard_container);
        RelativeLayout new_card = _create_card();
        card_container.addView(new_card);
    }

    public void goToMenu(View view){
        Intent intent = new Intent(Card_Creation_Screen.this, MainActivity.class);
        startActivity(intent);
    }

    public void goToStudy(View view){
        Intent intent = new Intent(Card_Creation_Screen.this, Study_Screen.class);
        startActivity(intent);
    }
}