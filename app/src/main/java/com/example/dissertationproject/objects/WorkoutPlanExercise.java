/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.objects;

import java.util.ArrayList;

public class WorkoutPlanExercise{
    private ExerciseTemplate exerciseTemplate;
    private ArrayList<RepLine> repLines;
    private ArrayList<Integer> targetReps;

    /**
     * Constructor which creates the workout plan exercise object
     * @param exerciseTemplate  the exercise template it belongs to
     */
    public WorkoutPlanExercise(ExerciseTemplate exerciseTemplate){
        this.exerciseTemplate =  exerciseTemplate;
        this.targetReps = new ArrayList<>();
        this.repLines = new ArrayList<>();
    }

    public ExerciseTemplate getExerciseTemplate() {
        return exerciseTemplate;
    }

    public ArrayList<Integer> getTargetReps() {
        return targetReps;
    }

    public ArrayList<RepLine> getRepLines() {
        return repLines;
    }

}
