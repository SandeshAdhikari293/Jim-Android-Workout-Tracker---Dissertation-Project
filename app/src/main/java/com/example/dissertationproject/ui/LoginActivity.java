/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.objects.enums.Category;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText password;
    private ProgressBar progressBar;
    private EditText email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Initalise variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        password = findViewById(R.id.etPasswordLogin);
        email = findViewById(R.id.etEmailLogin);
        progressBar = findViewById(R.id.pbLogin);
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Take the user to the registration page
     * @param v
     */
    public void register(View v){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    /**
     * Validate the user credentials and log them in
     * @param view
     */
    public void login(View view){
        //check if the fields are empty
        if(email.getText().toString().equals("") || password.getText().toString().equals("")){
            return;
        }
        //check that the user account exists
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);

                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        //TODO: Change back the negation to make it work properly again - just for testing purposes.
                        if(!user.isEmailVerified()){
                            //create a local instance of the user and cache their data
                            new User(user.getUid(), user.getDisplayName(), user.getEmail()).setActiveUser();
                            cacheUserData();

                            //clear the login fields to prevent unwanted access
                            email.setText("");
                            password.setText("");
                        }else{
                            Utils.errorDialog(this, "E-mail verification", "Please verify your email address.", "Continue");
                        }
                    } else {
                        Utils.errorDialog(this, "User login", "Please enter a valid e-mail address and password.", "Continue");
                    }
                });


    }


    /**
     * Cache the users data
     */
    public void cacheUserData(){
        loadProfile();
    }

    /**
     * Load the users profile information from the database
     */
    public void loadProfile(){
        db.collection("profiles").whereEqualTo("user_id", User.getActiveUser().getId())
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task1.getResult()) {

                            //set the user object attributes
                            User.getActiveUser().setAdmin( (boolean) document.get("is_admin"));
                            User.getActiveUser().setActivated( (boolean) document.get("active"));
                            User.getActiveUser().setProfileID(document.getId());

                        }

                        //if the user has been deactivated by an admin, log them out again
                        if(!User.getActiveUser().isActivated()){
                            mAuth.signOut();
                            Utils.errorDialog(this, "Account deactivated",
                                    "Your account has been deactivated, please contact" +
                                            " an admin for support.", "Continue");
                            return;
                        }

                        //Admins need to have access to all users profiles, so cache them too
                        if(User.getActiveUser().isAdmin()){
                            loadUsersProfiles();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task1.getException());
                    }
                    loadExercises();
                });
    }

    /**
     * Load all the users exercises from the database and cache them
     */
    public void loadExercises(){
        db.collection("exercises")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String s = "";
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.get("user").toString().equals(User.activeUser.getId())){
                                s = s + document.getData();
                            }
                            //create new objects and assign the correct attributes
                            ExerciseTemplate exerciseTemplate = new ExerciseTemplate(
                                    document.getId(), document.get("name").toString(),
                                    document.get("description").toString(),
                                    Category.enumFromName(document.get("category").toString()));

                            //give the user this object
                            User.activeUser.getExerciseList().add(exerciseTemplate);

                        }

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                    loadWorkoutPlans();
                });
    }

    /**
     * Cache all the users workout plan information from the database
     */
    public void loadWorkoutPlans(){
        //load from the database
        db.collection("workout_plans")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //check if the data belongs to the active user
                            if(document.get("user").toString().equals(User.activeUser.getId())) {

                                //create a new workout plan object and assign to the user
                                WorkoutPlan workoutPlan = new WorkoutPlan(document.getId(),
                                        User.activeUser,
                                        document.get("name").toString(),
                                        document.get("description").toString());

                                User.activeUser.getWorkoutList().add(workoutPlan);

                                //get all exercises from that workout plan
                                db.collection("plan_exercises")
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task1.getResult()) {

                                                    if(document1.get("workout_plan_id").toString().equals(workoutPlan.getId())) {

                                                        //create a variable for it with attributes
                                                        String exercisePlanId = document1.getId();
                                                        Exercise exercise = new Exercise(ExerciseTemplate.getFromID(document1.get("exercise_template_id").toString()));

                                                        if(exercise == null) continue;

                                                        //add the exercise to the workout
                                                        workoutPlan.getExercises().add(exercise);


                                                        //get all the set data for that exercise
                                                        db.collection("exercise_plan_sets")
                                                                .get()
                                                                .addOnCompleteListener(task11 -> {
                                                                    if (task11.isSuccessful()) {
                                                                        HashMap<Integer, HashMap<Integer,Integer>> reps = new HashMap<>();

                                                                        for (QueryDocumentSnapshot document11 : task11.getResult()) {
                                                                            if(document11.get("exercise_plan_id").toString().equals(exercisePlanId)) {
                                                                                //add set data to the exercise
                                                                                HashMap<Integer, Integer> val = new HashMap<>();
                                                                                val.put(0,Integer.parseInt(document11.get("reps").toString()));
                                                                                reps.put(reps.size(), val);

                                                                            }

                                                                        }
                                                                        exercise.setReps(reps);

                                                                    } else {
                                                                        Log.w(TAG, "Error getting documents.", task11.getException());
                                                                    }
                                                                });
                                                    }

                                                }
                                            } else {
                                                Log.w(TAG, "Error getting documents.", task1.getException());
                                            }
                                        });

                            }

                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                    loadWorkouts();
                });
    }

    /**
     * Load the logged workouts from the database and cache into memory
     */
    public void loadWorkouts(){
        db.collection("workout_log")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.get("user").toString().equals(User.activeUser.getId())) {

                                //create a new object with attributes
                                Workout workout = new Workout(document.get("name").toString());
                                workout.setId(document.getId());

                                workout.setEndTime(Long.parseLong(document.get("end_time").toString()));
                                workout.setStartTime(Long.parseLong(document.get("start_time").toString()));

                                User.getActiveUser().getWorkoutLog().add(workout);

                                //load all the exercises from the log
                                db.collection("exercise_workout_log")
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task1.getResult()) {

                                                    //check that the exercise corresponds to this workout
                                                    if(document1.get("workout_id").toString().equals(workout.getId())) {

                                                        //create a new exercise object
                                                        String exerciseLogId = document1.getId();
                                                        Exercise exercise = new Exercise(ExerciseTemplate.getFromID(document1.get("exercise_template_id").toString()));

                                                        if(exercise == null) continue;
                                                        workout.getExercises().add(exercise);

                                                        //load all the set information for that exercise
                                                        db.collection("exercise_workout_sets")
                                                                .get()
                                                                .addOnCompleteListener(task11 -> {
                                                                    if (task11.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document11 : task11.getResult()) {
                                                                            //if the id's match, then assign the rep data to the exercise
                                                                            if(document11.get("exercise_workout_id").toString().equals(exerciseLogId)) {
                                                                                HashMap<Integer, Integer> val = new HashMap<>();
                                                                                val.put(Integer.parseInt(document11.get("weight").toString()) ,Integer.parseInt(document11.get("reps").toString()));
                                                                                exercise.getReps().put(exercise.getReps().size(), val);
                                                                            }
                                                                        }
                                                                    } else {
                                                                        Log.w(TAG, "Error getting documents.", task11.getException());
                                                                    }
                                                                });
                                                    }
                                                }
                                            } else {
                                                Log.w(TAG, "Error getting documents.", task1.getException());
                                            }
                                        });
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        //Hide the progress bar as all the data has been loaded
        progressBar.setVisibility(View.INVISIBLE);

    }

    /**
     * Load all users profiles, needed for admins.
     */
    public void loadUsersProfiles(){
        //iterate over the profiles table in the database
        db.collection("profiles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //create user objects for that data
                            User u = new User(document.get("user_id").toString(), document.get("name").toString(), document.get("email").toString());
                            u.setAdmin((boolean) document.get("is_admin"));
                            u.setActivated((boolean) document.get("active"));
                            u.setProfileID(document.getId());
                            User.users.add(u);
                        }

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    /**
     * Send an email to the user when they have forgotten their password
     * @param v
     */
    public void forgotPassword(View v){
        if(!email.getText().toString().equals("")){
            //Go through firebase auth to sent the email reset
            FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString());
            Utils.errorDialog(this, "Password reset", "An E-mail has sent to "+email.getText().toString(),"Continue");
        }else {
            Utils.errorDialog(this, "Password reset", "Please enter an E-mail address in the form above and click again.", "Continue");
        }
    }
}