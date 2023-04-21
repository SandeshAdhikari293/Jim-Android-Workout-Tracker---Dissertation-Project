/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.exercises;

import static android.content.ContentValues.TAG;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.enums.Category;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateExerciseActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Spinner category;
    private EditText name;
    private EditText desc;
    private String updateID = "";
    private ExerciseTemplate updating;

    /**
     * When the view is created, initialise variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercises);
        getSupportActionBar().hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(Color.parseColor("#0B173B"));
        window.setStatusBarColor(Color.parseColor("#0B173B"));

        //Initialise the spinner and its data
        Spinner spinner = findViewById(R.id.spinExerciseCateg);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Category.categories());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        category = findViewById(R.id.spinExerciseCateg);
        name = findViewById(R.id.etNameExercise);
        desc = findViewById(R.id.etDescriptionExercise);
        Button del = findViewById(R.id.btnDeleteExercise);

        //Check if the exercise is being updated or not
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            updateID = extras.getString("exerciseID");
        }

        //apply the name and description if updating
        if(isUpdating()){
            updating = ExerciseTemplate.getFromID(updateID);
            name.setText(updating.getName());
            desc.setText(updating.getDesc());
        }else{
            del.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Determines whether the exercise is being updated or a new one created
     * @return  boolean
     */
    public boolean isUpdating(){
        if(updateID.equals("")) return false;
        else return true;
    }

    /**
     * Save the exercise to the database
     * @param v
     */
    public void createExercise(View v){


        if(!isUpdating()) {
            //Store the data in a hashmap for the exercise
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

                        //create a new exercise template object and
                        //store the exercise in cached data as well
                        ExerciseTemplate exerciseTemplate = new ExerciseTemplate(
                                documentReference.getId(), name.getText().toString(),
                                desc.getText().toString(),
                                Category.valueOf(category.getSelectedItem().toString()));

                        User.getActiveUser().getExerciseList().add(exerciseTemplate);
                        finish();
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

            Toast.makeText(getApplicationContext(),name.getText().toString()+" has been created",Toast.LENGTH_SHORT).show();
        }else{

            //update the existing exercise in the database
            updating.setName(name.getText().toString());
            updating.setDesc(desc.getText().toString());
            updating.setCategory(Category.valueOf(category.getSelectedItem().toString()));

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