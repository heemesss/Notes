package com.example.myapplicationv3;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
//    private EditText editTextLessonName;
//    private TableLayout tableLayoutSchedule;
    private Button buttonListLesson, buttonScheduleLesson, buttonScheduleTime;
    private Button buttonClearDatabase, buttonClose, buttonBack, buttonSaveAndExit;
    private TableLayout tableLayoutSchedule;
    private TextView textEdit;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSettings();

//        editTextLessonName = findViewById(R.id.edit_text_lesson_name);
//        tableLayoutSchedule = findViewById(R.id.table_layout_schedule);
//        buttonAddLesson = findViewById(R.id.button_add_lesson);

//        populateTable();
//
//        buttonAddLesson.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String lessonName = editTextLessonName.getText().toString();
//                if (!lessonName.isEmpty()) {
//                    dbHelper.addLesson(lessonName);
//                    editTextLessonName.setText("");
//                    populateTable();
//                }
//            }
//        });
    }

//    private void populateTable() {
//        tableLayoutSchedule.removeAllViews();
//
//        List<String> lessons = dbHelper.getAllLessons();
//        if (lessons.size() > 0) {
//            TableRow headerRow = new TableRow(this);
//            headerRow.setGravity(Gravity.CENTER_HORIZONTAL);
//            addHeaderCell(headerRow, "День");
//            addHeaderCell(headerRow, "Урок");
//            tableLayoutSchedule.addView(headerRow);
//
//            for (int i = 1; i <= 7; i++) {
//                TableRow row = new TableRow(this);
//                row.setGravity(Gravity.CENTER_HORIZONTAL);
//                addDataCell(row, getDayName(i));
//                addDataCell(row, lessons.get(i - 1)); // Уроков может быть больше, чем дней
//                tableLayoutSchedule.addView(row);
//            }
//        }
//    }

    private void setSettings(){
        setContentView(R.layout.activity_settings);

        buttonListLesson = findViewById(R.id.button_list_lessons);
        buttonScheduleLesson = findViewById(R.id.button_schedule_lessons);
        buttonScheduleTime = findViewById(R.id.button_schedule_time);
        buttonClose = findViewById(R.id.button_close);
        buttonClearDatabase = findViewById(R.id.button_clear_database);

        dbHelper = new DatabaseHelper(this);

        buttonListLesson.setOnClickListener(v -> setListLesson());
        buttonScheduleLesson.setOnClickListener(v -> setScheduleLesson());
        buttonScheduleTime.setOnClickListener(v -> setScheduleTime());
        buttonClose.setOnClickListener(v -> finish());
        buttonClearDatabase.setOnClickListener(v -> showConfirmationDialog());
    }

    private void setListLesson(){
        setContentView(R.layout.activity_list_lessons);
        buttonBack = findViewById(R.id.button_back);
        buttonSaveAndExit = findViewById(R.id.button_save_and_exit);
        textEdit = findViewById(R.id.editTextTime);
        String lessons = "";
        for (String s : dbHelper.getAllLessons()) {
            lessons = lessons + s + "\n";
        }
        textEdit.setText(lessons.trim());

        buttonBack.setOnClickListener(v -> setSettings());

        buttonSaveAndExit.setOnClickListener(v -> {dbHelper.setLesson(String.valueOf(textEdit.getText()));
            Log.i(TAG, "setListLesson: " + textEdit.getText());setSettings();});
    }

    private void setScheduleLesson(){
        setContentView(R.layout.activity_schedule_lessons);
        buttonBack = findViewById(R.id.button_back);
        buttonSaveAndExit = findViewById(R.id.button_save_and_exit);
        tableLayoutSchedule = findViewById(R.id.table_layout_schedule);

        buttonBack.setOnClickListener(v -> setSettings());
        // ToDo

        tableLayoutSchedule.removeAllViews();
        TableRow headerRow = new TableRow(this);
        headerRow.setGravity(Gravity.CENTER_HORIZONTAL);
        addHeaderCell(headerRow, "№");
        for (int i = 1; i < 8; i++){
            addHeaderCell(headerRow, getDayName(i));
        }
        tableLayoutSchedule.addView(headerRow);
        List<List<String>> list = dbHelper.getScheduleLessons();
        Log.i(TAG, "setScheduleLesson: " + list.toString());
        List<List<EditText>> editTextList = new ArrayList<>();
        for (int i = 1 ; i < 8; i++){
            TableRow tableRow = new TableRow(this);
            addDataCell(tableRow, "" + i);
            editTextList.add(new ArrayList<>());
            for (int j = 0; j < 7; j++){
                EditText editText = new EditText(this);
                if (list.size() > i - 1){
                    if (list.get(i - 1).size() > j){
                        editText.setText(list.get(i - 1).get(j));
                        Log.i(TAG, "setScheduleLesson: " + list.get(i - 1).get(j));
                    }
                }
                editTextList.get(i - 1).add(editText);
                editText.setBackground(ColorDrawable.createFromPath("#FFFFFF"));
                tableRow.addView(editText);
            }
            tableLayoutSchedule.addView(tableRow);
        }

        buttonSaveAndExit.setOnClickListener(v -> {dbHelper.setScheduleLessons(editTextList);
            setSettings();});

    }

    private void setScheduleTime(){
        setContentView(R.layout.activity_schedule_time);
        buttonBack = findViewById(R.id.button_back);
        buttonSaveAndExit = findViewById(R.id.button_save_and_exit);
        textEdit = findViewById(R.id.editTextTime);
        String times = "";
        for (String s : dbHelper.getAllTimes()) {
            times = times + s + "\n";
        }
        textEdit.setText(times.trim());

        buttonBack.setOnClickListener(v -> setSettings());

        buttonSaveAndExit.setOnClickListener(v -> {dbHelper.setTimes(String.valueOf(textEdit.getText()));
            Log.i(TAG, "setScheduleTime: " + textEdit.getText());setSettings();});
    }

    private void addHeaderCell(TableRow row, String text) {
        TextView cell = new TextView(this);
        cell.setText(text);
        cell.setPadding(16, 8, 16, 8);
        cell.setBackgroundResource(android.R.color.darker_gray);
        row.addView(cell);
    }

    private void addDataCell(TableRow row, String text) {
        TextView cell = new TextView(this);
        cell.setText(text);
        cell.setPadding(16, 8, 16, 8);
        row.addView(cell);
    }

    private String getDayName(int dayNumber) {
        switch (dayNumber) {
            case 1:
                return "Понедельник";
            case 2:
                return "Вторник";
            case 3:
                return "Среда";
            case 4:
                return "Четверг";
            case 5:
                return "Пятница";
            case 6:
                return "Суббота";
            case 7:
                return "Воскресенье";
            default:
                return "";
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Подтверждение")
                .setMessage("Вы действительно хотите очистить базу данных?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.clearDatabase();
//                        populateTable();
                    }
                })
                .setNegativeButton("Нет", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}