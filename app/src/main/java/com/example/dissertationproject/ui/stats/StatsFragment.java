/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.stats;

import android.graphics.Color;
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

import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.databinding.FragmentStatsBinding;
import com.example.dissertationproject.objects.enums.Category;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.enums.Metric;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.example.dissertationproject.statisitics.LineChartXAxisValueFormatter;
import com.example.dissertationproject.statisitics.RadarChartXAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;
    private LineChart lineChart;
    private RadarChart radarChart;
    private Spinner exerciseSpinner;
    private Spinner workoutSpinner;
    private Spinner metricSpinner;
    private TextView oneWeek;
    private TextView oneMonth;
    private TextView sixMonths;
    private ArrayList<String> time;

    /**
     * View is created
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the stats view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        time = new ArrayList<>();

        time.add("One week");
        time.add("One month");
        time.add("Six months");
        time.add("All time");

        return root;
    }

    /**
     * Initialise UI components and data
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
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

    /**
     * Initialises the workout spinner data
     */
    private void initWorkoutSpinnerData(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, time);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        workoutSpinner.setAdapter(adapter);

        //Update the radar chart when a new item is selected
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

    /**
     * Initialise the metric spinner data
     */
    private void initMetricSpinnerData(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Metric.getList());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        metricSpinner.setAdapter(adapter);

        //update the line chart to display new information when spinner item is changed
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

    /**
     * Calculates the volume distribution of workouts in a particular time frame
     * @param timeFrame the time frame of exercises to sample
     * @return          a hashmap containing volume distribution between categories
     */
    private HashMap<Category, Double> calculateVolumeDistribution(long timeFrame){

        HashMap<Category, Double> volumeDistribution = new HashMap<>();
        for(Workout workout : User.getActiveUser().getWorkoutLog()){
            if(workout.getEndTime() > timeFrame){ //if the workout is within the timeframe
                for(Exercise exercise : workout.getExercises()){
                    Category category = exercise.getCategory();

                    //calculate the volume distribution for each muscle category for workouts within
                    // that timeframe.
                    double volume = 0;
                    for(Map.Entry<Integer, HashMap<Integer, Integer>> rep : exercise.getReps().entrySet()){
                        for(Map.Entry<Integer, Integer> r : rep.getValue().entrySet()){
                            volume = volume + (r.getKey() * r.getValue()); //add the volume together
                        }
                    }
                    //add the data to a hashmap, with the muscle group as key and volume as value
                    if(!volumeDistribution.containsKey(category)){
                        volumeDistribution.put(category, volume);
                    }else volumeDistribution.put(category, volumeDistribution.get(category) + volume);

                }
            }
        }
        return volumeDistribution;
    }

    /**
     * Display the radar chart information based upon data from the corresponding spinner
     */
    private void displayRadarChart(){
        long timeFrame = 0;

        //Select the desired time frame from the spinner item selected
        if(workoutSpinner.getSelectedItem().toString().equals(time.get(0))){
            timeFrame = Utils.timeLastOneWeek();
        }else if(workoutSpinner.getSelectedItem().toString().equals(time.get(1))){
            timeFrame = Utils.timeLastOneMonth();
        }else if(workoutSpinner.getSelectedItem().toString().equals(time.get(2))){
            timeFrame = Utils.timeLastSixMonths();
        }

        //iterate over each muscle group and populate an array with entries
        ArrayList<RadarEntry> radarEntries = new ArrayList<>();
        for(int i = 0; i <= 6; i++){
            boolean added = false;
            for(Map.Entry<Category, Double> entry : calculateVolumeDistribution(timeFrame).entrySet()){

                if(entry.getKey().getPosition() == i){
                    radarEntries.add(new RadarEntry(entry.getValue().floatValue(), i));
                    added = true;
                }
            }
            if(!added){
                radarEntries.add(new RadarEntry(0, i));
            }
        }

        //Initialise radar data set and object
        RadarDataSet radarDataSet = new RadarDataSet(radarEntries, "");
        RadarData radarData = new RadarData(radarDataSet);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setXOffset(0f);
        xAxis.setYOffset(0f);
        xAxis.setTextSize(8f);

        //format values
        xAxis.setValueFormatter(new RadarChartXAxisValueFormatter());

        radarChart.setData(radarData);
        radarDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        radarDataSet.setValueTextColor(Color.BLACK);
        radarDataSet.setValueTextSize(18f);
    }

    /**
     * Method which determines what metric to display on the line chart
     */
    public void displayLineChart(){
        String metric = metricSpinner.getSelectedItem().toString();

        if(metric.equals(Metric.MAX_WEIGHT.getName())){
            displayMaxWeightChart();
        } else if (metric.equals(Metric.VOLUME.getName())) {
            displayVolumeChart();
        } else if (metric.equals(Metric.AVG_WEIGHT.getName())) {
            
        }
    }

    /**
     * Initialises the spinner data for selecting different exercises
     */
    public void initExerciseSpinnerData(){
        ArrayList<String> exercises = new ArrayList<>();
        for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()){
            exercises.add(exerciseTemplate.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, exercises);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);

        //Display a different chart when the exercises
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


    /**
     * Displays the data for the max weight chart
     */
    public void displayMaxWeightChart(){
        ArrayList<Long> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();

        if(exerciseSpinner.getSelectedItem() != null) {
            ExerciseTemplate exerciseTemplate = ExerciseTemplate.getFromName(exerciseSpinner.getSelectedItem().toString());

            List<Entry> entries = new ArrayList<>();

            //Calculate the highest weight lifted
            //Iterate to find the max weight per workout per exercise
            for (Workout workout : User.getActiveUser().getWorkoutLog()) {
                int maxWeight = -1;
                for (Exercise exercise : workout.getExercises()) {
                    if (exercise.getId().equals(exerciseTemplate.getId())) {
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


            //Display the chart
            lineChart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.invalidate(); // refresh

            //Update predictions for this data set
            calculatePredictions(x, y);

        }
    }

    /**
     * Calculates and updates the predictions and dislpays them to user
     * @param x the x dataset
     * @param y the y dataset
     */
    public void calculatePredictions(ArrayList<Long> x, ArrayList<Double> y){
        LinearRegression linearRegression = new LinearRegression();
        linearRegression.fit(x, y);

        double oneWeekRounded = Math.round(linearRegression.predict(Utils.timeInOneWeek()) * 100.0) / 100.0;
        double oneMonthRounded = Math.round(linearRegression.predict(Utils.timeInOneMonth()) * 100.0) / 100.0;
        double sixMonthsRounded = Math.round(linearRegression.predict(Utils.timeInSixMonths()) * 100.0) / 100.0;

        oneWeek.setText("One week: ~ "+ oneWeekRounded +"kg");
        oneMonth.setText("One Month: ~ "+ oneMonthRounded + "kg");
        sixMonths.setText("Six Months: ~ "+ sixMonthsRounded + "kg");
    }

    /**
     * Displays the chart showing volume for each exercise
     */
    public void displayVolumeChart(){
        if(exerciseSpinner.getSelectedItem() != null) {
            ExerciseTemplate exerciseTemplate = ExerciseTemplate.getFromName(exerciseSpinner.getSelectedItem().toString());

            List<Entry> entries = new ArrayList<>();

            //Calculate the volume of each exercise per workout
            for (Workout workout : User.getActiveUser().getWorkoutLog()) {
                int volume = 0;
                for (Exercise exercise : workout.getExercises()) {
                    if (exercise.getId().equals(exerciseTemplate.getId())) {
                        for (Map.Entry<Integer, HashMap<Integer, Integer>> rep : exercise.getReps().entrySet()) {
                            for (Map.Entry<Integer, Integer> r : rep.getValue().entrySet()) {

                                volume = volume + (r.getKey() * r.getValue());
                            }
                        }
                    }
                }
                entries.add(new Entry(TimeUnit.MILLISECONDS.toDays(workout.getEndTime()), volume));
            }


            //Update the line chart
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