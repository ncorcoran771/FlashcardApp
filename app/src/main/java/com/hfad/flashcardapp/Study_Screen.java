package com.hfad.flashcardapp;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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

    // Debounce variables
    private long lastClickTime = 0;
    private static final long DEBOUNCE_TIME = 500; // 500ms debounce time

    // Front and back card views
    private RelativeLayout cardFront;
    private RelativeLayout cardBack;

    // Camera distance for 3D rotation effect
    private float scale;
    private static final float CAMERA_DISTANCE = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_study_screen);

        // Initialize sound effect
        swoosh_sound = MediaPlayer.create(this, R.raw.swoosh);

        // Setup for card flipping
        cardFront = findViewById(R.id.flashcard);
        // Assuming you'll add a back card view to your layout
        // If not, you'll need to modify your layout to include a back card view
        cardBack = findViewById(R.id.flashcard_back);
        if (cardBack == null) {
            // If no separate back card view, we'll use same view with different content
            cardBack = cardFront;
        }

        // Set the camera distance for 3D effect
        scale = getResources().getDisplayMetrics().density;
        cardFront.setCameraDistance(CAMERA_DISTANCE * scale);
        cardBack.setCameraDistance(CAMERA_DISTANCE * scale);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Setup click listener for card flip
            cardFront.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Implement debouncing for card flip
                    if (SystemClock.elapsedRealtime() - lastClickTime < DEBOUNCE_TIME) {
                        return; // Ignore clicks that happen too soon after each other
                    }
                    lastClickTime = SystemClock.elapsedRealtime();

                    // Play sound only if it's not already playing
                    if (!swoosh_sound.isPlaying()) {
                        swoosh_sound.start();
                    }

                    flipCard();
                }
            });

            updateView();
            return insets;
        });
    }

    private void flipCard() {
        TextView questionText = findViewById(R.id.question_text);
        TextView typeText = findViewById(R.id.card_type);

        // Create flip animation
        final AnimatorSet flipOutAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(
                this, R.animator.card_flip_out);
        final AnimatorSet flipInAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(
                this, R.animator.card_flip_in);

        // Check if we need to show question or answer
        if (isFlipped) {
            // Going back to question
            flipOutAnimator.setTarget(cardFront);
            flipInAnimator.setTarget(cardFront);

            flipOutAnimator.addListener(new android.animation.Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(android.animation.Animator animation) {}

                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    questionText.setText(question_array[card_index]);
                    typeText.setText("Question:");
                    flipInAnimator.start();
                }

                @Override
                public void onAnimationCancel(android.animation.Animator animation) {}

                @Override
                public void onAnimationRepeat(android.animation.Animator animation) {}
            });

        } else {
            // Going to answer
            flipOutAnimator.setTarget(cardFront);
            flipInAnimator.setTarget(cardFront);

            flipOutAnimator.addListener(new android.animation.Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(android.animation.Animator animation) {}

                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    questionText.setText(answer_array[card_index]);
                    typeText.setText("Answer:");
                    flipInAnimator.start();
                }

                @Override
                public void onAnimationCancel(android.animation.Animator animation) {}

                @Override
                public void onAnimationRepeat(android.animation.Animator animation) {}
            });
        }

        flipOutAnimator.start();
        isFlipped = !isFlipped;
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
        // Add debouncing for next card button
        if (SystemClock.elapsedRealtime() - lastClickTime < DEBOUNCE_TIME) {
            return; // Ignore clicks that happen too soon after each other
        }
        lastClickTime = SystemClock.elapsedRealtime();

        card_index = (card_index + 1) % deck_size;
        isFlipped = false;
        updateView();
    }

    public void prev_card(View view){
        // Add debouncing for previous card button
        if (SystemClock.elapsedRealtime() - lastClickTime < DEBOUNCE_TIME) {
            return; // Ignore clicks that happen too soon after each other
        }
        lastClickTime = SystemClock.elapsedRealtime();

        card_index = (card_index - 1);
        if(card_index < 0){
            card_index = deck_size - 1;
        }
        isFlipped = false;
        updateView();
    }

    public void goToMenu(View view){
        // Add debouncing for menu button too
        if (SystemClock.elapsedRealtime() - lastClickTime < DEBOUNCE_TIME) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        if (swoosh_sound != null) {
            swoosh_sound.release();
            swoosh_sound = null;
        }
    }
}