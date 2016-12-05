package com.github.florent37.sample.singledateandtimepicker;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.florent37.singledateandtimepicker.SingleDayAndDatePicker;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DoubleDatePickerDialog {

    public interface Listener {
        void onDateSelected(List<Date> dates);
    }

    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;

    @Bind(R.id.aller) Button aller;
    @Bind(R.id.retour) Button retour;

    @Bind(R.id.picker_aller) SingleDayAndDatePicker pickerAller;
    @Bind(R.id.picker_retour) SingleDayAndDatePicker pickerRetour;

    @Bind(R.id.tab0) View tab0;
    @Bind(R.id.tab1) View tab1;

    protected DoubleDatePickerDialog(Context context) {
        this.bottomSheetHelper = new BottomSheetHelper(context, R.layout.bottom_sheet_double_picker);

        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            ButterKnife.bind(this, activity);

            tab1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    tab1.getViewTreeObserver().removeOnPreDrawListener(this);
                    tab1.setTranslationX(tab1.getWidth());
                    return false;
                }
            });

            this.bottomSheetHelper.setListener(new BottomSheetHelper.Listener() {
                @Override
                public void onOpen() {

                }

                @Override
                public void onClose() {
                    DoubleDatePickerDialog.this.onClose();
                }
            });

            aller.setSelected(true);
        }
    }

    private void onClose() {
        if(listener != null){
            listener.onDateSelected(Arrays.asList(pickerAller.getDate(), pickerRetour.getDate()));
        }
    }

    protected DoubleDatePickerDialog setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public void display(){
        this.bottomSheetHelper.display();
    }

    @OnClick(R.id.aller)
    public void onAllerClicked(){
        displayAller();
    }

    @OnClick(R.id.allerOk)
    public void onAllerOkClicked(){
        displayRetour();
    }

    private void displayAller() {
        if(!isAllerVisible()) {
            aller.setSelected(true);
            retour.setSelected(false);

            tab0.animate().translationX(0);
            tab1.animate().translationX(tab1.getWidth());
        }
    }

    @OnClick(R.id.retour)
    public void onRetourClicked(){
        displayRetour();
    }

    @OnClick(R.id.retourOk)
    public void onRetourOkClicked(){
        close();
    }

    private void close() {
        bottomSheetHelper.tryHide();
    }

    private void displayRetour() {
        if(isAllerVisible()) {
            aller.setSelected(false);
            retour.setSelected(true);

            tab0.animate().translationX(-tab0.getWidth());
            tab1.animate().translationX(0);
        }
    }

    private boolean isAllerVisible(){
        return tab0.getTranslationX() == 0;
    }
}
