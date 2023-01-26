package com.example.dissertationproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dissertationproject.objects.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateExerciseActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner category;
    EditText name;
    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercises);

        String[] categories = {"Chest", "Back", "Shoulders", "Biceps", "Triceps", "Forearms", "Quads",
                "Hamstrings", "Calves"};
        Spinner spinner = findViewById(R.id.spinExerciseCateg);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        category = findViewById(R.id.spinExerciseCateg);
        name = findViewById(R.id.etNameExercise);
        desc = findViewById(R.id.etDescriptionExercise);

    }

    public void createExercise(View v){
// Create a new user with a first and last name
        Map<String, Object> exercise = new HashMap<>();
        exercise.put("user", User.activeUser.getId());
        exercise.put("name", name.getText().toString());
        exercise.put("category", category.getSelectedItem().toString());
        exercise.put("description", desc.getText().toString());

        // Add a new document with a generated ID
        db.collection("exercises")
                .add(exercise)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}