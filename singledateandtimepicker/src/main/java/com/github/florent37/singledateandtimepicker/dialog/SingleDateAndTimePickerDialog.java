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

import java.util.Date;

public class SingleDateAndTimePickerDialog extends BaseDialog {

    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private SingleDateAndTimePicker picker;
    private boolean okClicked = false;
    @Nullable
    private String title;
    private boolean curved = false;
    private boolean mustBeOnFuture = false;
    private int minutesStep = WheelMinutePicker.STEP_MINUTES_DEFAULT;

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

            if (mainColor != null){
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
            
            if (titleTextColor != null){
                titleTextView.setTextColor(titleTextColor);
            }
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

    public SingleDateAndTimePickerDialog setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public SingleDateAndTimePickerDialog setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        return this;
    }

    @Override
    public void display() {
        super.display();
        bottomSheetHelper.display();
    }

    @Override
    public void close() {
        super.close();
        bottomSheetHelper.hide();
  
        if (listener != null && okClicked) {
            listener.onDateSelected(picker.getDate());
        }
    }

/*
    @Deprecated
    public void setMinDateRange(Date minDate){
        //TODO
    }

    @Deprecated
    public void setMaxDateRange(Date maxDate){
        //TODO
    }

    @Deprecated
    public void setAmPm(boolean isAmPm){
        //TODO
    }
*/

    public interface Listener {
        void onDateSelected(Date date);
    }

    public static class Builder {
        private final Context context;
        private SingleDateAndTimePickerDialog dialog;

        @Nullable
        private Listener listener;

        @Nullable
        private String title;

        private boolean bottomSheet;

        private boolean curved;
        private boolean mustBeOnFuture;
        private int minutesStep = WheelMinutePicker.STEP_MINUTES_DEFAULT;

        @ColorInt
        @Nullable
        private Integer backgroundColor = null;

        @ColorInt 
        @Nullable
        private Integer mainColor = null;

        @ColorInt 
        @Nullable
        private Integer titleTextColor = null;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(@Nullable String title) {
            this.title = title;
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

        public Builder minutesStep(int minutesStep){
            this.minutesStep = minutesStep;
            return this;
        }

        public Builder listener(@Nullable Listener listener) {
            this.listener = listener;
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

        /*
        @Deprecated
        public Builder minDateRange(Date minDate){
            //TODO
        }

        @Deprecated
        public void maxDateRange(Date maxDate){
            //TODO
        }

        @Deprecated
        public void setAmPm(boolean isAmPm){
            //TODO
        }
        */

        public SingleDateAndTimePickerDialog build() {
            final SingleDateAndTimePickerDialog dialog = new SingleDateAndTimePickerDialog(context, bottomSheet)
                    .setTitle(title)
                    .setListener(listener)
                    .setCurved(curved)
                    .setMinutesStep(minutesStep)
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
    }
}
