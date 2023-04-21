/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.workouts.workoutPlan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.ui.workouts.ActiveWorkoutActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<WorkoutPlanAdapter.ViewHolder> {
	private final Context context;
	private final ArrayList<WorkoutPlan> workoutPlan;

	/**
	 * Constructor to initialise variables
	 * @param context		the application context
	 * @param workoutPlan	the workout plan data set
	 */
	public WorkoutPlanAdapter(Context context, ArrayList<WorkoutPlan> workoutPlan) {
		this.context = context;
		this.workoutPlan = workoutPlan;
	}

	/**
	 * Inflate the layout for each item of the recycler view
	 * @param parent The ViewGroup into which the new View will be added after it is bound to
	 *               an adapter position.
	 * @param viewType The view type of the new View.
	 *
	 * @return	 inflated view
	 */
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_card, parent, false);
		return new ViewHolder(view);
	}

	/**
	 * Manipulate the data for each card in the recycler view
	 * @param holder The ViewHolder which should be updated to represent the contents of the
	 *        item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 */
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		WorkoutPlan model = workoutPlan.get(position);
		holder.planName.setText(model.getName());
		holder.planDesc.setText(model.getDesc());

		//Run a workout when the button is clicked
		holder.playWorkout.setOnClickListener(view -> {
			ActiveWorkoutActivity.plan = model;
			context.startActivity(new Intent(context, ActiveWorkoutActivity.class));
		});

		//Edit a workout when button clicked
		holder.editWorkout.setOnClickListener(view1 -> {
			CreateWorkoutActivity.exercises = new ArrayList<>();
			//Pass through the workout plan id as an intent
			Intent intent = new Intent(context, CreateWorkoutActivity.class);
			intent.putExtra("workoutPlanID", model.getId());
			context.startActivity(intent);
		});


		//Display each exercise of the workout plan
		for(Exercise exercise : model.getExercises()){
			TextView textView = new TextView(context);
			textView.setTextSize(22);
			textView.setText(exercise.getName());
			holder.linearLayout.addView(textView);

			//display the reps of each exercise of the workout plan
			for(Map.Entry<Integer, HashMap<Integer, Integer>> rep : exercise.getReps().entrySet()){
				for(Map.Entry<Integer, Integer> r : rep.getValue().entrySet()){
					TextView reps = new TextView(context);
					reps.setTextSize(18);
					reps.setText((rep.getKey() + 1)+") 1 x "+ r.getValue());

					holder.linearLayout.addView(reps);
				}
			}
		}
	}


	/**
	 * @return integer showing number of card items in recycler view
	 */
	@Override
	public int getItemCount() {
		return workoutPlan.size();
	}


	/**
	 * View holder class for initialising the views
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView planName;
		private final TextView planDesc;
		private final LinearLayout linearLayout;
		private final FloatingActionButton playWorkout;
		private final FloatingActionButton editWorkout;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			planName = itemView.findViewById(R.id.idTVCourseName);
			planDesc = itemView.findViewById(R.id.idTVWorkoutDescription);
			linearLayout = itemView.findViewById(R.id.llExercisesOnPlan);
			playWorkout = itemView.findViewById(R.id.fbtnStartWorkout);
			editWorkout = itemView.findViewById(R.id.fbtnEditWorkoutPlan);
		}
	}
}


