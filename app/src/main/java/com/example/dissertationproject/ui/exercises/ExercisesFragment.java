package com.example.dissertationproject.ui.exercises;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dissertationproject.databinding.FragmentExercisesBinding;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class ExercisesFragment extends Fragment {

    private FragmentExercisesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExercisesViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ExercisesViewModel.class);

        binding = FragmentExercisesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textExercises;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        displayExercises();

        return root;

    }

    public void displayExercises(){
        String s = "";
        for(ExerciseTemplate exerciseTemplate : User.activeUser.getExerciseList()){
            s = s + exerciseTemplate.getCategory() + " | " + exerciseTemplate.getName() + " | "
                    + exerciseTemplate.getDesc() +" \n";
        }
        binding.tvExerciseList.setText(s);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}