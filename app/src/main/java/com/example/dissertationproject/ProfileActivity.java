package com.example.dissertationproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.tvProfile);
        changeName = findViewById(R.id.etChangeDisplayName);

        uid = getIntent().getStringExtra("uid");

        name.setText("User: " + User.getUserFromID(uid).getName());

        if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            //User is editing themself.
        }
    }

    public void saveChanges(View view){
        System.out.println("USER ID: " + uid);

        db.collection("profiles").whereEqualTo("user_id", uid).get().addOnCompleteListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots.getResult()){
                System.out.println("PROFILE ID: " + snapshot.getId());

                db.collection("profiles").document(snapshot.getId())
                        .update(
                                "name", changeName.getText().toString()
                        );
                finish();
                return;
            }
        });


    }

    public void deleteAccount(View view){
        if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    });
        }
    }
}