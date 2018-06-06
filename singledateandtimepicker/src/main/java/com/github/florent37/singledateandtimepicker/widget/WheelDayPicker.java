package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.DateHelper;
import com.github.florent37.singledateandtimepicker.R;
import com.github.florent37.wheelpicker.WheelAdapter;
import com.github.florent37.wheelpicker.WheelPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.DAYS_PADDING;

public class WheelDayPicker extends WheelPicker<Date> implements DateTimeWheelPicker {

    private SimpleDateFormat simpleDateFormat;

    private OnDaySelectedListener onDaySelectedListener;

    private String todayText;

    public WheelDayPicker(Context context) {
        super(context);
        init();
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        simpleDateFormat = new SimpleDateFormat("EEE d MMM YYYY", getCurrentLocale());
        todayText = getResources().getString(R.string.picker_today);
        setAdapter(new DateWheelAdapter(generateAdapterValues()));
        setDefault(new Date());
    }

    public WheelDayPicker setDayFormatter(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
        notifyDatasetChanged();
        return this;
    }

    @Override
    protected void onItemSelected(int position, Date date) {
        if (onDaySelectedListener != null) {
            onDaySelectedListener.onDaySelected(this, position, formatDate(date), date);
        }
    }

    protected List<Date> generateAdapterValues() {
        final List<Date> days = new ArrayList<>();

        Calendar instance = Calendar.getInstance();
        instance.setTime(normalizeDate(instance.getTime()));
        instance.add(Calendar.DATE, -1 * DAYS_PADDING - 1);

        for (int i = 0; i < 2 * DAYS_PADDING + 1; i++) {
            instance.add(Calendar.DATE, 1);
            days.add(instance.getTime());
        }

        return days;
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    public Date getCurrentDate() {
        return adapter.getItem(super.getCurrentItemPosition());
    }

    public void setTodayText(String todayText) {
        this.todayText = todayText;
        notifyDatasetChanged();
    }

    public interface OnDaySelectedListener {
        void onDaySelected(WheelDayPicker picker, int position, String name, Date date);
    }

    private String formatDate(Date date) {
        if (normalizeDate(date).equals(normalizeDate(DateHelper.today()))) {
            return todayText;
        }
        return simpleDateFormat.format(date);
    }

    private Date normalizeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    public void setDefaultDate(Date date) {
        setDefault(normalizeDate(date));
    }

    @Override
    public void selectDate(Date date) {
        int position = findIndexOfDate(date);
        scrollTo(position);
        setSelectedItemPosition(position);
    }

    @Override
    public int findIndexOfDate(Date date) {
        return adapter.getItemPosition(normalizeDate(date));
    }

    class DateWheelAdapter extends WheelAdapter<Date> {
        public DateWheelAdapter() {
        }

        public DateWheelAdapter(List<Date> data) {
            super(data);
        }

        @Override
        public String getItemText(int position) {
            Date date = getItem(position);
            return formatDate(date);
        }
    }
}