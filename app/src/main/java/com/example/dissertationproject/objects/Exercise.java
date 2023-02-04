package com.example.dissertationproject.objects;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class Exercise {
    private ExerciseTemplate template;
    private HashMap<Integer, Integer> reps;

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


    public HashMap<Integer, Integer> getReps() {
        return reps;
    }

    public void setReps(HashMap<Integer, Integer> reps) {
        this.reps = reps;
    }
}
