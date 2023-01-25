package com.example.dissertationproject.objects;

import java.util.ArrayList;

public class WorkoutPlan {
    String id;
    User user;
    String name;
    String desc;
    ArrayList exercises;

    public WorkoutPlan(String id, User user, String name, String desc){
        this.id = id;
        this.user = user;
        this.name = name;
        this.desc = desc;

        this.exercises = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList exercises) {
        this.exercises = exercises;
    }
}
