/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProfileActivity extends AppCompatActivity {
    private String uid;
    private TextView name;
    private EditText changeName;
    private CheckBox makeAdmin;
    private Button delete;
    private boolean admin;
    private User user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Initialise all the variables when the view is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.tvProfile);
        makeAdmin = findViewById(R.id.chbAdminProfile);
        changeName = findViewById(R.id.etChangeDisplayName);
        delete = findViewById(R.id.btnDeleteAccount);

        //get the unique id of the user profile being displayed, as admins and view
        //others profiles
        uid = getIntent().getStringExtra("uid");

        if (!uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            user = User.getUserFromID(uid);

            if(User.activeUser.isAdmin()){
                makeAdmin.setChecked(user.isAdmin());
                admin = true;
            }

            //toggle the activation if an admin presses it.
            if(user.isActivated())
                delete.setText("Deactivate");
            else
                delete.setText("Reactivate");
        }else{
            makeAdmin.setVisibility(View.INVISIBLE);
            user = User.getActiveUser();
        }

        name.setText(user.getName());
        changeName.setText(user.getName());

        if(!user.isActivated()){
            name.setText(name.getText() + " (inactive)");
            name.setTextColor(Color.RED);
        }

    }

    /**
     * Save the changes to the profile in the database
     * @param view
     */
    public void saveChanges(View view){
        //update the correct field with the changed attributes
        db.collection("profiles").whereEqualTo("user_id", uid).get().addOnCompleteListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots.getResult()){
                db.collection("profiles").document(snapshot.getId())
                        .update(
                                "name", changeName.getText().toString(),
                                "is_admin", makeAdmin.isChecked()
                        );
                user.setName(changeName.getText().toString());
                user.setAdmin(makeAdmin.isChecked());
                finish();
                return;
            }
        });
    }

    /**
     * Permanently delete a users account
     * @param view
     */
    public void deleteAccount(View view){
        //Admins can't delete accounts from the app, but can deactivate or reactivate them
        if(admin){
            db.collection("profiles").whereEqualTo("user_id", uid).get().addOnCompleteListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots.getResult()){
                    db.collection("profiles").document(snapshot.getId())
                            .update(
                                    "active", !user.isActivated()
                            );
                    finish();
                    user.setActivated(!user.isActivated());

                    if(user.isActivated()){
                        Toast.makeText(getApplicationContext(),user.getName()+" has been set to active",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),user.getName()+" has been deactivated",Toast.LENGTH_SHORT).show();
                    }

                    return;
                }
            });
        }else{
            //Delete the user account then delete the user profile
            Runnable runnable = () -> {
                if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.delete()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    db.collection("profiles").document(this.user.getProfileID())
                                            .delete()
                                            .addOnCompleteListener(aVoid -> {
                                                finish();
                                                FirebaseAuth.getInstance().signOut();
                                                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                                            });
                                }
                            });
                }
            };

            //warning message to user
            Utils.confirmDialog(this, "Delete your account", "WARNING! You are about to delete your account. This action is irreversible.", "Cancel", "Confirm", () -> {}, runnable);

        }
    }
}