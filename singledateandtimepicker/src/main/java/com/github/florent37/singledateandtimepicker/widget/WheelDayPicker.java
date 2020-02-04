package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.DateHelper;
import com.github.florent37.singledateandtimepicker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.*;

public class WheelDayPicker extends WheelPicker<String> {

    private static final String DAY_FORMAT_PATTERN = "EEE d MMM";

    private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat customDateFormat;

    private OnDaySelectedListener onDaySelectedListener;
    private Date minDate;
    private Date maxDate;

    public WheelDayPicker(Context context) {
        super(context);
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        simpleDateFormat = new SimpleDateFormat(DAY_FORMAT_PATTERN, getCurrentLocale());
        simpleDateFormat.setTimeZone(DateHelper.getTimeZone());
    }

    @Override
    public void setCustomLocale(Locale customLocale) {
        super.setCustomLocale(customLocale);
        simpleDateFormat = new SimpleDateFormat(DAY_FORMAT_PATTERN, getCurrentLocale());
        simpleDateFormat.setTimeZone(DateHelper.getTimeZone());
    }

    @Override
    protected String initDefault() {
        return getTodayText();
    }

    @NonNull
    private String getTodayText() {
        return getLocalizedString(R.string.picker_today);
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

        int minPadding = DAYS_PADDING;
        int maxPadding = DAYS_PADDING;

        if (minDate != null) {
            int dayDifference = calculateDayDifference(minDate);
            minPadding = dayDifference;
        }

        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(DateHelper.getTimeZone());
        instance.add(Calendar.DATE, -1 * minPadding - 1);

        for (int i = (-1) * minPadding; i < 0; ++i) {
            instance.add(Calendar.DAY_OF_MONTH, 1);
            days.add(getFormattedValue(instance.getTime()));
        }

        //today
        days.add(getTodayText());

        instance = Calendar.getInstance();
        instance.setTimeZone(DateHelper.getTimeZone());

        for (int i = 0; i < maxPadding; ++i) {
            instance.add(Calendar.DATE, 1);
            days.add(getFormattedValue(instance.getTime()));
        }

        return days;
    }

    private int calculateDayDifference(Date minDate) {
        Date today = Calendar.getInstance().getTime();
        long diffInMillies = Math.abs(today.getTime() - minDate.getTime());
        return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    protected String getFormattedValue(Object value) {
        return getDateFormat().format(value);
    }

    public void setMinDate(@Nullable Date date) {
        if (date != null) {
            this.minDate = date;
            updateAdapter();
        }
    }

    public void setMaxDate(Date date) {
        if (date != null) {
            this.maxDate = date;
            updateAdapter();
        }
    }

    public WheelDayPicker setDayFormatter(SimpleDateFormat simpleDateFormat) {
        simpleDateFormat.setTimeZone(DateHelper.getTimeZone());
        this.customDateFormat = simpleDateFormat;
        updateAdapter();
        return this;
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    public Date getCurrentDate() {
        return convertItemToDate(super.getCurrentItemPosition());
    }

    private SimpleDateFormat getDateFormat() {
        if (customDateFormat != null) {
            return customDateFormat;
        }
        return simpleDateFormat;
    }

    private Date convertItemToDate(int itemPosition) {
        Date date = null;
        final String itemText = adapter.getItemText(itemPosition);
        final Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTimeZone(DateHelper.getTimeZone());

        final int todayPosition = adapter.getData().indexOf(getTodayText());

        if (getTodayText().equals(itemText)) {
            date = todayCalendar.getTime();
        } else {
            try {
                date = getDateFormat().parse(itemText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (date != null) {
            //try to know the year
            final Calendar dateCalendar = DateHelper.getCalendarOfDate(date);

            todayCalendar.add(Calendar.DATE, (itemPosition - todayPosition));

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

    public interface OnDaySelectedListener {
        void onDaySelected(WheelDayPicker picker, int position, String name, Date date);
    }
}