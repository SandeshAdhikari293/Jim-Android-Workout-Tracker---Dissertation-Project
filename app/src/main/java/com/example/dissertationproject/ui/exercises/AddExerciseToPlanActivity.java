/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.exercises;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dissertationproject.ui.workouts.workoutPlan.CreateWorkoutActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlanExercise;

import java.util.ArrayList;

public class AddExerciseToPlanActivity extends AppCompatActivity {

    private Spinner spinner;

    /**
     * Initialise variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_exercise_to_plan);


        //Initialise the spinner and its data
        spinner = findViewById(R.id.spnSelectExercise);
        ArrayList<String> exercises = new ArrayList<>();

        for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()) {
            exercises.add(exerciseTemplate.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, exercises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * When the confirm button is pressed, add the workout plan to the users account
     * @param v the view
     */
    public void confirmExerciseToPlan(View v){
        //Add a new workout plan to the users list
        CreateWorkoutActivity.exercises.add(new WorkoutPlanExercise(ExerciseTemplate.getFromName(spinner.getSelectedItem().toString())));

        finish();
    }
}