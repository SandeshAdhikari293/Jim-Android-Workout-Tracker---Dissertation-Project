/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.objects.enums;

import java.util.ArrayList;

public enum Metric {
    MAX_WEIGHT("Max weight"), AVG_WEIGHT("Average weight"), VOLUME("Volume");

    private String name;
    Metric(String name) {
    }

    /**
     * Iterate over the enums and create an array list containing them in their formatted versions
     * @return
     */
    public static ArrayList<String> getList() {
        ArrayList<String> metrics = new ArrayList<>();

        int count = 0;
        for(Metric m : Metric.values()){
            String camelCase = m.toString().substring(0, 1)+ m.toString().substring(1).toLowerCase();
            camelCase = camelCase.replaceAll("_", " ");
            metrics.add(camelCase);
            count++;
        }
        return metrics;
    }
}
