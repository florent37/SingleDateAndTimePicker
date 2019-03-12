package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.github.florent37.singledateandtimepicker.DateHelper.getMonth;
import static com.github.florent37.singledateandtimepicker.DateHelper.today;

public class WheelMonthPicker extends WheelPicker<String> {

    private int lastScrollPosition;

    private MonthSelectedListener listener;

    private boolean displayMonthNumbers = false;

    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {

    }

    @Override
    protected List<String> generateAdapterValues() {
        final List<String> monthList = new ArrayList<>();

        final SimpleDateFormat month_date = new SimpleDateFormat("MMMM", getCurrentLocale());
        final Calendar cal = Calendar.getInstance(getCurrentLocale());
        cal.set(Calendar.DAY_OF_MONTH, 1);

        for (int i = 0; i < 12; i++) {
            cal.set(Calendar.MONTH, i);
            if (displayMonthNumbers) {
                monthList.add(String.format("%02d", i + 1));
            } else {
                monthList.add(month_date.format(cal.getTime()));
            }
        }

        return monthList;
    }


    @Override
    protected String initDefault() {
        return String.valueOf(getMonth(today()));
    }

    public void setOnMonthSelectedListener(MonthSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (listener != null) {
            listener.onMonthSelected(this, position, item);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, String item) {
        if (lastScrollPosition != position) {
            onItemSelected(position, item);
            lastScrollPosition = position;
        }
    }

    public boolean displayMonthNumbers() {
        return displayMonthNumbers;
    }

    public void setDisplayMonthNumbers(boolean displayMonthNumbers) {
        this.displayMonthNumbers = displayMonthNumbers;
    }

    public int getCurrentMonth() {
        return getCurrentItemPosition();
    }

    public interface MonthSelectedListener {
        void onMonthSelected(WheelMonthPicker picker, int monthIndex, String monthName);
    }
}