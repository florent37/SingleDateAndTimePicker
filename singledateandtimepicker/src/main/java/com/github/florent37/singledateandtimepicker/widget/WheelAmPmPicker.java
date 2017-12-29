package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelAmPmPicker extends WheelPicker<String> {

    public static final int INDEX_AM = 0;
    public static final int INDEX_PM = 1;

    public WheelAmPmPicker(Context context) {
        super(context);
    }

    public WheelAmPmPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void initAdapter() {
        final List<String> values = new ArrayList<>();
        final Resources resources = getResources();
        values.add(resources.getString(R.string.picker_am));
        values.add(resources.getString(R.string.picker_pm));
        setAdapter(new Adapter<>(values));
    }

    public boolean isAmPosition(int position){
        return position == INDEX_AM;
    }

    @Override
    protected String getFormattedValue(Object value) {
        if (value instanceof Date) {
            Calendar instance = Calendar.getInstance();
            instance.setTime((Date) value);
            return getResources().getString(instance.get(Calendar.AM_PM) == Calendar.PM ? R.string.picker_pm: R.string.picker_am);
        }
        return String.valueOf(value);
    }

    @Override
    public int getDefaultItemPosition() {
        return INDEX_AM;
    }

    public boolean isAm() {
        return getCurrentItemPosition() == INDEX_AM;
    }

    public boolean isPm() {
        return getCurrentItemPosition() == INDEX_PM;
    }

    public interface Listener extends WheelPicker.Listener<WheelAmPmPicker, String> {

    }
}