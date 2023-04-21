/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.objects;

import com.example.dissertationproject.objects.enums.Category;

public class ExerciseTemplate {
    private String id;
    private String name;
    private String desc;
    private Category category;

    /**
     * Create an Exercise Template object
     * @param id        the id of the exercise from the database
     * @param name      the name of the exercise
     * @param desc      the description set by the user
     * @param category  the category of muscle groups which it belongs to
     */
    public ExerciseTemplate(String id, String name, String desc, Category category){
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.category = category;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Gets the object from the name of the exercise template
     * @param name  the name of the exercise template
     * @return      the exercise template object
     */
    public static ExerciseTemplate getFromName(String name){
        for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()){
            if(exerciseTemplate.getName().equals(name)){
                return exerciseTemplate;
            }
        }
        return null;
    }

    /**
     * Gets the object from the id of the exercise template
     * @param id    the id of the exercise template
     * @return      the exercise template object
     */
    public static ExerciseTemplate getFromID(String id){
        for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()){
            if(exerciseTemplate.getId().equals(id)){
                return exerciseTemplate;
            }
        }
        return null;
    }
}
