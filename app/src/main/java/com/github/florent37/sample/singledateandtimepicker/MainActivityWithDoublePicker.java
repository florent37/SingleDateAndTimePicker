package com.github.florent37.sample.singledateandtimepicker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityWithDoublePicker extends AppCompatActivity {

    @Bind(R.id.doubleText)
    TextView doubleText;
    @Bind(R.id.singleText)
    TextView singleText;

    SimpleDateFormat simpleDateFormat;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    DoubleDateAndTimePickerDialog.Builder doubleBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_double_picker);
        ButterKnife.bind(this);

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (singleBuilder != null)
            singleBuilder.close();
        if (doubleBuilder != null)
            doubleBuilder.close();
    }

    @OnClick(R.id.singleLayout)
    public void simpleClicked() {

        //final Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.DAY_OF_MONTH, 1);
        //calendar.set(Calendar.MONTH, 0);
        //calendar.set(Calendar.YEAR, 2017);
        //final Date minDate = calendar.getTime();

        //calendar.set(Calendar.DAY_OF_MONTH, 5);
        //final Date maxDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                //.curved()

                .backgroundColor(Color.BLACK)
                .mainColor(Color.GREEN)
                //.mustBeOnFuture()

                //.minutesStep(15)
                //.mustBeOnFuture()
                //.minDateRange(minDate)
                //.maxDateRange(maxDate)
                .title("Simple")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        singleText.setText(simpleDateFormat.format(date));
                    }
                });
        singleBuilder.display();
    }

    @OnClick(R.id.doubleLayout)
    public void doubleClicked() {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2017);
        final Date minDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 5);
        final Date maxDate = calendar.getTime();

        doubleBuilder = new DoubleDateAndTimePickerDialog.Builder(this)
                //.bottomSheet()
                //.curved()

                .backgroundColor(Color.BLACK)
                .mainColor(Color.GREEN)
                .minutesStep(15)
                .mustBeOnFuture()

                .minDateRange(minDate)
                .maxDateRange(maxDate)

                .title("Double")
                .tab0Text("Depart")
                .tab1Text("Return")
                .listener(new DoubleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(List<Date> dates) {
                        final StringBuilder stringBuilder = new StringBuilder();
                        for (Date date : dates) {
                            stringBuilder.append(simpleDateFormat.format(date)).append("\n");
                        }
                        doubleText.setText(stringBuilder.toString());
                    }
                });
        doubleBuilder.display();
    }
}