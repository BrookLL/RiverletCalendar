package com.riverlet.lib.calendar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DayView extends LinearLayout {

    private TextView dateText;
    private boolean isCurrentViewMonth;
    private boolean isToday;
    private GradientDrawable todayBackground;
    private GradientDrawable selectedBackground;

    public DayView(Context context) {
        this(context, null);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        initBackground();

        dateText = new TextView(getContext());
        dateText.setTextColor(0xff333333);
        dateText.setTextSize(15);
        dateText.setGravity(Gravity.CENTER);
        addView(dateText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    private void initBackground() {
        StateListDrawable background = new StateListDrawable();

        todayBackground = new GradientDrawable();
        todayBackground.setShape(GradientDrawable.OVAL);
        todayBackground.setStroke(2, 0xff199dff);
        todayBackground.setColor(0xff199dff);

        selectedBackground = new GradientDrawable();
        selectedBackground.setShape(GradientDrawable.OVAL);
        selectedBackground.setStroke(2, 0xff199dff);
        selectedBackground.setColor(0x00000000);

    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        refreshView();
    }

    public void setDate(int date) {
        dateText.setText(String.valueOf(date));
    }

    public boolean isCurrentViewMonth() {
        return isCurrentViewMonth;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
        refreshView();
    }


    public void setIsCurrentViewMonth(boolean isCurrentViewMonth) {
        this.isCurrentViewMonth = isCurrentViewMonth;
        refreshView();
    }

    private void refreshView() {
        if (!isCurrentViewMonth) {
            dateText.setTextColor(0xffa0a0a0);
            setBackgroundColor(0x00000000);
            return;
        }
        if (!isToday && !isSelected()) {
            dateText.setTextColor(0xff333333);
            setBackgroundColor(0x00000000);
        }
        if (!isToday && isSelected()) {
            dateText.setTextColor(0xff333333);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(selectedBackground);
            } else {
                setBackgroundDrawable(selectedBackground);
            }
        }
        if (isToday && isSelected()) {
            dateText.setTextColor(0xffffffff);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(todayBackground);
            } else {
                setBackgroundDrawable(todayBackground);
            }
        }
        if (isToday && !isSelected()) {
            dateText.setTextColor(0xff199dff);
            setBackgroundColor(0x00000000);
        }
    }

}
