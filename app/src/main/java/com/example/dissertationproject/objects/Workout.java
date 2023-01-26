package com.example.dissertationproject.objects;

import java.util.ArrayList;

public class Workout {
    private String name;
    private String date;
    private ArrayList<Exercise> exercises;

    public Workout(String name) {
        this.name = name;

        this.exercises = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}
