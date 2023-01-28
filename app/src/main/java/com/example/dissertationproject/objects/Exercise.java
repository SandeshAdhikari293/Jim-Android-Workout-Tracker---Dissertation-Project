package com.example.dissertationproject.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Exercise {
    private ExerciseTemplate template;
    private ArrayList<Integer> reps;

    public Exercise(ExerciseTemplate template){

    }

    public ExerciseTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ExerciseTemplate template) {
        this.template = template;
    }

    public ArrayList<Integer> getReps() {
        return reps;
    }

    public void setReps(ArrayList<Integer> reps) {
        this.reps = reps;
    }
}
