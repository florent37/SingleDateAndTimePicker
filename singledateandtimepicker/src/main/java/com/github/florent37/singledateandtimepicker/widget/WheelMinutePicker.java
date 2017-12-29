package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelMinutePicker extends WheelPicker<String> {
    public static final int MIN_MINUTES = 0;
    public static final int MAX_MINUTES = 59;
    public static final int STEP_MINUTES_DEFAULT = 5;

    private int stepMinutes = STEP_MINUTES_DEFAULT;

    public WheelMinutePicker(Context context) {
        super(context);
    }

    public WheelMinutePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void initAdapter(){
        final List<String> minutes = new ArrayList<>();
        for (int min = MIN_MINUTES; min <= MAX_MINUTES; min += stepMinutes) {
            minutes.add(getFormattedValue(min));
        }
        setAdapter(new Adapter<String>(minutes));
        setDefault(getFormattedValue(Calendar.getInstance().get(Calendar.MINUTE)));
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

    public void setStepMinutes(int stepMinutes) {
        if (stepMinutes < 60 && stepMinutes > 0) {
            this.stepMinutes = stepMinutes;
            initAdapter();
        }
    }

    private int convertItemToMinute(Object item) {
        return Integer.valueOf(String.valueOf(item));
    }

    public int getCurrentMinute() {
        return convertItemToMinute(adapter.getItem(getCurrentItemPosition()));
    }

    public interface Listener extends WheelPicker.Listener<WheelMinutePicker, Integer> {
    }
}