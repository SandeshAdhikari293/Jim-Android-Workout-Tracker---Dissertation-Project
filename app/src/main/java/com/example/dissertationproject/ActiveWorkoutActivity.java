package com.example.dissertationproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.RepLine;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.example.dissertationproject.workoutPlan.CreateWorkoutPlanAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActiveWorkoutActivity extends AppCompatActivity {

    public static WorkoutPlan plan;
    Workout workout;
    RecyclerView recyclerView;
    public static ArrayList<WorkoutPlanExercise> exercises;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    EditText name;
    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_workout);

        exercises = new ArrayList<>();

        recyclerView = findViewById(R.id.rcvActiveWorkout);
        recyclerView.setHasFixedSize(true);
//        plan = User.activeUser.getWorkoutList().get(0);

        name = findViewById(R.id.etActiveWorkoutName);
        desc = findViewById(R.id.etActiveWorkoutDesc);

        name.setText(plan.getName());
        desc.setText(plan.getDesc());

//        System.out.println(exe);
        for(Exercise exercise : plan.getExercises()){
            System.out.println(exercise.getTemplate().getName());

            WorkoutPlanExercise workoutPlanExercise = new WorkoutPlanExercise(exercise.getTemplate());
            for(int rep : exercise.getReps()){
                workoutPlanExercise.getTargetReps().add(rep);
            }
            exercises.add(workoutPlanExercise);

        }

        CreateWorkoutPlanAdapter wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);
    }

    public void saveActiveWorkout(View v){

        Map<String, Object> workout = new HashMap<>();
        workout.put("user", User.activeUser.getId());
        workout.put("name", name.getText().toString());
        workout.put("description", desc.getText().toString());

        db.collection("workout_log")
                .add(workout)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    for(WorkoutPlanExercise e : exercises){

                        Exercise exercise = new Exercise(e.getExerciseTemplate());

                        for(RepLine repLine : e.getRepLines()){
                            int rep = Integer.parseInt(repLine.getReps().getText().toString());
                            e.getTargetReps().add(rep);
                            exercise.getReps().add(rep);
                        }

//                            workoutPlan.getExercises().add(exercise);

                        Map<String, Object> exercises = new HashMap<>();
                        exercises.put("exercise_template_id", e.getExerciseTemplate().getId());
                        exercises.put("workout_id", documentReference.getId());


                        db.collection("exercise_workout_log")
                                .add(exercises)
                                .addOnSuccessListener(documentReference12 -> {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference12.getId());

                                    for(int reps : e.getTargetReps()){

                                        Map<String, Object> exerciseReps = new HashMap<>();
                                        exerciseReps.put("exercise_workout_id", documentReference12.getId());
                                        exerciseReps.put("reps", reps);


                                        db.collection("exercise_workout_sets")
                                                .add(exerciseReps)
                                                .addOnSuccessListener(documentReference1 -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference1.getId()))
                                                .addOnFailureListener(e1 -> Log.w(TAG, "Error adding document", e1));
                                    }
                                })
                                .addOnFailureListener(e12 -> Log.w(TAG, "Error adding document", e12));
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


        finish();
    }



}