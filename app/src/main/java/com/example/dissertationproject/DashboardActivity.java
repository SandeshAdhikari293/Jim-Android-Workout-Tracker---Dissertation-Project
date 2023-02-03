package com.example.dissertationproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dissertationproject.objects.WorkoutPlan;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dissertationproject.databinding.ActivityCreateExerciseBinding;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ActivityCreateExerciseBinding binding;
    public static WorkoutPlan activeWorkoutPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_log, R.id.navigation_stats, R.id.navigation_workouts, R.id.navigation_exercises)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_create_exercise);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void addExercise(View v){
        startActivity(new Intent(DashboardActivity.this, CreateExerciseActivity.class));
    }

    public void createWorkoutPlan(View v){
        CreateWorkoutActivity.exercises = new ArrayList<>();
        startActivity(new Intent(DashboardActivity.this, CreateWorkoutActivity.class));
    }

//    public void startWorkout(View v){
//        startActivity(new Intent(DashboardActivity.this, ActiveWorkoutActivity.class));
//    }
}