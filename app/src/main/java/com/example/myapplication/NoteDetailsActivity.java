package com.example.myapplication;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    // UI elements for note title and content
    EditText titleEditText, contentEditText;
    ImageButton saveNoteButton;
    TextView pageTitleTextView, deleteNoteTextViewButton;
    String title, content, docId;
    boolean isEditing = false;

    /**
     * Called when the activity is first created.
     * This is where you should do all of your normal static set up: create views, bind data to lists, etc.
     * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
     * Always followed by onStart().
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        // Initialize UI elements
        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.content_text);
        saveNoteButton = findViewById(R.id.save_note_button);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewButton = findViewById(R.id.deleteNote_textViewButton);

        // Receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()) {
            isEditing = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if(isEditing) {
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewButton.setVisibility(TextView.VISIBLE);
        }

        // Set a click listener on the save button to save the note
        saveNoteButton.setOnClickListener(v -> saveNote());

        deleteNoteTextViewButton.setOnClickListener(v-> deleteNoteFromFirebase());
    }

    /**
     * Validates the note input fields and saves the note.
     * If the title is missing, it sets an error on the title field.
     * Otherwise, it creates a Note object and saves it to Firebase.
     */
    void saveNote() {
        // Retrieve note title and content from input fields
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        // Check if the note title is empty
        if (noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Missing Title!"); // Set error message on title field
            return; // Exit the method without saving the note
        }

        // Create a new Note object and set its properties
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        // Save the note to Firebase
        saveNoteToFirebase(note);
    }

    /**
     * Saves the given note to Firebase Firestore.
     *
     * @param note The note to be saved.
     */
    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        if(isEditing) {
            // Get the current document reference from the Firestore collection for notes; Update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        } else {
            // Get a new document reference from the Firestore collection for notes; Create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        // Save the note to the document reference
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Note is successfully added
                    Utility.showToast(NoteDetailsActivity.this, "Note added!");
                    finish(); // Close the activity
                } else {
                    // Note could not be added
                    Utility.showToast(NoteDetailsActivity.this, "Note cannot be added!");
                }
            }
        });
    }

    void deleteNoteFromFirebase() {
        DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        // Delete the note from the document reference
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Note is successfully deleted
                    Utility.showToast(NoteDetailsActivity.this, "Note deleted!");
                    finish(); // Close the activity
                } else {
                    // Note could not be added
                    Utility.showToast(NoteDetailsActivity.this, "Note cannot be deleted!");
                }
            }
        });
    }
}
