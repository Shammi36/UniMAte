
package com.example.unimate;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CGPAActivity extends AppCompatActivity {

    EditText courseName, courseCredit;
    Spinner gradeSpinner;
    Button addCourse, calculateCGPA;
    LinearLayout courseContainer;
    TextView cgpaResult;

    ArrayList<String> courseList = new ArrayList<>();
    ArrayList<Double> creditList = new ArrayList<>();
    ArrayList<Double> pointList = new ArrayList<>();

    String[] grades = {
            "A+ (4.00)",
            "A (3.75)",
            "A- (3.50)",
            "B+ (3.25)",
            "B (3.00)",
            "B- (2.75)",
            "C+ (2.50)",
            "C (2.25)",
            "D (2.00)",
            "F (0.00)"
    };

    double[] gradePoints = {
            4.00, 3.75, 3.50, 3.25, 3.00,
            2.75, 2.50, 2.25, 2.00, 0.00
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgpa);

        courseName = findViewById(R.id.etCourseName);
        courseCredit = findViewById(R.id.etCredit);
        gradeSpinner = findViewById(R.id.spGrade);

        addCourse = findViewById(R.id.btnAddCourse);
        calculateCGPA = findViewById(R.id.btnCalculateCGPA);

        courseContainer = findViewById(R.id.courseContainer);
        cgpaResult = findViewById(R.id.tvCGPAResult);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                grades
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        gradeSpinner.setAdapter(adapter);

        addCourse.setOnClickListener(v -> addCourse());

        calculateCGPA.setOnClickListener(v -> calculateCGPA());
    }

    private void addCourse() {

        String name = courseName.getText().toString().trim();
        String creditText = courseCredit.getText().toString().trim();

        if (name.isEmpty() || creditText.isEmpty()) {
            Toast.makeText(this, "Fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double credit;

        try {
            credit = Double.parseDouble(creditText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter a valid credit",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (credit <= 0) {
            Toast.makeText(this, "Credit must be greater than 0",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int gradePosition = gradeSpinner.getSelectedItemPosition();
        double point = gradePoints[gradePosition];

        courseList.add(name);
        creditList.add(credit);
        pointList.add(point);

        TextView item = new TextView(this);

        item.setText(name + " | Credit: " + credit +
                " | Grade: " + grades[gradePosition]);

        item.setTextSize(16);
        item.setPadding(15, 15, 15, 15);
        item.setBackgroundColor(0xFFE1EBF8);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 8, 0, 8);
        item.setLayoutParams(params);

        courseContainer.addView(item);

        courseName.setText("");
        courseCredit.setText("");

        Toast.makeText(this, "Course Added!",
                Toast.LENGTH_SHORT).show();
    }

    private void calculateCGPA() {

        if (creditList.isEmpty()) {
            Toast.makeText(this, "Add at least one course",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPoints = 0;
        double totalCredits = 0;

        for (int i = 0; i < creditList.size(); i++) {

            totalPoints += creditList.get(i) * pointList.get(i);
            totalCredits += creditList.get(i);
        }

        double cgpa = totalPoints / totalCredits;

        cgpaResult.setText(String.format(
                java.util.Locale.US,
                "Your CGPA: %.2f",
                cgpa
        ));
    }
}
