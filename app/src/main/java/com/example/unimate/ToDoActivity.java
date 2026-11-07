
package com.example.unimate;

import android.os.Bundle;
import android.database.Cursor;
import android.graphics.Paint;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public class ToDoActivity extends AppCompatActivity {

    EditText taskName, taskDescription, taskDate;
    Button addTask;
    LinearLayout taskContainer;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        databaseHelper = new DatabaseHelper(this);

        taskName = findViewById(R.id.etTaskName);
        taskDescription = findViewById(R.id.etTaskDescription);
        taskDate = findViewById(R.id.etTaskDate);

        addTask = findViewById(R.id.btnAddTask);
        taskContainer = findViewById(R.id.taskContainer);

        loadTasks();

        addTask.setOnClickListener(v -> {

            String name = taskName.getText().toString().trim();
            String description = taskDescription.getText().toString().trim();
            String date = taskDate.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty()
                    || date.isEmpty()) {

                Toast.makeText(this, "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean saved = databaseHelper.insertTask(
                    name, description, date);

            if (saved) {
                Toast.makeText(this, "Task Saved!",
                        Toast.LENGTH_SHORT).show();

                taskName.setText("");
                taskDescription.setText("");
                taskDate.setText("");

                loadTasks();
            }
        });
    }

    private void loadTasks() {

        taskContainer.removeAllViews();

        Cursor cursor = databaseHelper.getAllTasks();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String date = cursor.getString(3);
            int completed = cursor.getInt(4);

            TextView item = new TextView(this);

            item.setText("Task: " + name +
                    "\nDescription: " + description +
                    "\nDue Date: " + date +
                    (completed == 1 ? "\nStatus: Completed" :
                            "\nStatus: Pending"));

            item.setTextSize(16);
            item.setPadding(20, 20, 20, 20);
            item.setBackgroundColor(0xFFE1EBF8);

            if (completed == 1) {
                item.setPaintFlags(
                        item.getPaintFlags() |
                                Paint.STRIKE_THRU_TEXT_FLAG);
            }

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 12, 0, 8);
            item.setLayoutParams(params);

            taskContainer.addView(item);

            Button completeButton = new Button(this);
            completeButton.setText(
                    completed == 1 ? "Completed" : "Mark as Completed");

            completeButton.setEnabled(completed == 0);

            completeButton.setOnClickListener(v -> {
                if (databaseHelper.completeTask(id)) {
                    Toast.makeText(this, "Task Completed!",
                            Toast.LENGTH_SHORT).show();
                    loadTasks();
                }
            });

            taskContainer.addView(completeButton);

            Button editButton = new Button(this);
            editButton.setText("Edit");

            editButton.setOnClickListener(v -> {

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(30, 10, 30, 10);

                EditText editName = new EditText(this);
                editName.setText(name);
                editName.setHint("Task Name");
                layout.addView(editName);

                EditText editDescription = new EditText(this);
                editDescription.setText(description);
                editDescription.setHint("Description");
                layout.addView(editDescription);

                EditText editDate = new EditText(this);
                editDate.setText(date);
                editDate.setHint("Due Date");
                layout.addView(editDate);

                new AlertDialog.Builder(this)
                        .setTitle("Edit Task")
                        .setView(layout)
                        .setPositiveButton("Update", (dialog, which) -> {

                            boolean updated = databaseHelper.updateTask(
                                    id,
                                    editName.getText().toString().trim(),
                                    editDescription.getText().toString().trim(),
                                    editDate.getText().toString().trim()
                            );

                            if (updated) {
                                Toast.makeText(this, "Task Updated!",
                                        Toast.LENGTH_SHORT).show();
                                loadTasks();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            taskContainer.addView(editButton);

            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");

            deleteButton.setOnClickListener(v -> {

                if (databaseHelper.deleteTask(id)) {
                    Toast.makeText(this, "Task Deleted!",
                            Toast.LENGTH_SHORT).show();
                    loadTasks();
                }
            });

            taskContainer.addView(deleteButton);
        }

        cursor.close();
    }
}

