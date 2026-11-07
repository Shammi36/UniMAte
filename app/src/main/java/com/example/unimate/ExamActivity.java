
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

public class ExamActivity extends AppCompatActivity {

    EditText examName, examSubject, examDate, examTime;
    Button addExam;
    LinearLayout examContainer;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        databaseHelper = new DatabaseHelper(this);

        examName = findViewById(R.id.etExamName);
        examSubject = findViewById(R.id.etExamSubject);
        examDate = findViewById(R.id.etExamDate);
        examTime = findViewById(R.id.etExamTime);

        addExam = findViewById(R.id.btnAddExam);
        examContainer = findViewById(R.id.examContainer);

        loadExams();

        addExam.setOnClickListener(v -> {

            String name = examName.getText().toString().trim();
            String subject = examSubject.getText().toString().trim();
            String date = examDate.getText().toString().trim();
            String time = examTime.getText().toString().trim();

            if (name.isEmpty() || subject.isEmpty()
                    || date.isEmpty() || time.isEmpty()) {

                Toast.makeText(this, "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean saved = databaseHelper.insertExam(
                    name, subject, date, time);

            if (saved) {

                Toast.makeText(this, "Exam Saved!",
                        Toast.LENGTH_SHORT).show();

                examName.setText("");
                examSubject.setText("");
                examDate.setText("");
                examTime.setText("");

                loadExams();
            }
        });
    }

    private void loadExams() {

        examContainer.removeAllViews();

        Cursor cursor = databaseHelper.getAllExams();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String subject = cursor.getString(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);

            TextView item = new TextView(this);

            item.setText("Exam: " + name +
                    "\nSubject: " + subject +
                    "\nDate: " + date +
                    "\nTime: " + time);

            item.setTextSize(16);
            item.setPadding(20, 20, 20, 20);
            item.setBackgroundColor(0xFFE1EBF8);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 12, 0, 8);
            item.setLayoutParams(params);

            examContainer.addView(item);

            Button editButton = new Button(this);
            editButton.setText("Edit");

            editButton.setOnClickListener(v -> {

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(30, 10, 30, 10);

                EditText editName = new EditText(this);
                editName.setText(name);
                editName.setHint("Exam Name");
                layout.addView(editName);

                EditText editSubject = new EditText(this);
                editSubject.setText(subject);
                editSubject.setHint("Subject");
                layout.addView(editSubject);

                EditText editDate = new EditText(this);
                editDate.setText(date);
                editDate.setHint("Date");
                layout.addView(editDate);

                EditText editTime = new EditText(this);
                editTime.setText(time);
                editTime.setHint("Time");
                layout.addView(editTime);

                new AlertDialog.Builder(this)
                        .setTitle("Edit Exam")
                        .setView(layout)
                        .setPositiveButton("Update", (dialog, which) -> {

                            boolean updated = databaseHelper.updateExam(
                                    id,
                                    editName.getText().toString().trim(),
                                    editSubject.getText().toString().trim(),
                                    editDate.getText().toString().trim(),
                                    editTime.getText().toString().trim()
                            );

                            if (updated) {
                                Toast.makeText(this, "Exam Updated!",
                                        Toast.LENGTH_SHORT).show();
                                loadExams();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            examContainer.addView(editButton);

            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");

            deleteButton.setOnClickListener(v -> {

                boolean deleted = databaseHelper.deleteExam(id);

                if (deleted) {
                    Toast.makeText(this, "Exam Deleted!",
                            Toast.LENGTH_SHORT).show();
                    loadExams();
                }
            });

            examContainer.addView(deleteButton);
        }

        cursor.close();
    }
}
