package com.riverlet.calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.riverlet.lib.calendar.MonthView;
import com.riverlet.lib.calendar.RCalendarView;
import com.riverlet.lib.entity.Month;
import com.riverlet.lib.viewpager.InfiniteViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Integer> dataList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
    private List<Calendar> calendarList = new ArrayList<>(Arrays.asList(Calendar.getInstance()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView monthText = findViewById(R.id.text_month);
        RCalendarView calendarView = findViewById(R.id.calendarView);
        monthText.setText(calendarView.getCurrentMonth().toString());
        calendarView.setOnCurrentMonthChangeCallback(new RCalendarView.OnCurrentMonthChangeCallback() {
            @Override
            public void onCurrentMonthChange(Month month) {
                monthText.setText(month.toString());
            }
        });
    }
}
