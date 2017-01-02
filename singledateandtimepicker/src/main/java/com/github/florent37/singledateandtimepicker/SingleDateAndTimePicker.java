package com.github.florent37.singledateandtimepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.text.format.DateFormat;

import com.github.florent37.singledateandtimepicker.widget.WheelDayPicker;
import com.github.florent37.singledateandtimepicker.widget.WheelHourPicker;
import com.github.florent37.singledateandtimepicker.widget.WheelMinutePicker;
import com.github.florent37.singledateandtimepicker.widget.WheelPicker;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class SingleDateAndTimePicker extends LinearLayout {

    public static final boolean IS_CYCLIC_DEFAULT = true;
    public static final boolean IS_CURVED_DEFAULT = false;
    public static final boolean MUST_BE_ON_FUTUR_DEFAULT = false;
    public static final int DELAY_BEFORE_CHECK_PAST = 200;
    private static final int VISIBLE_ITEM_COUNT_DEFAULT = 7;
    private static final int PM_HOUR_ADDITION = 12;

    private WheelDayPicker daysPicker;
    private WheelMinutePicker minutesPicker;
    private WheelHourPicker hoursPicker;

    private View amPmLayout;
    private CheckedTextView amTextView;
    private CheckedTextView pmTextView;

    private Listener listener;

    private int textColor;
    private int selectedTextColor;
    private int textSize;
    private int selectorColor;
    private boolean isCyclic;
    private boolean isCurved;
    private int visibleItemCount;
    private View dtSelector;
    private boolean mustBeOnFuture;

    private boolean isAmPm;

    public SingleDateAndTimePicker(Context context) {
        this(context, null);
    }

    public SingleDateAndTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleDateAndTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        inflate(context, R.layout.single_day_picker, this);

        isAmPm = !(new DateFormat().is24HourFormat(context));

        daysPicker = (WheelDayPicker) findViewById(R.id.daysPicker);
        minutesPicker = (WheelMinutePicker) findViewById(R.id.minutesPicker);
        hoursPicker = (WheelHourPicker) findViewById(R.id.hoursPicker);
        dtSelector = findViewById(R.id.dtSelector);
        amPmLayout = findViewById(R.id.amPmLayout);
        amTextView = (CheckedTextView) findViewById(R.id.amTextView);
        pmTextView = (CheckedTextView) findViewById(R.id.pmTextView);

        daysPicker.setOnDaySelectedListener(new WheelDayPicker.OnDaySelectedListener() {
            @Override
            public void onDaySelected(WheelDayPicker picker, int position, String name, Date date) {
                updateListener();
                checkInPast(picker);
            }
        });

        minutesPicker.setOnMinuteSelectedListener(new WheelMinutePicker.OnMinuteSelectedListener() {
            @Override
            public void onMinuteSelected(WheelMinutePicker picker, int position, int minutes) {
                updateListener();
                checkInPast(picker);
            }

            @Override
            public void onMinuteCurrentScrolled(WheelMinutePicker picker, int position, int minutes) {

            }

            @Override
            public void onMinuteScrolledNewHour(WheelMinutePicker picker) {
                hoursPicker.scrollTo(hoursPicker.getCurrentItemPosition() + 1);
            }
        });

        hoursPicker.setOnHourSelectedListener(new WheelHourPicker.OnHourSelectedListener() {
            @Override
            public void onHourSelected(WheelHourPicker picker, int position, int hours) {
                updateListener();
                checkInPast(picker);
            }

            @Override
            public void onHourCurrentScrolled(WheelHourPicker picker, int position, int hours) {

            }

            @Override
            public void onHourCurrentNewDay(WheelHourPicker picker) {
                daysPicker.scrollTo(daysPicker.getCurrentItemPosition() + 1);
            }
        });

        updatePicker();
        updateViews();
    }

    public void setCurved(boolean curved) {
        isCurved = curved;
        updatePicker();
    }

    public void setCyclic(boolean cyclic) {
        isCyclic = cyclic;
        updatePicker();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        updatePicker();
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        updatePicker();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        updatePicker();
    }

    public void setSelectorColor(int selectorColor) {
        this.selectorColor = selectorColor;
        updateViews();
    }
    
    public void setVisibleItemCount(int visibleItemCount) {
        this.visibleItemCount = visibleItemCount;
        updatePicker();
    }

    public void setIsAmPm(boolean isAmPm) {
        this.isAmPm = isAmPm;
        updateViews();
    }

    public boolean isAmPm() {
        return isAmPm;
    }

    private void updatePicker() {
        if (daysPicker != null && minutesPicker != null && hoursPicker != null) {
            for (WheelPicker wheelPicker : Arrays.asList(daysPicker, minutesPicker, hoursPicker)) {
                wheelPicker.setItemTextColor(textColor);
                wheelPicker.setSelectedItemTextColor(selectedTextColor);
                wheelPicker.setItemTextSize(textSize);
                wheelPicker.setCyclic(isCyclic);
                wheelPicker.setCurved(isCurved);
                wheelPicker.setVisibleItemCount(visibleItemCount);
            }
        }
        
        if (hoursPicker != null) {
            hoursPicker.setIsAmPm(isAmPm);
        }
    }

    private void updateViews(){
        dtSelector.setBackgroundColor(selectorColor);
    }

    private void checkInPast(final WheelPicker picker) {
        picker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mustBeOnFuture && isInPast(getDate())) {
                    daysPicker.scrollTo(daysPicker.getDefaultItemPosition());
                    minutesPicker.scrollTo(minutesPicker.getDefaultItemPosition());
                    hoursPicker.scrollTo(hoursPicker.getDefaultItemPosition());
                }
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private boolean isInPast(Date date) {
        final Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.MILLISECOND, 0);
        todayCalendar.set(Calendar.SECOND, 0);

        final Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.set(Calendar.MILLISECOND, 0);
        dateCalendar.set(Calendar.SECOND, 0);

        dateCalendar.setTime(date);
        return dateCalendar.before(todayCalendar);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Date getDate() {
        int hour = hoursPicker.getCurrentHour();
        if(isAmPm && pmTextView.isChecked()) {
            hour += PM_HOUR_ADDITION;
        }
        final int minute = minutesPicker.getCurrentMinute();

        final Calendar calendar = Calendar.getInstance();
        final Date dayDate = daysPicker.getCurrentDate();
        calendar.setTime(dayDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        final Date time = calendar.getTime();
        return time;
    }

    public void setStepMinutes(int minutesStep) {
        minutesPicker.setStepMinutes(minutesStep);
    }

    public void setHoursStep(int hoursStep) {
        hoursPicker.setHoursStep(hoursStep);
    }

    public void selectDate(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        Date date = calendar.getTime();
        int indexOfDay = daysPicker.findIndexOfDate(date);
        if (indexOfDay != 0) {
            daysPicker.setSelectedItemPosition(indexOfDay);
        }
        int indexOfHour = hoursPicker.findIndexOfDate(date);
        if (indexOfHour != 0) {
            hoursPicker.setSelectedItemPosition(indexOfHour);
        }
        int indexOfMin = minutesPicker.findIndexOfDate(date);
        if (indexOfMin != 0) {
            minutesPicker.setSelectedItemPosition(indexOfMin);
        }
    }

    private void updateListener() {
        final int hour = hoursPicker.getCurrentHour();
        final int minute = minutesPicker.getCurrentMinute();
        final String displayed = daysPicker.getCurrentDay() + " " + hour + ":" + minute;

        if (listener != null) {
            listener.onDateChanged(displayed, getDate());
        }
    }

    public void setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
    }

    public boolean mustBeOnFuture() {
        return mustBeOnFuture;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SingleDateAndTimePicker);

        textColor = a.getColor(R.styleable.SingleDateAndTimePicker_picker_textColor,
                getResources().getColor(R.color.picker_default_text_color));
        selectedTextColor = a.getColor(R.styleable.SingleDateAndTimePicker_picker_selectedTextColor,
                getResources().getColor(R.color.picker_default_selected_text_color));
        selectorColor = a.getColor(R.styleable.SingleDateAndTimePicker_picker_selectorColor,
                getResources().getColor(R.color.picker_default_selector_color));
        textSize = a.getDimensionPixelSize(R.styleable.SingleDateAndTimePicker_picker_textSize,
                getResources().getDimensionPixelSize(R.dimen.WheelItemTextSize));
        isCurved = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_curved, IS_CURVED_DEFAULT);
        isCyclic = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_cyclic, IS_CYCLIC_DEFAULT);
        mustBeOnFuture = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_mustBeOnFuture, MUST_BE_ON_FUTUR_DEFAULT);
        visibleItemCount = a.getInt(R.styleable.SingleDateAndTimePicker_picker_visibleItemCount, VISIBLE_ITEM_COUNT_DEFAULT);

        a.recycle();
    }

    public interface Listener {
        void onDateChanged(String displayed, Date date);
    }
}
