package com.example.myapplicationv3;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "databasev3.db";
    private static final int DATABASE_VERSION = 1;

    // Таблица Notes
    public static final String TABLE_NOTES = "Notes";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_NOTE = "note";

    // Таблица Lessons
    public static final String TABLE_LESSONS = "Lessons";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LESSON = "lesson";

    // Таблица ScheduleLessons
    public static final String TABLE_SCHEDULE_LESSONS = "ScheduleLessons";
    public static final String COLUMN_MONDAY = "monday";
    public static final String COLUMN_TUESDAY = "tuesday";
    public static final String COLUMN_WEDNESDAY = "wednesday";
    public static final String COLUMN_THURSDAY = "thursday";
    public static final String COLUMN_FRIDAY = "friday";
    public static final String COLUMN_SATURDAY = "saturday";
    public static final String COLUMN_SUNDAY = "sunday";

    // Таблица ScheduleTime
    public static final String TABLE_SCHEDULE_TIME = "ScheduleTime";
    public static final String COLUMN_TIME = "time";

    // SQL-запросы для создания таблиц
    private static final String CREATE_TABLE_NOTES =
            "CREATE TABLE " + TABLE_NOTES + "(" +
                    COLUMN_DAY + " INTEGER PRIMARY KEY," +
                    COLUMN_NOTE + " TEXT)";

    private static final String CREATE_TABLE_LESSONS =
            "CREATE TABLE " + TABLE_LESSONS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LESSON + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_SCHEDULE_LESSONS =
            "CREATE TABLE " + TABLE_SCHEDULE_LESSONS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_MONDAY + " TEXT," +
                    COLUMN_TUESDAY + " TEXT," +
                    COLUMN_WEDNESDAY + " TEXT," +
                    COLUMN_THURSDAY + " TEXT," +
                    COLUMN_FRIDAY + " TEXT," +
                    COLUMN_SATURDAY + " TEXT," +
                    COLUMN_SUNDAY + " TEXT)";

    private static final String CREATE_TABLE_SCHEDULE_TIME =
            "CREATE TABLE " + TABLE_SCHEDULE_TIME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_MONDAY + " TEXT," +
                    COLUMN_TUESDAY + " TEXT," +
                    COLUMN_WEDNESDAY + " TEXT," +
                    COLUMN_THURSDAY + " TEXT," +
                    COLUMN_FRIDAY + " TEXT," +
                    COLUMN_SATURDAY + " TEXT," +
                    COLUMN_SUNDAY + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_LESSONS);
        db.execSQL(CREATE_TABLE_SCHEDULE_LESSONS);
        db.execSQL(CREATE_TABLE_SCHEDULE_TIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE_LESSONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE_TIME);
        onCreate(db);
    }

    // Метод для получения заметки по дате
    @SuppressLint("Range")
    public String getNote(int day) {
        String note = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES,
                new String[]{COLUMN_NOTE},
                COLUMN_DAY + "=?",
                new String[]{String.valueOf(day)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));
        }

        cursor.close();
        return note;
    }

    // Метод для сохранения заметки
    public void saveNote(int day, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY, day);
        values.put(COLUMN_NOTE, note);

        long id = db.insertWithOnConflict(TABLE_NOTES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // Метод для получения уроков по дню
    public String getLessonsForDay(int day) {
        StringBuilder lessons = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEDULE_LESSONS,
                new String[]{getColumnName(day)},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String lesson = cursor.getString(cursor.getColumnIndex(getColumnName(day)));
                if (!lesson.isEmpty()) {
                    lessons.append(lesson).append("\n");
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lessons.toString().trim(); // Убираем последний символ переноса строки
    }

    // Метод для получения времени уроков по дню
//    public String getTimesForDay(int day) {
//        String times = "";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_SCHEDULE_TIME,
//                new String[]{COLUMN_TIME},
//                null, null, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
//                if (!time.isEmpty()) {
//                    times += time + "\n";
//                }
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        return times.trim(); // Убираем последний символ переноса строки
//    }

    // Вспомогательный метод для получения имени столбца по номеру дня
    private String getColumnName(int day) {
        switch (day) {
            case 1:
                return COLUMN_MONDAY;
            case 2:
                return COLUMN_TUESDAY;
            case 3:
                return COLUMN_WEDNESDAY;
            case 4:
                return COLUMN_THURSDAY;
            case 5:
                return COLUMN_FRIDAY;
            case 6:
                return COLUMN_SATURDAY;
            case 7:
                return COLUMN_SUNDAY;
            default:
                return "";
        }
    }

    // Метод для добавления нового урока
    public void setLesson(String lesson) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LESSONS, null, null);

        ContentValues values = new ContentValues();
        for (String s: lesson.split("\n")) {
            if (s.isEmpty())
                continue;
            values.put(COLUMN_LESSON, s);
            Log.i(TAG, "setLesson: " + s);
            db.insert(TABLE_LESSONS, null, values);
        }
        db.close();
    }

//    public void setTimes(String times) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_SCHEDULE_TIME, null, null);
//
//        ContentValues values = new ContentValues();
//        for (String s: times.split("\n")) {
//            if (s.isEmpty())
//                continue;
//            values.put(COLUMN_TIME, s);
//            Log.i(TAG, "setTimes: " + s);
//            db.insert(TABLE_SCHEDULE_TIME, null, values);
//        }
//        db.close();
//    }

    // Метод для получения всех уроков
    public List<String> getAllLessons() {
        List<String> lessons = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LESSONS,
                new String[]{COLUMN_LESSON}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String lesson = cursor.getString(cursor.getColumnIndex(COLUMN_LESSON));
                lessons.add(lesson);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lessons;
    }

//    public List<String> getAllTimes() {
//        List<String> times = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_SCHEDULE_TIME,
//                new String[]{COLUMN_TIME}, null, null, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
//                times.add(time);
//            } while (cursor.moveToNext());
//        }
//
//        db.close();
//        cursor.close();
//        return times;
//    }

    public void setScheduleLessons(List<List<EditText>> scheduleLessons) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete(TABLE_SCHEDULE_LESSONS, null, null);
        boolean isRef = false;

        for (int i = 0; i < scheduleLessons.size(); i++) {
            values.put(COLUMN_ID, i);
            for (int j = 0; j < scheduleLessons.get(i).size(); j++) {
                String lessons = String.valueOf(scheduleLessons.get(i).get(j).getText());
                if (lessons.isEmpty())
                    continue;
                values.put(getColumnName(j + 1), lessons);
                isRef = true;
            }
            if (isRef){
                db.insert(TABLE_SCHEDULE_LESSONS, null, values);
                values.clear();
                isRef = false;
            }
        }
        db.close();
    }

    @SuppressLint("Range")
    public List<List<String>> getScheduleLessons() {
        List<List<String>> lessons = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEDULE_LESSONS,
                new String[]{COLUMN_ID, COLUMN_MONDAY, COLUMN_TUESDAY, COLUMN_WEDNESDAY,
                        COLUMN_THURSDAY, COLUMN_FRIDAY, COLUMN_SATURDAY, COLUMN_SUNDAY},
                null, null, null, null, null);

        try {
            cursor.moveToFirst();
            for (int i = 0; i < 10; i++) {
                lessons.add(new ArrayList<>());

                if (cursor.getInt(cursor.getColumnIndex(COLUMN_ID)) > i)
                    continue;

                for (int day = 1; day < 8; day++) {
                    lessons.get(i).add(cursor.getString(cursor.getColumnIndex(getColumnName(day))));

                }
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.i(TAG, "getScheduleLessons: " + e);
        }
        db.close();
        cursor.close();

        return lessons;
    }

    public void setTimes(List<List<EditText>> times) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete(TABLE_SCHEDULE_TIME, null, null);
        boolean isRef = false;

        for (int i = 0; i < times.size(); i++) {
            values.put(COLUMN_ID, i);
            for (int j = 0; j < times.get(i).size(); j++) {
                String time = String.valueOf(times.get(i).get(j).getText());
                if (time.isEmpty())
                    continue;
                values.put(getColumnName(j + 1), time);
                isRef = true;
            }
            if (isRef){
                db.insert(TABLE_SCHEDULE_TIME, null, values);
                values.clear();
                isRef = false;
            }
        }
        db.close();
    }

    @SuppressLint("Range")
    public List<List<String>> getTimes() {
        List<List<String>> times = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEDULE_TIME,
                new String[]{COLUMN_ID, COLUMN_MONDAY, COLUMN_TUESDAY, COLUMN_WEDNESDAY,
                        COLUMN_THURSDAY, COLUMN_FRIDAY, COLUMN_SATURDAY, COLUMN_SUNDAY},
                null, null, null, null, null);

        try {
            cursor.moveToFirst();
            for (int i = 0; i < 10; i++) {
                times.add(new ArrayList<>());

                if (cursor.getInt(cursor.getColumnIndex(COLUMN_ID)) > i){
                    continue;
                }
                for (int day = 1; day < 8; day++) {
                    times.get(i).add(cursor.getString(cursor.getColumnIndex(getColumnName(day))));
                }
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.i(TAG, "getScheduleLessons: " + e);
        }
        db.close();
        cursor.close();

        return times;
    }

    // Метод для очистки базы данных
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, null, null);
        db.delete(TABLE_LESSONS, null, null);
        db.delete(TABLE_SCHEDULE_LESSONS, null, null);
        db.delete(TABLE_SCHEDULE_TIME, null, null);
        db.close();
    }
}