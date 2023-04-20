/**
 * @author: Sandesh Adhikari
 */

package com.example.dissertationproject.objects;

import java.util.HashMap;

public class Exercise extends ExerciseTemplate{
    private HashMap<Integer, HashMap<Integer, Integer>> reps;

    /**
     * Constructor which creates an exercise, which is a child of ExerciseTemplate
     * @param ExerciseTemplate the exercise template parent
     */
    public Exercise(ExerciseTemplate template){
        super(template.getId(), template.getName(), template.getDesc(), template.getCategory());
        setReps(new HashMap<>());
    }

    /**
     * Get the rep hashmap
     * @return HashMap with rep data
     */
    public HashMap<Integer, HashMap<Integer, Integer>> getReps() {
        return reps;
    }

    /**
     * Set the rep hashmap
     * @param reps the reps hashmap to set the data
     */
    public void setReps(HashMap<Integer, HashMap<Integer, Integer>> reps) {
        this.reps = reps;
    }
}
