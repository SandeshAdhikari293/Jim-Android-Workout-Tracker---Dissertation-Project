/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject.ui;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dissertationproject.CredentialValidation;
import com.example.dissertationproject.R;
import com.example.dissertationproject.Utils;
import com.example.dissertationproject.objects.Exercise;
import com.example.dissertationproject.objects.ExerciseTemplate;
import com.example.dissertationproject.objects.RepLine;
import com.example.dissertationproject.objects.User;
import com.example.dissertationproject.objects.WorkoutPlan;
import com.example.dissertationproject.objects.WorkoutPlanExercise;
import com.example.dissertationproject.objects.enums.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText password;
    private EditText confirmPassword;
    private EditText email;
    private EditText name;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CredentialValidation validation;


    /**
     * Initialise variables when the screen is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        password = findViewById(R.id.etPassword);
        confirmPassword = findViewById(R.id.cnfrmPassword);

        email = findViewById(R.id.etEmail);
        name = findViewById(R.id.etName);

        validation = new CredentialValidation();

    }


    /**
     * Create a new user in the database through firebase auth
     * @param v
     */
    public void createUser(View v){
        //check that text fields aren't null, which would throw an error
        if(password.getText() == null || confirmPassword.getText() == null){
            return;
        }

        //Check if the email is valid
        if(!validation.isEmailValid(email.getText().toString())){
            Utils.errorDialog(this, "E-mail address", "Please enter a valid e-mail address format.", "Continue");
            return;
        }

        //Check if password is valid
        if(!validation.isPasswordValid(password.getText().toString())){
            Utils.errorDialog(this, "Password", "Please enter a valid password It contains at least 8 characters and at most 20 characters.\n" +
                    "It contains at least one digit.\n" +
                    "It contains at least one upper case alphabet.\n" +
                    "It contains at least one lower case alphabet.\n" +
                    "It contains at least one special character which includes !@#$%&*()-+=^.\n" +
                    "It doesn’t contain any white space.", "Continue");
            return;
        }

        //Check if fields are empty
        if(email.getText().toString().equals("") || password.getText().toString().equals("") || confirmPassword.getText().toString().equals("")){
            Utils.errorDialog(this, "Registration error", "Fields can not be empty", "Continue");
            return;
        }

        //Check that the confirmation password matches
        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            Utils.errorDialog(this, "Registration error", "Passwords do not match", "Continue");
            return;
        }


        //Create a new user through firebase auth
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

                //attributes for the user profile
                Map<String, Object> profile = new HashMap<>();
                profile.put("user_id", user.getUid());
                profile.put("name", name.getText().toString());
                profile.put("email", user.getEmail());
                profile.put("active", true);
                profile.put("is_admin", false);

                // Add a new document with a generated ID for the profile of that user
                db.collection("profiles")
                        .add(profile)
                        .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

                //send a verification email to the user email
//                addDefaultData(user.getUid());
                user.sendEmailVerification().addOnCompleteListener(task12 -> {
                    Runnable exit = () -> finish();
                    Utils.confirmDialog(this, "Account verifcation",
                            "Please verify your email address", "Cancel",
                            "Continue", exit, exit);
                });

            }else{
                Utils.errorDialog(this, "Registration error", "This account is unable to be created", "Continue");
            }
        });
    }

    /**
     * Takes the user back to the login screen
     * @param v
     */
    public void login(View v){
        finish();
    }



}