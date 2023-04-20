/**
 * @author: Sandesh Adhikari
 */
package com.example.dissertationproject.objects;

public enum Category {
    Chest, BACK, SHOULDERS, BICEPS, TRICEPS, FOREARMS, QUADS, HAMSTRINGS, CALVES;

    /**
     * Iterates each category, capitalising the first letter
     * @return Camel cased string array
     */
    public static String[] categories() {
        String[] categories = new String[Category.values().length];

        int count = 0;
        for(Category c : Category.values()){
            String camelCase = c.toString().substring(0, 1)+ c.toString().substring(1).toLowerCase();
            categories[count] = camelCase;
            count++;
        }
        return categories;
    }
}
