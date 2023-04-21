/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui.admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertationproject.ui.ProfileActivity;
import com.example.dissertationproject.R;
import com.example.dissertationproject.objects.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdminPanelAdapter extends RecyclerView.Adapter<AdminPanelAdapter.ViewHolder> {
	private final Context context;
	private final ArrayList<User> users;

	/**
	 * Constructor for when the adapter is created
	 * @param context	the context of the application
	 * @param users		the list of users being displayed
	 */
	public AdminPanelAdapter(Context context, ArrayList<User> users) {
		this.context = context;
		this.users = users;
	}

	/**
	 * When the view holder is created
	 * @param parent The ViewGroup into which the new View will be added after it is bound to
	 *               an adapter position.
	 * @param viewType The view type of the new View.
	 *
	 * @return
	 */
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// to inflate the layout for each item of recycler view.
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_card, parent, false);
		return new ViewHolder(view);
	}

	/**
	 * Apply changes for each item in the data set
	 * @param holder The ViewHolder which should be updated to represent the contents of the
	 *        item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 */
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

		User model = users.get(position);
		holder.name.setText(model.getName());

		if(!model.isActivated())
			holder.name.setTextColor(Color.RED);
		if(model.isAdmin())
			holder.name.setTextColor(Color.MAGENTA);

		holder.email.setText(model.getEmail());

		//The editing profile button listener
		holder.editProfile.setOnClickListener(view -> {

			//Pass the unique id to the next activity
			Intent intent = new Intent(context, ProfileActivity.class);
			intent.putExtra("uid", model.getId());
			context.startActivity(intent);
		});

	}

	/**
	 * The number of times in the data set
	 * @return	an integer telling the size of the dataset
	 */
	@Override
	public int getItemCount() {
		return users.size();
	}


	/**
	 * Viewholder initialising UI components such as TextViews
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView name;
		private final TextView email;
		private final LinearLayout linearLayout;
		private final FloatingActionButton editProfile;

		/**
		 * Initialise the variables
		 * @param itemView
		 */
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.idTVAdminName);
			email = itemView.findViewById(R.id.idTVAdminEmail);

			linearLayout = itemView.findViewById(R.id.llExercisesOnPlan);
			editProfile = itemView.findViewById(R.id.fbtnEditProfile);

		}
	}
}


