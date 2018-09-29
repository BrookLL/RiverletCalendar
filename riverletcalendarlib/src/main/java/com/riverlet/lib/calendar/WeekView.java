package com.riverlet.lib.calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeekView extends LinearLayout {
    private static final String[] WEEKS = new String[]{"日", "一", "二", "三", "四", "五", "六"};

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        for (int i = 0; i < WEEKS.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(WEEKS[i]);
            textView.setTextColor(0xff333333);
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            addView(textView, layoutParams);
        }
    }
}
