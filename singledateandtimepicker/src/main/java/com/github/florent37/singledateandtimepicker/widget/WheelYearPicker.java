package com.github.florent37.singledateandtimepicker.widget;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelYearPicker extends WheelPicker<String> {

    private SimpleDateFormat simpleDateFormat;
    protected int minYear;
    protected int maxYear;

    private OnYearSelectedListener onYearSelectedListener;

    public WheelYearPicker(Context context) {
        super(context);
    }

    public WheelYearPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        simpleDateFormat = new SimpleDateFormat("yyyy", getCurrentLocale());

        Calendar instance = Calendar.getInstance();
        int currentYear = instance.get(Calendar.YEAR);
        this.minYear = currentYear - SingleDateAndTimeConstants.MIN_YEAR_DIFF;
        this.maxYear = currentYear + SingleDateAndTimeConstants.MAX_YEAR_DIFF;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (onYearSelectedListener != null) {
            final int year = convertItemToYear(position);
            onYearSelectedListener.onYearSelected(this, position, year);
        }
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
        notifyDatasetChanged();
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
        notifyDatasetChanged();
    }

    @Override
    protected List<String> generateAdapterValues() {
        final List<String> years = new ArrayList<>();

        final Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, minYear-1);

        for (int i = minYear; i <= maxYear; i++) {
            instance.add(Calendar.YEAR, 1);
            years.add(getFormattedValue(instance.getTime()));
        }

        return years;
    }
    @Override
    public int findIndexOfDate(@NonNull Date date) {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int dateYear = instance.get(Calendar.YEAR);
        int idx = dateYear - minYear;
        if (idx < 0 || idx > (maxYear - minYear))
            return -1; //not in our range
        return idx;
    }

    protected String getFormattedValue(Object value) {
        return simpleDateFormat.format(value);
    }

    public void setOnYearSelectedListener(OnYearSelectedListener onYearSelectedListener) {
        this.onYearSelectedListener = onYearSelectedListener;
    }

    public int getCurrentYear() {
        return convertItemToYear(super.getCurrentItemPosition());
    }

    private int convertItemToYear(int itemPosition) {
        return minYear + itemPosition;
    }

    public interface OnYearSelectedListener {
        void onYearSelected(WheelYearPicker picker, int position, int year);
    }
}
