package com.github.florent37.singledateandtimepicker.dialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.github.florent37.singledateandtimepicker.R;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import java.util.Date;

public class SingleDateAndTimePickerDialog {

    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private SingleDateAndTimePicker picker;

    @Nullable
    private String title;

    public SingleDateAndTimePickerDialog(Context context) {
        this(context, false);
    }

    public SingleDateAndTimePickerDialog(Context context, boolean iosTheme) {
        final int layout = iosTheme ? R.layout.bottom_sheet_picker_ios : R.layout.bottom_sheet_picker;
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
        view.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        view.findViewById(R.id.sheetContentLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView titleTextView = (TextView) view.findViewById(R.id.sheetTitle);
        if(titleTextView != null){
            titleTextView.setText(title);
        }
    }

    private void onClose() {
        if (listener != null) {
            listener.onDateSelected(picker.getDate());
        }
    }

    public SingleDateAndTimePickerDialog setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public SingleDateAndTimePickerDialog setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public void display() {
        this.bottomSheetHelper.display();
    }

    public void close() {
        bottomSheetHelper.hide();
    }

    public interface Listener {
        void onDateSelected(Date date);
    }
}
