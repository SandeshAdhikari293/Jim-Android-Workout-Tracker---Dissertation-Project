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
import android.widget.Button;
import android.widget.EditText;

import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.RepLine;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.example.dissertationproject.workoutPlan.CreateWorkoutPlanAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.checkerframework.checker.units.qual.A;

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

    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        exercises.clear();
        setContentView(R.layout.activity_create_workout);

        delete = findViewById(R.id.btnDeleteWorkoutPlan);

        workoutPlan = new WorkoutPlan("", User.activeUser, "Unavailable", "Unavailable");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("workoutPlanID");
            workoutPlan = WorkoutPlan.getWorkoutPlanFromID(id);
        }

        if(isUpdating()){
            exercises.clear();
            System.out.println("updating");
            ((EditText)findViewById(R.id.etWorkoutPlanName)).setText(workoutPlan.getName());
            ((EditText)findViewById(R.id.etWorkoutPlanDescription)).setText(workoutPlan.getDesc());

            for(Exercise e : workoutPlan.getExercises()) {
                WorkoutPlanExercise exercise = new WorkoutPlanExercise(e.getTemplate());
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



        recyclerView = findViewById(R.id.rcCreateWorkout);
        recyclerView.setHasFixedSize(true);

        wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
                // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
       linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layout manager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);

    }

    public boolean isUpdating(){
        return !workoutPlan.getId().equals("");
    }

    @Override
    protected void onResume() {


//        CreateWorkoutPlanAdapter wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layout manager and adapter to our recycler view.
        wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);

//        System.out.println();
        System.out.println("size: " + workoutPlan.getExercises().size());
        for(Exercise ep : workoutPlan.getExercises()){
            System.out.println(ep.getTemplate().getName());
            System.out.println(ep.getReps());
        }

        super.onResume();
    }

    public void saveWorkoutPlan(View view){
        workoutPlan.setName(((EditText)findViewById(R.id.etWorkoutPlanName)).getText().toString());
        workoutPlan.setDesc(((EditText)findViewById(R.id.etWorkoutPlanDescription)).getText().toString());


        if(!isUpdating()) {
            Map<String, Object> workout = new HashMap<>();
            workout.put("user", User.activeUser.getId());
            workout.put("name", workoutPlan.getName());
            workout.put("description", workoutPlan.getDesc());

            for (WorkoutPlanExercise e : exercises) {
                Exercise exercise = new Exercise(e.getExerciseTemplate());

                for (RepLine repLine : e.getRepLines()) {
                    int rep = Integer.parseInt(repLine.getReps().getText().toString());
//                int weight = Integer.parseInt(repLine.getWeight().getText().toString());
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
                            exercises.put("exercise_template_id", e.getTemplate().getId());
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
        }else{

            //TODO: delete all the previous exercises and add entirely new ones?
            // i think this would work, because the exercises are exclusively linked to this workout
            // and none others.

            workoutPlan.setExercises(new ArrayList<>());

            System.out.println("BEFORE:");
            System.out.println("size: " + workoutPlan.getExercises().size());
            for(Exercise ep : workoutPlan.getExercises()){
                System.out.println(ep.getTemplate().getName());
                System.out.println(ep.getReps());
            }


            for (WorkoutPlanExercise e : exercises) {
                Exercise exercise = new Exercise(e.getExerciseTemplate());

                for (RepLine repLine : e.getRepLines()) {
                    HashMap<Integer, Integer> val = new HashMap<>();
                    System.out.println("exercise: "+e.getExerciseTemplate().getName());

                    int rep = Integer.parseInt(repLine.getReps().getText().toString());
                    val.put(0, rep);
                    exercise.getReps().put(exercise.getReps().size(), val);

                    System.out.println("rep: "+ rep);
                }
                workoutPlan.getExercises().add(exercise);
            }

            System.out.println("size: " + workoutPlan.getExercises().size());
            for(Exercise ep : workoutPlan.getExercises()){
                System.out.println(ep.getTemplate().getName());
                System.out.println(ep.getReps());
            }

            //Deleting the exercises
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

                    update();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });

            //Apparently doesn't work.
            //This adds all the exercises and reps back into the database.

        }

        finish();
    }

    public void update(){

        for(Exercise exercise : workoutPlan.getExercises()){
            System.out.println("BNne: " + exercise.getTemplate().getName() + " | " + exercise.getReps());
            Map<String, Object> exerciseInfo = new HashMap<>();
            exerciseInfo.put("exercise_template_id", exercise.getTemplate().getId());
            exerciseInfo.put("workout_plan_id", workoutPlan.getId());

            db.collection("plan_exercises")
                    .add(exerciseInfo)
                    .addOnSuccessListener(documentReference -> {

                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());


                        for(Map.Entry<Integer, HashMap<Integer, Integer>> r : exercise.getReps().entrySet()){
                            for(int rep : r.getValue().values()){

                                Map<String, Object> exerciseReps = new HashMap<>();
                                exerciseReps.put("exercise_plan_id", documentReference.getId());
                                exerciseReps.put("reps", rep);

                                System.out.println("Saving: "+exercise.getTemplate().getName() + " | " + rep +" <-- "+ workoutPlan.getName());

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

    public void addExerciseToPlan(View view){
        startActivity(new Intent(CreateWorkoutActivity.this, AddExerciseToPlanActivity.class));
    }

    public void onDeleteWorkoutPlan(View v){

        User.getActiveUser().getWorkoutList().remove(workoutPlan);
        //Delete all references to it.
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
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });


        db.collection("workout_plans").document(workoutPlan.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));

        finish();
    }
}