/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.workouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.RepLine;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ActiveWorkoutAdapter extends RecyclerView.Adapter<ActiveWorkoutAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<WorkoutPlanExercise> workoutPlanExercises;

	/**
	 * Constructor to initialise variables
	 * @param context				the application context
	 * @param workoutPlanExercises	the dataset
	 */
	public ActiveWorkoutAdapter(Context context, ArrayList<WorkoutPlanExercise> workoutPlanExercises) {
		this.context = context;
		this.workoutPlanExercises = workoutPlanExercises;
	}

	/**
	 * Inflate the layout for each item in the recycler view
	 * @param parent The ViewGroup into which the new View will be added after it is bound to
	 *               an adapter position.
	 * @param viewType The view type of the new View.
	 *
	 * @return	inflated view
	 */
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_workout_card, parent, false);

		return new ViewHolder(view);
	}

	/**
	 * Manipulate data for each card
	 * @param holder The ViewHolder which should be updated to represent the contents of the
	 *        item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 */
	@SuppressLint("ResourceAsColor")
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		WorkoutPlanExercise model = workoutPlanExercises.get(position);
		holder.exerciseNameTV.setText(model.getExerciseTemplate().getName());
		holder.exerciseDescTV.setText(model.getExerciseTemplate().getDesc());

		//Display the target reps for each workout when the workout is first loaded up
		if(!model.getTargetReps().isEmpty()){
			int count = 1;
			for(int rep : model.getTargetReps()){
				RepLine.addRepLine(context, holder.linearLayout, count, rep, model, false,
						false);
				count++;
			}
		}

		//When a new line is added
		holder.add.setOnClickListener(view -> {
			RepLine.addRepLine(context, holder.linearLayout, model.getRepLines().size() + 1,
					0, model, true, false);
		});

	}

	/**
	 * @return the number of card items in the recycler view
	 */
	@Override
	public int getItemCount() {
		return workoutPlanExercises.size();
	}

	/**
	 * View holder class for initialising views
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView exerciseNameTV;
		private final TextView exerciseDescTV;
		private LinearLayout linearLayout;
		private final FloatingActionButton add;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			linearLayout = itemView.findViewById(R.id.llRepLayout);
			exerciseNameTV = itemView.findViewById(R.id.idTVExerciseName);
			exerciseDescTV = itemView.findViewById(R.id.idTVExerciseDescription);
			add = itemView.findViewById(R.id.fabtnAddSet);
		}
	}
}


