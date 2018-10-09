package com.riverlet.lib.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
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
    private InfiniteViewPager infiniteViewPager;
    private OnCurrentMonthChangeCallback onCurrentMonthChangeCallback;
    private Month currentMonth;

    public RCalendarView(@NonNull Context context) {
        this(context, null);
    }

    public RCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        //星期
        WeekView weekView = new WeekView(getContext());
        addView(weekView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));

        //初始月份
        List<Month> monthList = new ArrayList<>();
        Month month = new Month();
        Calendar calendar = Calendar.getInstance();
        month.setYear(calendar.get(Calendar.YEAR));
        month.setMonth(calendar.get(Calendar.MONTH) + 1);
        monthList.add(month);
        currentMonth = month;
        //初始化无限的ViewPager
        infiniteViewPager = new InfiniteViewPager(getContext());
        addView(infiniteViewPager);

        //给InfiniteViewPager设置ViewHolderCreator
        infiniteViewPager.setData(monthList, new InfiniteViewPager.ViewHolderCreator() {
            @Override
            public InfiniteViewPager.ViewHolder create() {
                MonthView monthView = new MonthView(getContext());
                monthView.setOnDayClickListener(new MonthView.OnDayClickListener() {
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
        //给InfiniteViewPager设置需要新增数据时的回调
        infiniteViewPager.setOnNeedAddDataCallback(new InfiniteViewPager.OnNeedAddDataCallback<Month>() {
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

        //当前显示月份更改的回调
        infiniteViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                InfiniteViewPager.ViewHolder holder = infiniteViewPager.getCurrentItemViewHolder();
                Month month = (Month) holder.getData();
                MonthView monthView = (MonthView) holder.getView();
                monthView.refreshSelectIndex();
                currentMonth = month;
                Log.d(TAG, month.toString());
                if (onCurrentMonthChangeCallback != null) {
                    onCurrentMonthChangeCallback.onCurrentMonthChange(month);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public Month getCurrentMonth() {
        return currentMonth;
    }

    /**
     * 设置月份更改的回调
     *
     * @param onCurrentMonthChangeCallback
     */
    public void setOnCurrentMonthChangeCallback(OnCurrentMonthChangeCallback onCurrentMonthChangeCallback) {
        this.onCurrentMonthChangeCallback = onCurrentMonthChangeCallback;
    }

    /**
     * 当前显示月份更改时的回调
     */
    public interface OnCurrentMonthChangeCallback {
        void onCurrentMonthChange(Month month);
    }
}
