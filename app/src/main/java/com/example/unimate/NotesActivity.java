
package com.example.unimate;

import android.os.Bundle;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public class NotesActivity extends AppCompatActivity {

    EditText noteTitle, noteContent;
    Button saveNote;
    LinearLayout notesContainer;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        databaseHelper = new DatabaseHelper(this);

        noteTitle = findViewById(R.id.etNoteTitle);
        noteContent = findViewById(R.id.etNoteContent);
        saveNote = findViewById(R.id.btnSaveNote);
        notesContainer = findViewById(R.id.notesContainer);

        loadNotes();

        saveNote.setOnClickListener(v -> {

            String title = noteTitle.getText().toString().trim();
            String content = noteContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean saved = databaseHelper.insertNote(title, content);

            if (saved) {
                Toast.makeText(this, "Note Saved!",
                        Toast.LENGTH_SHORT).show();

                noteTitle.setText("");
                noteContent.setText("");

                loadNotes();
            }
        });
    }

    private void loadNotes() {

        notesContainer.removeAllViews();

        Cursor cursor = databaseHelper.getAllNotes();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);

            TextView item = new TextView(this);

            item.setText("Title: " + title +
                    "\n\n" + content);

            item.setTextSize(16);
            item.setPadding(20, 20, 20, 20);
            item.setBackgroundColor(0xFFE1EBF8);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 12, 0, 8);
            item.setLayoutParams(params);

            notesContainer.addView(item);

            Button editButton = new Button(this);
            editButton.setText("Edit");

            editButton.setOnClickListener(v -> {

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(30, 10, 30, 10);

                EditText editTitle = new EditText(this);
                editTitle.setText(title);
                editTitle.setHint("Note Title");
                layout.addView(editTitle);

                EditText editContent = new EditText(this);
                editContent.setText(content);
                editContent.setHint("Note Content");
                editContent.setMinLines(4);
                layout.addView(editContent);

                new AlertDialog.Builder(this)
                        .setTitle("Edit Note")
                        .setView(layout)
                        .setPositiveButton("Update", (dialog, which) -> {

                            boolean updated = databaseHelper.updateNote(
                                    id,
                                    editTitle.getText().toString().trim(),
                                    editContent.getText().toString().trim()
                            );

                            if (updated) {
                                Toast.makeText(this, "Note Updated!",
                                        Toast.LENGTH_SHORT).show();
                                loadNotes();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            notesContainer.addView(editButton);

            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");

            deleteButton.setOnClickListener(v -> {

                boolean deleted = databaseHelper.deleteNote(id);

                if (deleted) {
                    Toast.makeText(this, "Note Deleted!",
                            Toast.LENGTH_SHORT).show();

                    loadNotes();
                }
            });

            notesContainer.addView(deleteButton);
        }

        cursor.close();
    }
}
