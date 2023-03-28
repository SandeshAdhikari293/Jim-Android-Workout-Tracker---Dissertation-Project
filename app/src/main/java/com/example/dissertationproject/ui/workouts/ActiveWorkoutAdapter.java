package com.example.dissertationproject.ui.workouts;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ActiveWorkoutAdapter extends RecyclerView.Adapter<ActiveWorkoutAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<WorkoutPlanExercise> workoutPlanExercises;

	// Constructor
	public ActiveWorkoutAdapter(Context context, ArrayList<WorkoutPlanExercise> workoutPlanExercises) {
		this.context = context;
		this.workoutPlanExercises = workoutPlanExercises;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// to inflate the layout for each item of recycler view.
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_workout_card, parent, false);


		return new ViewHolder(view);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		WorkoutPlanExercise model = workoutPlanExercises.get(position);
		holder.exerciseNameTV.setText(model.getExerciseTemplate().getName());
		holder.exerciseDescTV.setText(model.getExerciseTemplate().getDesc());

		if(!model.getTargetReps().isEmpty()){
			int count = 1;
			for(int rep : model.getTargetReps()){

				LinearLayout hor = new LinearLayout(context);
				hor.setOrientation(LinearLayout.HORIZONTAL);

				holder.linearLayout.addView(hor);

				TextView txt = new TextView(context);
				txt.setText((count) +") ");

				EditText etWeight = new EditText(context);
				etWeight.setHint("Weight: ");

				EditText et = new EditText(context);
				et.setHint("Target reps: "+rep);

				Button rm = new Button(context);
				rm.setText("-");

				RepLine rl = new RepLine(txt, et, rm, hor);
				rl.setWeight(etWeight);

				model.getRepLines().add(rl);

				rm.setOnClickListener(view1 -> {

					holder.linearLayout.removeView(hor);

					model.getRepLines().remove(rl);
				});

				hor.addView(txt);
				hor.addView(etWeight);
				hor.addView(et);
				hor.addView(rm);

				count++;
			}
		}

		holder.add.setOnClickListener(view -> {

			LinearLayout hor = new LinearLayout(view.getContext());
			hor.setOrientation(LinearLayout.HORIZONTAL);

			holder.linearLayout.addView(hor);

			TextView txt = new TextView(view.getContext());
			txt.setText((model.getRepLines().size() + 1) +") ");


			EditText etWeight = new EditText(context);
			etWeight.setHint("Weight: ");


			EditText et = new EditText(view.getContext());
			et.setHint("Enter the reps");

			Button rm = new Button(view.getContext());
			rm.setText("-");

			RepLine rl = new RepLine(txt, et, rm, hor);

			model.getRepLines().add(rl);

			rm.setOnClickListener(view1 -> {

				holder.linearLayout.removeView(hor);

				model.getRepLines().remove(rl);
			});

			hor.addView(txt);
			hor.addView(etWeight);
			hor.addView(et);
			hor.addView(rm);

//				holder.linearLayout.addView(et);

		});

//		holder.courseNameTV.setText(model.getExerciseTemplate().getName());
//		holder.courseRatingTV.setText("" + model.getWorkoutPlan().getName());
//		holder.courseIV.setImageResource(model.getCourse_image());
	}


	@Override
	public int getItemCount() {
		// this method is used for showing number of card items in recycler view
		return workoutPlanExercises.size();
	}


	// View holder class for initializing of your views such as TextView and Imageview
	public static class ViewHolder extends RecyclerView.ViewHolder {
//		private final ImageView courseIV;
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


