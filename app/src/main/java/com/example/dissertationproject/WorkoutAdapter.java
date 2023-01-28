package com.example.dissertationproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.objects.WorkoutPlan;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>{
    ArrayList<WorkoutPlan> workoutPlans;
    WorkoutViewHolder workoutViewHolder;
    public WorkoutAdapter(ArrayList<WorkoutPlan> workoutPlans){
        this.workoutPlans = workoutPlans;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_card, parent, false);
        workoutViewHolder = new WorkoutViewHolder(v);
        return workoutViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
//        System.out.println("posititon: "+ getItemCount());
        System.out.println(workoutPlans.get(position).getName());
        workoutViewHolder.text.setText(workoutPlans.get(position).getName());
//        workoutViewHolder.personAge.setText(persons.get(i).age);
//        workoutViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return workoutPlans.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView text;
        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
//            cv = itemView.findViewById(R.id.);
//            text = itemView.findViewById(R.id.tvCard);
        }
    }

}
