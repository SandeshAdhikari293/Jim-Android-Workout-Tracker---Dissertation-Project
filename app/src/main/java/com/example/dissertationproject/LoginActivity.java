package com.example.dissertationproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            new User(user.getUid(), user.getDisplayName(), user.getEmail()).setActiveUser();

                            //TODO: load all the exercises and workouts for the user from the database.

                            cacheUserData();

                            System.out.println("Login success");
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            System.out.println("Login failed");
//                            updateUI(null);
                        }
                    }
                });
    }

    public void cacheUserData(){
        loadExercises();
        loadWorkoutPlans();
        loadWorkouts();

    }

    public void loadExercises(){
        db.collection("exercises")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                    }
                });
    }

    public void loadWorkoutPlans(){
        db.collection("workout_plans")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("user").toString().equals(User.activeUser.getId())) {

                                    WorkoutPlan workoutPlan = new WorkoutPlan(document.getId(),
                                            User.activeUser,
                                            document.get("name").toString(),
                                            document.get("description").toString());

                                    User.activeUser.getWorkoutList().add(workoutPlan);

                                    db.collection("exercise_plans")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                                            if(document.get("workout_plan_id").toString().equals(workoutPlan.getId())) {

                                                                String exercisePlanId = document.getId();
                                                                Exercise exercise = new Exercise(ExerciseTemplate.getFromID(document.get("exercise_template_id").toString()));

                                                                workoutPlan.getExercises().add(exercise);


                                                                db.collection("exercise_plan_sets")
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    ArrayList<Integer> reps = new ArrayList<>();
                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                                                        if(document.get("exercise_plan_id").toString().equals(exercisePlanId)) {
                                                                                            reps.add(Integer.parseInt(document.get("reps").toString()));
                                                                                        }

                                                                                    }
                                                                                    exercise.setReps(reps);

                                                                                } else {
                                                                                    Log.w(TAG, "Error getting documents.", task.getException());
                                                                                }
                                                                            }
                                                                        });


                                                            }

                                                        }
                                                    } else {
                                                        Log.w(TAG, "Error getting documents.", task.getException());
                                                    }
                                                }
                                            });

                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void loadWorkouts(){

    }
}