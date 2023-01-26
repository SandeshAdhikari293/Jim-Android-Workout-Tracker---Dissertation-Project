package com.example.dissertationproject.objects;

import java.util.HashMap;

public class Exercise {
    private ExerciseTemplate template;
    private HashMap<Integer, Integer> repSets;

    public Exercise(ExerciseTemplate template){

    }

    public ExerciseTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ExerciseTemplate template) {
        this.template = template;
    }

    public HashMap<Integer, Integer> getRepSets() {
        return repSets;
    }

    public void setRepSets(HashMap<Integer, Integer> repSets) {
        this.repSets = repSets;
    }
}
