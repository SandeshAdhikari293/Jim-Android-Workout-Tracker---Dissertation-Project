package com.example.dissertationproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText password;
    EditText email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        password = findViewById(R.id.etPasswordLogin);
        email = findViewById(R.id.etEmailLogin);

    }

    public void register(View v){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void login(View view){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        //TODO: Change back the negation to make it work properly again - just for testing purposes.
                        if(!user.isEmailVerified()){
                            new User(user.getUid(), user.getDisplayName(), user.getEmail()).setActiveUser();
                            cacheUserData();
                        }else{
                            Utils.errorDialog(this, "E-mail verification", "Please verify your email address.", "Continue");
                        }
                    } else {
                        Utils.errorDialog(this, "User login", "Please enter a valid e-mail address and password.", "Continue");
                    }
                });
    }

    public void errorMessage(Context c, String msg){
        Toast toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
        View view = toast.getView();
//        view.setBackgroundResource(R.drawable.custom_background);
        TextView text = view.findViewById(android.R.id.message);
        /*Here you can do anything with above textview like text.setTextColor(Color.parseColor("#000000"));*/
        text.setTextColor(Color.parseColor("#000000"));
        toast.show();
    }


    public void cacheUserData(){
        loadProfile();

    }

    public void loadProfile(){
        System.out.println("Loading profiles....");
        db.collection("profiles").whereEqualTo("user_id", User.getActiveUser().getId())
                .get()
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task1.getResult()) {
                            User.getActiveUser().setAdmin( (boolean) document.get("is_admin"));
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task1.getException());
                    }
                    loadExercises();
                });
    }

    public void loadExercises(){
        System.out.println("Loading exercises...");
        db.collection("exercises")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String s = "";
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.get("user").toString().equals(User.activeUser.getId())){
                                s = s + document.getData();
                            }
                            ExerciseTemplate exerciseTemplate = new ExerciseTemplate(
                                    document.getId(), document.get("name").toString(),
                                    document.get("description").toString(),
                                    document.get("category").toString());

                            User.activeUser.getExerciseList().add(exerciseTemplate);

                        }

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                    loadWorkoutPlans();
                });
    }

    public void loadWorkoutPlans(){
        System.out.println("Loading plans now...");
        db.collection("workout_plans")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.get("user").toString().equals(User.activeUser.getId())) {

                                WorkoutPlan workoutPlan = new WorkoutPlan(document.getId(),
                                        User.activeUser,
                                        document.get("name").toString(),
                                        document.get("description").toString());

                                User.activeUser.getWorkoutList().add(workoutPlan);

                                db.collection("plan_exercises")
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task1.getResult()) {

                                                    if(document1.get("workout_plan_id").toString().equals(workoutPlan.getId())) {

                                                        String exercisePlanId = document1.getId();
                                                        Exercise exercise = new Exercise(ExerciseTemplate.getFromID(document1.get("exercise_template_id").toString()));

                                                        workoutPlan.getExercises().add(exercise);


                                                        db.collection("exercise_plan_sets")
                                                                .get()
                                                                .addOnCompleteListener(task11 -> {
                                                                    if (task11.isSuccessful()) {
                                                                        HashMap<Integer, HashMap<Integer,Integer>> reps = new HashMap<>();

                                                                        for (QueryDocumentSnapshot document11 : task11.getResult()) {
                                                                            if(document11.get("exercise_plan_id").toString().equals(exercisePlanId)) {
                                                                                HashMap<Integer, Integer> val = new HashMap<>();
                                                                                val.put(0,Integer.parseInt(document11.get("reps").toString()));
                                                                                reps.put(reps.size(), val);

                                                                            }

                                                                        }
                                                                        exercise.setReps(reps);
                                                                        System.out.println("Loading: "+ exercise.getTemplate().getName() + " | "+ reps + " <-- "+ workoutPlan.getName());

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

    public void loadWorkouts(){
        System.out.println("Loading logs..");
        db.collection("workout_log")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.get("user").toString().equals(User.activeUser.getId())) {

                                Workout workout = new Workout(document.get("name").toString());
                                workout.setId(document.getId());

                                workout.setEndTime(Long.parseLong(document.get("end_time").toString()));

                                User.getActiveUser().getWorkoutLog().add(workout);

                                db.collection("exercise_workout_log")
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task1.getResult()) {

                                                    if(document1.get("workout_id").toString().equals(workout.getId())) {

                                                        String exerciseLogId = document1.getId();
                                                        Exercise exercise = new Exercise(ExerciseTemplate.getFromID(document1.get("exercise_template_id").toString()));

                                                        System.out.println("workout: " + workout.getId() + " | " + workout.getName());
                                                        System.out.println(exercise.getTemplate().getName() + "");
                                                        workout.getExercises().add(exercise);

                                                        db.collection("exercise_workout_sets")
                                                                .get()
                                                                .addOnCompleteListener(task11 -> {
                                                                    if (task11.isSuccessful()) {

                                                                        for (QueryDocumentSnapshot document11 : task11.getResult()) {

                                                                            if(document11.get("exercise_workout_id").toString().equals(exerciseLogId)) {
//                                                                                    System.out.println("found, now add " + document11.get("reps"));
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

    }

    public void forgotPassword(View v){
        if(!email.getText().toString().equals("")){
            FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    System.out.println("OK SENT");
                }
            });
            Utils.errorDialog(this, "Password reset", "A E-mail has sent to "+email.getText().toString(),"Continue");
        }
    }
}