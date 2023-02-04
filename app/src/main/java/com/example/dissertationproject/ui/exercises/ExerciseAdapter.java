package com.example.dissertationproject.ui.exercises;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.CreateExerciseActivity;
import com.example.dissertationproject.DashboardActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<ExerciseTemplate> exercises;

	// Constructor
	public ExerciseAdapter(Context context, ArrayList<ExerciseTemplate> exercises) {
		this.context = context;
		this.exercises = exercises;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// to inflate the layout for each item of recycler view.
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		ExerciseTemplate model = exercises.get(position);

		holder.eName.setText(model.getName());
		holder.eDesc.setText(model.getDesc() + " | " +  model.getCategory());

		holder.edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, CreateExerciseActivity.class);
				intent.putExtra("exerciseID", exercises.get(position).getId());
				context.startActivity(intent);

				System.out.println("intent: " +  exercises.get(position).getId());
			}
		});
//		holder.planName.setText(model.getName());
//		holder.planDesc.setText("" + model.get);
//		holder.courseIV.setImageResource(model.getCourse_image());

//		for(Exercise exercise : model.getExercises()){
//			TextView textView = new TextView(context);
//			textView.setText(exercise.getTemplate().getName());
//			holder.linearLayout.addView(textView);
//
//			int set = 1;
//			for(Map.Entry<Integer, Integer> rep : exercise.getReps().entrySet()){
//
//				TextView reps = new TextView(context);
//				reps.setText(set+") "+rep.getKey()+" x "+ rep.getValue());
//				set++;
//
//				holder.linearLayout.addView(reps);
//			}
//		}
	}


	@Override
	public int getItemCount() {
		// this method is used for showing number of card items in recycler view
		return exercises.size();
	}


	// View holder class for initializing of your views such as TextView and Imageview
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView eName;
		private final TextView eDesc;
		private final FloatingActionButton edit;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			eName = itemView.findViewById(R.id.tvExerciseCardName);
			eDesc = itemView.findViewById(R.id.tvExerciseCardDesc);
			edit = itemView.findViewById(R.id.fbtnEditExerciseCard);

		}
	}
}


