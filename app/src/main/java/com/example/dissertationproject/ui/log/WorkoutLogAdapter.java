package com.example.dissertationproject.ui.log;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.ActiveWorkoutActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.Workout;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<Workout> workouts;

	// Constructor
	public WorkoutLogAdapter(Context context,ArrayList<Workout> workouts) {
		this.context = context;
		this.workouts = workouts;
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
		Workout model = workouts.get(position);
		holder.planName.setText(model.getName());
//		holder.planDesc.setText("" + model.get);
//		holder.courseIV.setImageResource(model.getCourse_image());

		for(Exercise exercise : model.getExercises()){
			TextView textView = new TextView(context);
			textView.setText(exercise.getTemplate().getName());
			holder.linearLayout.addView(textView);

			int set = 1;
			for(Map.Entry<Integer, Integer> rep : exercise.getReps().entrySet()){

				TextView reps = new TextView(context);
				reps.setText(set+") "+rep.getKey()+" x "+ rep.getValue());
				set++;

				holder.linearLayout.addView(reps);
			}
		}
	}


	@Override
	public int getItemCount() {
		// this method is used for showing number of card items in recycler view
		return workouts.size();
	}


	// View holder class for initializing of your views such as TextView and Imageview
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView planName;
		private final TextView planDesc;

		private final LinearLayout linearLayout;

		private final FloatingActionButton playWorkout;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			planName = itemView.findViewById(R.id.idTVCourseName);
			planDesc = itemView.findViewById(R.id.idTVCourseRating);
			linearLayout = itemView.findViewById(R.id.llExercisesOnPlan);
			playWorkout = itemView.findViewById(R.id.fbtnStartWorkout);

			playWorkout.setVisibility(View.INVISIBLE);
			playWorkout.setClickable(false);
		}
	}
}


