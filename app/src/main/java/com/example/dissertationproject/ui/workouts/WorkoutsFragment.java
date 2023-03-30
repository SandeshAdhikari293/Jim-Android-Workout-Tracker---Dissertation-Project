package com.example.dissertationproject.ui.workouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.CreateWorkoutActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.databinding.FragmentWorkoutsBinding;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.workoutPlan.WorkoutPlanAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsFragment extends Fragment {

    private FragmentWorkoutsBinding binding;
    RecyclerView recyclerView;
    SearchView search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkoutsViewModel workoutsViewModel =
                new ViewModelProvider(this).get(WorkoutsViewModel.class);

        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);
//        View view =  inflater.inflate(R.layout.fragment_workouts, container, false);

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rcvWorkouts);
        recyclerView.setHasFixedSize(true);
        search = view.findViewById(R.id.searchbar_workout);

        WorkoutPlanAdapter wpAdapter = new WorkoutPlanAdapter(getContext(), User.activeUser.getWorkoutList());


        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);


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

    public void update(String search){

        ArrayList<WorkoutPlan> plan = new ArrayList<>();

        for(WorkoutPlan workoutPlan : User.getActiveUser().getWorkoutList()){
            if(Utils.findSimilarity(workoutPlan.getName(), search) > 0.3 || search.equals("")){
                plan.add(workoutPlan);

            }
        }

        WorkoutPlanAdapter wpAdapter = new WorkoutPlanAdapter(getContext(), plan);


        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}