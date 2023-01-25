package com.example.dissertationproject.objects;

import java.util.ArrayList;

public class User {
    public static User activeUser = null;
    private String id;
    private String name;
    private String email;
    private ArrayList<ExerciseTemplate> exerciseList;
    private ArrayList<WorkoutPlan> workoutList;

    public User(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;

        this.exerciseList = new ArrayList<>();
        this.workoutList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<ExerciseTemplate> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(ArrayList<ExerciseTemplate> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public ArrayList<WorkoutPlan> getWorkoutList() {
        return workoutList;
    }

    public void setWorkoutList(ArrayList<WorkoutPlan> workoutList) {
        this.workoutList = workoutList;
    }

    public void setActiveUser() {
        User.activeUser = this;
    }
}
