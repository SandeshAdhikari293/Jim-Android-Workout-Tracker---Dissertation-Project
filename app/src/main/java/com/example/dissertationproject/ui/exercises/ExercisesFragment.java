/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.exercises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.databinding.FragmentExercisesBinding;
import com.example.dissertationproject.objects.enums.Category;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;

import java.util.ArrayList;

public class ExercisesFragment extends Fragment {
    private FragmentExercisesBinding binding;
    RecyclerView recyclerView;

    /**
     * Method when the view is created
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExercisesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;

    }

    /**
     * initialise UI components
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcvExerciseList);

        //fill spinner with categories
        Spinner spinner = view.findViewById(R.id.spinnerSortCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, Category.categories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Update the list when the spinner is selected to filter for exercises
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateList(spinner.getSelectedItem().toString(), "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updateList("All Exercises", "");
            }
        });


        SearchView searchView = view.findViewById(R.id.searchbar_exercise);

        //When the user searches in the search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                updateList("All Exercises", s);
                return false;
            }
        });
        updateList("All Exercises", "");
    }

    /**
     * Filter the list of exercises shown based on filters and search
     * @param filter    the category filtered
     * @param search    the string the user searched
     */
    public void updateList(String filter, String search){
        recyclerView.setHasFixedSize(true);

        ArrayList<ExerciseTemplate> exercises = new ArrayList<>();

        if(!search.equals("")){
            //Filter by search
            for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()){
                if(Utils.findSimilarity(exerciseTemplate.getName(), search) > 0.3){
                    exercises.add(exerciseTemplate);
                }
            }
        }else{
            //Filter by category
            for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()){
                if(exerciseTemplate.getCategory().equals(filter) || filter.equals("All Exercises")){
                    exercises.add(exerciseTemplate);
                }
            }
        }

        ExerciseAdapter wpAdapter = new ExerciseAdapter(getContext(), exercises);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(wpAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}