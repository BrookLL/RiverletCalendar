package com.riverlet.lib.viewpager;

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

public class InfiniteViewPager extends ViewPager {
    private static final String TAG = "InfiniteViewPager";
    private static final int MAX_VIEW_LIMIT = 2;
    private static final int MID_PAGES_INDEX = Integer.MAX_VALUE / 2;
    private ViewHolderCreator viewHolderCreator;
    private Map<Integer, Object> dataMap = new HashMap<>();
    private List<Object> dataList = new ArrayList<>();
    private OnNeedAddDataListener onNeedAddDataListener;

    public InfiniteViewPager(@NonNull Context context) {
        this(context, null);
    }

    public InfiniteViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOffscreenPageLimit(MAX_VIEW_LIMIT);
    }

    public void setOnNeedAddDataListener(OnNeedAddDataListener onNeedAddDataListener) {
        this.onNeedAddDataListener = onNeedAddDataListener;
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

    public interface OnNeedAddDataListener<T> {
        T addFirst(int position, T t);

        T addLast(int position, T t);
    }

    private Object getDataOfItem(int position) {
        int key = position - MID_PAGES_INDEX;
        Log.d(TAG, "position:" + position + "...key:" + key);
        if (dataMap.containsKey(key)) {
            return dataMap.get(key);
        } else {
            if (onNeedAddDataListener != null) {
                if (key > 0) {
                    Object object = onNeedAddDataListener.addLast(position, dataMap.get(key - 1));
                    dataList.add(object);
                    dataMap.put(key, object);
                    return object;
                } else {
                    Object object = onNeedAddDataListener.addFirst(position, dataMap.get(key + 1));
                    Log.d(TAG, "object:" + object);
                    dataList.add(0, object);
                    dataMap.put(key, object);
                    return object;
                }
            }

        }
        return null;
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
        for (int i = 0; i < dataList.size(); i++) {
            dataMap.put(i - dataList.size() / 2, dataList.get(i));
        }
        setAdapter(new LoopPagerAdapter());
        setCurrentItem(MID_PAGES_INDEX, false);
    }

}
