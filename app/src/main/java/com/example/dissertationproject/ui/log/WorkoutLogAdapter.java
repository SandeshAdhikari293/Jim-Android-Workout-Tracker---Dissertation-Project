/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.log;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<Workout> workouts;
	FirebaseFirestore db = FirebaseFirestore.getInstance();

	/**
	 * Constructor for adapter
	 * @param context	the context of the application
	 * @param workouts	the data set of workouts
	 */
	public WorkoutLogAdapter(Context context,ArrayList<Workout> workouts) {
		this.context = context;
		this.workouts = workouts;
	}

	/**
	 * Inflate the layout for each item of the recycler view
	 * @param parent The ViewGroup into which the new View will be added after it is bound to
	 *               an adapter position.
	 * @param viewType The view type of the new View.
	 *
	 * @return view holder
	 */
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_card, parent, false);
		return new ViewHolder(view);
	}

	/**
	 * 	Manipulates the variables for each card
	 * @param holder The ViewHolder which should be updated to represent the contents of the
	 *        item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 */
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Workout model = workouts.get(position);
		holder.planName.setText(model.getName());

		//Create a new text view to display the date and duration of the workout
		TextView duration = new TextView(context);
		duration.setTextSize(22);
		duration.setText(model.getDate() +" | "+model.getDuration());
		duration.setTypeface(null, Typeface.BOLD);
		duration.setGravity(View.TEXT_ALIGNMENT_CENTER);
		holder.linearLayout.addView(duration);

		//Iterate all exercises from the workout object
		for(Exercise exercise : model.getExercises()){
			//Create a new text view of the exercise name
			TextView textView = new TextView(context);
			textView.setTextSize(22);
			textView.setText(exercise.getName());
			holder.linearLayout.addView(textView);

			//Create a new text view for each set of the exercise and display it
			int set = 1;
			for(Map.Entry<Integer, HashMap<Integer, Integer>> rep : exercise.getReps().entrySet()){
				for(Map.Entry<Integer, Integer> r : rep.getValue().entrySet()){
					TextView reps = new TextView(context);
					reps.setTextSize(18);
					reps.setText(set+") "+r.getKey() + " x "+ r.getValue());
					set++;

					holder.linearLayout.addView(reps);
				}
			}
		}

		//onClick listener for the delete button
		holder.editButton.setOnClickListener(view -> {
			Utils.confirmDialog(context,"Delete workout", "Are you sure you want to delete this workout?", "Cancel", "Confirm",() -> {
			}, ()->{
				//remove from cached data
				User.getActiveUser().getWorkoutLog().remove(model);

				//remove from database
				db.collection("workout_log").document(model.getId()).delete().addOnCompleteListener(task -> {
					Utils.errorDialog(context,"Deleted", "Workout has been deleted", "Continue");
				});
			});
		});
	}


	/**
	 * @return	the number of card items in recycler view
	 */
	@Override
	public int getItemCount() {
		return workouts.size();
	}


	// View holder class for initializing of your views such as TextView and Imageview

	/**
	 * View holder class for initialising views (e.g. TextViews)
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView planName;
		private final LinearLayout linearLayout;
		private final FloatingActionButton playWorkout;
		private final FloatingActionButton editButton;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			//Initialise the variables
			planName = itemView.findViewById(R.id.idTVCourseName);
			linearLayout = itemView.findViewById(R.id.llExercisesOnPlan);
			playWorkout = itemView.findViewById(R.id.fbtnStartWorkout);
			editButton = itemView.findViewById(R.id.fbtnEditWorkoutPlan);

			playWorkout.setVisibility(View.INVISIBLE);
			playWorkout.setClickable(false);
		}
	}
}


