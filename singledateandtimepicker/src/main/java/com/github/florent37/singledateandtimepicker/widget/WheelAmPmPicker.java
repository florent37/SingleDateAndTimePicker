package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.DateHelper;
import com.github.florent37.singledateandtimepicker.R;
import com.github.florent37.wheelpicker.WheelAdapter;
import com.github.florent37.wheelpicker.WheelPicker;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WheelAmPmPicker extends WheelPicker<String> implements DateTimeWheelPicker {

    public static final int INDEX_AM = 0;
    public static final int INDEX_PM = 1;

    @Nullable
    private AmPmListener amPmListener;

    public WheelAmPmPicker(Context context) {
        super(context);
        init();
    }

    public WheelAmPmPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        setAdapter(new WheelAdapter(generateAdapterValues()));

        if (DateHelper.getHour(DateHelper.today(), true) >= SingleDateAndTimeConstants.MAX_HOUR_AM_PM) {
            setDefault(getContext().getString(R.string.picker_pm));
        } else {
            setDefault(getContext().getString(R.string.picker_am));
        }
    }

    protected List<String> generateAdapterValues() {
        return Arrays.asList(
                getContext().getString(R.string.picker_am),
                getContext().getString(R.string.picker_pm)
        );
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

    public boolean isAm() {
        return getCurrentItemPosition() == INDEX_AM;
    }

    public boolean isPm() {
        return getCurrentItemPosition() == INDEX_PM;
    }

    @Override
    public void setDefaultDate(Date date) {
        if (DateHelper.getHour(date, true) >= SingleDateAndTimeConstants.MAX_HOUR_AM_PM) {
            setDefault(getContext().getString(R.string.picker_pm));
        } else {
            setDefault(getContext().getString(R.string.picker_am));
        }
    }

    @Override
    public void selectDate(Date date) {
        int position = findIndexOfDate(date);
        scrollTo(position);
        setSelectedItemPosition(position);
    }

    @Override
    public int findIndexOfDate(Date date) {
        if (DateHelper.getHour(date, true) >= SingleDateAndTimeConstants.MAX_HOUR_AM_PM) {
            return adapter.getItemPosition(getContext().getString(R.string.picker_pm));
        } else {
            return adapter.getItemPosition(getContext().getString(R.string.picker_am));
        }
    }

    public interface AmPmListener {
        void onAmPmChanged(WheelAmPmPicker pmPicker, boolean isAm);
    }
}