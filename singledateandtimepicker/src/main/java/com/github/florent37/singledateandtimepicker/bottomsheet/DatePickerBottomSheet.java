package com.github.florent37.singledateandtimepicker.bottomsheet;

import android.content.Context;
import android.view.View;
import com.github.florent37.singledateandtimepicker.R;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import java.util.Date;

public class DatePickerBottomSheet {

    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private SingleDateAndTimePicker picker;

    public DatePickerBottomSheet(Context context) {
        this.bottomSheetHelper = new BottomSheetHelper(context, R.layout.bottom_sheet_picker);

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
                DatePickerBottomSheet.this.onClose();
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
    }

    private void onClose() {
        if (listener != null) {
            listener.onDateSelected(picker.getDate());
        }
    }

    public DatePickerBottomSheet setListener(Listener listener) {
        this.listener = listener;
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
