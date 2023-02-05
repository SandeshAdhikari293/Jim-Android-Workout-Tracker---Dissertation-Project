package com.example.dissertationproject.objects;

import java.util.HashMap;

public class Exercise {
    private ExerciseTemplate template;
    private HashMap<Integer, HashMap<Integer, Integer>> reps;

    public Exercise(ExerciseTemplate template){
        this.template = template;
        setReps(new HashMap<>());
    }

    public ExerciseTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ExerciseTemplate template) {
        this.template = template;
    }


    public HashMap<Integer, HashMap<Integer, Integer>> getReps() {
        return reps;
    }

    public void setReps(HashMap<Integer, HashMap<Integer, Integer>> reps) {
        this.reps = reps;
    }
}
