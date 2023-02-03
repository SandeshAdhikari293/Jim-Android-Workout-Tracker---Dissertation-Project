package com.example.dissertationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ActiveWorkoutActivity extends AppCompatActivity {

    public static WorkoutPlan plan;
    Workout workout;
    RecyclerView recyclerView;
    public static ArrayList<WorkoutPlanExercise> exercises;

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

//        LinearLayout linearLayout = findViewById();

//        for(Exercise e : plan.getExercises()){
//            WorkoutPlanExercise exercise = new WorkoutPlanExercise(e.getTemplate());
//            for(int reps : e.getReps()){
//
////                LinearLayout hor = new LinearLayout(this);
////                hor.setOrientation(LinearLayout.HORIZONTAL);
////
////                holder.linearLayout.addView(hor);
////
////                TextView txt = new TextView(view.getContext());
////                txt.setText((model.getRepLines().size() + 1) +") ");
////
////                EditText et = new EditText(view.getContext());
////                et.setHint("Enter the target reps");
////
////                Button rm = new Button(view.getContext());
////                rm.setText("-");
////
////                RepLine rl = new RepLine(txt, et, rm);
////                model.getRepLines().add(rl);
////
////                rm.setOnClickListener(view1 -> {
////
////                    holder.linearLayout.removeView(hor);
////
////                    model.getRepLines().remove(rl);
////                });
////
////
////                hor.addView(txt);
////                hor.addView(et);
////                hor.addView(rm);
//
////                exercise.getRepLines().add(new RepLine());
//            }
//
//
////            exercises.add(exercise);
//        }

//        WorkoutPlan workoutPlan = new WorkoutPlan("abc", User.activeUser, "abc", "abc");
//
//        exercises.add(new WorkoutPlanExercise(workoutPlan, User.activeUser.getExerciseList().get(0)));
//        exercises.add(new WorkoutPlanExercise(workoutPlan, User.activeUser.getExerciseList().get(1)));


        CreateWorkoutPlanAdapter wpAdapter = new CreateWorkoutPlanAdapter(this, exercises);
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);
    }

    public void saveActiveWorkout(View v){
        finish();
    }



}