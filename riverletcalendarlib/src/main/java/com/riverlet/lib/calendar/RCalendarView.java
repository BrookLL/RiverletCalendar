package com.riverlet.lib.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.riverlet.lib.entity.Day;
import com.riverlet.lib.entity.Month;
import com.riverlet.lib.viewpager.InfiniteViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RCalendarView extends LinearLayout {
    private static final String TAG = "RCalendarView";

    public RCalendarView(@NonNull Context context) {
        this(context, null);
    }

    public RCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        WeekView weekView = new WeekView(getContext());
        addView(weekView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));

        List<Month> monthList = new ArrayList<>();
        Month month = new Month();
        Calendar calendar = Calendar.getInstance();
        month.setYear(calendar.get(Calendar.YEAR));
        month.setMonth(calendar.get(Calendar.MONTH) + 1);
        monthList.add(month);
        final InfiniteViewPager infiniteViewPager = new InfiniteViewPager(getContext());
        addView(infiniteViewPager);
        infiniteViewPager.setData(monthList, new InfiniteViewPager.ViewHolderCreator() {
            @Override
            public InfiniteViewPager.ViewHolder create() {
                MonthView monthView = new MonthView(getContext());
                monthView.setOnDayClickListenter(new MonthView.OnDayClickListenter() {
                    @Override
                    public void onDayClick(int currentViewMonth, Day day) {
                        if (currentViewMonth < day.getMonth()) {
                            infiniteViewPager.setCurrentItem(infiniteViewPager.getCurrentItem() + 1);
                        }
                        if (currentViewMonth > day.getMonth()) {
                            infiniteViewPager.setCurrentItem(infiniteViewPager.getCurrentItem() - 1);
                        }
                    }
                });
                return new InfiniteViewPager.ViewHolder<MonthView, Month>(monthView) {

                    @Override
                    public void update(InfiniteViewPager.ViewHolder<MonthView, Month> holder, Month month) {
                        holder.getView().setMonth(month);
                    }
                };
            }
        });

        infiniteViewPager.setOnNeedAddDataListener(new InfiniteViewPager.OnNeedAddDataListener<Month>() {
            @Override
            public Month addFirst(int position, Month month) {
                Month newMonth = new Month();
                int lastMonth = month.getMonth();
                newMonth.setMonth(lastMonth - 1 >= 1 ? lastMonth - 1 : 12);
                newMonth.setYear(lastMonth - 1 >= 1 ? month.getYear() : month.getYear() - 1);
                return newMonth;
            }

            @Override
            public Month addLast(int position, Month month) {
                Month newMonth = new Month();
                int lastMonth = month.getMonth();
                newMonth.setMonth(lastMonth + 1 <= 12 ? lastMonth + 1 : 1);
                newMonth.setYear(lastMonth + 1 <= 12 ? month.getYear() : month.getYear() + 1);
                return newMonth;
            }
        });
    }
}
