package com.example.dissertationproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.RepLine;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.example.dissertationproject.workoutPlan.CreateWorkoutPlanAdapter;
import com.example.dissertationproject.workoutPlan.WorkoutPlanAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateWorkoutActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView recyclerView;

    WorkoutPlan workoutPlan;

    CreateWorkoutPlanAdapter wpAdapter;
    LinearLayoutManager linearLayoutManager;

    public static ArrayList<WorkoutPlanExercise> exercises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        workoutPlan = new WorkoutPlan("n/a", User.activeUser, "Unavailable", "Unavailable");


//        exercises = new ArrayList<>();
        recyclerView = findViewById(R.id.rcCreateWorkout);
        recyclerView.setHasFixedSize(true);

//        WorkoutPlan workoutPlan = new WorkoutPlan("abc", User.activeUser, "abc", "abc");
//
//        exercises.add(new WorkoutPlanExercise(workoutPlan, User.activeUser.getExerciseList().get(0)));
//        exercises.add(new WorkoutPlanExercise(workoutPlan, User.activeUser.getExerciseList().get(1)));


        wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
                // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
       linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);

    }

    @Override
    protected void onResume() {
        System.out.println(exercises);


//        CreateWorkoutPlanAdapter wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);



        super.onResume();
    }

    public void saveWorkoutPlan(View view){
        workoutPlan.setName(((EditText)findViewById(R.id.etWorkoutPlanName)).getText().toString());
        workoutPlan.setDesc(((EditText)findViewById(R.id.etWorkoutPlanDescription)).getText().toString());


        Map<String, Object> workout = new HashMap<>();
        workout.put("user", User.activeUser.getId());
        workout.put("name", workoutPlan.getName());
        workout.put("description", workoutPlan.getDesc());
//        workout.put("description", workoutPlan.get);




        for(WorkoutPlanExercise e : exercises){
            Exercise exercise = new Exercise(e.getExerciseTemplate());

            for(RepLine repLine : e.getRepLines()) {
                int rep = Integer.parseInt(repLine.getReps().getText().toString());
                exercise.getReps().add(rep);
            }
            workoutPlan.getExercises().add(exercise);

        }

        User.activeUser.getWorkoutList().add(workoutPlan);

        // Debugging info

//        System.out.println("Workouts: " + User.activeUser.getWorkoutList().size());
//        for(WorkoutPlan wk : User.activeUser.getWorkoutList()){
//            for(Exercise ex : wk.getExercises()){
//                System.out.println(ex.getTemplate().getName() + " : " + ex.getReps());
//            }
//        }

        // Add a new document with a generated ID
        db.collection("workout_plans")
                .add(workout)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        for(WorkoutPlanExercise e : exercises){

                            Exercise exercise = new Exercise(e.getExerciseTemplate());

                            for(RepLine repLine : e.getRepLines()){
                                int rep = Integer.parseInt(repLine.getReps().getText().toString());
                                e.getTargetReps().add(rep);
                                exercise.getReps().add(rep);
                            }

                            workoutPlan.getExercises().add(exercise);

                            Map<String, Object> exercises = new HashMap<>();
                            exercises.put("exercise_template_id", e.getExerciseTemplate().getId());
                            exercises.put("workout_plan_id", documentReference.getId());

                            db.collection("exercise_plans")
                                    .add(exercises)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                            for(int reps : e.getTargetReps()){

                                                Map<String, Object> exerciseReps = new HashMap<>();
                                                exerciseReps.put("exercise_plan_id", documentReference.getId());
                                                exerciseReps.put("reps", reps);

                                                db.collection("exercise_plan_sets")
                                                        .add(exerciseReps)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());


                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error adding document", e);
                                                            }
                                                        });
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }

                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
//        System.out.println(User.activeUser.getWorkoutList());
    }

    public void addExerciseToPlan(View view){
        startActivity(new Intent(CreateWorkoutActivity.this, AddExerciseToPlanActivity.class));
    }
}