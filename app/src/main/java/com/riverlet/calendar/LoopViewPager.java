package com.riverlet.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LoopViewPager extends ViewPager {
    private static final String TAG = "LoopViewPager";
    private static final int MAX_VIEW_LIMIT = 2;
    private static final int MID_PAGES_INDEX = Integer.MAX_VALUE / 2;
    private ViewHolderCreator viewHolderCreator;
    private List dataList = new ArrayList();

    public LoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOffscreenPageLimit(MAX_VIEW_LIMIT);
    }

    private class LoopPagerAdapter extends PagerAdapter {

        LinkedList<ViewHolder> viewHolders = new LinkedList<ViewHolder>() {
            {
                while (this.size() < 7) {
                    add(viewHolderCreator.create());
                }
            }
        };
        Map<Integer, ViewHolder> usedViewHolders = new HashMap<>();

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ViewHolder holder = viewHolders.removeFirst();
            Object object = getDataOfItem(position);
            holder.setData(object);
            container.addView(holder.view);
            usedViewHolders.put(position, holder);
            return holder.view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ViewHolder holder = usedViewHolders.remove(position);
            container.removeView(holder.view);
            viewHolders.addLast(holder);
        }
    }

    private Object getDataOfItem(int position) {
        int size = dataList.size();
        int i = (((position - MID_PAGES_INDEX) % size + size / 2) + size) % size;
        Log.d(TAG, "position=" + position + "...i=" + i);
        return dataList.get(i);
    }

    /**
     * ViewHolder
     *
     * @param <V> View类型
     * @param <T> 数据类型
     */
    public static abstract class ViewHolder<V extends View, T> {
        V view;
        T data;

        public ViewHolder(V view) {
            this.view = view;
        }

        public T getData() {
            return data;
        }

        public V getView() {
            return view;
        }

        private void setData(T data) {
            this.data = data;
            update(this, data);
        }

        public abstract void update(ViewHolder<V, T> holder, T t);
    }

    /**
     * 创建ViewHolder的工具子类
     */
    public static abstract class ViewHolderCreator {
        public abstract ViewHolder create();
    }

    /**
     * 设置基础数据和ViewHolderCreator
     *
     * @param dataList
     * @param viewHolderCreator
     */
    public void setData(List dataList, @NonNull ViewHolderCreator viewHolderCreator) {
        this.dataList.addAll(dataList);
        this.viewHolderCreator = viewHolderCreator;
        setAdapter(new LoopPagerAdapter());
        setCurrentItem(MID_PAGES_INDEX, false);
    }

}
