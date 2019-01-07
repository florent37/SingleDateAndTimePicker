package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.MAX_MINUTES;
import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.MIN_MINUTES;
import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.STEP_MINUTES_DEFAULT;

public class WheelMinutePicker extends WheelPicker<String> {

    private int stepMinutes;

    private OnMinuteChangedListener onMinuteChangedListener;
    private OnFinishedLoopListener onFinishedLoopListener;

    public WheelMinutePicker(Context context) {
        super(context);
    }

    public WheelMinutePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        stepMinutes = STEP_MINUTES_DEFAULT;
    }

    private ArrayList<Integer> generateMinutes(){
        ArrayList<Integer> values = new ArrayList<>();
        for (int i=MIN_MINUTES; i<=MAX_MINUTES; i+=stepMinutes){
            values.add(i);
        }
        return values;
    }

    @Override
    protected List<String> generateAdapterValues() {
        final List<String> minutes = new ArrayList<>();
        for (int minute : generateMinutes()) {
            minutes.add(getFormattedValue(minute));
        }
        return minutes;
    }

    private int findIndexOfMinute(int currentMinute) {
        ArrayList<Integer> minutes = generateMinutes();
        int idx = 0;
        for(; idx < (minutes.size() - 1); idx++){
            int minute = minutes.get(idx);
            if (minute >= currentMinute)
                break;
        }
        return idx;
    }

    @Override
    public int findIndexOfDate(@NonNull Date date) {
        return findIndexOfMinute(DateHelper.getMinuteOf(date));
    }

    protected String getFormattedValue(Object value) {
        Object valueItem = value;
        if (value instanceof Date) {
            final Calendar instance = Calendar.getInstance();
            instance.setTime((Date) value);
            valueItem = instance.get(Calendar.MINUTE);
        }
        return String.format(getCurrentLocale(), FORMAT, valueItem);
    }

    public void setStepMinutes(int stepMinutes) {
        if (stepMinutes < 60 && stepMinutes > 0) {
            this.stepMinutes = stepMinutes;
            updateAdapter();
        }
    }

    private int convertItemToMinute(Object item) {
        return Integer.valueOf(String.valueOf(item));
    }

    public int getCurrentMinute() {
        return convertItemToMinute(adapter.getItem(getCurrentItemPosition()));
    }

    public WheelMinutePicker setOnMinuteChangedListener(OnMinuteChangedListener onMinuteChangedListener) {
        this.onMinuteChangedListener = onMinuteChangedListener;
        return this;
    }

    public WheelMinutePicker setOnFinishedLoopListener(OnFinishedLoopListener onFinishedLoopListener) {
        this.onFinishedLoopListener = onFinishedLoopListener;
        return this;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        super.onItemSelected(position, item);
        if (onMinuteChangedListener != null) {
            onMinuteChangedListener.onMinuteChanged(this, convertItemToMinute(item));
        }
    }

    @Override
    protected void onFinishedLoop() {
        super.onFinishedLoop();
        if (onFinishedLoopListener != null) {
            onFinishedLoopListener.onFinishedLoop(this);
        }
    }

    public interface OnMinuteChangedListener {
        void onMinuteChanged(WheelMinutePicker picker, int minutes);
    }

    public interface OnFinishedLoopListener {
        void onFinishedLoop(WheelMinutePicker picker);
    }
}