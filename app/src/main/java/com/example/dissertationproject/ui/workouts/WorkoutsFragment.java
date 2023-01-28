package com.example.dissertationproject.ui.workouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.cards.CourseAdapter;
import com.example.dissertationproject.cards.CourseModel;
import com.example.dissertationproject.R;
import com.example.dissertationproject.databinding.FragmentWorkoutsBinding;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.example.dissertationproject.workoutPlan.WorkoutPlanAdapter;

import java.util.ArrayList;

public class WorkoutsFragment extends Fragment {

    private FragmentWorkoutsBinding binding;
    RecyclerView recyclerView;

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

//        ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
//        courseModelArrayList.add(new CourseModel("DSA in Java", 4, R.drawable.ic_home_black_24dp));
//        courseModelArrayList.add(new CourseModel("Java Course", 3, R.drawable.ic_home_black_24dp));
//        courseModelArrayList.add(new CourseModel("C++ Course", 4, R.drawable.ic_home_black_24dp));
//        courseModelArrayList.add(new CourseModel("DSA in C++", 4, R.drawable.ic_home_black_24dp));
//        courseModelArrayList.add(new CourseModel("Kotlin for Android", 4, R.drawable.ic_home_black_24dp));
//        courseModelArrayList.add(new CourseModel("Java for Android", 4, R.drawable.ic_home_black_24dp));
//        courseModelArrayList.add(new CourseModel("HTML and CSS", 4, R.drawable.ic_home_black_24dp));

        // we are initializing our adapter class and passing our arraylist to it.
//        CourseAdapter courseAdapter = new CourseAdapter(getContext(), courseModelArrayList);

        WorkoutPlan workoutPlan = new WorkoutPlan("abc", User.activeUser, "abc", "abc");

        ArrayList<WorkoutPlanExercise> exercises = new ArrayList<>();
        exercises.add(new WorkoutPlanExercise(workoutPlan, User.activeUser.getExerciseList().get(0)));
        exercises.add(new WorkoutPlanExercise(workoutPlan, User.activeUser.getExerciseList().get(1)));
        WorkoutPlanAdapter wpAdapter = new WorkoutPlanAdapter(getContext(), exercises);


        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wpAdapter);

//        ArrayList<WorkoutPlan> workoutPlans = new ArrayList<>();
//        WorkoutPlan plan = new WorkoutPlan("abc", User.activeUser, "Test", "Test");
//        WorkoutPlan plan1 = new WorkoutPlan("1", User.activeUser, "Test1", "Test1");
//
//        workoutPlans.add(plan);
//        workoutPlans.add(plan1);
//
//        LinearLayoutManager llm = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(llm);
//
//        WorkoutAdapter adapter = new WorkoutAdapter(workoutPlans);
//        recyclerView.setAdapter(adapter);
//        System.out.println("Adapter attached");

    }

    @Override
    public void onStart() {
        super.onStart();
//        EditText editText = new EditText(getContext());
//        editText.setText("Hello");
//
//        recyclerView.addView(editText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}