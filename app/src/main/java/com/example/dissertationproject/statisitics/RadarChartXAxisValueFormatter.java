package com.example.dissertationproject.statisitics;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RadarChartXAxisValueFormatter extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        switch ((int) value){
            case 0:
                return "Chest";
            case 1:
                return "Back";
            case 2:
                return "Shoulders";
            case 3:
                return "Triceps";
            case 4:
                return "Biceps";
            case 5:
                return "Quads";
            case 6:
                return "Hamstrings";
            default:
                return "N/A";
        }
    }
}