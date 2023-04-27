/**
 * @author: Sandesh Adhikari
 */
package com.example.dissertationproject.objects.enums;

public enum Category {
    CHEST("Chest", 0), BACK("Back", 1),
    SHOULDERS("Shoulders", 2), BICEPS("Biceps", 3),
    TRICEPS("Triceps", 4), FOREARMS("Forearms", 5),
    QUADS("Quads", 6), HAMSTRINGS("Hamstrings", 7),
    CALVES("Calves", 8);

    private String name;
    private int position;

    Category(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Iterates each category and adds them to an array
     * @return string array of all items
     */
    public static String[] categories() {
        String[] categories = new String[Category.values().length];

        int count = 0;
        for(Category c : Category.values()){
            categories[count] = c.getName();;
            count++;
        }
        return categories;
    }

    /**
     * Iterates each category and adds them to an array, includes all categories used for the spinner
     * @return string array of all items
     */
    public static String[] categoriesForSpinner() {
        String[] categories = new String[Category.values().length + 1];

        categories[0] = "All Exercises";

        int count = 1;
        for(Category c : Category.values()){
            categories[count] = c.getName();;
            count++;
        }

        return categories;
    }

    /**
     * Gets the category object from its name
     * @param name  the name of the category
     * @return      the category object
     */
    public static Category enumFromName(String name){
        for(Category c : values()){
            if(c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }

}
