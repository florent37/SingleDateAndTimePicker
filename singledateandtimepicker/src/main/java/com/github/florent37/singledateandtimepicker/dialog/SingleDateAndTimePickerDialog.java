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

    @Nullable private String title;

    private boolean curved = false;

    private SingleDateAndTimePickerDialog(Context context) {
        this(context, false);
    }

    private SingleDateAndTimePickerDialog(Context context, boolean bottomSheet) {
        final int layout = bottomSheet ? R.layout.bottom_sheet_picker_bottom_sheet : R.layout.bottom_sheet_picker;
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
        if (titleTextView != null) {
            titleTextView.setText(title);
        }

        if (curved) {
            picker.setCurved(true);
            picker.setVisibleItemCount(7);
        } else {
            picker.setCurved(false);
            picker.setVisibleItemCount(5);
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

    public SingleDateAndTimePickerDialog setCurved(boolean curved) {
        this.curved = curved;
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

    public static class Builder {
        private final Context context;

        @Nullable private Listener listener;

        @Nullable private String title;

        private boolean bottomSheet;

        private boolean curved;

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

        public Builder listener(@Nullable Listener listener) {
            this.listener = listener;
            return this;
        }

        public SingleDateAndTimePickerDialog build() {
            return new SingleDateAndTimePickerDialog(context, bottomSheet).setTitle(title)
                .setListener(listener)
                .setCurved(curved);
        }

        public void display() {
            final SingleDateAndTimePickerDialog dialog = build();
            dialog.display();
        }
    }
}
