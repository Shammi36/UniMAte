
package com.example.unimate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button routine, assignment, notes, exam, todo, cgpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        routine = findViewById(R.id.btnRoutine);
        assignment = findViewById(R.id.btnAssignment);
        notes = findViewById(R.id.btnNotes);
        exam = findViewById(R.id.btnExam);
        todo = findViewById(R.id.btnTodo);
        cgpa = findViewById(R.id.btnCGPA);

        routine.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,
                        ClassRoutineActivity.class)));

        assignment.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,
                        AssignmentActivity.class)));

        notes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,
                        NotesActivity.class)));

        exam.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,
                        ExamActivity.class)));

        todo.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,
                        ToDoActivity.class)));

        cgpa.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,
                        CGPAActivity.class)));
    }
}
