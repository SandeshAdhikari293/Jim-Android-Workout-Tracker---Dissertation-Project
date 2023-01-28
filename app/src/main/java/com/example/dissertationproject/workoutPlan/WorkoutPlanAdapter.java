package com.example.dissertationproject.workoutPlan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;

import java.util.ArrayList;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<WorkoutPlanAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<WorkoutPlanExercise> workoutPlanExercises;

	// Constructor
	public WorkoutPlanAdapter(Context context, ArrayList<WorkoutPlanExercise> workoutPlanExercises) {
		this.context = context;
		this.workoutPlanExercises = workoutPlanExercises;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// to inflate the layout for each item of recycler view.
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_card, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		WorkoutPlanExercise model = workoutPlanExercises.get(position);
		holder.courseNameTV.setText(model.getExerciseTemplate().getName());
		holder.courseRatingTV.setText("" + model.getWorkoutPlan().getName());
//		holder.courseIV.setImageResource(model.getCourse_image());
	}


	@Override
	public int getItemCount() {
		// this method is used for showing number of card items in recycler view
		return workoutPlanExercises.size();
	}


	// View holder class for initializing of your views such as TextView and Imageview
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final ImageView courseIV;
		private final TextView courseNameTV;
		private final TextView courseRatingTV;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			courseIV = itemView.findViewById(R.id.idIVCourseImage);
			courseNameTV = itemView.findViewById(R.id.idTVCourseName);
			courseRatingTV = itemView.findViewById(R.id.idTVCourseRating);
		}
	}
}

