/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dissertationproject.R;
import com.example.dissertationproject.databinding.ActivityCreateExerciseBinding;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.ui.exercises.CreateExerciseActivity;
import com.example.dissertationproject.ui.workouts.workoutPlan.CreateWorkoutActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ActivityCreateExerciseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Setting up the bottom navigation view
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_log, R.id.navigation_stats, R.id.navigation_workouts, R.id.navigation_exercises, R.id.navigation_admin)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_create_exercise);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Remove the admin screen if the user is not an admin
        if(!User.activeUser.isAdmin()){
            binding.navView.getMenu().removeItem(R.id.navigation_admin);
        }


    }

    /**
     * create an action bar button
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle button activities
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //The user has clicked the profile button
        if (id == R.id.mybutton) {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Take the user to the create exercise screen
     * @param v
     */
    public void addExercise(View v){
        startActivity(new Intent(DashboardActivity.this, CreateExerciseActivity.class));
    }

    /**
     * Take the user to the user profile screen
     * @param v
     */
    public void userProfile(View v){
        Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
        intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        startActivity(intent);
    }

    /**
     * Take the user to the create workout plan screen
     * @param v
     */
    public void createWorkoutPlan(View v){
        CreateWorkoutActivity.exercises = new ArrayList<>();

        startActivity(new Intent(DashboardActivity.this, CreateWorkoutActivity.class));
    }

}