package com.github.florent37.singledateandtimepicker.widget;

import java.util.Date;

public interface DateTimeWheelPicker {
    void setDefaultDate(Date date);

    void selectDate(Date date);

    int findIndexOfDate(Date date);
}
