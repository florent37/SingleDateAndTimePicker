package com.github.florent37.singledateandtimepicker.dialog;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.florent37.singledateandtimepicker.widget.WheelMinutePicker;

import java.util.Date;

/**
 * Created by nor on 1/2/2017.
 */

public abstract class BaseDialog {
    public static final int DEFAULT_ITEM_COUNT_MODE_CURVED = 7;
    public static final int DEFAULT_ITEM_COUNT_MODE_NORMAL = 5;

    private boolean isDiplaying;
    @Nullable
    @ColorInt
    protected Integer backgroundColor = null;

    @Nullable
    @ColorInt
    protected Integer mainColor = null;

    @Nullable
    @ColorInt
    protected Integer titleTextColor = null;

    protected boolean okClicked = false;
    protected boolean curved = false;
    protected boolean mustBeOnFuture = false;
    protected int minutesStep = WheelMinutePicker.STEP_MINUTES_DEFAULT;

    @Nullable
    protected Date minDate;
    @Nullable
    protected Date maxDate;

    public void display() {
        this.isDiplaying = true;
    }

    public void close() {
        this.isDiplaying = false;
    }

    public boolean isDiplaying() {
        return isDiplaying;
    }

    public void setBackgroundColor(@ColorInt Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setMainColor(@ColorInt Integer mainColor) {
        this.mainColor = mainColor;
    }

    public void setTitleTextColor(@NonNull @ColorInt int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    protected void onClose() {
        this.isDiplaying = false;
    }
}
