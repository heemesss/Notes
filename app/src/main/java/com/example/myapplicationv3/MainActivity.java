package com.example.myapplicationv3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCalendar = findViewById(R.id.button_calendar);
        Button buttonSchedule = findViewById(R.id.button_schedule);
        Button buttonSettings = findViewById(R.id.button_settings);

        buttonCalendar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CalendarActivity.class)));

        buttonSchedule.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ScheduleActivity.class)));

        buttonSettings.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class)));
    }
}