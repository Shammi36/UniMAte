
package com.example.unimate;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class ClassRoutineActivity extends AppCompatActivity {

    EditText courseName, courseCode, classTime, room, teacher;
    LinearLayout routineContainer;
    Button addRoutine;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_routine);

        databaseHelper = new DatabaseHelper(this);

        courseName = findViewById(R.id.etCourseName);
        courseCode = findViewById(R.id.etCourseCode);
        classTime = findViewById(R.id.etClassTime);
        room = findViewById(R.id.etRoom);
        teacher = findViewById(R.id.etTeacher);

        addRoutine = findViewById(R.id.btnAddRoutine);
        routineContainer = findViewById(R.id.routineContainer);

        loadRoutines();

        addRoutine.setOnClickListener(v -> {

            String name = courseName.getText().toString().trim();
            String code = courseCode.getText().toString().trim();
            String time = classTime.getText().toString().trim();
            String roomNo = room.getText().toString().trim();
            String teacherName = teacher.getText().toString().trim();

            if (name.isEmpty() || code.isEmpty() ||
                    time.isEmpty() || roomNo.isEmpty() ||
                    teacherName.isEmpty()) {

                Toast.makeText(this, "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = databaseHelper.insertRoutine(
                    name, code, time, roomNo, teacherName
            );

            if (inserted) {
                Toast.makeText(this, "Routine Saved Successfully!",
                        Toast.LENGTH_SHORT).show();

                courseName.setText("");
                courseCode.setText("");
                classTime.setText("");
                room.setText("");
                teacher.setText("");

                loadRoutines();

            } else {
                Toast.makeText(this, "Failed to Save Routine",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRoutines() {

        routineContainer.removeAllViews();

        Cursor cursor = databaseHelper.getAllRoutines();

        while (cursor.moveToNext()) {

            String name = cursor.getString(1);
            String code = cursor.getString(2);
            String time = cursor.getString(3);
            String roomNo = cursor.getString(4);
            String teacherName = cursor.getString(5);

            TextView routine = new TextView(this);

            routine.setText(
                    "Course: " + name +
                            "\nCode: " + code +
                            "\nTime: " + time +
                            "\nRoom: " + roomNo +
                            "\nTeacher: " + teacherName
            );

            routine.setTextSize(16);
            routine.setPadding(20, 20, 20, 20);
            routine.setBackgroundColor(0xFFE1EBF8);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            params.setMargins(0, 12, 0, 12);
            routine.setLayoutParams(params);
            int routineId = cursor.getInt(0);

            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");

            deleteButton.setOnClickListener(v -> {

                boolean deleted = databaseHelper.deleteRoutine(routineId);

                if (deleted) {
                    Toast.makeText(this, "Routine Deleted!",
                            Toast.LENGTH_SHORT).show();

                    loadRoutines();
                }
            });
            Button editButton = new Button(this);
            editButton.setText("Edit");

            editButton.setOnClickListener(v -> {

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(30, 10, 30, 10);

                EditText editName = new EditText(this);
                editName.setHint("Course Name");
                editName.setText(name);
                layout.addView(editName);

                EditText editCode = new EditText(this);
                editCode.setHint("Course Code");
                editCode.setText(code);
                layout.addView(editCode);

                EditText editTime = new EditText(this);
                editTime.setHint("Class Time");
                editTime.setText(time);
                layout.addView(editTime);

                EditText editRoom = new EditText(this);
                editRoom.setHint("Room Number");
                editRoom.setText(roomNo);
                layout.addView(editRoom);

                EditText editTeacher = new EditText(this);
                editTeacher.setHint("Teacher Name");
                editTeacher.setText(teacherName);
                layout.addView(editTeacher);

                new AlertDialog.Builder(this)
                        .setTitle("Edit Routine")
                        .setView(layout)
                        .setPositiveButton("Update", (dialog, which) -> {

                            boolean updated = databaseHelper.updateRoutine(
                                    routineId,
                                    editName.getText().toString().trim(),
                                    editCode.getText().toString().trim(),
                                    editTime.getText().toString().trim(),
                                    editRoom.getText().toString().trim(),
                                    editTeacher.getText().toString().trim()
                            );

                            if (updated) {
                                Toast.makeText(this, "Routine Updated!",
                                        Toast.LENGTH_SHORT).show();
                                loadRoutines();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            routineContainer.addView(editButton);
            routineContainer.addView(deleteButton);
            routineContainer.addView(routine);
        }

        cursor.close();
    }
}
