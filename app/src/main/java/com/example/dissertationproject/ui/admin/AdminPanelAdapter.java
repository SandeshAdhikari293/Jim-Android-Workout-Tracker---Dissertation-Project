package com.example.dissertationproject.ui.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.DashboardActivity;
import com.example.dissertationproject.ProfileActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

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
		holder.name.setText(model.getName());
		holder.email.setText(model.getEmail());

		holder.editProfile.setOnClickListener(view -> {
			Intent intent = new Intent(context, ProfileActivity.class);
			intent.putExtra("uid", model.getId());
			context.startActivity(intent);
		});

	}


	@Override
	public int getItemCount() {
		// this method is used for showing number of card items in recycler view
		return users.size();
	}


	// View holder class for initializing of your views such as TextView and Imageview
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView name;
		private final TextView email;

		private final LinearLayout linearLayout;

		private final FloatingActionButton editProfile;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.idTVAdminName);
			email = itemView.findViewById(R.id.idTVAdminEmail);

			linearLayout = itemView.findViewById(R.id.llExercisesOnPlan);
			editProfile = itemView.findViewById(R.id.fbtnEditProfile);

		}
	}
}


