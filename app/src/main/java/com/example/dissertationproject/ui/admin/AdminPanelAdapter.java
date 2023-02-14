package com.example.dissertationproject.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminPanelAdapter extends RecyclerView.Adapter<AdminPanelAdapter.ViewHolder> {

	private final Context context;
	private final ArrayList<User> users;

	// Constructor
	public AdminPanelAdapter(Context context, ArrayList<User> users) {
		this.context = context;
		this.users = users;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// to inflate the layout for each item of recycler view.
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_card, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		User model = users.get(position);
		holder.planName.setText(model.getName());

		holder.planName.setText(model.getName());
		holder.planDesc.setText(model.getEmail());

	}


	@Override
	public int getItemCount() {
		// this method is used for showing number of card items in recycler view
		return users.size();
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


