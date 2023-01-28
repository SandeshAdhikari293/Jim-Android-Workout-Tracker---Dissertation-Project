package com.example.dissertationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.example.dissertationproject.workoutPlan.CreateWorkoutPlanAdapter;
import com.example.dissertationproject.workoutPlan.WorkoutPlanAdapter;

import java.util.ArrayList;

public class CreateWorkoutActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    WorkoutPlan workoutPlan;

    public static ArrayList<WorkoutPlanExercise> exercises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        workoutPlan = new WorkoutPlan("idk", User.activeUser, "testiguess", "no descr");


        exercises = new ArrayList<>();
        recyclerView = findViewById(R.id.rcCreateWorkout);
        recyclerView.setHasFixedSize(true);

        WorkoutPlan workoutPlan = new WorkoutPlan("abc", User.activeUser, "abc", "abc");

//        exercises.add(new WorkoutPlanExercise(workoutPlan, User.activeUser.getExerciseList().get(0)));
//        exercises.add(new WorkoutPlanExercise(workoutPlan, User.activeUser.getExerciseList().get(1)));


        CreateWorkoutPlanAdapter wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
                // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);

    }

    @Override
    protected void onResume() {
        System.out.println(exercises);


        CreateWorkoutPlanAdapter wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);

        super.onResume();
    }

    public void saveWorkoutPlan(View view){
        finish();
    }

    public void addExerciseToPlan(View view){
        startActivity(new Intent(CreateWorkoutActivity.this, AddExerciseToPlanActivity.class));
    }
}