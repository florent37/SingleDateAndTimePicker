package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.florent37.wheelpicker.WheelAdapter;
import com.github.florent37.wheelpicker.WheelPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.github.florent37.singledateandtimepicker.DateHelper.today;

public class WheelMonthPicker extends WheelPicker<String> implements DateTimeWheelPicker {

    private final SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM", Locale.getDefault());

    private int lastScrollPosition;

    private MonthSelectedListener listener;

    public WheelMonthPicker(Context context) {
        this(context, null);
        init();
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        setAdapter(new WheelAdapter(generateAdapterValues()));
        setDefault(monthFormatter.format(today()));
    }

    protected List<String> generateAdapterValues() {
        final List<String> monthList = new ArrayList<>();

        final Calendar cal = Calendar.getInstance(Locale.getDefault());

        for (int i = 0; i < 12; i++) {
            cal.set(Calendar.MONTH, i);
            monthList.add(monthFormatter.format(cal.getTime()));
        }

        return monthList;
    }

    public void setListener(MonthSelectedListener listener) {
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

    @Override
    public void setDefaultDate(Date date) {
        setDefault(monthFormatter.format(date));
    }

    @Override
    public void selectDate(Date date) {
        int position = findIndexOfDate(date);
        scrollTo(position);
        setSelectedItemPosition(position);
    }

    @Override
    public int findIndexOfDate(Date date) {
        String value = monthFormatter.format(date);
        return adapter.getItemPosition(value);
    }

    public interface MonthSelectedListener {
        void onMonthSelected(WheelMonthPicker picker, int monthIndex, String monthName);
    }
}