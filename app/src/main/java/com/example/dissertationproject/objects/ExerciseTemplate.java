package com.example.dissertationproject.objects;

public class ExerciseTemplate {
    private String id;
    private String name;
    private String desc;
    private String category;

    public ExerciseTemplate(String id, String name, String desc, String category){
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
