package com.github.florent37.sample.singledateandtimepicker;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingleDatePickerMainActivityWithDoublePicker extends AppCompatActivity {

    @BindView(R.id.doubleText)
    TextView doubleText;

    @BindView(R.id.singleText)
    TextView singleText;

    @BindView(R.id.singleTimeText)
    TextView singleTimeText;

    @BindView(R.id.singleDateText)
    TextView singleDateText;

    @BindView(R.id.singleDateLocaleText)
    TextView singleDateLocaleText;


    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat simpleTimeFormat;
    SimpleDateFormat simpleDateOnlyFormat;
    SimpleDateFormat simpleDateLocaleFormat;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    DoubleDateAndTimePickerDialog.Builder doubleBuilder;

    private static String TAG = "SingleDatePickerMainActivityWithDoublePicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_date_picker_activity_main_double_picker);
        ButterKnife.bind(this);

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());

        this.simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        this.simpleDateOnlyFormat = new SimpleDateFormat("EEE d MMM", Locale.getDefault());

        this.simpleDateLocaleFormat = new SimpleDateFormat("EEE d MMM", Locale.GERMAN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (singleBuilder != null)
            singleBuilder.dismiss();
        if (doubleBuilder != null)
            doubleBuilder.dismiss();
    }



    @OnClick(R.id.singleTimeText)
    public void simpleTimeClicked() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 50);

        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .setTimeZone(TimeZone.getDefault())
                .bottomSheet()
                .curved()

                .defaultDate(defaultDate)

                //.titleTextColor(Color.GREEN)
                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)

                .displayMinutes(true)
                .displayHours(true)
                .displayDays(false)
                //.displayMonth(true)
                //.displayYears(true)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        Log.d(TAG, "Dialog displayed");
                    }

                    @Override
                    public void onClosed(SingleDateAndTimePicker picker) {
                        Log.d(TAG, "Dialog closed");
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
                .setTimeZone(TimeZone.getDefault())
                .bottomSheet()
                .curved()

                //.titleTextColor(Color.GREEN)
                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)

                .displayHours(false)
                .displayMinutes(false)
                .displayDays(true)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        Log.d(TAG, "Dialog displayed");
                    }

                    @Override
                    public void onClosed(SingleDateAndTimePicker picker) {
                        Log.d(TAG, "Dialog closed");
                    }
                })

                .title("")
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
        calendar.set(Calendar.DAY_OF_MONTH, 4); // 4. Feb. 2018
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 13);

        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .setTimeZone(TimeZone.getDefault())
                .bottomSheet()
                .curved()

                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)

                .displayHours(false)
                .displayMinutes(false)
                .displayDays(false)
                .displayMonth(true)
                .displayDaysOfMonth(true)
                .displayYears(true)
                .defaultDate(defaultDate)
                .displayMonthNumbers(true)

                //.mustBeOnFuture()

                //.minutesStep(15)
                //.mustBeOnFuture()
                //.defaultDate(defaultDate)
               // .minDateRange(minDate)
               // .maxDateRange(maxDate)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        Log.d(TAG, "Dialog displayed");
                    }

                    @Override
                    public void onClosed(SingleDateAndTimePicker picker) {
                        Log.d(TAG, "Dialog closed");
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
                .setTimeZone(TimeZone.getDefault())
                //.bottomSheet()
                //.curved()

//                .backgroundColor(Color.BLACK)
//                .mainColor(Color.GREEN)
                .minutesStep(15)
                .mustBeOnFuture()

                .minDateRange(minDate)
                .maxDateRange(maxDate)

                .secondDateAfterFirst(true)

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

    @OnClick(R.id.singleDateLocaleLayout)
    public void singleDateLocaleClicked() {
        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .customLocale(Locale.GERMAN)
                .bottomSheet()
                .curved()
                .displayHours(false)
                .displayMinutes(false)
                .displayDays(true)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        Log.d(TAG, "Dialog displayed");
                    }

                    @Override
                    public void onClosed(SingleDateAndTimePicker picker) {
                        Log.d(TAG, "Dialog closed");
                    }
                })

                .title("")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        singleDateLocaleText.setText(simpleDateLocaleFormat.format(date));
                    }
                });
        singleBuilder.display();
    }
}