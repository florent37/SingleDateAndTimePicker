package com.github.florent37.singledateandtimepicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {

    private static TimeZone timeZone = TimeZone.getDefault();

    public static void setTimeZone(TimeZone timeZoneValue)  {
        timeZone = timeZoneValue;
    }

    public static TimeZone getTimeZone() {
        return timeZone;
    }

    public static Calendar getCalendarOfDate(Date date){
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeZone(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public static int getHour(Date date){
        return getCalendarOfDate(date).get(Calendar.HOUR);
    }

    public static int getHourOfDay(Date date){
        return getCalendarOfDate(date).get(Calendar.HOUR);
    }

    public static int getHour(Date date, boolean isAmPm){
        if(isAmPm){
            return getHourOfDay(date);
        } else {
            return getHour(date);
        }
    }

    public static int getMinuteOf(Date date) {
        return getCalendarOfDate(date).get(Calendar.MINUTE);
    }

    public static Date today() {
        Calendar now  = Calendar.getInstance(Locale.getDefault());
        now.setTimeZone(timeZone);
        return now.getTime();
    }

    public static int getMonth(Date date) {
        return getCalendarOfDate(date).get(Calendar.MONTH);
    }

    public static int getDay(Date date){
        return getCalendarOfDate(date).get(Calendar.DAY_OF_MONTH);
    }

    public static int compareDateIgnoreTime(Date first, Date second) {
        Date firstZeroTime = getZeroTimeDate(first);
        Date secondZeroTime = getZeroTimeDate(second);

        return firstZeroTime.compareTo(secondZeroTime);
    }

    private static Date getZeroTimeDate(Date date) {
        Date res = date;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( res );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }

}
