package com.example.dissertationproject.ui.log;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.databinding.FragmentLogBinding;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.workoutPlan.WorkoutPlanAdapter;

public class LogFragment extends Fragment {

    private FragmentLogBinding binding;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LogViewModel homeViewModel =
                new ViewModelProvider(this).get(LogViewModel.class);

        binding = FragmentLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        getActivity().getWindow().setStatusBarColor(Color.parseColor("#0B173B"));
        getActivity().getWindow().setNavigationBarColor(Color.parseColor("#0B173B"));

        final TextView textView = binding.textDashboard;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rcvWorkoutLog);
        recyclerView.setHasFixedSize(true);

        WorkoutLogAdapter wpAdapter = new WorkoutLogAdapter(getContext(), User.activeUser.getWorkoutLog());


        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layout manager and adapter to our recycler view.
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(wpAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}