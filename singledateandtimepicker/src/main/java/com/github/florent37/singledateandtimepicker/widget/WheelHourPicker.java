package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.*;
import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.MAX_HOUR_DEFAULT;
import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.MIN_HOUR_DEFAULT;

public class WheelHourPicker extends WheelPicker<String> {

    private int minHour;
    private int maxHour;
    private int hoursStep;

    protected boolean isAmPm;
    private FinishedLoopListener finishedLoopListener;
    private OnHourChangedListener hourChangedListener;

    public WheelHourPicker(Context context) {
        super(context);
    }

    public WheelHourPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        isAmPm = false;
        minHour = MIN_HOUR_DEFAULT;
        maxHour = MAX_HOUR_DEFAULT;
        hoursStep = STEP_HOURS_DEFAULT;
    }

    private ArrayList<Integer> generateHours(){
        ArrayList<Integer> values = new ArrayList<Integer>();
        if (isAmPm){
            values.add(12);
            for(int i=hoursStep; i < maxHour; i += hoursStep){
                values.add(i);
            }
        }
        else{
            for(int i=minHour; i <= maxHour; i += hoursStep){
                values.add(i);
            }
        }
        return values;
    }

    @Override
    protected List<String> generateAdapterValues() {
        final List<String> hours = new ArrayList<>();

        for (int hour : generateHours()){
            hours.add(getFormattedValue(hour));
        }
        return hours;
    }

    private int findIndexOfHour(int currentHour){
        ArrayList<Integer> hours = generateHours();
        int idx = 0;
        for (; idx < (hours.size() - 1); idx++){
            int hour = hours.get(idx);
            if (isAmPm) {
                hour %= 12;
                currentHour %= 12;
            }
            if (hour >= currentHour)
                break;
        }
        return idx;
    }

    @Override
    public int findIndexOfDate(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        return findIndexOfHour(currentHour);
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

    public void setIsAmPm(boolean isAmPm) {
        this.isAmPm = isAmPm;
        if (isAmPm) {
            setMaxHour(MAX_HOUR_AM_PM);
        } else {
            setMaxHour(MAX_HOUR_DEFAULT);
        }
        updateAdapter();
    }

    public void setMaxHour(int maxHour) {
        if (maxHour >= MIN_HOUR_DEFAULT && maxHour <= MAX_HOUR_DEFAULT) {
            this.maxHour = maxHour;
        }
        notifyDatasetChanged();
    }

    public void setMinHour(int minHour) {
        if (minHour >= MIN_HOUR_DEFAULT && minHour <= MAX_HOUR_DEFAULT) {
            this.minHour = minHour;
        }
        notifyDatasetChanged();
    }

    public void setHoursStep(int hoursStep) {
        if (hoursStep >= MIN_HOUR_DEFAULT && hoursStep <= MAX_HOUR_DEFAULT) {
            this.hoursStep = hoursStep;
        }
        notifyDatasetChanged();
    }

    private int convertItemToHour(Object item) {
        Integer hour = Integer.valueOf(String.valueOf(item));
        return findIndexOfHour(hour);
    }

    public int getCurrentHour() {
        return convertItemToHour(adapter.getItem(getCurrentItemPosition()));
    }

    @Override
    protected void onItemSelected(int position, String item) {
        super.onItemSelected(position, item);

        if (hourChangedListener != null) {
            hourChangedListener.onHourChanged(this, convertItemToHour(item));
        }
    }

    public WheelHourPicker setOnFinishedLoopListener(FinishedLoopListener finishedLoopListener) {
        this.finishedLoopListener = finishedLoopListener;
        return this;
    }

    public WheelHourPicker setHourChangedListener(OnHourChangedListener hourChangedListener) {
        this.hourChangedListener = hourChangedListener;
        return this;
    }

    @Override
    protected void onFinishedLoop() {
        super.onFinishedLoop();
        if (finishedLoopListener != null) {
            finishedLoopListener.onFinishedLoop(this);
        }
    }

    public interface FinishedLoopListener {
        void onFinishedLoop(WheelHourPicker picker);
    }

    public interface OnHourChangedListener {
        void onHourChanged(WheelHourPicker picker, int hour);
    }
}