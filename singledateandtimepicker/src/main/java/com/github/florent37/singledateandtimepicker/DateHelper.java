package com.github.florent37.singledateandtimepicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public static Calendar getCalendarOfDate(Date date){
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
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
        return Calendar.getInstance(Locale.getDefault()).getTime();
    }

    public static int getMonth(Date date) {
        return getCalendarOfDate(date).get(Calendar.MONTH);
    }

    public static int getDay(Date date){
        return getCalendarOfDate(date).get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isSameDay(Date a, Date b)
    {
        return a.getYear()  == b.getYear()  &&
                a.getMonth() == b.getMonth() &&
                a.getDate()   == b.getDate();
    }

    //returns the difference in days: endDate - startDate  (negative if endDate is before startDate)
    public static int daysBetween(Date endDate, Date startDate)
    {
        //there is a fast and potentially incorrect implementation, and a slow yet correct one, we're
        // going with a hybrid approach that is both fast and correct, stackoverflow: /3796841

        long diffTime = endDate.getTime() - startDate.getTime();
        int diffDays = (int) (diffTime / (1000 * 60 * 60 * 24));

        Calendar assumed = Calendar.getInstance();
        assumed.setTime(startDate);
        assumed.add(Calendar.DAY_OF_YEAR, diffDays);

        int diff = assumed.getTime().before(endDate) ? 1 : -1; //we're either undershooting or overshooting

        while(!isSameDay(assumed.getTime(), endDate)){
            assumed.add(Calendar.DAY_OF_YEAR, diff);
            diffDays += diff;
        }

        return diffDays;
    }
}
