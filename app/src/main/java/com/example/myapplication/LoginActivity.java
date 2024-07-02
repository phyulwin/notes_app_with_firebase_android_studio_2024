package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;
    ProgressBar progressBar;
    TextView createAccountButtonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        createAccountButtonTextView = findViewById(R.id.create_account_text_view_button);

        loginButton.setOnClickListener(v-> loginUser());
        createAccountButtonTextView.setOnClickListener(v-> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }

    void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateDate(email, password);
        if(!isValidated) {
            return;
        }

        loginAccountInFirebase(email, password);
    }

    void loginAccountInFirebase(String email, String password) {
        // Get an instance of FirebaseAuth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // Show progress indicator to the user while login is in progress
        changeInProgress(true);
        // Sign in the user with the provided email and password
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Hide progress indicator once login is complete
                changeInProgress(false);

                if (task.isSuccessful()) {
                    // Login is successful
                    // Handle successful login here (e.g., navigate to another activity)
                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                        // Go to mainactivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Utility.showToast(LoginActivity.this, "Email not verified. Please verify your email.");
                    }
                } else {
                    // Login failed
                    // Handle failed login here (e.g., show an error message)
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            // Show the progress bar and hide the login button when in progress
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        } else {
            // Hide the progress bar and show the login button when not in progress
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    boolean validateDate(String email, String password) {
        // Validate data input by the user

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid Email Address.");
            return false;
        }
        if(password.length()<6) {
            passwordEditText.setError("Password must be 6 characters at least.");
            return false;
        }
        return true;
    }
}