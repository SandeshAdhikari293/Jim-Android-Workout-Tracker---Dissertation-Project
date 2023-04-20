package com.example.dissertationproject;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText password;
    EditText confirmPassword;
    EditText email;
    EditText name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CredentialValidation validation;


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

//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(Color.parseColor("#7f77e5"));
//            window.setNavigationBarColor(Color.parseColor("#db8ccd"));
//        }

    }


    public void createUser(View v){
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

                //TODO: remove the gradle for firebase admin
                Map<String, Object> profile = new HashMap<>();
                profile.put("user_id", user.getUid());
                profile.put("name", name.getText().toString());
                profile.put("email", user.getEmail());
                profile.put("active", true);
                profile.put("is_admin", false);

                // Add a new document with a generated ID
                db.collection("profiles")
                        .add(profile)
                        .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

                finish();
                user.sendEmailVerification().addOnCompleteListener(task12 -> finish());
            }else{
                Utils.errorDialog(this, "Registration error", "This account is unable to be created", "Continue");
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