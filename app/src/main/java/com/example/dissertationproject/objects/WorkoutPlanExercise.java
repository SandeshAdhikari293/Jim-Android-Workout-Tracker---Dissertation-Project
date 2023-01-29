package com.example.dissertationproject.objects;

import java.util.ArrayList;

public class WorkoutPlanExercise{

    private ExerciseTemplate exerciseTemplate;
    private WorkoutPlan workoutPlan;

    private ArrayList<RepLine> repLines;

    private ArrayList<Integer> targetReps;

    public WorkoutPlanExercise(WorkoutPlan plan, ExerciseTemplate exerciseTemplate){
        this.workoutPlan = plan;
        this.exerciseTemplate =  exerciseTemplate;
        this.targetReps = new ArrayList<>();
        this.repLines = new ArrayList<>();
    }

    public WorkoutPlanExercise(ExerciseTemplate exerciseTemplate){
        this.exerciseTemplate =  exerciseTemplate;
        this.targetReps = new ArrayList<>();
        this.repLines = new ArrayList<>();
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public ExerciseTemplate getExerciseTemplate() {
        return exerciseTemplate;
    }

    public void setExerciseTemplate(ExerciseTemplate exerciseTemplate) {
        this.exerciseTemplate = exerciseTemplate;
    }

    public ArrayList<Integer> getTargetReps() {
        return targetReps;
    }

    public void setTargetReps(ArrayList<Integer> targetReps) {
        this.targetReps = targetReps;
    }


    public ArrayList<RepLine> getRepLines() {
        return repLines;
    }

    public void setRepLines(ArrayList<RepLine> repLines) {
        this.repLines = repLines;
    }
}
