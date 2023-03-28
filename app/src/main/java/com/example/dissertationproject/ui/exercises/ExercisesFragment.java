package com.example.dissertationproject.ui.exercises;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.databinding.FragmentExercisesBinding;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;

import java.util.ArrayList;


public class ExercisesFragment extends Fragment {

    private FragmentExercisesBinding binding;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExercisesViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ExercisesViewModel.class);

        binding = FragmentExercisesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textExercises;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcvExerciseList);

        String[] categories = {"All Exercises", "Chest", "Back", "Shoulders", "Biceps", "Triceps", "Forearms", "Quads",
                "Hamstrings", "Calves"};
        Spinner spinner = view.findViewById(R.id.spinnerSortCategory);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateList(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updateList("All Exercises");
            }
        });

        updateList("All Exercises");

    }

    public void updateList(String filter){
        recyclerView.setHasFixedSize(true);

        ArrayList<ExerciseTemplate> exercises = new ArrayList<>();

        for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()){
            if(exerciseTemplate.getCategory().equals(filter) || filter.equals("All Exercises")){
                exercises.add(exerciseTemplate);
            }
        }

        ExerciseAdapter wpAdapter = new ExerciseAdapter(getContext(), exercises);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(wpAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}