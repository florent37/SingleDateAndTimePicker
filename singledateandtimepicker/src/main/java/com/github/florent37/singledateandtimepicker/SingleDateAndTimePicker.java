package com.github.florent37.singledateandtimepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.florent37.singledateandtimepicker.widget.WheelAmPmPicker;
import com.github.florent37.singledateandtimepicker.widget.WheelDayPicker;
import com.github.florent37.singledateandtimepicker.widget.WheelHourPicker;
import com.github.florent37.singledateandtimepicker.widget.WheelMinutePicker;
import com.github.florent37.singledateandtimepicker.widget.WheelPicker;

import java.text.SimpleDateFormat;
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

    private static final CharSequence FORMAT_24_HOUR = "EEE d MMM H:mm";
    private static final CharSequence FORMAT_12_HOUR = "EEE d MMM h:mm a";

    private WheelDayPicker daysPicker;
    private WheelMinutePicker minutesPicker;
    private WheelHourPicker hoursPicker;
    private WheelAmPmPicker amPmPicker;

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

    private Date minDate;
    private Date maxDate;

    private boolean displayDays = true;
    private boolean displayMinutes = true;
    private boolean displayHours = true;

    private boolean isAmPm;
    private int selectorHeight;

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

        isAmPm = !(DateFormat.is24HourFormat(context));

        daysPicker = (WheelDayPicker) findViewById(R.id.daysPicker);
        minutesPicker = (WheelMinutePicker) findViewById(R.id.minutesPicker);
        hoursPicker = (WheelHourPicker) findViewById(R.id.hoursPicker);
        amPmPicker = (WheelAmPmPicker) findViewById(R.id.amPmPicker);
        dtSelector = findViewById(R.id.dtSelector);

        final ViewGroup.LayoutParams dtSelectorLayoutParams = dtSelector.getLayoutParams();
        dtSelectorLayoutParams.height = selectorHeight;
        dtSelector.setLayoutParams(dtSelectorLayoutParams);

        daysPicker.setOnDaySelectedListener(new WheelDayPicker.OnDaySelectedListener() {
            @Override
            public void onDaySelected(WheelDayPicker picker, int position, String name, Date date) {
                updateListener();
                checkMinMaxDate(picker);
            }
        });

        minutesPicker.setOnMinuteSelectedListener(new WheelMinutePicker.OnMinuteSelectedListener() {
            @Override
            public void onMinuteSelected(WheelMinutePicker picker, int position, int minutes) {
                updateListener();
                checkMinMaxDate(picker);
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
                checkMinMaxDate(picker);
            }

            @Override
            public void onHourCurrentScrolled(WheelHourPicker picker, int position, int hours) {

            }

            @Override
            public void onHourCurrentNewDay(WheelHourPicker picker) {
                daysPicker.scrollTo(daysPicker.getCurrentItemPosition() + 1);
            }
        });

        amPmPicker.setOnAmPmSelectedListener(new WheelAmPmPicker.OnAmPmSelectedListener() {
            @Override
            public void onAmSelected(WheelAmPmPicker picker) {
                updateListener();
                checkMinMaxDate(picker);
            }

            @Override
            public void onPmSelected(WheelAmPmPicker picker) {
                updateListener();
                checkMinMaxDate(picker);
            }
        });

        updatePicker();
        updateViews();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        daysPicker.setEnabled(enabled);
        minutesPicker.setEnabled(enabled);
        hoursPicker.setEnabled(enabled);
        amPmPicker.setEnabled(enabled);
    }

    public void setDisplayDays(boolean displayDays) {
        this.displayDays = displayDays;
        updateViews();
        updatePicker();
    }

    public void setDisplayMinutes(boolean displayMinutes) {
        this.displayMinutes = displayMinutes;
        updateViews();
        updatePicker();
    }

    public void setDisplayHours(boolean displayHours) {
        this.displayHours = displayHours;
        updateViews();
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
        updatePicker();
    }

    public void setDayFormatter(SimpleDateFormat simpleDateFormat) {
        if (simpleDateFormat != null) {
            this.daysPicker.setDayFormatter(simpleDateFormat);
        }
    }

    public boolean isAmPm() {
        return isAmPm;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    private void updatePicker() {
        if (daysPicker != null && minutesPicker != null && hoursPicker != null && amPmPicker != null) {
            for (WheelPicker wheelPicker : Arrays.asList(daysPicker, minutesPicker, hoursPicker, amPmPicker)) {
                wheelPicker.setItemTextColor(textColor);
                wheelPicker.setSelectedItemTextColor(selectedTextColor);
                wheelPicker.setItemTextSize(textSize);
                wheelPicker.setVisibleItemCount(visibleItemCount);
                wheelPicker.setCurved(isCurved);
                if (wheelPicker != amPmPicker) {
                    wheelPicker.setCyclic(isCyclic);
                }
            }
        }

        if (amPmPicker != null) {
            amPmPicker.setVisibility((isAmPm && displayHours) ? VISIBLE : GONE);
        }
        if (hoursPicker != null) {
            hoursPicker.setIsAmPm(isAmPm);
        }

        if (hoursPicker != null) {
            hoursPicker.setVisibility(displayHours ? VISIBLE : GONE);
        }
        if (minutesPicker != null) {
            minutesPicker.setVisibility(displayMinutes ? VISIBLE : GONE);
        }
        if (daysPicker != null) {
            daysPicker.setVisibility(displayDays ? VISIBLE : GONE);
        }
    }

    private void updateViews() {
        dtSelector.setBackgroundColor(selectorColor);
    }

    private void checkMinMaxDate(final WheelPicker picker) {
        checkBeforeMinDate(picker);
        checkAfterMaxDate(picker);
    }

    private void checkBeforeMinDate(final WheelPicker picker) {
        picker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (minDate != null && isBeforeMinDate(getDate())) {
                    //scroll to Min position
                    daysPicker.scrollTo(daysPicker.findIndexOfDate(minDate));
                    minutesPicker.scrollTo(minutesPicker.findIndexOfDate(minDate));
                    hoursPicker.scrollTo(hoursPicker.findIndexOfDate(minDate));
                }
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void checkAfterMaxDate(final WheelPicker picker) {
        picker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (maxDate != null && isAfterMaxDate(getDate())) {
                    //scroll to Max position
                    daysPicker.scrollTo(daysPicker.findIndexOfDate(maxDate));
                    minutesPicker.scrollTo(minutesPicker.findIndexOfDate(maxDate));
                    hoursPicker.scrollTo(hoursPicker.findIndexOfDate(maxDate));
                }
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private boolean isBeforeMinDate(Date date) {
        final Calendar minDateCalendar = Calendar.getInstance();
        minDateCalendar.setTime(minDate);
        minDateCalendar.set(Calendar.MILLISECOND, 0);
        minDateCalendar.set(Calendar.SECOND, 0);

        final Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        dateCalendar.set(Calendar.MILLISECOND, 0);
        dateCalendar.set(Calendar.SECOND, 0);

        return dateCalendar.before(minDateCalendar);
    }

    private boolean isAfterMaxDate(Date date) {
        final Calendar maxDateCalendar = Calendar.getInstance();
        maxDateCalendar.setTime(maxDate);
        maxDateCalendar.set(Calendar.MILLISECOND, 0);
        maxDateCalendar.set(Calendar.SECOND, 0);

        final Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        dateCalendar.set(Calendar.MILLISECOND, 0);
        dateCalendar.set(Calendar.SECOND, 0);

        return dateCalendar.after(maxDateCalendar);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Date getDate() {
        int hour = hoursPicker.getCurrentHour();
        if (isAmPm && amPmPicker.isPm()) {
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
        if (indexOfDay != -1) {
            daysPicker.setSelectedItemPosition(indexOfDay);
        }
        int indexOfHour = hoursPicker.findIndexOfDate(date);
        if (indexOfHour != -1) {
            if (isAmPm) {
                if (calendar.get(Calendar.HOUR_OF_DAY) >= WheelHourPicker.MAX_HOUR_AM_PM) {
                    amPmPicker.setPmSelected();
                } else {
                    amPmPicker.setAmSelected();
                }
            }
            hoursPicker.setSelectedItemPosition(indexOfHour);
        }
        int indexOfMin = minutesPicker.findIndexOfDate(date);
        if (indexOfMin != -1) {
            minutesPicker.setSelectedItemPosition(indexOfMin);
        }
    }

    private void updateListener() {
        final Date date = getDate();
        CharSequence format = isAmPm ? FORMAT_12_HOUR : FORMAT_24_HOUR;
        String displayed = DateFormat.format(format, date).toString();
        if (listener != null) {
            listener.onDateChanged(displayed, date);
        }
    }

    public void setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        if (mustBeOnFuture) {
            minDate = Calendar.getInstance().getTime(); //minDate is Today
        }
    }

    public boolean mustBeOnFuture() {
        return mustBeOnFuture;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SingleDateAndTimePicker);

        final Resources resources = getResources();
        textColor = a.getColor(R.styleable.SingleDateAndTimePicker_picker_textColor,
                resources.getColor(R.color.picker_default_text_color));
        selectedTextColor = a.getColor(R.styleable.SingleDateAndTimePicker_picker_selectedTextColor,
                resources.getColor(R.color.picker_default_selected_text_color));
        selectorColor = a.getColor(R.styleable.SingleDateAndTimePicker_picker_selectorColor,
                resources.getColor(R.color.picker_default_selector_color));
        selectorHeight = a.getDimensionPixelSize(R.styleable.SingleDateAndTimePicker_picker_selectorHeight, resources.getDimensionPixelSize(R.dimen.wheelSelectorHeight));
        textSize = a.getDimensionPixelSize(R.styleable.SingleDateAndTimePicker_picker_textSize,
                resources.getDimensionPixelSize(R.dimen.WheelItemTextSize));
        isCurved = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_curved, IS_CURVED_DEFAULT);
        isCyclic = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_cyclic, IS_CYCLIC_DEFAULT);
        mustBeOnFuture = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_mustBeOnFuture, MUST_BE_ON_FUTUR_DEFAULT);
        visibleItemCount = a.getInt(R.styleable.SingleDateAndTimePicker_picker_visibleItemCount, VISIBLE_ITEM_COUNT_DEFAULT);

        displayDays = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayDays, displayDays);
        displayMinutes = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayMinutes, displayMinutes);
        displayHours = a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayHours, displayHours);

        a.recycle();
    }

    public interface Listener {
        void onDateChanged(String displayed, Date date);
    }
}
