package com.example.dissertationproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dissertationproject.objects.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.concurrent.atomic.AtomicReference;

public class ProfileActivity extends AppCompatActivity {
    String uid;
    TextView name;
    EditText changeName;
    CheckBox makeAdmin;
    Button delete;
    boolean admin;
    User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.tvProfile);
        makeAdmin = findViewById(R.id.chbAdminProfile);
        changeName = findViewById(R.id.etChangeDisplayName);
        delete = findViewById(R.id.btnDeleteAccount);

        uid = getIntent().getStringExtra("uid");

//        makeAdmin.setActivated(false);

        System.out.println("UID: "+ uid);
        System.out.println("id:: "+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (!uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            user = User.getUserFromID(uid);

            if(User.activeUser.isAdmin()){
                makeAdmin.setChecked(user.isAdmin());
                admin = true;
            }

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

    public void saveChanges(View view){
        db.collection("profiles").whereEqualTo("user_id", uid).get().addOnCompleteListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots.getResult()){
                System.out.println("PROFILE ID: " + snapshot.getId());

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

    public void deleteAccount(View view){
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

            Utils.confirmDialog(this, "Delete your account", "WARNING! You are about to delete your account. This action is irreversible.", "Cancel", "Confirm", () -> {}, runnable);

        }
    }
}