/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.workouts;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.RepLine;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.example.dissertationproject.ui.workouts.ActiveWorkoutAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActiveWorkoutActivity extends AppCompatActivity {
    public static WorkoutPlan plan;
    public static ArrayList<WorkoutPlanExercise> exercises;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText name;
    private EditText desc;
    private long startTime;

    /**
     * Method for when the view is first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_workout);
        getSupportActionBar().hide();

        //initialise variables
        exercises = new ArrayList<>();
        recyclerView = findViewById(R.id.rcvActiveWorkout);
        recyclerView.setHasFixedSize(true);
        name = findViewById(R.id.etActiveWorkoutName);
        desc = findViewById(R.id.etActiveWorkoutDesc);
        name.setText(plan.getName());
        desc.setText(plan.getDesc());

        //Iterate all the exercises in the workout plan and add them to the recycler view
        for(Exercise exercise : plan.getExercises()){
            WorkoutPlanExercise workoutPlanExercise = new WorkoutPlanExercise(exercise);
            for(Map.Entry<Integer, HashMap<Integer, Integer>> rep : exercise.getReps().entrySet()){
                for(int r : rep.getValue().values()){
                    workoutPlanExercise.getTargetReps().add(r);
                }
            }
            exercises.add(workoutPlanExercise);
        }

        //set up the recycler view
        ActiveWorkoutAdapter wpAdapter = new ActiveWorkoutAdapter(this, exercises);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);

        startTime = System.currentTimeMillis(); // get the time the workout was started
    }

    /**
     * Save the workout to the database
     * @param v the current view
     */
    public void saveActiveWorkout(View v){

        //create a workout object and add the corresponding variables
        Workout wk = new Workout(name.getText().toString());
        wk.setEndTime(System.currentTimeMillis());
        wk.setStartTime(startTime);

        //Create a hashmap with variables for the database
        Map<String, Object> workout = new HashMap<>();
        workout.put("user", User.activeUser.getId());
        workout.put("name", name.getText().toString());
        workout.put("description", desc.getText().toString());
        workout.put("end_time", wk.getEndTime());
        workout.put("start_time", wk.getStartTime());

        //store the information in the database
        db.collection("workout_log")
                .add(workout)
                .addOnSuccessListener(documentReference -> {

                    //Store each exercise
                    for(WorkoutPlanExercise e : exercises){
                        Exercise exercise = new Exercise(e.getExerciseTemplate());
                        for(RepLine repLine : e.getRepLines()){
                            int rep= 0;
                            int weight = 0;

                            if(!repLine.getReps().getText().toString().equals("")){
                                rep = Integer.parseInt(repLine.getReps().getText().toString());
                            }
                            if (!repLine.getWeight().getText().toString().equals("")) {
                                weight = Integer.parseInt(repLine.getWeight().getText().toString());
                            }

                            e.getTargetReps().add(rep);

                            HashMap<Integer, Integer> val = new HashMap<>();
                            val.put(weight, rep);
                            exercise.getReps().put(exercise.getReps().size(), val);
                        }

                        //add each exercise to the workout object
                        wk.getExercises().add(exercise);

                        Map<String, Object> exercises = new HashMap<>();
                        exercises.put("exercise_template_id", e.getExerciseTemplate().getId());
                        exercises.put("workout_id", documentReference.getId());

                        //Store each exercise of the workout in the database
                        db.collection("exercise_workout_log")
                                .add(exercises)
                                .addOnSuccessListener(documentReference12 -> {

                                    for(Map.Entry<Integer, HashMap<Integer, Integer>> reps : exercise.getReps().entrySet()){

                                        for(Map.Entry<Integer, Integer> r : reps.getValue().entrySet()){
                                            Map<String, Object> exerciseReps = new HashMap<>();
                                            exerciseReps.put("exercise_workout_id", documentReference12.getId());
                                            exerciseReps.put("reps", r.getValue());
                                            exerciseReps.put("weight", r.getKey());

                                            //Store all the information about the sets for each exercise
                                            db.collection("exercise_workout_sets")
                                                    .add(exerciseReps)
                                                    .addOnFailureListener(e1 -> Log.w(TAG, "Error adding document", e1));
                                        }
                                    }
                                })
                                .addOnFailureListener(e12 -> Log.w(TAG, "Error adding document", e12));
                    }

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        //Add the workout object to the users cached data and inform them
        User.activeUser.getWorkoutLog().add(wk);
        Toast.makeText(getApplicationContext(),wk.getName()+" has been logged",Toast.LENGTH_SHORT).show();
        finish();

    }



}