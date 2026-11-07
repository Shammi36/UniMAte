
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
public class AssignmentActivity extends AppCompatActivity {

    EditText assignmentName, subject, deadline;
    Button addAssignment;
    LinearLayout assignmentContainer;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        databaseHelper = new DatabaseHelper(this);

        assignmentName = findViewById(R.id.etAssignmentName);
        subject = findViewById(R.id.etSubject);
        deadline = findViewById(R.id.etDeadline);

        addAssignment = findViewById(R.id.btnAddAssignment);
        assignmentContainer = findViewById(R.id.assignmentContainer);

        loadAssignments();

        addAssignment.setOnClickListener(v -> {

            String name = assignmentName.getText().toString().trim();
            String subjectName = subject.getText().toString().trim();
            String dueDate = deadline.getText().toString().trim();

            if (name.isEmpty() || subjectName.isEmpty()
                    || dueDate.isEmpty()) {

                Toast.makeText(this, "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = databaseHelper.insertAssignment(
                    name, subjectName, dueDate);

            if (inserted) {

                Toast.makeText(this, "Assignment Saved!",
                        Toast.LENGTH_SHORT).show();

                assignmentName.setText("");
                subject.setText("");
                deadline.setText("");

                loadAssignments();

            } else {
                Toast.makeText(this, "Failed to Save",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAssignments() {

        assignmentContainer.removeAllViews();

        Cursor cursor = databaseHelper.getAllAssignments();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String subjectName = cursor.getString(2);
            String dueDate = cursor.getString(3);

            TextView item = new TextView(this);

            item.setText(
                    "Assignment: " + name +
                            "\nSubject: " + subjectName +
                            "\nDeadline: " + dueDate
            );

            item.setTextSize(16);
            item.setPadding(20, 20, 20, 20);
            item.setBackgroundColor(0xFFE1EBF8);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 12, 0, 12);
            item.setLayoutParams(params);

            assignmentContainer.addView(item);
            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");

            deleteButton.setOnClickListener(v -> {

                boolean deleted = databaseHelper.deleteAssignment(id);

                if (deleted) {
                    Toast.makeText(this, "Assignment Deleted!",
                            Toast.LENGTH_SHORT).show();

                    loadAssignments();
                }
            });
            Button editButton = new Button(this);
            editButton.setText("Edit");

            editButton.setOnClickListener(v -> {

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(30, 10, 30, 10);

                EditText editName = new EditText(this);
                editName.setText(name);
                editName.setHint("Assignment Name");
                layout.addView(editName);

                EditText editSubject = new EditText(this);
                editSubject.setText(subjectName);
                editSubject.setHint("Subject Name");
                layout.addView(editSubject);

                EditText editDeadline = new EditText(this);
                editDeadline.setText(dueDate);
                editDeadline.setHint("Deadline");
                layout.addView(editDeadline);

                new AlertDialog.Builder(this)
                        .setTitle("Edit Assignment")
                        .setView(layout)
                        .setPositiveButton("Update", (dialog, which) -> {

                            boolean updated = databaseHelper.updateAssignment(
                                    id,
                                    editName.getText().toString().trim(),
                                    editSubject.getText().toString().trim(),
                                    editDeadline.getText().toString().trim()
                            );

                            if (updated) {
                                Toast.makeText(this, "Assignment Updated!",
                                        Toast.LENGTH_SHORT).show();
                                loadAssignments();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            assignmentContainer.addView(editButton);

            assignmentContainer.addView(deleteButton);
        }

        cursor.close();
    }
}
