/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.log;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.databinding.FragmentLogBinding;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;

import java.util.ArrayList;
import java.util.Collections;

public class LogFragment extends Fragment {
    private FragmentLogBinding binding;
    private RecyclerView recyclerView;

    /**
     * V
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        getActivity().getWindow().setStatusBarColor(Color.parseColor("#0B173B"));
        getActivity().getWindow().setNavigationBarColor(Color.parseColor("#0B173B"));

        return root;
    }

    /**
     * Initialse recycler view and dataset
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rcvWorkoutLog);
        recyclerView.setHasFixedSize(true);

        ArrayList<Workout> reverse = (ArrayList<Workout>) User.getActiveUser().getWorkoutLog().clone();
        Collections.reverse(reverse);

        WorkoutLogAdapter wpAdapter = new WorkoutLogAdapter(getContext(), reverse);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(wpAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}