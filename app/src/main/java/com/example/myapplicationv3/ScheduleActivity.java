package com.example.myapplicationv3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private ListView listView;
    private DatabaseHelper dbHelper;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        radioGroup = findViewById(R.id.radio_group);
        listView = findViewById(R.id.list_view);
        buttonBack = findViewById(R.id.button_back);
        dbHelper = new DatabaseHelper(this);

        loadLessons();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (findViewById(R.id.radio_lessons).getId() == checkedId){
                    loadLessons();
                } else {
                    loadTimes();
                }
            }
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadLessons() {
        List<Map<String, String>> data = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("day", getDayName(i));
            item.put("lessons", dbHelper.getLessonsForDay(i));
            data.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"day", "lessons"},
                new int[]{android.R.id.text1, android.R.id.text2});

        listView.setAdapter(adapter);
    }

    private void loadTimes() {
        List<Map<String, String>> data = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Map<String, String> item = new HashMap<>();
            item.put("day", getDayName(i));
            item.put("times", dbHelper.getTimesForDay(i));
            data.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"day", "times"},
                new int[]{android.R.id.text1, android.R.id.text2});

        listView.setAdapter(adapter);
    }

    private String getDayName(int dayNumber) {
        switch (dayNumber) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
            default:
                return "";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}