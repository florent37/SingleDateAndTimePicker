package com.github.florent37.sample.singledateandtimepicker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityWithDoublePicker extends AppCompatActivity {

    @Bind(R.id.doubleText)
    TextView doubleText;
    @Bind(R.id.singleText)
    TextView singleText;

    @Bind(R.id.singleTimeText)
    TextView singleTimeText;

    @Bind(R.id.singleDateText)
    TextView singleDateText;



    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;
    SimpleDateFormat simpleDateOnlyFormat;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    DoubleDateAndTimePickerDialog.Builder doubleBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_double_picker);
        ButterKnife.bind(this);

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());

        this.simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        this.simpleDateOnlyFormat = new SimpleDateFormat("EEE d MMM", Locale.getDefault());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (singleBuilder != null)
            singleBuilder.close();
        if (doubleBuilder != null)
            doubleBuilder.close();
    }



    @OnClick(R.id.singleTimeText)
    public void simpleTimeClicked() {

        final Calendar calendar = Calendar.getInstance();
        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)

                .bottomSheet()
                .curved()

                .backgroundColor(Color.BLACK)
                .mainColor(Color.GREEN)

                .displayHours(true)
                .displayMinutes(true)
                .displayDays(false)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {

                    }
                })

                .title("Simple Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        singleTimeText.setText(simpleTimeFormat.format(date));
                    }
                });
        singleBuilder.display();
    }


    @OnClick(R.id.singleDateText)
    public void simpleDateClicked() {

        final Calendar calendar = Calendar.getInstance();
        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)

                .bottomSheet()
                .curved()

                .backgroundColor(Color.BLACK)
                .mainColor(Color.GREEN)

                .displayHours(false)
                .displayMinutes(false)
                .displayDays(true)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {

                    }
                })

                .title("Simple Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        singleDateText.setText(simpleDateOnlyFormat.format(date));
                    }
                });
        singleBuilder.display();
    }

    @OnClick(R.id.singleLayout)
    public void simpleClicked() {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2017);
        final Date minDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 5);
        final Date maxDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, 2);
        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()

                .backgroundColor(Color.BLACK)
                .mainColor(Color.GREEN)

                //.displayHours(false)
                //.displayMinutes(false)

                //.mustBeOnFuture()

                //.minutesStep(15)
                //.mustBeOnFuture()
                .defaultDate(defaultDate)
                .minDateRange(minDate)
                .maxDateRange(maxDate)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {

                    }
                })

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

        final Date now = new Date();
        final Calendar calendarMin = Calendar.getInstance();
        final Calendar calendarMax = Calendar.getInstance();

        calendarMin.setTime(now); // Set min now
        calendarMax.setTime(new Date(now.getTime() + TimeUnit.DAYS.toMillis(150))); // Set max now + 150 days

        final Date minDate = calendarMin.getTime();
        final Date maxDate = calendarMax.getTime();

        doubleBuilder = new DoubleDateAndTimePickerDialog.Builder(this)
                //.bottomSheet()
                //.curved()

                .backgroundColor(Color.BLACK)
                .mainColor(Color.GREEN)
                .minutesStep(15)
                .mustBeOnFuture()

                .minDateRange(minDate)
                .maxDateRange(maxDate)
                //.defaultDate(now)
                .tab0Date(now)
                .tab1Date(new Date(now.getTime() + TimeUnit.HOURS.toMillis(1)))

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