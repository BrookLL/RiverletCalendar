package com.riverlet.calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.riverlet.lib.calendar.MonthView;
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

//        InfiniteViewPager infiniteViewPager2 = findViewById(R.id.viewpager2);
//        infiniteViewPager2.setData(calendarList, new InfiniteViewPager.ViewHolderCreator() {
//            @Override
//            public InfiniteViewPager.ViewHolder create() {
//                MonthView calendarView = new MonthView(MainActivity.this);
//                return new InfiniteViewPager.ViewHolder<MonthView, Calendar>(calendarView) {
//
//                    @Override
//                    public void update(InfiniteViewPager.ViewHolder<MonthView, Calendar> holder, Calendar calendar) {
//                        holder.getView().setCalendar(calendar);
//                    }
//                };
//            }
//        });
//
//        infiniteViewPager2.setOnNeedAddDataListener(new InfiniteViewPager.OnNeedAddDataListener() {
//            Calendar first = Calendar.getInstance();
//            Calendar last = Calendar.getInstance();
//
//            @Override
//            public Object addFirst(int position, Object lastObject) {
//                first.add(Calendar.MONTH, -1);
//                return first;
//            }
//
//            @Override
//            public Object addLast(int position, Object lastObject) {
//                last.add(Calendar.MONTH, 1);
//                return last;
//            }
//        });



//        LoopViewPager loopViewPager2 = findViewById(R.id.viewpager3);
//        loopViewPager2.setData(dataList2, new LoopViewPager.ViewHolderCreator() {
//            @Override
//            public LoopViewPager.ViewHolder create() {
//                TextView textView = new TextView(MainActivity.this);
//                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                textView.setGravity(Gravity.CENTER);
//                textView.setTextSize(30);
//                textView.setBackgroundColor(Color.GREEN);
//                textView.setTextColor(0xffffffff);
//                return new LoopViewPager.ViewHolder<TextView, Integer>(textView) {
//                    @Override
//                    public void update(LoopViewPager.ViewHolder<TextView, Integer> holder, Integer integer) {
//                        holder.view.setText(integer + "");
//                    }
//                };
//            }
//        });

    }
}
