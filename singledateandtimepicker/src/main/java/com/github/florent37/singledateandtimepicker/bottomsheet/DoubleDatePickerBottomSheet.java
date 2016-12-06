package com.github.florent37.singledateandtimepicker.bottomsheet;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.github.florent37.singledateandtimepicker.R;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DoubleDatePickerBottomSheet {

    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private TextView buttonTab0;
    private TextView buttonTab1;
    private TextView buttonOk;
    private SingleDateAndTimePicker pickerTab0;
    private SingleDateAndTimePicker pickerTab1;
    private View tab0;
    private View tab1;

    @Nullable
    private String tab0Text, tab1Text, title;
    @Nullable
    private String buttonOkText;

    public DoubleDatePickerBottomSheet(Context context) {
        this(context, false);
    }

    public DoubleDatePickerBottomSheet(Context context, boolean iosTheme) {
        final int layout = iosTheme ? R.layout.bottom_sheet_double_picker_ios : R.layout.bottom_sheet_double_picker;
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
                DoubleDatePickerBottomSheet.this.onClose();
            }
        });
    }

    private void init(View view) {
        buttonTab0 = (TextView) view.findViewById(R.id.buttonTab0);
        buttonTab1 = (TextView) view.findViewById(R.id.buttonTab1);
        pickerTab0 = (SingleDateAndTimePicker) view.findViewById(R.id.picker_tab_0);
        pickerTab1 = (SingleDateAndTimePicker) view.findViewById(R.id.picker_tab_1);
        tab0 = view.findViewById(R.id.tab0);
        tab1 = view.findViewById(R.id.tab1);

        TextView titleTextView = (TextView) view.findViewById(R.id.sheetTitle);
        if(titleTextView != null){
            titleTextView.setText(title);
        }

        view.findViewById(R.id.sheetContentLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tab1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tab1.getViewTreeObserver().removeOnPreDrawListener(this);
                tab1.setTranslationX(tab1.getWidth());
                return false;
            }
        });

        buttonTab0.setSelected(true);

        if(tab0Text != null){
            buttonTab0.setText(tab0Text);
        }
        buttonTab0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTab0();
            }
        });

        if(tab1Text != null){
            buttonTab1.setText(tab1Text);
        }
        buttonTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTab1();
            }
        });

        buttonOk = (TextView) view.findViewById(R.id.buttonOk);

        if(buttonOkText != null){
            buttonOk.setText(buttonOkText);
        }

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTab0Visible()) {
                    displayTab1();
                } else {
                    close();
                }
            }
        });

    }

    public DoubleDatePickerBottomSheet setTab0Text(String tab0Text) {
        this.tab0Text = tab0Text;
        return this;
    }

    public DoubleDatePickerBottomSheet setTab1Text(String tab1Text) {
        this.tab1Text = tab1Text;
        return this;
    }

    public DoubleDatePickerBottomSheet setButtonOkText(@Nullable String buttonOkText) {
        this.buttonOkText = buttonOkText;
        return this;
    }

    public DoubleDatePickerBottomSheet setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    private void onClose() {
        if (listener != null) {
            listener.onDateSelected(Arrays.asList(pickerTab0.getDate(), pickerTab1.getDate()));
        }
    }

    public DoubleDatePickerBottomSheet setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public void display() {
        this.bottomSheetHelper.display();
    }

    private void displayTab0() {
        if (!isTab0Visible()) {
            buttonTab0.setSelected(true);
            buttonTab1.setSelected(false);

            tab0.animate().translationX(0);
            tab1.animate().translationX(tab1.getWidth());
        }
    }

    public void close() {
        bottomSheetHelper.hide();
    }

    private void displayTab1() {
        if (isTab0Visible()) {
            buttonTab0.setSelected(false);
            buttonTab1.setSelected(true);

            tab0.animate().translationX(-tab0.getWidth());
            tab1.animate().translationX(0);
        }
    }

    private boolean isTab0Visible() {
        return tab0.getTranslationX() == 0;
    }

    public interface Listener {
        void onDateSelected(List<Date> dates);
    }
}
