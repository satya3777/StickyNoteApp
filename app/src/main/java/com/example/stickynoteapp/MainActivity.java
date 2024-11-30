package com.example.stickynoteapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "StickyNotesPrefs";
    private static final String NOTES_KEY = "notesList";
    private EditText noteEditText;
    private Button addButton;
    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> notesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        noteEditText = findViewById(R.id.noteEditText);
        addButton = findViewById(R.id.addButton);
        notesRecyclerView = findViewById(R.id.notesRecyclerView);

        // Initialize notes list
        notesList = new ArrayList<>();

        // Set up RecyclerView
        noteAdapter = new NoteAdapter(notesList);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(noteAdapter);

        // Add note button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteContent = noteEditText.getText().toString().trim();
                if (!noteContent.isEmpty()) {
                    // Create a new note and add to list
                    Note newNote = new Note(noteContent);
                    notesList.add(newNote);
                    noteAdapter.notifyDataSetChanged();

                    // Clear the EditText
                    noteEditText.setText("");

                    // Show a success message
                    Toast.makeText(MainActivity.this, "Note added!", Toast.LENGTH_SHORT).show();
                } else {
                    // Show a message if the input is empty
                    Toast.makeText(MainActivity.this, "Please enter a note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadNotes() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(NOTES_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Note>>(){}.getType();
            notesList = gson.fromJson(json, type);
            noteAdapter = new NoteAdapter(notesList);
            notesRecyclerView.setAdapter(noteAdapter);
        }
    }
    private void saveNotes() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(notesList);
        editor.putString(NOTES_KEY, json);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }
}
