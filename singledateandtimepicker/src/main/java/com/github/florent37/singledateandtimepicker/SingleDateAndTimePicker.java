package com.github.florent37.singledateandtimepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
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
    public static final boolean CAN_BE_ON_PAST_DEFAULT = false;
    public static final int DELAY_BEFORE_CHECK_PAST = 200;
    private static final int VISIBLE_ITEM_COUNT_DEFAULT = 7;

    private WheelDayPicker daysPicker;
    private WheelMinutePicker minutesPicker;
    private WheelHourPicker hoursPicker;

    private Listener listener;

    private int textColor;
    private int selectedTextColor;
    private int textSize;
    private boolean isCyclic;
    private boolean isCurved;
    private int visibleItemCount;

    private boolean canBeOnPast;

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

        daysPicker = (WheelDayPicker) findViewById(R.id.daysPicker);
        minutesPicker = (WheelMinutePicker) findViewById(R.id.minutesPicker);
        hoursPicker = (WheelHourPicker) findViewById(R.id.hoursPicker);

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
        });

        hoursPicker.setOnHourSelectedListener(new WheelHourPicker.OnHourSelectedListener() {
            @Override
            public void onHourSelected(WheelHourPicker picker, int position, int hours) {
                updateListener();
                checkInPast(picker);
            }
        });

        updatePicker();
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

    private void updatePicker(){
        if(daysPicker != null && minutesPicker != null && hoursPicker != null) {
            for (WheelPicker wheelPicker : Arrays.asList(daysPicker, minutesPicker, hoursPicker)) {
                wheelPicker.setItemTextColor(textColor);
                wheelPicker.setSelectedItemTextColor(selectedTextColor);
                wheelPicker.setItemTextSize(textSize);
                wheelPicker.setCyclic(isCyclic);
                wheelPicker.setCurved(isCurved);
                wheelPicker.setVisibleItemCount(visibleItemCount);
            }
        }
    }

    private void checkInPast(final WheelPicker picker) {
        picker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!canBeOnPast && isInPast(getDate())) {
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

    public Date getDate(){
        final int hour = hoursPicker.getCurrentHour();
        final int minute = minutesPicker.getCurrentMinute();

        final Calendar calendar = Calendar.getInstance();
        final Date dayDate = daysPicker.getCurrentDate();
        calendar.setTime(dayDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        final Date time = calendar.getTime();
        return time;
    }

    private void updateListener() {
        final int hour = hoursPicker.getCurrentHour();
        final int minute = minutesPicker.getCurrentMinute();
        final String displayed = daysPicker.getCurrentDay() + " " + hour + ":" + minute;

        if (listener != null) {
            listener.onDateChanged(displayed, getDate());
        }
    }

    public void setCanBeOnPast(boolean canBeOnPast) {
        this.canBeOnPast = canBeOnPast;
    }

    public boolean canBeOnPast() {
        return canBeOnPast;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SingleDateAndTimePicker);

        textColor = a.getColor(R.styleable.SingleDateAndTimePicker_picker_textColor,
            getResources().getColor(R.color.picker_default_text_color));
        selectedTextColor = a.getColor(R.styleable.SingleDateAndTimePicker_picker_selectedTextColor,
            getResources().getColor(R.color.picker_default_selected_text_color));
        textSize = a.getDimensionPixelSize(R.styleable.SingleDateAndTimePicker_picker_textSize,
            getResources().getDimensionPixelSize(R.dimen.WheelItemTextSize));
        isCurved = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_curved, IS_CURVED_DEFAULT);
        isCyclic = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_cyclic, IS_CYCLIC_DEFAULT);
        canBeOnPast = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_canBeOnPast, CAN_BE_ON_PAST_DEFAULT);
        visibleItemCount = a.getInt(R.styleable.SingleDateAndTimePicker_picker_visibleItemCount, VISIBLE_ITEM_COUNT_DEFAULT);

        a.recycle();
    }

    public interface Listener {
        void onDateChanged(String displayed, Date date);
    }
}
