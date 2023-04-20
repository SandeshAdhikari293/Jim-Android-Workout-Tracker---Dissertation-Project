/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.statisitics;

import com.example.dissertationproject.objects.Category;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class RadarChartXAxisValueFormatter extends IndexAxisValueFormatter {

    /**
     * Formats a float into a string
     * @param value     float to be formatted
     * @return          a string of the corresponding category
     */
    @Override
    public String getFormattedValue(float value) {
        return Category.categories()[0];
    }
}