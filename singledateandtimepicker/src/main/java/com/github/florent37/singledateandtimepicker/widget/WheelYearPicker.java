package com.github.florent37.singledateandtimepicker.widget;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.DateHelper;
import com.github.florent37.singledateandtimepicker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelYearPicker extends WheelPicker<String> {

    private SimpleDateFormat simpleDateFormat;

    private OnYearSelectedListener onYearSelectedListener;

    public WheelYearPicker(Context context) {
        super(context);
    }

    public WheelYearPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        simpleDateFormat = new SimpleDateFormat("EEE d MMM", getCurrentLocale());
    }

    @Override
    protected String initDefault() {
        return getTodayText();
    }

    @NonNull
    private String getTodayText() {
        return getResources().getString(R.string.picker_today);
    }

    public WheelYearPicker setDayFormatter(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
        adapter.setData(generateAdapterValues());
        notifyDatasetChanged();
        return this;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (onYearSelectedListener != null) {
            final Date date = convertItemToDate(position);
            onYearSelectedListener.onYearSelected(this, position, item, date);
        }
    }

    @Override
    protected List<String> generateAdapterValues() {
        final List<String> years = new ArrayList<>();

        final Calendar instance = Calendar.getInstance();
        int currentYear = instance.get(Calendar.YEAR);
        instance.set(Calendar.YEAR, currentYear - 100);

        for (int i = currentYear - 100; i < currentYear + 100; i++) {
            instance.add(Calendar.YEAR, 1);
            years.add(getFormattedValue(instance.getTime()));
        }

        return years;
    }

    protected String getFormattedValue(Object value) {
        return simpleDateFormat.format(value);
    }

    public void setOnYearSelectedListener(OnYearSelectedListener onYearSelectedListener) {
        this.onYearSelectedListener = onYearSelectedListener;
    }

    public Date getCurrentDate() {
        return convertItemToDate(super.getCurrentItemPosition());
    }

    private Date convertItemToDate(int itemPosition) {
        Date date = null;
        final String itemText = adapter.getItemText(itemPosition);
        final Calendar todayCalendar = Calendar.getInstance();

        final int todayPosition = adapter.getData().indexOf(getTodayText());

        if (getTodayText().equals(itemText)) {
            date = todayCalendar.getTime();
        } else {
            try {
                date = simpleDateFormat.parse(itemText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (date != null) {
            //try to know the year
            final Calendar dateCalendar = DateHelper.getCalendarOfDate(date);

            todayCalendar.add(Calendar.YEAR, (itemPosition - todayPosition));

            dateCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR));
            date = dateCalendar.getTime();
        }

        return date;
    }

    public void setTodayText(String todayText) {
        int index = adapter.getData().indexOf(getTodayText());
        if (index != -1) {
            adapter.getData().set(index, todayText);
            notifyDatasetChanged();
        }
    }

    public interface OnYearSelectedListener {
        void onYearSelected(WheelYearPicker picker, int position, String name, Date date);
    }
}