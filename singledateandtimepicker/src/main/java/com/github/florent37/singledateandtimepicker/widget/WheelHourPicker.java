package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelHourPicker extends WheelPicker<String> {

    public static final int MIN_HOUR_DEFAULT = 0;
    public static final int MAX_HOUR_DEFAULT = 23;
    public static final int MAX_HOUR_AM_PM = 12;
    public static final int STEP_HOURS_DEFAULT = 1;

    private int minHour = MIN_HOUR_DEFAULT;
    private int maxHour = MAX_HOUR_DEFAULT;
    private int hoursStep = STEP_HOURS_DEFAULT;

    protected boolean isAmPm = false;

    public WheelHourPicker(Context context) {
        super(context);
    }

    public WheelHourPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initAdapter() {
        final List<String> hours = new ArrayList<>();

        if (isAmPm) {
            hours.add(getFormattedValue(12));
            for (int hour = hoursStep; hour < maxHour; hour += hoursStep) {
                hours.add(getFormattedValue(hour));
            }
        } else {
            for (int hour = minHour; hour <= maxHour; hour += hoursStep) {
                hours.add(getFormattedValue(hour));
            }
        }

        setAdapter(new Adapter<String>(hours));
        setDefault(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
    }

    @Override
    public int findIndexOfDate(@NonNull Date date) {
        if (isAmPm) {
            final int hours = date.getHours();
            if (hours >= MAX_HOUR_AM_PM) {
                Date copy = new Date(date.getTime());
                copy.setHours(hours % 12);
                return super.findIndexOfDate(copy);
            }
        }
        return super.findIndexOfDate(date);
    }

    protected String getFormattedValue(Object value) {
        Object valueItem = value;
        if (value instanceof Date) {
            Calendar instance = Calendar.getInstance();
            instance.setTime((Date) value);
            valueItem = instance.get(Calendar.HOUR_OF_DAY);
        }
        return String.format(getCurrentLocale(), FORMAT, valueItem);
    }

    @Override
    public void setDefault(String defaultValue) {
        try {
            int hour = Integer.parseInt(defaultValue);
            if (isAmPm && hour >= MAX_HOUR_AM_PM) {
                hour -= MAX_HOUR_AM_PM;
            }

            super.setDefault(getFormattedValue(hour));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setIsAmPm(boolean isAmPm) {
        this.isAmPm = isAmPm;
        if (isAmPm) {
            setMaxHour(MAX_HOUR_AM_PM);
        } else {
            setMaxHour(MAX_HOUR_DEFAULT);
        }
    }

    public void setMaxHour(int maxHour) {
        if (maxHour >= MIN_HOUR_DEFAULT && maxHour <= MAX_HOUR_DEFAULT) {
            this.maxHour = maxHour;
        }
        initAdapter();
    }

    public void setMinHour(int minHour) {
        if (minHour >= MIN_HOUR_DEFAULT && minHour <= MAX_HOUR_DEFAULT) {
            this.minHour = minHour;
        }
        initAdapter();
    }

    public void setHoursStep(int hoursStep) {
        if (hoursStep >= MIN_HOUR_DEFAULT && hoursStep <= MAX_HOUR_DEFAULT) {
            this.hoursStep = hoursStep;
        }
        initAdapter();
    }

    private int convertItemToHour(Object item) {
        Integer hour = Integer.valueOf(String.valueOf(item));
        if (!isAmPm) {
            return hour;
        }

        if (hour == 12) {
            hour = 0;
        }

        return hour;
    }

    public int getCurrentHour() {
        return convertItemToHour(adapter.getItem(getCurrentItemPosition()));
    }

    public interface Listener extends WheelPicker.Listener<WheelHourPicker, String>{

    }
}