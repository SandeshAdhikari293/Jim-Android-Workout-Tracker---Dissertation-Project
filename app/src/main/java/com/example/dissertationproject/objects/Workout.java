/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.objects;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Workout {
    private String name;
    private long endTime;
    private long startTime;
    private ArrayList<Exercise> exercises;
    private String id;

    /**
     * Constructor which creates the object
     * @param name  the name of the workout
     */
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

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return startTime;
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

    /**
     * Calculates the duration of the workout based on start and end time
     * @return  a String which tells the duration in hours and minutes
     */
    public String getDuration(){
        long time = getEndTime() - getStartTime();
        int hours = (int) (time / 3600000);
        int minutes = (int) ((time - (hours * 3600000)) / 60000);

        return String.format("%d hours, %d minutes", hours, minutes);

    }

    /**
     * The date which the workout was run calculated from milliseconds
     * @return  formatted String of the date
     */
    public String getDate(){
        long time = TimeUnit.DAYS.toMillis(TimeUnit.MILLISECONDS.toDays(getEndTime()));
        Date timeMilliseconds = new Date(time);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy");

        return dateTimeFormat.format(timeMilliseconds);
    }
}
