package com.example.dissertationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlanExercise;

import java.util.ArrayList;

public class AddExerciseToPlanActivity extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_to_plan);

        spinner = findViewById(R.id.spnSelectExercise);

        ArrayList<String> exercises = new ArrayList<>();

        for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()){
            exercises.add(exerciseTemplate.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, exercises);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    public void confirmExerciseToPlan(View v){
        CreateWorkoutActivity.exercises.add(new WorkoutPlanExercise(ExerciseTemplate.getFromName(spinner.getSelectedItem().toString())));
        finish();
    }
}