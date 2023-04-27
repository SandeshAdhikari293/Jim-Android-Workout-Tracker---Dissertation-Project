/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.workouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.databinding.FragmentWorkoutsBinding;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.ui.workouts.workoutPlan.WorkoutPlanAdapter;

import java.util.ArrayList;

public class WorkoutsFragment extends Fragment {

    private FragmentWorkoutsBinding binding;
    private RecyclerView recyclerView;
    private SearchView search;
    private static final double SEARCH_SIMILARITY = 0.3;

    /**
     * Initialise the binding
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the card view
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        return root;
    }

    /**
     * Initialise adapter and variables
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rcvWorkouts);
        recyclerView.setHasFixedSize(true);
        search = view.findViewById(R.id.searchbar_workout);

        //Initialise recycler view and adapter
        WorkoutPlanAdapter wpAdapter = new WorkoutPlanAdapter(getContext(), User.activeUser.getWorkoutList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);

        //Update the display when the user types in the search view
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                update(s);
                return false;
            }
        });

    }

    /**
     * Update the recycler view to show relevant information based on the search
     * @param search    the search query from the user
     */
    public void update(String search){
        ArrayList<WorkoutPlan> plan = new ArrayList<>();

        for(WorkoutPlan workoutPlan : User.getActiveUser().getWorkoutList()){
            //if similarity between the name of the workout plan and the search result are greater
            //than a similarity threshold, display that data
            if(Utils.findSimilarity(workoutPlan.getName(), search) > SEARCH_SIMILARITY || search.equals("")){
                plan.add(workoutPlan);

            }
        }

        //update the recyclerview
        WorkoutPlanAdapter wpAdapter = new WorkoutPlanAdapter(getContext(), plan);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        update("");
    }
}