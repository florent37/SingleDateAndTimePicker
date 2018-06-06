package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.florent37.wheelpicker.WheelAdapter;
import com.github.florent37.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.MAX_MINUTES;
import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.MIN_MINUTES;
import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.STEP_MINUTES_DEFAULT;

public class WheelMinutePicker extends WheelPicker<String> implements DateTimeWheelPicker {

    private int stepMinutes;

    private OnMinuteChangedListener onMinuteChangedListener;
    private OnFinishedLoopListener onFinishedLoopListener;

    public WheelMinutePicker(Context context) {
        super(context);
        init();
    }

    public WheelMinutePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        stepMinutes = STEP_MINUTES_DEFAULT;
        setAdapter(new WheelAdapter(generateAdapterValues()));
        setDefault(formatMinutes(new Date().getMinutes()));
    }

    protected List<String> generateAdapterValues() {
        final List<String> minutes = new ArrayList<>();
        for (int min = MIN_MINUTES; min <= MAX_MINUTES; min += stepMinutes) {
            minutes.add(formatMinutes(min));
        }
        return minutes;
    }

    private String formatMinutes(int minutes) {
        return String.format(getCurrentLocale(), FORMAT, minutes);
    }

    public void setStepMinutes(int stepMinutes) {
        if (stepMinutes < 60 && stepMinutes > 0) {
            this.stepMinutes = stepMinutes;
            setAdapter(new WheelAdapter(generateAdapterValues()));
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

    @Override
    public void setDefaultDate(Date date) {
        setDefault(formatMinutes(date.getMinutes()));
    }

    @Override
    public void selectDate(Date date) {
        int position = findIndexOfDate(date);
        scrollTo(position);
        setSelectedItemPosition(position);
    }

    @Override
    public int findIndexOfDate(Date date) {
        String value = formatMinutes(date.getMinutes());
        return adapter.getItemPosition(value);
    }

    public interface OnMinuteChangedListener {
        void onMinuteChanged(WheelMinutePicker picker, int minutes);
    }

    public interface OnFinishedLoopListener {
        void onFinishedLoop(WheelMinutePicker picker);
    }
}