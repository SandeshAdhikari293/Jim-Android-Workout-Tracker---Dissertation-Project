package com.example.dissertationproject.objects;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class User {
    public static User activeUser = null;
    private String id;
    private String name;
    private String email;
    private ArrayList<ExerciseTemplate> exerciseList;
    private ArrayList<WorkoutPlan> workoutList;

    private boolean admin;
    private boolean activated;
    private ArrayList<Workout> workoutLog;

    public static ArrayList<User> users = new ArrayList<>();


    public User(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
        setAdmin(false);
        setActivated(true);
        this.exerciseList = new ArrayList<>();
        this.workoutList = new ArrayList<>();
        this.workoutLog = new ArrayList<>();
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

    public ArrayList<Workout> getWorkoutLog() {
        return workoutLog;
    }

    public void setWorkoutLog(ArrayList<Workout> workoutLog) {
        this.workoutLog = workoutLog;
    }

    public void setActiveUser() {
        User.activeUser = this;
    }

    public static User getActiveUser() {
        return activeUser;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isActivated() {
        return activated;
    }

    public static User getUserFromID(String id){
        for(User u : users){
            if(u.getId().equals(id)){
                return u;
            }
        }
        return null;
    }

}
