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
    private OnNeedAddDataCallback onNeedAddDataCallback;
    private LoopPagerAdapter adatper;

    public InfiniteViewPager(@NonNull Context context) {
        this(context, null);
    }

    public InfiniteViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOffscreenPageLimit(MAX_VIEW_LIMIT);
    }

    public void setOnNeedAddDataCallback(OnNeedAddDataCallback onNeedAddDataCallback) {
        this.onNeedAddDataCallback = onNeedAddDataCallback;
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

    /**
     * 需要新增数据的回调
     * @param <T>
     */
    public interface OnNeedAddDataCallback<T> {
        /**
         * 新增数据在前（左）
         * @param position 触发回调的位置
         * @param t 当前最靠前（左）的数据
         * @return 新增数据
         */
        T addFirst(int position, T t);

        /**
         * 新增数据在后（右）
         * @param position 触发回调的位置
         * @param t 当前最靠前后（右）的数据
         * @return 新增数据
         */
        T addLast(int position, T t);
    }

    private Object getDataOfItem(int position) {
        int key = position - MID_PAGES_INDEX;
        Log.d(TAG, "position:" + position + "...key:" + key);
        if (dataMap.containsKey(key)) {
            return dataMap.get(key);
        } else {
            if (onNeedAddDataCallback != null) {
                if (key > 0) {
                    Object object = onNeedAddDataCallback.addLast(position, dataMap.get(key - 1));
                    dataList.add(object);
                    dataMap.put(key, object);
                    return object;
                } else {
                    Object object = onNeedAddDataCallback.addFirst(position, dataMap.get(key + 1));
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
        adatper = new LoopPagerAdapter();
        setAdapter(adatper);
        setCurrentItem(MID_PAGES_INDEX, false);
    }

    /**
     * 获取当前显示Item的ViewHolder
     * @return
     */
    public ViewHolder getCurrentItemViewHolder(){
        return adatper.usedViewHolders.get(getCurrentItem());
    }

    /**
     * 根据Item位置获取它的ViewHolder
     * @param position
     * @return
     */
    public ViewHolder getViewHolderOfPosition(int position) {
        return adatper.usedViewHolders.get(position);
    }
}
