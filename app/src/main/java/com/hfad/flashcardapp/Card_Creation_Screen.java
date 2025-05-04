package com.hfad.flashcardapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Card_Creation_Screen extends AppCompatActivity {

    int starting_number_of_cards = 3;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_card_creation_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            db = FirebaseFirestore.getInstance();
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




    private void _add_card_attributes(RelativeLayout card, int id){
        EditText question_section = new EditText(this);
        View white_bar = new View(this);
        EditText answer_section = new EditText(this);

        question_section.setHint("Enter Question:");
        question_section.setTag("question_" + id);

        white_bar.setBackgroundColor(Color.WHITE);

        answer_section.setHint("Enter Answer:");
        answer_section.setTag("answer_" + id);

        card.addView(question_section, _get_question_params());
        card.addView(white_bar, _get_bar_params());
        card.addView(answer_section, _get_answer_params());
    }

    private String _get_flash_title(){
        EditText deckNameComp = findViewById(R.id.deck_name);
        return deckNameComp.getText().toString();
    }

    private Map<String, Object> _get_card_info(RelativeLayout card, int index){
        Map<String, Object> card_data = new HashMap<>();


        EditText question_section = card.findViewWithTag("question_" + index);
        EditText answer_section = card.findViewWithTag("answer_" + index);

        card_data.put("question", question_section.getText().toString());
        card_data.put("answer", answer_section.getText().toString());

        return card_data;
    }

    private Map<String, Object> _get_dataset_info(){
        Map<String, Object> data_info = new HashMap<>();

        EditText title_text = (EditText) findViewById(R.id.deck_name);
        data_info.put("title", title_text.getText().toString());

        long time = System.currentTimeMillis();

        data_info.put("creation_time", time);
        data_info.put("access_time", time);

        return data_info;
    }

    private void _save_cards(){
        String dataset_title = _get_flash_title();
        LinearLayout card_container = findViewById(R.id.flashcard_container);
        CollectionReference new_dataset = db.collection(dataset_title);
        CollectionReference set_dataset = db.collection("flashset_dataset");
        for (int i = 0; i < card_container.getChildCount(); i++){
            RelativeLayout new_card = (RelativeLayout) card_container.getChildAt(i);
            new_dataset.add(_get_card_info(new_card, i));
        }
        set_dataset.add(_get_dataset_info());
    }
    private RelativeLayout _create_card(){
        LinearLayout card_container = findViewById(R.id.flashcard_container);
        RelativeLayout new_card = new RelativeLayout(this);
        _add_card_attributes(new_card, card_container.getChildCount());
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
        _save_cards();
        intent.putExtra("flashcard_set", _get_flash_title());
        startActivity(intent);
    }
}