/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.exercises;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<ExerciseTemplate> exercises;

	/**
	 * Constructor initialises variables when created
	 * @param context	the context of the application
	 * @param exercises	the data set to be displayed
	 */
	public ExerciseAdapter(Context context, ArrayList<ExerciseTemplate> exercises) {
		this.context = context;
		this.exercises = exercises;
	}

	/**
	 * inflate the layout for each item of the recycler view
	 * @param parent The ViewGroup into which the new View will be added after it is bound to
	 *               an adapter position.
	 * @param viewType The view type of the new View.
	 *
	 * @return
	 */
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent, false);
		return new ViewHolder(view);
	}

	/**
	 * Apply changes to each item in the data set
	 * @param holder The ViewHolder which should be updated to represent the contents of the
	 *        item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 */
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		ExerciseTemplate model = exercises.get(position);

		holder.eName.setText(model.getName());
		holder.eDesc.setText(model.getDesc() + " | " +  model.getCategory());

		holder.edit.setOnClickListener(view -> {
			Intent intent = new Intent(context, CreateExerciseActivity.class);
			intent.putExtra("exerciseID", exercises.get(position).getId());
			context.startActivity(intent);
		});
	}


	/**
	 * Shows the number of card items in recycler view
	 * @return integer
	 */
	@Override
	public int getItemCount() {
		return exercises.size();
	}


	/**
	 * View holder class for initialisation of views such as TextView
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView eName;
		private final TextView eDesc;
		private final FloatingActionButton edit;

		/**
		 * initialise views
		 * @param itemView
		 */
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			eName = itemView.findViewById(R.id.tvExerciseCardName);
			eDesc = itemView.findViewById(R.id.tvExerciseCardDesc);
			edit = itemView.findViewById(R.id.fbtnEditExerciseCard);

		}
	}
}


