/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.WriteResult;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RunnableScheduledFuture;

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


//        if(email.getText().toString().equals("") || password.getText().toString().equals("")){
//            return;
//        }
        //check that the user account exists
        //email.getText().toString()
        //password.getText().toString()
        mAuth.signInWithEmailAndPassword("sandeshadhikari55@gmail.com", "Password$123")
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);

                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        if(user.isEmailVerified()){
                            //create a local instance of the user and cache their data
                            new User(user.getUid(), user.getDisplayName(), user.getEmail()).setActiveUser();
                            cacheUserData();

                            //clear the login fields to prevent unwanted access
                            email.setText("");
                            password.setText("");
                        }else{
                            Utils.errorDialog(this, "E-mail verification", "Please verify your email address.", "Continue");
                            progressBar.setVisibility(View.INVISIBLE);
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
                            if(document.get("user").toString().equals(User.activeUser.getId())) {
                                s = s + document.getData();

                                //create new objects and assign the correct attributes
                                ExerciseTemplate exerciseTemplate = new ExerciseTemplate(
                                        document.getId(), document.get("name").toString(),
                                        document.get("description").toString(),
                                        Category.enumFromName(document.get("category").toString()));

                                //give the user this object
                                User.activeUser.getExerciseList().add(exerciseTemplate);
                            }
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
                                db.collection("plan_exercises").whereEqualTo("workout_plan_id", workoutPlan.getId().toString())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task1.getResult()) {

                                                    if(document1.get("workout_plan_id").toString().equals(workoutPlan.getId())) {

                                                        //create a variable for it with attributes
                                                        String exercisePlanId = document1.getId();
                                                        if(document1.get("exercise_template_id").toString().equals("")) continue;

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
        //load default data if no workouts or exercises available for user
        if(User.getActiveUser().getExerciseList().isEmpty()
                && User.getActiveUser().getWorkoutList().isEmpty()){
            addDefaultData();
        }
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

    /**
     * Create the default exercises and workouts for users when they register
     */
    public void addDefaultData(){
        //A list of all default exercises
        ArrayList<ExerciseTemplate> defaultExercises = new ArrayList<>();
        ExerciseTemplate benchPress = new ExerciseTemplate("", "Bench Press", "Barbell", Category.CHEST);
        ExerciseTemplate pullUps = new ExerciseTemplate("", "Pull-ups", "Pronated grip", Category.BACK);
        ExerciseTemplate squats = new ExerciseTemplate("", "Squats", "Barbell back squats", Category.QUADS);
        ExerciseTemplate OHP = new ExerciseTemplate("", "Military Press", "Barbell", Category.SHOULDERS);
        ExerciseTemplate tricepPushdown = new ExerciseTemplate("", "Tricep Pushdown", "Cable machine, rope attachment", Category.TRICEPS);
        ExerciseTemplate bicepCurls = new ExerciseTemplate("", "Bicep Curls", "EZ-Bar bicep curls", Category.BICEPS);

        ExerciseTemplate legCurl = new ExerciseTemplate("", "Leg Curl", "Seated machine", Category.HAMSTRINGS);
        ExerciseTemplate legExtension = new ExerciseTemplate("", "Leg Extensions", "Machine", Category.QUADS);
        ExerciseTemplate calfRaise = new ExerciseTemplate("", "Calf Raises", "Machine", Category.CALVES);
        ExerciseTemplate deadLift = new ExerciseTemplate("", "Deadlift", "Barbell", Category.BACK);

        defaultExercises.add(benchPress);
        defaultExercises.add(pullUps);
        defaultExercises.add(squats);
        defaultExercises.add(tricepPushdown);
        defaultExercises.add(bicepCurls);
        defaultExercises.add(legCurl);
        defaultExercises.add(legExtension);
        defaultExercises.add(legCurl);
        defaultExercises.add(calfRaise);
        defaultExercises.add(deadLift);

        // Get a new write batch



        WriteBatch batch = db.batch();



// Commit the batch
        //add default exercises to database
        for(ExerciseTemplate template : defaultExercises){
            User.getActiveUser().getExerciseList().add(template);

            Map<String, Object> exercise = new HashMap<>();
            exercise.put("user", User.getActiveUser().getId());
            exercise.put("name", template.getName());
            exercise.put("category", template.getCategory().getName());
            exercise.put("description", template.getDesc());

//            Reference newRef1 = db.collection("exercises");
            DocumentReference exercisesRef = db.collection("exercises").document();
            template.setId(exercisesRef.getId());
            batch.set(exercisesRef, exercise);

            // Add a new document with a generated ID
//            db.collection("exercises")
//                    .add(exercise)
//                    .addOnSuccessListener(documentReference -> {
//                        template.setId(documentReference.getId());
//                    });
        }

        batch.commit().addOnCompleteListener(task -> {

            //Now create the default workouts
            ArrayList<WorkoutPlan> defaultWorkoutPlans = new ArrayList<>();
            WorkoutPlan legs = new WorkoutPlan("", User.getActiveUser(), "Leg day",
                    "A basic workout targeting quads, hamstrings and calves");
            legs.getExercises().add(addExercise(squats, 3, 10));
            legs.getExercises().add(addExercise(legExtension, 4, 8));
            legs.getExercises().add(addExercise(legCurl, 3, 10));
            legs.getExercises().add(addExercise(calfRaise, 4, 12));

            WorkoutPlan fullBody = new WorkoutPlan("", User.getActiveUser(), "Full body",
                    "A basic workout targeting most muscle groups");
            fullBody.getExercises().add(addExercise(squats, 5, 5));
            fullBody.getExercises().add(addExercise(benchPress, 5, 5));
            fullBody.getExercises().add(addExercise(pullUps, 3, 10));
            fullBody.getExercises().add(addExercise(tricepPushdown, 4, 12));
            fullBody.getExercises().add(addExercise(bicepCurls, 4, 12));

            WorkoutPlan upperBody = new WorkoutPlan("", User.getActiveUser(), "Upper body",
                    "A basic workout targeting most muscle groups");
            upperBody.getExercises().add(addExercise(benchPress, 5, 5));
            upperBody.getExercises().add(addExercise(OHP, 3, 8));
            upperBody.getExercises().add(addExercise(pullUps, 3, 10));
            upperBody.getExercises().add(addExercise(tricepPushdown, 4, 12));
            upperBody.getExercises().add(addExercise(bicepCurls, 4, 12));

            defaultWorkoutPlans.add(legs);
            defaultWorkoutPlans.add(fullBody);
            defaultWorkoutPlans.add(upperBody);

            //Save the workouts to the database
            for(WorkoutPlan workoutPlan : defaultWorkoutPlans){
                User.getActiveUser().getWorkoutList().add(workoutPlan);

                Map<String, Object> workout = new HashMap<>();
                workout.put("user", User.getActiveUser().getId());
                workout.put("name", workoutPlan.getName());
                workout.put("description", workoutPlan.getDesc());

                // Add a new document with a generated ID
                db.collection("workout_plans")
                        .add(workout)
                        .addOnSuccessListener(documentReference -> {
                            workoutPlan.setId(documentReference.getId());

                            for (Exercise e : workoutPlan.getExercises()) {

                                Map<String, Object> exercises = new HashMap<>();
                                exercises.put("exercise_template_id", e.getId());
                                exercises.put("workout_plan_id", documentReference.getId());

                                db.collection("plan_exercises")
                                        .add(exercises)
                                        .addOnSuccessListener(documentReference12 -> {
                                            for (HashMap<Integer, Integer> reps : e.getReps().values()) {
                                                for(int r : reps.values()){
                                                    Map<String, Object> exerciseReps = new HashMap<>();
                                                    exerciseReps.put("exercise_plan_id", documentReference12.getId());
                                                    exerciseReps.put("reps", r);

                                                    db.collection("exercise_plan_sets")
                                                            .add(exerciseReps);
                                                }
                                            }
                                        });
                            }

                        });
            }
        });
    }

    /**
     * Adds exercises to a default workout
     * @param template      the exercise template
     * @param sets          the number of sets for the exercise
     * @param targetReps    the number of reps to achieve
     * @return              an exercise object
     */
    public Exercise addExercise(ExerciseTemplate template, int sets, int targetReps){
        Exercise exercise = new Exercise(template);
        HashMap<Integer, Integer> reps = new HashMap<>();
        for(int i = 0; i < sets; i++){
            reps.put(0, targetReps);
            exercise.getReps().put(exercise.getReps().size(), reps);
        }

        return exercise;
    }
}