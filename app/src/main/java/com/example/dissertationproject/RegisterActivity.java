package com.example.dissertationproject;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText password;
    EditText email;
    EditText name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        password = findViewById(R.id.etPassword);
        email = findViewById(R.id.etEmail);
        name = findViewById(R.id.etName);
    }

    public void createUser(View v){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign in is successful
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name.getText().toString()).build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        });

                Map<String, Object> profile = new HashMap<>();
                profile.put("user_id", user.getUid());
                profile.put("is_admin", false);

                // Add a new document with a generated ID
                db.collection("profiles")
                        .add(profile)
                        .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

//                finish();
                //TODO: E-mail verification
            }
        });
    }

    public void login(View v){
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
//            reload();
            System.out.println("NAME IS: " +currentUser.getDisplayName());

        }
    }


}