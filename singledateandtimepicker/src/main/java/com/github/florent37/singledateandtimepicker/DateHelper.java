package com.github.florent37.singledateandtimepicker;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {
    // Don't use static, as timezone may change while app is alive
    private TimeZone timeZone = TimeZone.getDefault();

    public DateHelper() {
        this.timeZone = TimeZone.getDefault();
    }

    public DateHelper(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setTimeZone(TimeZone timeZoneValue) {
        timeZone = timeZoneValue;
        System.out.println("DateHelper setTimeZone(): " + timeZoneValue.getID());
    }

    @NonNull
    public TimeZone getTimeZone() {
        if(this.timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        System.out.println("DateHelper getTimeZone(): " + timeZone.getID());
        return timeZone;
    }

    public Calendar getCalendarOfDate(Date date) {
        final String formatter = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter, Locale.getDefault());
        System.out.println("DateHelper getCalendarOfDate(): Original " + simpleDateFormat.format(date));
        final Calendar calendar = Calendar.getInstance(getTimeZone());
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        System.out.println("DateHelper getCalendarOfDate(): " + simpleDateFormat.format(calendar.getTime()));
        return calendar;
    }

    public int getHour(Date date) {
        return getCalendarOfDate(date).get(Calendar.HOUR);
    }

    public int getHourOfDay(Date date) {
        return getCalendarOfDate(date).get(Calendar.HOUR_OF_DAY);
    }

    public int getHour(Date date, boolean isAmPm) {
        if (isAmPm) {
            return getHourOfDay(date);
        } else {
            return getHour(date);
        }
    }

    public int getMinuteOf(Date date) {
        return getCalendarOfDate(date).get(Calendar.MINUTE);
    }

    public Date today() {
        Calendar now = Calendar.getInstance(getTimeZone());
        return now.getTime();
    }

    public int getMonth(Date date) {
        return getCalendarOfDate(date).get(Calendar.MONTH);
    }

    public int getDay(Date date) {
        return getCalendarOfDate(date).get(Calendar.DAY_OF_MONTH);
    }

    public static int compareDateIgnoreTime(Date first, Date second) {
        Date firstZeroTime = getZeroTimeDateWithoutTimeZone(first);
        Date secondZeroTime = getZeroTimeDateWithoutTimeZone(second);
        return firstZeroTime.compareTo(secondZeroTime);
    }

    private static Date getZeroTimeDateWithoutTimeZone(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
