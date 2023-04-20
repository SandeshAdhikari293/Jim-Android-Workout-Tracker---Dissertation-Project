/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.objects;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private String profileID;
    public static ArrayList<User> users = new ArrayList<>();

    /**
     * Constructor which is called when object is created
     * @param id        the id of the user from the database
     * @param name      the name of the user
     * @param email     the users email address which they registered with
     */
    public User(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
        this.exerciseList = new ArrayList<>();
        this.workoutList = new ArrayList<>();
        this.workoutLog = new ArrayList<>();

        setAdmin(false);
        setActivated(true);
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

    public ArrayList<ExerciseTemplate> getExerciseList() {
        return exerciseList;
    }


    public ArrayList<WorkoutPlan> getWorkoutList() {
        return workoutList;
    }

    public ArrayList<Workout> getWorkoutLog() {
        Collections.sort(workoutLog, Comparator.comparing(Workout::getEndTime));
        return workoutLog;
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

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getProfileID() {
        return profileID;
    }

    /**
     * Gets a user object from their id
     * @param id    the id of the user
     * @return      the user object
     */
    public static User getUserFromID(String id){
        for(User u : users){
            if(u.getId().equals(id)){
                return u;
            }
        }
        return null;
    }

}
