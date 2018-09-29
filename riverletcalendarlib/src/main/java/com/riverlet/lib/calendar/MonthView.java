package com.riverlet.lib.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.riverlet.lib.entity.Day;
import com.riverlet.lib.entity.Month;

import java.util.Calendar;

public class MonthView extends RecyclerView {
    private static final String TAG = "MonthView";
    private Day[] dates;
    private int selectIndex = -1;
    private Calendar realCalendar = Calendar.getInstance();
    private Month month;
    private Day today;
    private int dayHeight;
    private int screenWidth;

    private int currentViewMonth;
    private int currentViewYear;

    private OnDayClickListenter onDayClickListenter;

    public MonthView(@NonNull Context context) {
        this(context, null);
    }

    public MonthView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        dayHeight = screenWidth / 7;
        init();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        params.height = screenWidth;
        super.setLayoutParams(params);
    }


    private void init() {
        realCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        today = new Day();
        today.setYear(realCalendar.get(Calendar.YEAR));
        today.setMonth(realCalendar.get(Calendar.MONTH) + 1);
        today.setDate(realCalendar.get(Calendar.DATE));
        today.setDay(realCalendar.get(Calendar.DAY_OF_WEEK));
        Log.d(TAG, "today:" + today.toString());

        setLayoutManager(new GridLayoutManager(getContext(), 7));
    }

    public void setMonth(Month month) {
        this.month = month;
        if (month != null) {
            initCalendar();
        }
    }

    public void setOnDayClickListenter(OnDayClickListenter onDayClickListenter) {
        this.onDayClickListenter = onDayClickListenter;
    }

    public void setSelectedDate(int selectedDate) {
        for (int i = 0; i < dates.length; i++) {
            if (dates[i].getMonth() == currentViewMonth && dates[i].getDate() == selectedDate) {
                DayView dayView = (DayView) getChildAt(selectIndex);
                dayView.setSelected(false);
                getChildAt(i).setSelected(true);
                selectIndex = i;
            }
        }
    }

    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, month.getYear());
        calendar.set(Calendar.MONTH, month.getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DATE, 1);
        int fristDateOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int firstDateIndex = fristDateOfWeek - 1;
        currentViewMonth = calendar.get(Calendar.MONTH) + 1;
        currentViewYear = calendar.get(Calendar.YEAR);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 0);

        int maxDateOfMonth = calendar.get(Calendar.DATE);
        int lastDateIndex = firstDateIndex + maxDateOfMonth;

        calendar.set(Calendar.DATE, 0);
        int maxDateOfLastMonth = calendar.get(Calendar.DATE);

        int lines = (lastDateIndex + 7) / 7;

        while (getItemDecorationCount() > 0) {
            removeItemDecorationAt(0);
        }
        addItemDecoration(new HorizontalDecoration((dayHeight * (7 - lines)) / (lines - 1)));
        dates = new Day[lines * 7];
        for (int i = 0; i < dates.length; i++) {
            dates[i] = new Day();
            if (i < firstDateIndex) {
                dates[i].setDate(maxDateOfLastMonth - firstDateIndex + 1 + i);
                dates[i].setMonth(currentViewMonth - 1 >= 0 ? currentViewMonth - 1 : 11);
                dates[i].setYear(currentViewMonth - 1 >= 0 ? currentViewYear : currentViewYear - 1);
            }
            if (i >= firstDateIndex && i < lastDateIndex) {
                dates[i].setDate(i - firstDateIndex + 1);
                dates[i].setMonth(currentViewMonth);
                dates[i].setYear(currentViewYear);
            }
            if (i >= lastDateIndex) {
                dates[i].setDate(i - lastDateIndex + 1);
                dates[i].setMonth(currentViewMonth + 1 <= 11 ? currentViewMonth + 1 : 0);
                dates[i].setYear(currentViewMonth + 1 <= 11 ? currentViewYear : currentViewYear + 1);
            }
        }

        setAdapter(new CalendarAdapter());
    }

    class CalendarAdapter extends Adapter<CalendarViewHolder> {
        @NonNull
        @Override
        public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            DayView view = new DayView(getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dayHeight));
            return new CalendarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CalendarViewHolder calendarViewHolder, final int position) {
            final Day day = dates[position];
            calendarViewHolder.dayView.setDate(day.getDate());
            calendarViewHolder.dayView.setIsCurrentViewMonth(day.getMonth() == currentViewMonth);
            boolean isToday = today.isToday(currentViewYear, currentViewMonth, day.getDate());
            if (isToday && selectIndex == -1) {
                selectIndex = position;
            }
            calendarViewHolder.dayView.setToday(isToday);
            calendarViewHolder.dayView.setSelected(selectIndex == position);
            calendarViewHolder.dayView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (day.getMonth() == currentViewMonth) {
                        DayView dayView = (DayView) getChildAt(selectIndex);
                        if (dayView != null) {
                            dayView.setSelected(false);
                        }
                        v.setSelected(true);
                        selectIndex = position;
                    }

                    if (onDayClickListenter != null) {
                        onDayClickListenter.onDayClick(currentViewMonth, day);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dates.length;
        }
    }

    class CalendarViewHolder extends ViewHolder {
        DayView dayView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayView = (DayView) itemView;
        }
    }

    class HorizontalDecoration extends ItemDecoration {
        Paint paint;
        int size;

        public HorizontalDecoration(int size) {
            this.size = size;
            paint = new Paint();
            paint.setColor(0x0000000);
            paint.setStyle(Paint.Style.FILL);
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull State state) {
            outRect.set(0, size, 0, 0);
        }

        @Override
        public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull State state) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
            int childSize = parent.getChildCount();
            for (int i = 7; i < childSize; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + layoutParams.bottomMargin;
                final int bottom = top + size;
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    public interface OnDayClickListenter {
        void onDayClick(int currentViewMonth, Day day);
    }
}
