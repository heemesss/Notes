package com.example.myapplicationv3;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private EditText editTextNote;
    private Button buttonSaveNote, buttonBack;
    private DatabaseHelper dbHelper;
    private int currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Запоминаем элементы интерфейса для последующего использования
        calendarView = findViewById(R.id.calendar_view);
        editTextNote = findViewById(R.id.edit_text_note);
        buttonSaveNote = findViewById(R.id.button_save_note);
        buttonBack = findViewById(R.id.button_close);
        dbHelper = new DatabaseHelper(this);

        // Получаем сегодняшнюю дату
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(calendarView.getDate())); // получаем дату с календаря

        currentDate = calendar.get(Calendar.YEAR) * 365 +
                calendar.get(Calendar.MONTH) * 31 +
                calendar.get(Calendar.DAY_OF_MONTH); // уникальное число для дня

        // Устанавливаем обработчик для выбора даты
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            int date = year * 365 + month * 31 + dayOfMonth; // уникальное число для дня

            // Получение заметки для выбранной даты
            String note = dbHelper.getNote(date);
            currentDate = date;
            if (!note.isEmpty()) {
                editTextNote.setText(note);
            } else {
                editTextNote.setText("");
            }
        });



        // Устанавливаем обработчик для кнопки сохранения
        buttonSaveNote.setOnClickListener(v -> {
            int date = currentDate;
            String note = editTextNote.getText().toString();

            if (!note.isEmpty()) {
                dbHelper.saveNote(date, note);
                Toast.makeText(this, "Заметка сохранена!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Пожалуйста, выберите дату и введите заметку.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Выводим запись с сегодняшнего дня
        String note = dbHelper.getNote(currentDate);
        if (!note.isEmpty()) {
            editTextNote.setText(note);
        } else {
            editTextNote.setText("");
        }

        buttonBack.setOnClickListener(v -> finish());
    }

    private int getCalendarDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(calendarView.getDate())); // получаем дату с календаря

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return year * 365 + month * 31 + dayOfMonth; // уникальное число для дня
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}