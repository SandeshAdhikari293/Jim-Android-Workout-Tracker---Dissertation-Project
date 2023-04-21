/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.workouts.workoutPlan;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.RepLine;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.example.dissertationproject.ui.exercises.AddExerciseToPlanActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateWorkoutActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private WorkoutPlan workoutPlan;
    private CreateWorkoutPlanAdapter wpAdapter;
    private LinearLayoutManager linearLayoutManager;
    public static ArrayList<WorkoutPlanExercise> exercises;
    private Button delete;

    /**
     * When the screen is created, initialise variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        exercises.clear(); //remove the exercises from the previous workout plan created
        setContentView(R.layout.activity_create_workout);
        delete = findViewById(R.id.btnDeleteWorkoutPlan);

        //create a new object
        workoutPlan = new WorkoutPlan("", User.activeUser, "Unavailable", "Unavailable");


        //check if the workout plan is being updated
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("workoutPlanID");
            workoutPlan = WorkoutPlan.getWorkoutPlanFromID(id);
        }

        if(isUpdating()){
            //add all the current data from the workout plan being updated
            exercises.clear();
            ((EditText)findViewById(R.id.etWorkoutPlanName)).setText(workoutPlan.getName());
            ((EditText)findViewById(R.id.etWorkoutPlanDescription)).setText(workoutPlan.getDesc());

            for(Exercise e : workoutPlan.getExercises()) {
                WorkoutPlanExercise exercise = new WorkoutPlanExercise(e);
                for (Map.Entry<Integer, HashMap<Integer, Integer>> entry : e.getReps().entrySet()) {
                    for(int r : entry.getValue().values()){
                        exercise.getTargetReps().add(r);
                    }
                }

                exercises.add(exercise);
            }
        }else{
            delete.setVisibility(View.INVISIBLE);
        }


        //initialise the recycler view to show all the exercises from that workout
        recyclerView = findViewById(R.id.rcCreateWorkout);
        recyclerView.setHasFixedSize(true);
        wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);
    }

    /**
     * Determine if the workout plan is being updated or not
     * @return  boolean
     */
    public boolean isUpdating(){
        return !workoutPlan.getId().equals("");
    }

    /**
     * When the screen is resumed, which occurs when an exercise is added.
     */
    @Override
    protected void onResume() {

        //Update the recycler view to reflect the changes
        wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);

        super.onResume();
    }

    /**
     * Save the workout plan to the database
     * @param view  the view of the application
     */
    public void saveWorkoutPlan(View view){
        workoutPlan.setName(((EditText)findViewById(R.id.etWorkoutPlanName)).getText().toString());
        workoutPlan.setDesc(((EditText)findViewById(R.id.etWorkoutPlanDescription)).getText().toString());


        if(!isUpdating()) {
            //create a new entry if not updating
            Map<String, Object> workout = new HashMap<>();
            workout.put("user", User.activeUser.getId());
            workout.put("name", workoutPlan.getName());
            workout.put("description", workoutPlan.getDesc());

            for (WorkoutPlanExercise e : exercises) {
                Exercise exercise = new Exercise(e.getExerciseTemplate());

                for (RepLine repLine : e.getRepLines()) {
                    int rep = 0;
                    if(!repLine.getReps().getText().toString().isEmpty())
                        rep = Integer.parseInt(repLine.getReps().getText().toString());
                    HashMap<Integer, Integer> val = new HashMap<>();
                    val.put(0, rep);
                    exercise.getReps().put(exercise.getReps().size(), val);
                }

                workoutPlan.getExercises().add(exercise);
            }

            User.activeUser.getWorkoutList().add(workoutPlan);

            // Add a new document with a generated ID
            db.collection("workout_plans")
                    .add(workout)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        workoutPlan.setId(documentReference.getId());

//                        System.out.println("Should exist: " + workoutPlan.getExercises().size());
                        for (Exercise e : workoutPlan.getExercises()) {


                            Map<String, Object> exercises = new HashMap<>();
                            exercises.put("exercise_template_id", e.getId());
                            exercises.put("workout_plan_id", documentReference.getId());

                            db.collection("plan_exercises")
                                    .add(exercises)
                                    .addOnSuccessListener(documentReference12 -> {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference12.getId());

                                        for (HashMap<Integer, Integer> reps : e.getReps().values()) {
                                            for(int r : reps.values()){
                                                Map<String, Object> exerciseReps = new HashMap<>();
                                                exerciseReps.put("exercise_plan_id", documentReference12.getId());
                                                exerciseReps.put("reps", r);

                                                db.collection("exercise_plan_sets")
                                                        .add(exerciseReps)
                                                        .addOnSuccessListener(documentReference121 -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference121.getId()))
                                                        .addOnFailureListener(e13 -> Log.w(TAG, "Error adding document", e13));
                                            }
                                            }
                                    })
                                    .addOnFailureListener(e14 -> Log.w(TAG, "Error adding document", e14));
                        }

                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
            Toast.makeText(getApplicationContext(),workoutPlan.getName()+" has been created",Toast.LENGTH_SHORT).show();

        }else{
            //update the current entry in the database
            workoutPlan.setExercises(new ArrayList<>());

            //Iterate through all exercises to be saved
            for (WorkoutPlanExercise e : exercises) {
                Exercise exercise = new Exercise(e.getExerciseTemplate());

                for (RepLine repLine : e.getRepLines()) {
                    HashMap<Integer, Integer> val = new HashMap<>();
                    int rep = Integer.parseInt(repLine.getReps().getText().toString());
                    val.put(0, rep);
                    exercise.getReps().put(exercise.getReps().size(), val);
                }
                workoutPlan.getExercises().add(exercise);
            }

            //Deleting the exercises in the current database for the workout
            CollectionReference itemsRef = db.collection("plan_exercises");
            Query query = itemsRef.whereEqualTo("workout_plan_id", workoutPlan.getId());
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        CollectionReference itemsRef1 = db.collection("exercise_plan_sets");
                        Query query1 = itemsRef1.whereEqualTo("exercise_plan_id", document.getId());
                        query1.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                for (DocumentSnapshot document1 : task1.getResult()) {
                                    itemsRef1.document(document1.getId()).delete();
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task1.getException());
                            }
                        });

                        itemsRef.document(document.getId()).delete();
                    }

                    //Once the previous entries are deleted, populate with new entries.
                    update();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });

            //Apparently doesn't work.
            //This adds all the exercises and reps back into the database.

            Toast.makeText(getApplicationContext(),workoutPlan.getName()+" has been updated",Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    /**
     * Update the database to reflect changes made
     */
    public void update(){
        for(Exercise exercise : workoutPlan.getExercises()){
            Map<String, Object> exerciseInfo = new HashMap<>();
            exerciseInfo.put("exercise_template_id", exercise.getId());
            exerciseInfo.put("workout_plan_id", workoutPlan.getId());

            //update the workout plan information
            db.collection("plan_exercises")
                    .add(exerciseInfo)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        //Update the exercises for the workout plan
                        for(Map.Entry<Integer, HashMap<Integer, Integer>> r : exercise.getReps().entrySet()){
                            for(int rep : r.getValue().values()){

                                Map<String, Object> exerciseReps = new HashMap<>();
                                exerciseReps.put("exercise_plan_id", documentReference.getId());
                                exerciseReps.put("reps", rep);

                                db.collection("exercise_plan_sets")
                                        .add(exerciseReps)
                                        .addOnSuccessListener(documentReference1 -> {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference1.getId());
                                        }).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                            }
                        }
                    }).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        }
    }

    /**
     * Change the view to add an exercise to the plan
     * @param view  the current view
     */
    public void addExerciseToPlan(View view){
        startActivity(new Intent(CreateWorkoutActivity.this, AddExerciseToPlanActivity.class));
    }

    /**
     * Delete a workout plan from the database
     * @param v the current view
     */
    public void onDeleteWorkoutPlan(View v){

        //remove the object from cached data
        User.getActiveUser().getWorkoutList().remove(workoutPlan);

        //Delete all references to the workout
        CollectionReference itemsRef = db.collection("plan_exercises");
        Query query = itemsRef.whereEqualTo("workout_plan_id", workoutPlan.getId());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {

                    //delete all exercises of the workout
                    CollectionReference itemsRef1 = db.collection("exercise_plan_sets");
                    Query query1 = itemsRef1.whereEqualTo("exercise_plan_id", document.getId());
                    query1.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (DocumentSnapshot document1 : task1.getResult()) {

                                //delete the database item
                                itemsRef1.document(document1.getId()).delete();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task1.getException());
                        }
                    });

                    itemsRef.document(document.getId()).delete();
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        //delete the actual workout itself
        db.collection("workout_plans").document(workoutPlan.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));

        finish();

        //inform the user
        Toast.makeText(getApplicationContext(),workoutPlan.getName()+" has been deleted",Toast.LENGTH_SHORT).show();

    }
}