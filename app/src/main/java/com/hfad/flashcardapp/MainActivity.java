package com.hfad.flashcardapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        Log.d("FIREBASE", "Firebase initialized: " + (FirebaseApp.getInstance() != null));
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
        new_button.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
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
        new_button.setTextColor(Color.WHITE); // or any color
        new_button.setBackgroundColor(Color.parseColor("#4285F4")); // custom hex color
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
        CollectionReference set_dataset = db.collection("flashset_dataset");
        LinearLayout card_segment = findViewById(R.id.flashcard_buttons);

        set_dataset.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Button new_button = this.create_flashcard_button(document.getString("title"));
                            card_segment.addView(new_button);
                        }
                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    public void goToStudy(View view){
        String buttonText = ((Button) view).getText().toString();
        Intent intent = new Intent(MainActivity.this, Study_Screen.class);
        intent.putExtra("flashcard_set", buttonText);
        startActivity(intent);
    }
}