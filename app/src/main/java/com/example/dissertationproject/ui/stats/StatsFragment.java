package com.example.dissertationproject.ui.stats;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dissertationproject.LoginActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.databinding.FragmentStatsBinding;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.example.dissertationproject.statisitics.LineChartXAxisValueFormatter;
import com.example.dissertationproject.statisitics.RadarChartXAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;
    LineChart lineChart;

    RadarChart radarChart;
    Spinner exerciseSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatsViewModel dashboardViewModel =
                new ViewModelProvider(this).get(StatsViewModel.class);

        binding = FragmentStatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lineChart = (LineChart) view.findViewById(R.id.chart);
        radarChart = (RadarChart) view.findViewById(R.id.rchart);

        ArrayList<RadarEntry> radarEntries = new ArrayList<>();

//        radarEntries.add(new RadarEntry(0, 0.21f));
//        radarEntries.add(new RadarEntry(1, 0.12f));
//        radarEntries.add(new RadarEntry(2, 0.20f));
//        radarEntries.add(new RadarEntry(2, 0.52f));
//        radarEntries.add(new RadarEntry(3, 0.29f));
//        radarEntries.add(new RadarEntry(4, 0.62f));

        HashMap<String, Integer> categoryCount = new HashMap<>();
        for(Workout workout : User.getActiveUser().getWorkoutLog()){

            for(Exercise exercise : workout.getExercises()){
                String category = exercise.getTemplate().getCategory();
                int volume = 0;
                for(Map.Entry<Integer, Integer> rep : exercise.getReps().entrySet()){
                    volume = volume + (rep.getKey() * rep.getValue());
                }
                if(!categoryCount.containsKey(category)){
                    categoryCount.put(category, volume);
                }else categoryCount.put(category, categoryCount.get(category) + volume);

            }

        }

//        System.out.println(categoryCount);
        radarEntries.add(new RadarEntry(0, 0));
        radarEntries.add(new RadarEntry(0, 1));
        radarEntries.add(new RadarEntry(0, 2));
        radarEntries.add(new RadarEntry(0, 3));
        radarEntries.add(new RadarEntry(0, 4));
        radarEntries.add(new RadarEntry(0, 5));
        radarEntries.add(new RadarEntry(0, 6));

//        radarEntries.add(new RadarEntry(512, 0));
//        radarEntries.add(new RadarEntry(90, 1));
//        radarEntries.add(new RadarEntry(2, 2));


        for(Map.Entry<String, Integer> entry : categoryCount.entrySet()){

            if(entry.getKey().equals("Chest")){
                radarEntries.add(new RadarEntry(entry.getValue(), 0));
            }
            if(entry.getKey().equals("Back")){
                radarEntries.add(new RadarEntry(entry.getValue(), 1));
            }
            if(entry.getKey().equals("Shoulders")){
                radarEntries.add(new RadarEntry(entry.getValue(), 2));
            }
            if(entry.getKey().equals("Biceps")){
                radarEntries.add(new RadarEntry(entry.getValue(), 3));
            }
            if(entry.getKey().equals("Triceps")){
                radarEntries.add(new RadarEntry(entry.getValue(),4));
            }
            if(entry.getKey().equals("Quads")){
                radarEntries.add(new RadarEntry(entry.getValue(), 5));
            }
            if(entry.getKey().equals("Hamstrings")){
                radarEntries.add(new RadarEntry(entry.getValue(), 6));
            }
        }

        RadarDataSet radarDataSet = new RadarDataSet(radarEntries, "");
        RadarData radarData = new RadarData(radarDataSet);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setXOffset(0f);
        xAxis.setYOffset(0f);
        xAxis.setTextSize(8f);

        xAxis.setValueFormatter(new RadarChartXAxisValueFormatter());

        radarChart.setData(radarData);
        radarDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        radarDataSet.setValueTextColor(Color.BLACK);
        radarDataSet.setValueTextSize(18f);

        exerciseSpinner = (Spinner) view.findViewById(R.id.spnChartExercise);

        ArrayList<String> exercises = new ArrayList<>();

        for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()){
            exercises.add(exerciseTemplate.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, exercises);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        exerciseSpinner.setAdapter(adapter);
        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                displayLineChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        displayLineChart();

    }

    public void displayLineChart(){

        ExerciseTemplate exerciseTemplate = ExerciseTemplate.getFromName(exerciseSpinner.getSelectedItem().toString());

        List<Entry> entries = new ArrayList<>();

        //Calculate the highest weight lifted
        for(Workout workout : User.getActiveUser().getWorkoutLog()){
            int maxWeight = -1;
            for(Exercise exercise : workout.getExercises()){
                if(exercise.getTemplate().equals(exerciseTemplate)){
                    for(Map.Entry<Integer, Integer> rep : exercise.getReps().entrySet()){
                        if(rep.getKey() > maxWeight){
                            maxWeight = rep.getKey();
                        }
                    }
                }
            }

            if(maxWeight >= 0) {
                entries.add(new Entry(TimeUnit.MILLISECONDS.toDays(workout.getEndTime()), maxWeight));
            }

        }

//        String[] dataObjects = {"a", "b","c","d"};
//        List<Entry> entries = new ArrayList<>();
//        int x = 0;
//        for (String data : dataObjects) {
//            // turn your data into Entry objects
////            System.out.println(System.currentTimeMillis());
//
//            int val = x * 86400000;
//            entries.add(new Entry(TimeUnit.MILLISECONDS.toDays((long)System.currentTimeMillis() + val), 23));
//            x++;
//        }

        // in this example, a LineChart is initialized from xml

        lineChart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
//        dataSet.setColor();
//        dataSet.setValueTextColor(...); // styling, ...

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}