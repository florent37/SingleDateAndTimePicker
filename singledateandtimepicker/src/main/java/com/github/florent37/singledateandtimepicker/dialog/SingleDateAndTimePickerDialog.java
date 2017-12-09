package com.github.florent37.singledateandtimepicker.dialog;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.R;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.widget.WheelMinutePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SingleDateAndTimePickerDialog extends BaseDialog {

    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private SingleDateAndTimePicker picker;

    @Nullable
    private String title;
    @Nullable
    private String todayText;
    @Nullable
    private DisplayListener displayListener;

    private SingleDateAndTimePickerDialog(Context context) {
        this(context, false);
    }

    private SingleDateAndTimePickerDialog(Context context, boolean bottomSheet) {
        final int layout = bottomSheet ? R.layout.bottom_sheet_picker_bottom_sheet :
                R.layout.bottom_sheet_picker;
        this.bottomSheetHelper = new BottomSheetHelper(context, layout);

        this.bottomSheetHelper.setListener(new BottomSheetHelper.Listener() {
            @Override
            public void onOpen() {
            }

            @Override
            public void onLoaded(View view) {
                init(view);
            }

            @Override
            public void onClose() {
                SingleDateAndTimePickerDialog.this.onClose();
            }
        });
    }


    private void init(View view) {
        picker = (SingleDateAndTimePicker) view.findViewById(R.id.picker);

        final TextView buttonOk = (TextView) view.findViewById(R.id.buttonOk);
        if (buttonOk != null) {
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    okClicked = true;
                    close();
                }
            });

            if (mainColor != null) {
                buttonOk.setTextColor(mainColor);
            }
        }

        final View sheetContentLayout = view.findViewById(R.id.sheetContentLayout);
        if (sheetContentLayout != null) {
            sheetContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            if (backgroundColor != null) {
                sheetContentLayout.setBackgroundColor(backgroundColor);
            }
        }

        final TextView titleTextView = (TextView) view.findViewById(R.id.sheetTitle);
        if (titleTextView != null) {
            titleTextView.setText(title);

            if (titleTextColor != null) {
                titleTextView.setTextColor(titleTextColor);
            }
        }

        picker.setTodayText(todayText);

        final View pickerTitleHeader = view.findViewById(R.id.pickerTitleHeader);
        if (mainColor != null && pickerTitleHeader != null) {
            pickerTitleHeader.setBackgroundColor(mainColor);
        }

        if (curved) {
            picker.setCurved(true);
            picker.setVisibleItemCount(7);
        } else {
            picker.setCurved(false);
            picker.setVisibleItemCount(5);
        }
        picker.setMustBeOnFuture(mustBeOnFuture);

        picker.setStepMinutes(minutesStep);

        if (dayFormatter != null) {
            picker.setDayFormatter(dayFormatter);
        }

        if (mainColor != null) {
            picker.setSelectedTextColor(mainColor);
        }

        if (minDate != null) {
            picker.setMinDate(minDate);
        }

        if (maxDate != null) {
            picker.setMaxDate(maxDate);
        }

        if (defaultDate != null) {
            picker.setDefaultDate(defaultDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(defaultDate);
            picker.selectDate(calendar);
        }

        picker.setDisplayDays(displayDays);
        picker.setDisplayMinutes(displayMinutes);
        picker.setDisplayHours(displayHours);
    }

    public SingleDateAndTimePickerDialog setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public SingleDateAndTimePickerDialog setCurved(boolean curved) {
        this.curved = curved;
        return this;
    }

    public SingleDateAndTimePickerDialog setMinutesStep(int minutesStep) {
        this.minutesStep = minutesStep;
        return this;
    }

    private void setDisplayListener(DisplayListener displayListener) {
        this.displayListener = displayListener;
    }

    public SingleDateAndTimePickerDialog setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public SingleDateAndTimePickerDialog setTodayText(@Nullable String todayText) {
        this.todayText = todayText;
        return this;
    }

    public SingleDateAndTimePickerDialog setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        return this;
    }

    public SingleDateAndTimePickerDialog setMinDateRange(Date minDate) {
        this.minDate = minDate;
        return this;
    }

    public SingleDateAndTimePickerDialog setMaxDateRange(Date maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public SingleDateAndTimePickerDialog setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
        return this;
    }

    public SingleDateAndTimePickerDialog setDisplayDays(boolean displayDays) {
        this.displayDays = displayDays;
        return this;
    }

    public SingleDateAndTimePickerDialog setDisplayMinutes(boolean displayMinutes) {
        this.displayMinutes = displayMinutes;
        return this;
    }

    public SingleDateAndTimePickerDialog setDisplayHours(boolean displayHours) {
        this.displayHours = displayHours;
        return this;
    }

    public SingleDateAndTimePickerDialog setDayFormatter(SimpleDateFormat dayFormatter) {
        this.dayFormatter = dayFormatter;
        return this;
    }

    @Override
    public void display() {
        super.display();
        bottomSheetHelper.display();
        if(displayListener != null){
            displayListener.onDisplayed(picker);
        }
    }

    @Override
    public void close() {
        super.close();
        bottomSheetHelper.hide();

        if (listener != null && okClicked) {
            listener.onDateSelected(picker.getDate());
        }
    }

    @Override
    public void dismiss(){
        super.dismiss();
        bottomSheetHelper.dismiss();
    }

    public interface Listener {
        void onDateSelected(Date date);
    }

    public interface DisplayListener {
        void onDisplayed(SingleDateAndTimePicker picker);
    }

    public static class Builder {
        private final Context context;
        private SingleDateAndTimePickerDialog dialog;

        @Nullable
        private Listener listener;
        @Nullable
        private DisplayListener displayListener;

        @Nullable
        private String title;

        @Nullable
        private String todayText;

        private boolean bottomSheet;

        private boolean curved;
        private boolean mustBeOnFuture;
        private int minutesStep = WheelMinutePicker.STEP_MINUTES_DEFAULT;

        private boolean displayDays = true;
        private boolean displayMinutes  = true;
        private boolean displayHours  = true;

        @ColorInt
        @Nullable
        private Integer backgroundColor = null;

        @ColorInt
        @Nullable
        private Integer mainColor = null;

        @ColorInt
        @Nullable
        private Integer titleTextColor = null;

        @Nullable
        private Date minDate;
        @Nullable
        private Date maxDate;
        @Nullable
        private Date defaultDate;

        @Nullable
        private SimpleDateFormat dayFormatter;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(@Nullable String title) {
            this.title = title;
            return this;
        }

        public Builder todayText(@Nullable String todayText) {
            this.todayText = todayText;
            return this;
        }

        public Builder bottomSheet() {
            this.bottomSheet = true;
            return this;
        }

        public Builder curved() {
            this.curved = true;
            return this;
        }

        public Builder mustBeOnFuture() {
            this.mustBeOnFuture = true;
            return this;
        }

        public Builder minutesStep(int minutesStep) {
            this.minutesStep = minutesStep;
            return this;
        }

        public Builder displayDays(boolean displayDays) {
            this.displayDays = displayDays;
            return this;
        }

        public Builder displayMinutes(boolean displayMinutes) {
            this.displayMinutes = displayMinutes;
            return this;
        }

        public Builder displayHours(boolean displayHours) {
            this.displayHours = displayHours;
            return this;
        }

        public Builder listener(@Nullable Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder displayListener(@Nullable DisplayListener displayListener) {
            this.displayListener = displayListener;
            return this;
        }

        public Builder titleTextColor(@NonNull @ColorInt int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder backgroundColor(@NonNull @ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder mainColor(@NonNull @ColorInt int mainColor) {
            this.mainColor = mainColor;
            return this;
        }

        public Builder minDateRange(Date minDate) {
            this.minDate = minDate;
            return this;
        }

        public Builder maxDateRange(Date maxDate) {
            this.maxDate = maxDate;
            return this;
        }

        public Builder defaultDate(Date defaultDate) {
            this.defaultDate = defaultDate;
            return this;
        }

        public Builder setDayFormatter(SimpleDateFormat dayFormatter) {
            this.dayFormatter = dayFormatter;
            return this;
        }

        public SingleDateAndTimePickerDialog build() {
            final SingleDateAndTimePickerDialog dialog = new SingleDateAndTimePickerDialog(context, bottomSheet)
                    .setTitle(title)
                    .setTodayText(todayText)
                    .setListener(listener)
                    .setCurved(curved)
                    .setMinutesStep(minutesStep)
                    .setMaxDateRange(maxDate)
                    .setMinDateRange(minDate)
                    .setDefaultDate(defaultDate)
                    .setDisplayHours(displayHours)
                    .setDisplayMinutes(displayMinutes)
                    .setDisplayDays(displayDays)
                    .setDayFormatter(dayFormatter)
                    .setMustBeOnFuture(mustBeOnFuture);

            if (mainColor != null) {
                dialog.setMainColor(mainColor);
            }

            if (backgroundColor != null) {
                dialog.setBackgroundColor(backgroundColor);
            }

            if (titleTextColor != null) {
                dialog.setTitleTextColor(titleTextColor);
            }

            if (displayListener != null) {
                dialog.setDisplayListener(displayListener);
            }

            return dialog;
        }

        public void display() {
            dialog = build();
            dialog.display();
        }

        public void close() {
            if (dialog != null) {
                dialog.close();
            }
        }

        public void dismiss(){
            if(dialog!=null)
                dialog.dismiss();
        }
    }
}
