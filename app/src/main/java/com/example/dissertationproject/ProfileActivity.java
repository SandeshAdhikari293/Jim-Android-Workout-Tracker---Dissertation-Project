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

import com.example.dissertationproject.objects.User;
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
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.tvProfile);
        makeAdmin = findViewById(R.id.chbAdminProfile);
        changeName = findViewById(R.id.etChangeDisplayName);
        delete = findViewById(R.id.btnDeleteAccount);
        uid = getIntent().getStringExtra("uid");
        user = User.getUserFromID(uid);
        name.setText("User: " + User.getUserFromID(uid).getName());

        makeAdmin.setActivated(false);
        changeName.setText(user.getName());

        if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            makeAdmin.setVisibility(View.INVISIBLE);
        }else{
            admin = true;

            System.out.println("is admin: " +user.isAdmin());
            makeAdmin.setChecked(user.isAdmin());

            if(user.isActivated())
                delete.setText("Deactivate account");
            else
                delete.setText("Reactivate account");
        }

        if(!user.isActivated()){
            name.setText(name.getText() + " (inactive)");
            name.setTextColor(Color.RED);
        }
    }

    public void saveChanges(View view){
        System.out.println("USER ID: " + uid);

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
                    return;
                }
            });
        }else{
            if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                finish();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
//                            Log.d(TAG, "User account deleted.");
                            }
                        });
            }
        }
    }
}