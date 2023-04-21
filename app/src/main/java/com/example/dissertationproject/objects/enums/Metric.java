/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.objects.enums;

import java.util.ArrayList;

public enum Metric {
    MAX_WEIGHT("Max weight"), AVG_WEIGHT("Average weight"), VOLUME("Volume");

    private String name;
    Metric(String name) {
        this.name = name;
    }

    /**
     * gets the name of the enum
     * @return  the name as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Iterate over the enums and create an array list containing them in their formatted versions
     * @return
     */
    public static ArrayList<String> getList() {
        ArrayList<String> metrics = new ArrayList<>();

        int count = 0;
        for(Metric m : Metric.values()){
            metrics.add(m.getName());
            count++;
        }
        return metrics;
    }
}
