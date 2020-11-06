package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WheelAmPmPicker extends WheelPicker<String> {

    public static final int INDEX_AM = 0;
    public static final int INDEX_PM = 1;

    @Nullable
    private AmPmListener amPmListener;

    public WheelAmPmPicker(Context context) {
        super(context);
    }

    public WheelAmPmPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {

    }

    @Override
    protected String initDefault() {
        if (dateHelper.getHour(dateHelper.today(), true) >= SingleDateAndTimeConstants.MAX_HOUR_AM_PM) {
            return getLocalizedString(R.string.picker_pm);
        } else {
            return getLocalizedString(R.string.picker_am);
        }
    }

    @Override
    protected List<String> generateAdapterValues(boolean showOnlyFutureDates){
        return Arrays.asList(
                getLocalizedString(R.string.picker_am),
                getLocalizedString(R.string.picker_pm)
        );
    }

    @Override
    public int findIndexOfDate(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(dateHelper.getTimeZone());
        calendar.setTime(date);
        final int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours >= SingleDateAndTimeConstants.MAX_HOUR_AM_PM) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setAmPmListener(@Nullable AmPmListener amPmListener) {
        this.amPmListener = amPmListener;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        super.onItemSelected(position, item);

        if (amPmListener != null) {
            amPmListener.onAmPmChanged(this, isAm());
        }
    }

    @Override
    public void setCyclic(boolean isCyclic) {
        super.setCyclic(false);
    }

    public boolean isAmPosition(int position) {
        return position == INDEX_AM;
    }

    @Override
    protected String getFormattedValue(Object value) {
        if (value instanceof Date) {
            Calendar instance = Calendar.getInstance();
            instance.setTimeZone(dateHelper.getTimeZone());
            instance.setTime((Date) value);
            return getLocalizedString(instance.get(Calendar.AM_PM) == Calendar.PM ? R.string.picker_pm : R.string.picker_am);
        }
        return String.valueOf(value);
    }

    public boolean isAm() {
        return getCurrentItemPosition() == INDEX_AM;
    }

    public boolean isPm() {
        return getCurrentItemPosition() == INDEX_PM;
    }

    public interface AmPmListener {
        void onAmPmChanged(WheelAmPmPicker pmPicker, boolean isAm);
    }
}