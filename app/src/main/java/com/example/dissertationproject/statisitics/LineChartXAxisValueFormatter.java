/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.statisitics;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {

    /**
     * Formats a millisecond value into a string
     * @param value     float to be formatted
     * @return          a formatted date string
     */
    @Override
    public String getFormattedValue(float value) {
        // Convert from days back to milliseconds to format time to show to the user
        long timeMil = TimeUnit.DAYS.toMillis((long)value);

        Date timeMilliseconds = new Date(timeMil);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM yyyy");

        return dateTimeFormat.format(timeMilliseconds);
    }
}