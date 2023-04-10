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
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.databinding.FragmentStatsBinding;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.example.dissertationproject.objects.WorkoutPlan;
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
    Spinner workoutSpinner;
    Spinner metricSpinner;

    TextView oneWeek;
    TextView oneMonth;
    TextView sixMonths;



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

        lineChart = view.findViewById(R.id.chart);
        radarChart = view.findViewById(R.id.rchart);

        oneWeek = view.findViewById(R.id.txtPredictOneWeek);
        oneMonth = view.findViewById(R.id.txtPredictOneMonth);
        sixMonths = view.findViewById(R.id.txtPredictSixMonths);

        exerciseSpinner = view.findViewById(R.id.spnChartExercise);
        metricSpinner = view.findViewById(R.id.spnChartMetric);
        workoutSpinner = view.findViewById(R.id.spnChartWorkout);

        initExerciseSpinnerData();
        initMetricSpinnerData();
        initWorkoutSpinnerData();

        displayLineChart();

    }

    public void initWorkoutSpinnerData(){
        ArrayList<String> workoutplans = new ArrayList<>();

        for(WorkoutPlan workoutPlan : User.getActiveUser().getWorkoutList()){
            workoutplans.add(workoutPlan.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, workoutplans);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        workoutSpinner.setAdapter(adapter);
        workoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                displayRadarChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initMetricSpinnerData(){
        ArrayList<String> metrics = new ArrayList<>();

        metrics.add("Max Weight");
        metrics.add("Average Weight");
        metrics.add("Volume");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, metrics);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        metricSpinner.setAdapter(adapter);
        metricSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                displayLineChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void displayRadarChart(){
        ArrayList<RadarEntry> radarEntries = new ArrayList<>();


        HashMap<String, Integer> categoryCount = new HashMap<>();
        for(Workout workout : User.getActiveUser().getWorkoutLog()){
//            if(workout.getName().equals(workoutSpinner.getSelectedItem().toString())){
                for(Exercise exercise : workout.getExercises()){
                    String category = exercise.getTemplate().getCategory();
                    int volume = 0;
                    for(Map.Entry<Integer, HashMap<Integer, Integer>> rep : exercise.getReps().entrySet()){
                        for(Map.Entry<Integer, Integer> r : rep.getValue().entrySet()){
                            volume = volume + (r.getKey() * r.getValue());
//                            volume = volume + 1;
                        }
                    }
                    if(!categoryCount.containsKey(category)){
                        categoryCount.put(category, volume);
                    }else categoryCount.put(category, categoryCount.get(category) + volume);

                }
//            }
        }

        //Setting default entries.
//        radarEntries.add(new RadarEntry(0, 0));
//        radarEntries.add(new RadarEntry(0, 1));
//        radarEntries.add(new RadarEntry(0, 2));
//        radarEntries.add(new RadarEntry(0, 3));
//        radarEntries.add(new RadarEntry(0, 4));
//        radarEntries.add(new RadarEntry(0, 5));
//        radarEntries.add(new RadarEntry(0, 6));

        String[] cats = {"Chest", "Back", "Shoulders", "Triceps", "Biceps", "Quads", "Hamstrings"};

        for(int i = 0; i <= 6; i++){
            boolean added = false;
            for(Map.Entry<String, Integer> entry : categoryCount.entrySet()){
                if(entry.getKey().equals(cats[i])){
                    radarEntries.add(new RadarEntry(entry.getValue(), i));
                    added = true;
                }
            }
            if(!added){
                radarEntries.add(new RadarEntry(0, i));
            }
        }

//        //TODO: Replace with a map or something
//        System.out.println(categoryCount.entrySet());
//        for(Map.Entry<String, Integer> entry : categoryCount.entrySet()){
////            if(entry.getKey().equals("Chest")){
////                radarEntries.add(new RadarEntry(entry.getValue(), 0));
////            }
////            if(entry.getKey().equals("Back")){
////                radarEntries.add(new RadarEntry(entry.getValue(), 1));
////            }
////            if(entry.getKey().equals("Shoulders")){
////                radarEntries.add(new RadarEntry(entry.getValue(), 2));
////            }
//            if(entry.getKey().equals("Biceps")){
//                radarEntries.add(new RadarEntry(entry.getValue(), 4));
//            }
////            if(entry.getKey().equals("Triceps")){
////                radarEntries.add(new RadarEntry(entry.getValue(),3));
////            }
////            if(entry.getKey().equals("Quads")){
////                System.out.println("Here!");
//////                radarEntries.remove(new RadarEntry(0, 5));
////                radarEntries.add(new RadarEntry(entry.getValue(), 5));
////            }
////            if(entry.getKey().equals("Hamstrings")){
////                radarEntries.add(new RadarEntry(entry.getValue(), 6));
////            }
//        }

        System.out.println(radarEntries);


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
    }

    public void displayLineChart(){
        String metric = metricSpinner.getSelectedItem().toString();
        if(metric.equals("Max Weight")){
            displayMaxWeightChart();
        } else if (metric.equals("Volume")) {
            displayVolumeChart();
        } else if (metric.equals("Average Weight")) {
            
        }
    }

    public void initExerciseSpinnerData(){
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
    }



    public void displayMaxWeightChart(){
        ArrayList<Long> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();

        if(exerciseSpinner.getSelectedItem() != null) {
            ExerciseTemplate exerciseTemplate = ExerciseTemplate.getFromName(exerciseSpinner.getSelectedItem().toString());

            List<Entry> entries = new ArrayList<>();

            //Calculate the highest weight lifted
            for (Workout workout : User.getActiveUser().getWorkoutLog()) {
                int maxWeight = -1;
                for (Exercise exercise : workout.getExercises()) {
                    if (exercise.getTemplate().equals(exerciseTemplate)) {
                        for (Map.Entry<Integer, HashMap<Integer, Integer>> rep : exercise.getReps().entrySet()) {
                            for (Map.Entry<Integer, Integer> r : rep.getValue().entrySet()) {
                                if (rep.getKey() > maxWeight) {
                                    maxWeight = r.getKey();
                                }
                            }
                        }
                    }
                }

                if (maxWeight >= 0) {
                    entries.add(new Entry(TimeUnit.MILLISECONDS.toDays(workout.getEndTime()), maxWeight));
                    x.add(workout.getEndTime());
                    y.add((double) maxWeight);
                }

            }


            lineChart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.invalidate(); // refresh

            calculatePredictions(x, y);

        }
    }

    public void calculatePredictions(ArrayList<Long> x, ArrayList<Double> y){
        LinearRegression linearRegression = new LinearRegression();
        linearRegression.fit(x, y);

        double oneWeekRounded = Math.round(linearRegression.predict(Utils.timeInOneWeek()) * 100.0) / 100.0;
        double oneMonthRounded = Math.round(linearRegression.predict(Utils.timeInOneMonth()) * 100.0) / 100.0;
        double sixMonthsRounded = Math.round(linearRegression.predict(Utils.timeInSixMonths()) * 100.0) / 100.0;

        oneWeek.setText("One week: ~ "+ oneWeekRounded +"kg");
        oneMonth.setText("One Month: ~ "+ oneMonthRounded + "kg");
        sixMonths.setText("Six Months: ~ "+ sixMonthsRounded + "kg");

        System.out.println("Calculating predictions");
    }

    public void displayVolumeChart(){
        if(exerciseSpinner.getSelectedItem() != null) {
            ExerciseTemplate exerciseTemplate = ExerciseTemplate.getFromName(exerciseSpinner.getSelectedItem().toString());

            List<Entry> entries = new ArrayList<>();

            //Calculate the highest weight lifted
            for (Workout workout : User.getActiveUser().getWorkoutLog()) {
                int volume = 0;
                for (Exercise exercise : workout.getExercises()) {
                    if (exercise.getTemplate().equals(exerciseTemplate)) {
                        for (Map.Entry<Integer, HashMap<Integer, Integer>> rep : exercise.getReps().entrySet()) {
                            for (Map.Entry<Integer, Integer> r : rep.getValue().entrySet()) {

                                volume = volume + (r.getKey() * r.getValue());
                            }
                        }
                    }
                }
                entries.add(new Entry(TimeUnit.MILLISECONDS.toDays(workout.getEndTime()), volume));
            }


            lineChart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.invalidate(); // refresh
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}