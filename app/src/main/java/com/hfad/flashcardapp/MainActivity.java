package com.hfad.flashcardapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        setSpinner();
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
        new_button.setBackgroundColor(Color.parseColor("#4CAF50")); // custom hex color
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

    private String getSortingMechanism(){
        Spinner dropdown = findViewById(R.id.sorting_dropdown);
        String dropdownSelection = dropdown.getSelectedItem().toString();

        if(dropdownSelection.equals("Recently Created")){
            return "creation_time";
        }
        else if(dropdownSelection.equals("Recently Accessed")){
            return "access_time";
        }
        else{
            return "title";
        }
    }

    private Query.Direction getIfInverse(){
        Spinner dropdown = findViewById(R.id.sorting_dropdown);
        String dropdownSelection = dropdown.getSelectedItem().toString();

        if(dropdownSelection.equals("Alphabetical A-Z")) {
            return Query.Direction.ASCENDING;
        }
        else{
            return Query.Direction.DESCENDING;
        }
    }

    private void setSpinner() {
        Spinner dropdown = findViewById(R.id.sorting_dropdown);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                build_buttons();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }



    private void build_buttons(){
        CollectionReference set_dataset = db.collection("flashset_dataset");
        LinearLayout card_segment = findViewById(R.id.flashcard_buttons);


        set_dataset.orderBy(getSortingMechanism(), getIfInverse()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        card_segment.removeAllViews();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Button new_button = this.create_flashcard_button(document.getString("title"));
                            card_segment.addView(new_button);
                        }
                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void updateAccessed(String flashcard_set) {
        CollectionReference ref = db.collection("flashset_dataset");
        long time = System.currentTimeMillis();

        ref.whereEqualTo("title", flashcard_set)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Found the document with matching title
                            DocumentReference docRef = ref.document(document.getId());
                            Map<String, Object> update = new HashMap<>();
                            update.put("access_time", time);
                            docRef.update(update)
                                    .addOnFailureListener(e -> Log.e("Firestore", "Failed to update access_time", e));
                        }
                    } else {
                        Log.e("Firestore", "Error finding document: ", task.getException());
                    }
                });
    }

    public void goToStudy(View view){
        String buttonText = ((Button) view).getText().toString();
        Intent intent = new Intent(MainActivity.this, Study_Screen.class);
        updateAccessed(buttonText);
        intent.putExtra("flashcard_set", buttonText);
        startActivity(intent);
    }

    public void goToSettings(View view){
        System.out.println("Settings button pressed");
        // Start the SettingsActivity
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}