package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter class for displaying notes in a RecyclerView using Firestore.
 */
public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context; // This should not be private.

    /**
     * Constructor for the NoteAdapter.
     *
     * @param options FirestoreRecyclerOptions containing the query and model class.
     * @param context Context from which the adapter is created.
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    /**
     * Binds the data from the Note model to the ViewHolder.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     * @param note     The Note object containing data for a single note.
     */
    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        // Set the note title, content, and timestamp in the respective TextViews
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));

        // Set a click listener on the itemView (the view representing a single item in the RecyclerView)
        holder.itemView.setOnClickListener(v -> {
            // Create a new Intent to start NoteDetailsActivity
            Intent intent = new Intent(context, NoteDetailsActivity.class);
            // Put extra data into the intent, passing the note's title and content to the NoteDetailsActivity
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);
            // Get the document ID of the current note from the snapshot at the given position
            String docId = this.getSnapshots().getSnapshot(position).getId();
            // Put the document ID into the intent to pass it to the NoteDetailsActivity
            intent.putExtra("docId", docId);
            // Start the NoteDetailsActivity with the prepared intent
            context.startActivity(intent);
        });

    }

    /**
     * Creates a new ViewHolder for the RecyclerView.
     *
     * @param parent   The parent ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new NoteViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for an individual note item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    /**
     * ViewHolder class for holding the views for a single note item.
     */
    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, contentTextView, timestampTextView;

        /**
         * Constructor for the NoteViewHolder.
         *
         * @param itemView The View representing an individual note item.
         */
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews
            titleTextView = itemView.findViewById(R.id.note_title_textview);
            contentTextView = itemView.findViewById(R.id.note_content_textview);
            timestampTextView = itemView.findViewById(R.id.note_timestamp_textview);
        }
    }
}
