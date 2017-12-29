package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WheelMonthPicker extends WheelPicker<String> {

    private int lastScrollPosition;

    private Listener listener;

    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAdapter();
    }

    private void initAdapter() {
        final SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.getDefault());
        final Calendar cal = Calendar.getInstance(Locale.getDefault());

        final List<String> monthList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            cal.set(Calendar.MONTH, i);
            String month_name = month_date.format(cal.getTime());
            monthList.add(month_name);
        }

        setAdapter(new Adapter<String>(monthList));

        super.defaultValue = month_date.format(Calendar.getInstance(Locale.getDefault()).getTime());

        updateDefault();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (listener != null) {
            listener.onSelected(this, position, item);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, String item) {
        if (lastScrollPosition != position) {
            if (listener != null) {
                listener.onSelected(this, position, item);
            }
            lastScrollPosition = position;
        }
    }

    private int convertItemToMinute(Object item) {
        return Integer.valueOf(String.valueOf(item));
    }

    public int getCurrentMinute() {
        return convertItemToMinute(adapter.getItem(getCurrentItemPosition()));
    }

    public interface Listener {
        void onSelected(WheelMonthPicker picker, int position, String month);
    }
}