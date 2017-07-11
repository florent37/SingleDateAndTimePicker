package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelMinutePicker extends WheelPicker {
    public static final int MIN_MINUTES = 0;
    public static final int MAX_MINUTES = 59;
    public static final int STEP_MINUTES_DEFAULT = 5;

    private int defaultMinute;
    private int stepMinutes = STEP_MINUTES_DEFAULT;

    private WheelPicker.Adapter adapter;

    private int lastScrollPosition;

    private OnMinuteSelectedListener onMinuteSelectedListener;

    public WheelMinutePicker(Context context) {
        this(context, null);
    }

    public WheelMinutePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAdapter();
    }

    private void initAdapter(){
        final List<String> minutes = new ArrayList<>();
        for (int min = MIN_MINUTES; min <= MAX_MINUTES; min += stepMinutes) {
            minutes.add(getFormattedValue(min));
        }
        adapter = new Adapter(minutes);
        setAdapter(adapter);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
     // todo fix.   calendar.setTime(defaultDate);
        defaultMinute = calendar.get(Calendar.MINUTE);

        updateDefaultMinute();
    }


    public void setOnMinuteSelectedListener(OnMinuteSelectedListener onMinuteSelectedListener) {
        this.onMinuteSelectedListener = onMinuteSelectedListener;
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (onMinuteSelectedListener != null) {
            onMinuteSelectedListener.onMinuteSelected(this, position, convertItemToMinute(item));
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (lastScrollPosition != position) {
            onMinuteSelectedListener.onMinuteCurrentScrolled(this, position, convertItemToMinute(item));
            if (lastScrollPosition == 11 && position == 0)
                if (onMinuteSelectedListener != null) {
                    onMinuteSelectedListener.onMinuteScrolledNewHour(this);
                }
            lastScrollPosition = position;
        }
    }

    private int findIndexOfMinute(int minute) {
        final int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; ++i) {
            final String object = adapter.getItemText(i);
            final Integer value = Integer.valueOf(object);
            if (minute < value) {
                return i - 1;
            }
        }
        return 0;
    }

    protected String getFormattedValue(Object value) {
        Object valueItem = value;
        if (value instanceof Date) {
            Calendar instance = Calendar.getInstance();
            instance.setTime((Date) value);
            valueItem = instance.get(Calendar.MINUTE);
        }
        return String.format(getCurrentLocale(), FORMAT, valueItem);
    }

    private void updateDefaultMinute() {
        setSelectedItemPosition(findIndexOfMinute(defaultMinute));
    }

    public void setDefaultMinute(int minutes) {
        this.defaultMinute = minutes;
        updateDefaultMinute();
    }

    public void setStepMinutes(int stepMinutes) {
        if (stepMinutes < 60 && stepMinutes > 0) {
            this.stepMinutes = stepMinutes;
            initAdapter();
        }
    }

    @Override
    public int getDefaultItemPosition() {
        return findIndexOfMinute(defaultMinute);
    }

    private int convertItemToMinute(Object item) {
        return Integer.valueOf(String.valueOf(item));
    }

    public int getCurrentMinute() {
        return convertItemToMinute(adapter.getItem(getCurrentItemPosition()));
    }

    public interface OnMinuteSelectedListener {
        void onMinuteSelected(WheelMinutePicker picker, int position, int minutes);

        void onMinuteCurrentScrolled(WheelMinutePicker picker, int position, int minutes);

        void onMinuteScrolledNewHour(WheelMinutePicker picker);
    }
}