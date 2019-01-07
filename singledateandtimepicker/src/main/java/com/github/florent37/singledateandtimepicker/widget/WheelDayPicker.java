package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.DateHelper;
import com.github.florent37.singledateandtimepicker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.*;

public class WheelDayPicker extends WheelPicker<String> {

    private SimpleDateFormat simpleDateFormat;

    private OnDaySelectedListener onDaySelectedListener;

    public WheelDayPicker(Context context) {
        super(context);
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        simpleDateFormat = new SimpleDateFormat("EEE d MMM", getCurrentLocale());
    }

    @NonNull
    private String getTodayText() {
        return getResources().getString(R.string.picker_today);
    }

    public WheelDayPicker setDayFormatter(SimpleDateFormat simpleDateFormat){
        this.simpleDateFormat = simpleDateFormat;
        adapter.setData(generateAdapterValues());
        notifyDatasetChanged();
        return this;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (onDaySelectedListener != null) {
            final Date date = convertItemToDate(position);
            onDaySelectedListener.onDaySelected(this, position, item, date);
        }
    }

    @Override
    protected List<String> generateAdapterValues() {
        final List<String> days = new ArrayList<>();

        Calendar instance = Calendar.getInstance();
        Date today = instance.getTime();
        instance.setTime(defaultDate);

        instance.add(Calendar.DATE, -DAYS_PADDING);
        for (int i = 0; i < (2 * DAYS_PADDING + 1); ++i) {
            if (DateHelper.isSameDay(today, instance.getTime())){
                days.add(getTodayText());
            }
            else {
                days.add(getFormattedValue(instance.getTime()));
            }
            instance.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }

    @Override
    public int findIndexOfDate(Date d){

        int diffDays = DateHelper.daysBetween(d, defaultDate);
        int mid = adapter.getItemCount() / 2;
        if (Math.abs(diffDays) > mid){
            return -1;
        }
        return mid + diffDays;
    }

    protected String getFormattedValue(Object value) {
        return simpleDateFormat.format(value);
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    public Date getCurrentDate() {
        return convertItemToDate(super.getCurrentItemPosition());
    }

    private Date convertItemToDate(int itemPosition) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(defaultDate);

        int diffDays = itemPosition - adapter.getItemCount() / 2;
        instance.add(Calendar.DAY_OF_YEAR, diffDays);

        return instance.getTime();
    }

    public void setTodayText(String todayText) {
        int index = findIndexOfDate(DateHelper.today()); //FIXME: add logging
        if (index != -1) {
            adapter.getData().set(index, todayText);
            notifyDatasetChanged();
        }
    }

    public interface OnDaySelectedListener {
        void onDaySelected(WheelDayPicker picker, int position, String name, Date date);
    }
}