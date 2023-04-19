package com.example.dissertationproject.workoutPlan;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.ActiveWorkoutActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.RepLine;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CreateWorkoutPlanAdapter extends RecyclerView.Adapter<CreateWorkoutPlanAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<WorkoutPlanExercise> workoutPlanExercises;

	// Constructor
	public CreateWorkoutPlanAdapter(Context context, ArrayList<WorkoutPlanExercise> workoutPlanExercises) {
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

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		WorkoutPlanExercise model = workoutPlanExercises.get(position);
		holder.exerciseNameTV.setText(model.getExerciseTemplate().getName());
		holder.exerciseDescTV.setText(model.getExerciseTemplate().getDesc());

		if(!model.getTargetReps().isEmpty()){
			model.getRepLines().clear();
			int count = 1;

			for(int rep : model.getTargetReps()){

				LinearLayout hor = new LinearLayout(context);
				hor.setOrientation(LinearLayout.HORIZONTAL);

				holder.linearLayout.addView(hor);

				TextView txt = new TextView(context);
				txt.setText((count) +") ");

				EditText et = new EditText(context);
				et.setHint("Target reps: "+rep);
				et.setText(rep + "");
				et.setInputType(InputType.TYPE_CLASS_NUMBER);

				Button rm = new Button(context);
				rm.setText("-");
				rm.setTextSize(14);
				rm.setBackgroundTintList(context.getResources().getColorStateList(R.color.dialog_color));

				RepLine rl = new RepLine(txt, et, rm, hor);

				model.getRepLines().add(rl);

				rm.setOnClickListener(view1 -> {

					holder.linearLayout.removeView(hor);

					model.getRepLines().remove(rl);
				});

				hor.addView(txt);
				hor.addView(et);
				hor.addView(rm);

				count++;
			}
		}else {

			for (RepLine line : new ArrayList<>(model.getRepLines())) {
				LinearLayout hor = new LinearLayout(context);
				hor.setOrientation(LinearLayout.HORIZONTAL);

				holder.linearLayout.addView(hor);

				TextView txt = new TextView(context);
				txt.setText(line.getTxt().toString());

				EditText et = new EditText(context);
				et.setHint("Enter the target reps");
				et.setText(line.getReps().getText());
				et.setInputType(InputType.TYPE_CLASS_NUMBER);

				Button rm = new Button(context);
				rm.setText("-");
				rm.setTextSize(14);
				rm.setBackgroundTintList(context.getResources().getColorStateList(R.color.dialog_color));

				RepLine rl = new RepLine(txt, et, rm, hor);

				model.getRepLines().add(rl);

				rm.setOnClickListener(view1 -> {

					holder.linearLayout.removeView(hor);

					model.getRepLines().remove(rl);
				});

				hor.addView(txt);
				hor.addView(et);
				hor.addView(rm);


				model.getRepLines().remove(line);
			}
		}

		holder.add.setOnClickListener(view -> {

			LinearLayout hor = new LinearLayout(view.getContext());
			hor.setOrientation(LinearLayout.HORIZONTAL);

			holder.linearLayout.addView(hor);

			TextView txt = new TextView(view.getContext());
			txt.setText((model.getRepLines().size() + 1) +") ");

			EditText et = new EditText(view.getContext());
			et.setHint("Enter the target reps");
			et.setInputType(InputType.TYPE_CLASS_NUMBER);

			Button rm = new Button(view.getContext());
			rm.setText("-");
			rm.setTextSize(14);
			rm.setBackgroundTintList(context.getResources().getColorStateList(R.color.dialog_color));
			RepLine rl = new RepLine(txt, et, rm, hor);

			model.getRepLines().add(rl);

			rm.setOnClickListener(view1 -> {

				holder.linearLayout.removeView(hor);

				model.getRepLines().remove(rl);
			});

			hor.addView(txt);
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


