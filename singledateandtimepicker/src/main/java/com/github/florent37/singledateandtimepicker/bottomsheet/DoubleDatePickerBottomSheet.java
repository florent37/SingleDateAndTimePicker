package com.github.florent37.singledateandtimepicker.bottomsheet;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import com.github.florent37.singledateandtimepicker.R;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DoubleDatePickerBottomSheet {

    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private Button aller;
    private Button retour;
    private SingleDateAndTimePicker pickerAller;
    private SingleDateAndTimePicker pickerRetour;
    private View tab0;
    private View tab1;

    public DoubleDatePickerBottomSheet(Context context) {
        this.bottomSheetHelper = new BottomSheetHelper(context, R.layout.bottom_sheet_double_picker);
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
        aller = (Button) view.findViewById(R.id.aller);
        retour = (Button) view.findViewById(R.id.retour);
        pickerAller = (SingleDateAndTimePicker) view.findViewById(R.id.picker_aller);
        pickerRetour = (SingleDateAndTimePicker) view.findViewById(R.id.picker_retour);
        tab0 = view.findViewById(R.id.tab0);
        tab1 = view.findViewById(R.id.tab1);

        tab1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tab1.getViewTreeObserver().removeOnPreDrawListener(this);
                tab1.setTranslationX(tab1.getWidth());
                return false;
            }
        });

        aller.setSelected(true);

        aller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAller();
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRetour();
            }
        });

        view.findViewById(R.id.retourOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        view.findViewById(R.id.allerOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRetour();
            }
        });
    }

    private void onClose() {
        if (listener != null) {
            listener.onDateSelected(Arrays.asList(pickerAller.getDate(), pickerRetour.getDate()));
        }
    }

    public DoubleDatePickerBottomSheet setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public void display() {
        this.bottomSheetHelper.display();
    }

    private void displayAller() {
        if (!isAllerVisible()) {
            aller.setSelected(true);
            retour.setSelected(false);

            tab0.animate().translationX(0);
            tab1.animate().translationX(tab1.getWidth());
        }
    }

    public void close() {
        bottomSheetHelper.hide();
    }

    private void displayRetour() {
        if (isAllerVisible()) {
            aller.setSelected(false);
            retour.setSelected(true);

            tab0.animate().translationX(-tab0.getWidth());
            tab1.animate().translationX(0);
        }
    }

    private boolean isAllerVisible() {
        return tab0.getTranslationX() == 0;
    }

    public interface Listener {
        void onDateSelected(List<Date> dates);
    }
}
