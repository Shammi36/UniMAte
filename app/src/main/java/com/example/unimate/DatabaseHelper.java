
package com.example.unimate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UniMate.db";
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_ROUTINE = "ClassRoutine";
    private static final String TABLE_ASSIGNMENT = "Assignments";
    private static final String TABLE_NOTES = "Notes";
    private static final String TABLE_EXAM = "Exams";
    private static final String TABLE_TODO = "Todos";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {

        String routineQuery = "CREATE TABLE IF NOT EXISTS " +
                TABLE_ROUTINE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "courseName TEXT, courseCode TEXT, " +
                "classTime TEXT, room TEXT, teacher TEXT)";

        db.execSQL(routineQuery);

        String assignmentQuery = "CREATE TABLE IF NOT EXISTS " +
                TABLE_ASSIGNMENT + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "assignmentName TEXT, subject TEXT, deadline TEXT)";

        db.execSQL(assignmentQuery);
        String notesQuery = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NOTES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "noteTitle TEXT, noteContent TEXT)";

        db.execSQL(notesQuery);
        String examQuery = "CREATE TABLE IF NOT EXISTS " +
                TABLE_EXAM + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "examName TEXT, examSubject TEXT, " +
                "examDate TEXT, examTime TEXT)";

        db.execSQL(examQuery);
        String todoQuery = "CREATE TABLE IF NOT EXISTS " +
                TABLE_TODO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "taskName TEXT, " +
                "taskDescription TEXT, " +
                "taskDate TEXT, " +
                "isCompleted INTEGER DEFAULT 0)";

        db.execSQL(todoQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createTables(db);
    }

    // Class Routine Methods

    public boolean insertRoutine(String name, String code,
                                 String time, String room,
                                 String teacher) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("courseName", name);
        values.put("courseCode", code);
        values.put("classTime", time);
        values.put("room", room);
        values.put("teacher", teacher);

        return db.insert(TABLE_ROUTINE, null, values) != -1;
    }

    public Cursor getAllRoutines() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ROUTINE, null);
    }

    public boolean deleteRoutine(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_ROUTINE, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateRoutine(int id, String name, String code,
                                 String time, String room, String teacher) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("courseName", name);
        values.put("courseCode", code);
        values.put("classTime", time);
        values.put("room", room);
        values.put("teacher", teacher);

        return db.update(TABLE_ROUTINE, values, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    // Assignment Methods

    public boolean insertAssignment(String name, String subject,
                                    String deadline) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("assignmentName", name);
        values.put("subject", subject);
        values.put("deadline", deadline);

        return db.insert(TABLE_ASSIGNMENT, null, values) != -1;
    }

    public Cursor getAllAssignments() {
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_ASSIGNMENT, null);
    }

    public boolean deleteAssignment(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_ASSIGNMENT, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateAssignment(int id, String name,
                                    String subject, String deadline) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("assignmentName", name);
        values.put("subject", subject);
        values.put("deadline", deadline);

        return db.update(TABLE_ASSIGNMENT, values, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }
    // Study Notes Methods

    public boolean insertNote(String title, String content) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("noteTitle", title);
        values.put("noteContent", content);

        return db.insert(TABLE_NOTES, null, values) != -1;
    }

    public Cursor getAllNotes() {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_NOTES, null);
    }

    public boolean deleteNote(int id) {

        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_NOTES, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateNote(int id, String title, String content) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("noteTitle", title);
        values.put("noteContent", content);

        return db.update(TABLE_NOTES, values, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }
    // Exam Schedule Methods

    public boolean insertExam(String name, String subject,
                              String date, String time) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("examName", name);
        values.put("examSubject", subject);
        values.put("examDate", date);
        values.put("examTime", time);

        return db.insert(TABLE_EXAM, null, values) != -1;
    }

    public Cursor getAllExams() {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_EXAM, null);
    }

    public boolean deleteExam(int id) {

        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_EXAM, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateExam(int id, String name, String subject,
                              String date, String time) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("examName", name);
        values.put("examSubject", subject);
        values.put("examDate", date);
        values.put("examTime", time);

        return db.update(TABLE_EXAM, values, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }
    // To-Do List Methods

    public boolean insertTask(String name, String description, String date) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("taskName", name);
        values.put("taskDescription", description);
        values.put("taskDate", date);
        values.put("isCompleted", 0);

        return db.insert(TABLE_TODO, null, values) != -1;
    }


    public Cursor getAllTasks() {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_TODO, null);
    }


    public boolean deleteTask(int id) {

        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_TODO, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }


    public boolean updateTask(int id, String name,
                              String description, String date) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("taskName", name);
        values.put("taskDescription", description);
        values.put("taskDate", date);

        return db.update(TABLE_TODO, values, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }


    public boolean completeTask(int id) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("isCompleted", 1);

        return db.update(TABLE_TODO, values, "id=?",
                new String[]{String.valueOf(id)}) > 0;
    }
}
