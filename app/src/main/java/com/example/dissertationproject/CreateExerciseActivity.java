package com.example.dissertationproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dissertationproject.objects.Category;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.Enums;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateExerciseActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner category;
    EditText name;
    EditText desc;

    private String updateID = "";
    ExerciseTemplate updating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercises);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(Color.parseColor("#0B173B"));
        window.setStatusBarColor(Color.parseColor("#0B173B"));

//        String[] categories = {"Chest", "Back", "Shoulders", "Biceps", "Triceps", "Forearms", "Quads",
//                "Hamstrings", "Calves"};
//        String[] categories = Category.values().to;

        Spinner spinner = findViewById(R.id.spinExerciseCateg);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Category.categories());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        category = findViewById(R.id.spinExerciseCateg);
        name = findViewById(R.id.etNameExercise);
        desc = findViewById(R.id.etDescriptionExercise);
        Button del = findViewById(R.id.btnDeleteExercise);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            updateID = extras.getString("exerciseID");
        }

        if(isUpdating()){
            updating = ExerciseTemplate.getFromID(updateID);
            name.setText(updating.getName());
            desc.setText(updating.getDesc());
        }else{
            del.setVisibility(View.INVISIBLE);
        }

    }
    public boolean isUpdating(){
        if(updateID.equals("")) return false;
        else return true;
    }

    public void createExercise(View v){

        if(!isUpdating()) {
// Create a new user with a first and last name
            Map<String, Object> exercise = new HashMap<>();
            exercise.put("user", User.activeUser.getId());
            exercise.put("name", name.getText().toString());
            exercise.put("category", category.getSelectedItem().toString());
            exercise.put("description", desc.getText().toString());

            // Add a new document with a generated ID
            db.collection("exercises")
                    .add(exercise)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        ExerciseTemplate exerciseTemplate = new ExerciseTemplate(documentReference.getId(), name.getText().toString(), desc.getText().toString(), category.getSelectedItem().toString());
                        User.getActiveUser().getExerciseList().add(exerciseTemplate);
                        finish();
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

            Toast.makeText(getApplicationContext(),name.getText().toString()+" has been created",Toast.LENGTH_SHORT).show();
        }else{
            //TODO: update the entry

            updating.setName(name.getText().toString());
            updating.setDesc(desc.getText().toString());
            updating.setCategory(category.getSelectedItem().toString());

            db.collection("exercises").document(updateID)
                    .update(
                            "name", name.getText().toString(),
                            "category", category.getSelectedItem().toString(),
                            "description",  desc.getText().toString()
                    );

            Toast.makeText(getApplicationContext(),name.getText().toString()+" has been updated",Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    public void deleteExercise(View v){
        if(isUpdating()){
            db.collection("exercises").document(updating.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        User.getActiveUser().getExerciseList().remove(updating);
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
        }
        finish();
        Toast.makeText(getApplicationContext(),name.getText().toString()+" has been deleted",Toast.LENGTH_SHORT).show();
    }
}