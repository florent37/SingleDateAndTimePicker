package com.github.florent37.wheelpicker;

import java.util.ArrayList;
import java.util.List;

public class WheelAdapter<V> implements WheelPicker.BaseAdapter {
    private List<V> data;

    public WheelAdapter() {
        this(new ArrayList<V>());
    }

    public WheelAdapter(List<V> data) {
        this.data = new ArrayList<V>();
        this.data.addAll(data);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public V getItem(int position) {
        final int itemCount = getItemCount();
        return data.get((position + itemCount) % itemCount);
    }

    @Override
    public String getItemText(int position) {
        return String.valueOf(data.get(position));
    }

    public List<V> getData() {
        return data;
    }

    public void setData(List<V> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    public int getItemPosition(V value) {
        int position = -1;
        if (data != null) {
            return data.indexOf(value);
        }
        return position;
    }
}