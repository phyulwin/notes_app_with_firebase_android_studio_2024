package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    // Declaring UI elements
    FloatingActionButton addNoteButton;  // Button to add a new note
    RecyclerView recyclerView;           // RecyclerView to display notes
    ImageButton menuButton;              // Button to show menu

    NoteAdapter noteAdapter;             // Adapter for RecyclerView to handle note display

    // Called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Setting the layout for this activity

        // Initialize the UI elements by finding them by their ID
        addNoteButton = findViewById(R.id.add_note_button);
        recyclerView = findViewById(R.id.recyler_view);
        menuButton = findViewById(R.id.menu_button);

        // Set click listener on the add note button to start NoteDetailsActivity
        addNoteButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NoteDetailsActivity.class)));

        // Set click listener on the menu button to show menu
        menuButton.setOnClickListener(v -> showMenu());

        // Setup the RecyclerView with data and layout
        setupRecyclerView();
    }

    // Method to show the menu
    void showMenu() {
        // Create a new PopupMenu anchored to the menuButton (ImageButton in MainActivity)
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuButton);
        // Add a menu item to the PopupMenu with the title "Logout"
        popupMenu.getMenu().add("Logout");
        // Show the PopupMenu anchored to the menuButton
        popupMenu.show();
        // Set a listener for when a menu item is clicked
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Check if the clicked menu item's title is "Logout"
                if (menuItem.getTitle().equals("Logout")) {
                    // Perform logout action: sign out the current user
                    FirebaseAuth.getInstance().signOut();
                    // Start LoginActivity to allow a new login session
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    // Finish MainActivity to close it after logout
                    finish();
                    // Indicate that the menu item click has been handled
                    return true;
                }
                // Return false if the menu item click is not handled
                return false;
            }
        });
    }

    // Method to setup the RecyclerView
    void setupRecyclerView() {
        // Create a query to fetch notes from Firestore, ordered by timestamp in descending order
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        // Configure FirestoreRecyclerOptions with the query and Note class
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        // Set the layout manager for RecyclerView to a linear layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize the NoteAdapter with options and context
        noteAdapter = new NoteAdapter(options, this);
        // Set the adapter for RecyclerView
        recyclerView.setAdapter(noteAdapter);
    }

    // Called when the activity is becoming visible to the user
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();  // Start listening for data changes in Firestore
    }

    // Called when the activity is no longer visible to the user
    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();  // Stop listening for data changes in Firestore
    }

    // Called when the activity has become visible (it is now "resumed")
    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();  // Notify the adapter that data might have changed
    }
}

/*
Gradle is an open-source build automation tool that is primarily used for Java and Android projects, but it can be used for various types of software projects. It is designed to automate the building, testing, publishing, and deployment processes of software applications. Here are some key features and concepts associated with Gradle:

Build Automation:

Gradle automates the process of compiling source code, packaging binaries, running tests, generating documentation, and more. It helps in managing project dependencies and build tasks efficiently.
DSL (Domain-Specific Language):

Gradle uses a DSL based on Groovy or Kotlin for writing build scripts. These scripts define the build process, including tasks, dependencies, and configurations.
Multi-project Builds:

Gradle supports multi-project builds, allowing you to define and manage builds for projects that have multiple subprojects. This is particularly useful for large applications with modular architectures.
Dependency Management:

Gradle has a powerful dependency management system that allows you to declare dependencies for your project. It supports various repositories like Maven Central, JCenter, and custom repositories.
Plugins:

Gradle's functionality can be extended using plugins. There are numerous plugins available for tasks like compiling Java code, building Android applications, publishing artifacts, and more.
 */