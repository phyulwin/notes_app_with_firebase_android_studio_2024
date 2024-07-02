package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the layout defined in activity_splash_screen.xml
        setContentView(R.layout.activity_splash_screen);

        // Use a Handler to delay the execution of code by 1000 milliseconds (1 second)
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get the currently signed-in user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                // Check if the user is signed in
                if (currentUser == null) {
                    // No user is signed in, navigate to the LoginActivity
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                } else {
                    // User is signed in, navigate to the MainActivity
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }

                // Finish the SplashScreen activity to prevent the user from returning to it
                finish();
            }
        }, 1000); // Delay for 1 second
    }
}
