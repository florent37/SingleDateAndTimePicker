package com.github.florent37.sample.singledateandtimepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivityWithDoublePicker extends AppCompatActivity {

    @Bind(R.id.doubleText) TextView doubleText;
    @Bind(R.id.singleText) TextView singleText;

    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_double_picker);
        ButterKnife.bind(this);

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());
    }

    @OnClick(R.id.singleLayout)
    public void simpleClicked() {
        new SingleDateAndTimePickerDialog.Builder(this)
            //.bottomSheet()
            //.curved()
            .title("Simple")
            .listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    singleText.setText(simpleDateFormat.format(date));
                }
            })
            .display();
    }

    @OnClick(R.id.doubleLayout)
    public void doubleClicked() {
        new DoubleDateAndTimePickerDialog.Builder(this)
            //.bottomSheet()
            //.curved()
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
        }).display();
    }
}