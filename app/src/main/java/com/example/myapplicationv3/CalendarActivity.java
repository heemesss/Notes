package com.example.myapplicationv3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private EditText editTextNote;
    private Button buttonSaveNote;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendar_view);
        editTextNote = findViewById(R.id.edit_text_note);
        buttonSaveNote = findViewById(R.id.button_save_note);
        dbHelper = new DatabaseHelper(this);

        // Устанавливаем обработчик для выбора даты
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int date = year * 365 + month * 31 + dayOfMonth;

                // Получение заметки для выбранной даты
                String note = dbHelper.getNote(date);
                if (!note.isEmpty()) {
                    editTextNote.setText(note);
                } else {
                    editTextNote.setText("");
                }
            }
        });

        buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(calendarView.getDate()));

                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                int date = year * 365 + month * 31 + dayOfMonth;
                String note = editTextNote.getText().toString();

                if (!note.isEmpty()) {
                    dbHelper.saveNote(date, note);
                    showMessage("Заметка сохранена!");
                } else {
                    showMessage("Пожалуйста, выберите дату и введите заметку.");
                }
            }
        });
    }

    private void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
        builder.setMessage(message)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}