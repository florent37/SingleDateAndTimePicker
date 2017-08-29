package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelAmPmPicker extends WheelPicker {

    public static final int INDEX_AM = 0;
    public static final int INDEX_PM = 1;
    private Adapter adapter;

    private int lastScrollPosition;

    private OnAmPmSelectedListener onAmPmSelectedListener;

    public WheelAmPmPicker(Context context) {
        this(context, null);
    }

    public WheelAmPmPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAdapter();
    }

    private void initAdapter() {
        final List<String> values = new ArrayList<>();
        Resources resources = getResources();
        values.add(resources.getString(R.string.picker_am));
        values.add(resources.getString(R.string.picker_pm));
        adapter = new Adapter(values);
        setAdapter(adapter);
    }


    public void setOnAmPmSelectedListener(OnAmPmSelectedListener onAmPmSelectedListener) {
        this.onAmPmSelectedListener = onAmPmSelectedListener;
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (onAmPmSelectedListener != null) {
            if (position == INDEX_AM) {
                onAmPmSelectedListener.onAmSelected(this);
            } else {
                onAmPmSelectedListener.onPmSelected(this);
            }
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (lastScrollPosition != position) {
            lastScrollPosition = position;
        }
    }

    @Override
    protected String getFormattedValue(Object value) {
        if (value instanceof Date) {
            Calendar instance = Calendar.getInstance();
            instance.setTime((Date) value);
            return getResources().getString(instance.get(Calendar.AM_PM) == Calendar.PM ? R.string.picker_pm: R.string.picker_am);
        }
        return String.valueOf(value);
    }

    @Override
    public int getDefaultItemPosition() {
        return INDEX_AM;
    }

    public boolean isAm() {
        return getCurrentItemPosition() == INDEX_AM;
    }

    public boolean isPm() {
        return getCurrentItemPosition() == INDEX_PM;
    }

    public interface OnAmPmSelectedListener {
        void onAmSelected(WheelAmPmPicker picker);

        void onPmSelected(WheelAmPmPicker picker);
    }
}