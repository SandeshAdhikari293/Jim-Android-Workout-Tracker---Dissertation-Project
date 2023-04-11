package com.example.dissertationproject.ui.log;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.ActiveWorkoutActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<Workout> workouts;
	FirebaseFirestore db = FirebaseFirestore.getInstance();

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

		TextView duration = new TextView(context);
		duration.setTextSize(22);
		duration.setText(model.getDate() +" | "+model.getDuration());
		duration.setTypeface(null, Typeface.BOLD);
		duration.setGravity(View.TEXT_ALIGNMENT_CENTER);
		holder.linearLayout.addView(duration);

		for(Exercise exercise : model.getExercises()){
			TextView textView = new TextView(context);
			textView.setTextSize(22);
			textView.setText(exercise.getTemplate().getName());
			holder.linearLayout.addView(textView);

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

		holder.editButton.setOnClickListener(view -> {
			Utils.confirmDialog(context,"Delete workout", "Are you sure you want to delete this workout?", "Cancel", "Confirm",() -> {
			}, ()->{
				User.getActiveUser().getWorkoutLog().remove(model);

				db.collection("workout_log").document(model.getId()).delete().addOnCompleteListener(task -> {
					Utils.errorDialog(context,"Deleted", "Workout has been deleted", "Continue");
					//todo: delete the exercises for this workout log
				});

			});
		});
	}


	@Override
	public int getItemCount() {
		// this method is used for showing number of card items in recycler view
		return workouts.size();
	}


	// View holder class for initializing of your views such as TextView and Imageview
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView planName;
//		private final TextView planDesc;

		private final LinearLayout linearLayout;

		private final FloatingActionButton playWorkout;
		private final FloatingActionButton editButton;


		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			planName = itemView.findViewById(R.id.idTVCourseName);
//			planDesc = itemView.findViewById(R.id.idTVCourseRating);
			linearLayout = itemView.findViewById(R.id.llExercisesOnPlan);
			playWorkout = itemView.findViewById(R.id.fbtnStartWorkout);
			editButton = itemView.findViewById(R.id.fbtnEditWorkoutPlan);
//			editButton = itemView.findViewById(R.id.fbtn);

//			editButton.setVisibility(View.INVISIBLE);
//			editButton.setClickable(false);

			playWorkout.setVisibility(View.INVISIBLE);
			playWorkout.setClickable(false);
//			playWorkout.setBackgroundDrawable(itemView.getResources().getDrawable(R.drawable.ic_arrow));
		}
	}
}


