/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.workouts.workoutPlan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.RepLine;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CreateWorkoutPlanAdapter extends RecyclerView.Adapter<CreateWorkoutPlanAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<WorkoutPlanExercise> workoutPlanExercises;

	/**
	 * Constructor to initialise variables on creation
	 * @param context				application context
	 * @param workoutPlanExercises	the dataset to display
	 */
	public CreateWorkoutPlanAdapter(Context context, ArrayList<WorkoutPlanExercise> workoutPlanExercises) {
		this.context = context;
		this.workoutPlanExercises = workoutPlanExercises;
	}

	/**
	 * Inflate the layout for each item of the recycler view
	 * @param parent The ViewGroup into which the new View will be added after it is bound to
	 *               an adapter position.
	 * @param viewType The view type of the new View.
	 *
	 * @return	the view for the cards
	 */
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_workout_card, parent, false);
		return new ViewHolder(view);
	}

	/**
	 * Manipulate each cards data
	 * @param holder The ViewHolder which should be updated to represent the contents of the
	 *        item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 */
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		WorkoutPlanExercise model = workoutPlanExercises.get(position);
		holder.exerciseNameTV.setText(model.getExerciseTemplate().getName());
		holder.exerciseDescTV.setText(model.getExerciseTemplate().getDesc());

		if(!model.getTargetReps().isEmpty()){
			model.getRepLines().clear();
			int count = 1;

			for(int rep : model.getTargetReps()){
				RepLine.addRepLine(context, holder.linearLayout, count, rep, model, false,
						false);

				count++;
			}
		}else {
			for (RepLine line : new ArrayList<>(model.getRepLines())) {
				RepLine.addRepLine(context, holder.linearLayout, 0, 0, model, false,
				true);

				model.getRepLines().remove(line);
			}
		}

		holder.add.setOnClickListener(view -> {

			RepLine.addRepLine(context, holder.linearLayout, 0, 0, model, false,
					true);

		});
	}


	/**
	 * @return integer showing number of card items in recycler view
	 */
	@Override
	public int getItemCount() {
		return workoutPlanExercises.size();
	}


	// View holder class for initializing of your views such as TextView and Imageview

	/**
	 * View holder class for initialising of views such as TextView and Imageview
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


