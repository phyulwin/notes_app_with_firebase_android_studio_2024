package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

/**
 * Utility class that provides helper methods for common tasks such as showing toast messages
 * and accessing Firestore collections.
 */
public class Utility {

    /**
     * Displays a toast message to the user.
     *
     * @param context the context from which this method is called
     * @param message the message to be displayed in the toast
     */
    static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets a reference to the Firestore collection for the current user's notes.
     *
     * @return a CollectionReference pointing to the current user's notes collection
     */
    static CollectionReference getCollectionReferenceForNotes() {
        // Get the currently authenticated user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Return a reference to the 'my notes' sub-collection under the 'notes' collection
        // for the current user
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my notes");
    }

    static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}
