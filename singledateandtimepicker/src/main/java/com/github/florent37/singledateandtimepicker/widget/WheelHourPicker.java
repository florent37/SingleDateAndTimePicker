package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelHourPicker extends WheelPicker {

    public static final int MIN_HOUR_DEFAULT = 0;
    public static final int MAX_HOUR_DEFAULT = 23;
    public static final int MAX_HOUR_AM_PM = 12;
    public static final int STEP_HOURS_DEFAULT = 1;

    private OnHourSelectedListener hoursSelectedListener;

    private int defaultHour;
    private int minHour = MIN_HOUR_DEFAULT;
    private int maxHour = MAX_HOUR_DEFAULT;
    private int hoursStep = STEP_HOURS_DEFAULT;

    private int lastScrollPosition;
    protected boolean isAmPm = false;

    private WheelPicker.Adapter adapter;

    public WheelHourPicker(Context context) {
        this(context, null);
    }

    public WheelHourPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAdapter();
    }

    private void initAdapter() {
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

        adapter = new Adapter(hours);
        setAdapter(adapter);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        // todo set default date calendar.setTime(defa);

        defaultHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (isAmPm && defaultHour >= MAX_HOUR_AM_PM) {
            defaultHour -= MAX_HOUR_AM_PM;
        }

        updateDefaultHour();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (hoursSelectedListener != null) {
            hoursSelectedListener.onHourSelected(this, position, convertItemToHour(item));
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (hoursSelectedListener != null) {
            hoursSelectedListener.onHourCurrentScrolled(this, position, convertItemToHour(item));
        }

        if (lastScrollPosition != position) {
            hoursSelectedListener.onHourCurrentScrolled(this, position, convertItemToHour(item));
            if (lastScrollPosition == MAX_HOUR_DEFAULT && position == 0)
                if (hoursSelectedListener != null) {
                    hoursSelectedListener.onHourCurrentNewDay(this);
                }
            lastScrollPosition = position;
        }
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

    private void updateDefaultHour() {
        setSelectedItemPosition(defaultHour);
    }

    @Override
    public int getDefaultItemPosition() {
        return defaultHour;
    }

    public void setOnHourSelectedListener(OnHourSelectedListener hoursSelectedListener) {
        this.hoursSelectedListener = hoursSelectedListener;
    }

    public void setDefaultHour(int hour) {
        if (isAmPm && hour >= MAX_HOUR_AM_PM) {
            hour -= MAX_HOUR_AM_PM;
        }

        defaultHour = hour;
        updateDefaultHour();
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

    public void setHoursStep(int hourStep) {
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

    public interface OnHourSelectedListener {
        void onHourSelected(WheelHourPicker picker, int position, int hours);

        void onHourCurrentScrolled(WheelHourPicker picker, int position, int hours);

        void onHourCurrentNewDay(WheelHourPicker picker);
    }
}