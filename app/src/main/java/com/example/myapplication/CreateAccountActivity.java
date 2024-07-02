package com.example.myapplication;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    // Declare UI elements for user inputs and actions
    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountButton;
    ProgressBar progressBar;
    TextView loginButtonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the layout defined in activity_create_account.xml
        setContentView(R.layout.activity_create_account);

        // Initialize UI elements by linking them to their respective views in the XML layout
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);

        createAccountButton = findViewById(R.id.create_account_button);
        progressBar = findViewById(R.id.progress_bar);
        loginButtonTextView = findViewById(R.id.login_text_view_button);

        createAccountButton.setOnClickListener(v-> createAccount());
        loginButtonTextView.setOnClickListener(v-> finish());
    }

    void createAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateDate(email, password, confirmPassword);
        if(!isValidated) {
            return;
        }

        createAccountInFirebase(email, password);
    }

    void createAccountInFirebase(String email, String password) {
        // Show progress indicator to the user while account creation is in progress
        changeInProgress(true);

        // Get an instance of FirebaseAuth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Create a new user with the provided email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
            new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    changeInProgress(false);
                    if (task.isSuccessful()) {
                        // Account creation successful
                        Utility.showToast(CreateAccountActivity.this, "Successfully created account! Check email to verify.");
                        // Send email verification to the newly created account
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        // Sign out the user to prevent unverified access
                        firebaseAuth.signOut();
                        // Close the current activity
                        finish();
                    } else {
                        // Account creation failed
                        Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                    }
                }
            });
    }


    void changeInProgress(boolean inProgress) {
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            createAccountButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createAccountButton.setVisibility(View.VISIBLE);
        }
    }

    boolean validateDate(String email, String password, String confirmPassword) {
        // Validate data input by the user

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid Email Address.");
            return false;
        }
        if(password.length()<6) {
            passwordEditText.setError("Password must be 6 characters at least.");
            return false;
        }
        if(!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Password does not match.");
            return false;
        }
        return true;
    }
}