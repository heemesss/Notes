package com.example.myapplicationv3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
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
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_MONDAY + " TEXT," +
                    COLUMN_TUESDAY + " TEXT," +
                    COLUMN_WEDNESDAY + " TEXT," +
                    COLUMN_THURSDAY + " TEXT," +
                    COLUMN_FRIDAY + " TEXT," +
                    COLUMN_SATURDAY + " TEXT," +
                    COLUMN_SUNDAY + " TEXT)";

    private static final String CREATE_TABLE_SCHEDULE_TIME =
            "CREATE TABLE " + TABLE_SCHEDULE_TIME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TIME + " TEXT NOT NULL)";

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
        String lessons = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEDULE_LESSONS,
                new String[]{getColumnName(day)},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String lesson = cursor.getString(cursor.getColumnIndex(getColumnName(day)));
                if (!lesson.isEmpty()) {
                    lessons += lesson + "\n";
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lessons.trim(); // Убираем последний символ переноса строки
    }

    // Метод для получения времени уроков по дню
    public String getTimesForDay(int day) {
        String times = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCHEDULE_TIME,
                new String[]{COLUMN_TIME},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                if (!time.isEmpty()) {
                    times += time + "\n";
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return times.trim(); // Убираем последний символ переноса строки
    }

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
    public void addLesson(String lesson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LESSON, lesson);

        db.insert(TABLE_LESSONS, null, values);
        db.close();
    }

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