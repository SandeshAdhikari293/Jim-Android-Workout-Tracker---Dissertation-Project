package com.example.dissertationproject.objects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Workout {
    private String name;
    private String date;
    private long endTime;
    private long startTime;
    private ArrayList<Exercise> exercises;

    private String id;

    public Workout(String name) {
        this.name = name;

        this.exercises = new ArrayList<>();
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getDuration(){
        long time = getEndTime() - getStartTime();

        int hours = (int) (time / 3600000);

        int minutes = (int) ((time - (hours * 3600000)) / 60000);

//        int seconds = (int) ((time - (minutes * 60000)) / 1000);

        return String.format("%d hours, %d minutes", hours, minutes);

    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
