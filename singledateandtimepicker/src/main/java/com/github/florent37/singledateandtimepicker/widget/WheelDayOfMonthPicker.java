package com.github.florent37.singledateandtimepicker.widget;


import android.content.Context;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.github.florent37.singledateandtimepicker.DateHelper.getDay;
import static com.github.florent37.singledateandtimepicker.DateHelper.today;

public class WheelDayOfMonthPicker extends WheelPicker<String> {

    private int lastScrollPosition;

    private DayOfMonthSelectedListener listener;

    public WheelDayOfMonthPicker(Context context) {
        this(context, null);
    }

    public WheelDayOfMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {

    }

    @Override
    protected List<String> generateAdapterValues() {
        final List<String> dayList = new ArrayList<>();

        final SimpleDateFormat dayOfMonth = new SimpleDateFormat("d", Locale.getDefault());
        final Calendar cal = Calendar.getInstance(Locale.getDefault());

        // TODO
        for (int i = 0; i < 30; i++) {
            cal.set(Calendar.DAY_OF_MONTH, i);
            dayList.add(dayOfMonth.format(cal.getTime()));
        }

        return dayList;
    }


    @Override
    protected String initDefault() {
        return String.valueOf(getDay(today()));
    }

    public void setDayOfMonthSelectedListener(DayOfMonthSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (listener != null) {
            listener.onDayOfMonthSelected(this, position);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, String item) {
        if (lastScrollPosition != position) {
            onItemSelected(position, item);
            lastScrollPosition = position;
        }
    }

    public int getCurrentDay() {
        return getCurrentItemPosition();
    }

    public interface DayOfMonthSelectedListener {
        void onDayOfMonthSelected(WheelDayOfMonthPicker picker, int dayIndex);
    }
}