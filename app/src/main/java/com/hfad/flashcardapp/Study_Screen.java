package com.hfad.flashcardapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Study_Screen extends AppCompatActivity {
    int card_index = 0;
    int deck_size;
    String[] question_array;
    String[] answer_array;
    MediaPlayer swoosh_sound;
    boolean isFlipped = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_study_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            swoosh_sound = MediaPlayer.create(this, R.raw.swoosh);
            RelativeLayout flash_card = findViewById(R.id.flashcard);
            flash_card.setOnClickListener(new View.OnClickListener() {
                boolean isFlipped = false;

                @Override
                public void onClick(View v) {
                    TextView questionText = findViewById(R.id.question_text);
                    TextView typeText = findViewById(R.id.card_type);
                    RelativeLayout flash_card = findViewById(R.id.flashcard);
                    flash_card.setRotationY(flash_card.getRotationY() + 180);
                    swoosh_sound.start();
                    flash_card.animate().rotationYBy(180);

                    if (isFlipped) {
                        questionText.setText(question_array[card_index]);
                        typeText.setText("Question:");
                    } else {
                        questionText.setText(answer_array[card_index]);
                        typeText.setText("Answer:");
                    }

                    isFlipped = !isFlipped;
                    };

            });

            updateView();
            return insets;
        });
    }


    private void _update_question() {
        question_array = getResources().getStringArray(R.array.question_list);
        answer_array = getResources().getStringArray(R.array.answer_list);
        String question = question_array[card_index];
        TextView question_region = findViewById(R.id.question_text);
        question_region.setText(question);
        this.deck_size = question_array.length;
    }
    private void _update_progress_bar(){
        RelativeLayout full_bar = findViewById(R.id.full_bar);
        RelativeLayout loaded_bar = findViewById(R.id.loaded_bar);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loaded_bar.getLayoutParams();
        layoutParams.width = full_bar.getWidth() * (card_index + 1) / deck_size;
        loaded_bar.setLayoutParams(layoutParams);
    }
    public void next_card(View view){
        card_index = (card_index + 1) % deck_size;
        isFlipped = false;
        updateView();
    }

    public void prev_card(View view){
        card_index = (card_index - 1);
        if(card_index < 0){
            card_index = deck_size - 1;
        }
        isFlipped = false;
        updateView();
    }



    public void goToMenu(View view){
        Intent intent = new Intent(Study_Screen.this, MainActivity.class);
        startActivity(intent);
    }

    public void updateView(){
        TextView index_view = findViewById(R.id.card_indexer);
        _update_question();
        _update_progress_bar();
        String index_string = (card_index + 1) + " / " + deck_size;
        index_view.setText(index_string);
    }
}

