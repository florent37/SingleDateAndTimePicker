package com.github.florent37.sample.singledateandtimepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.florent37.singledateandtimepicker.bottomsheet.DatePickerBottomSheet;
import com.github.florent37.singledateandtimepicker.bottomsheet.DoubleDatePickerBottomSheet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivityWithDoublePicker extends AppCompatActivity {

    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_double_picker);
        ButterKnife.bind(this);

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());
    }

    @OnClick(R.id.singleText)
    public void simpleClicked(final TextView textView) {
        new DatePickerBottomSheet(this).setListener(new DatePickerBottomSheet.Listener() {
            @Override
            public void onDateSelected(Date date) {
                textView.setText(simpleDateFormat.format(date));
            }
        }).display();
    }

    @OnClick(R.id.doubleText)
    public void doubleClicked(final TextView textView) {
        new DoubleDatePickerBottomSheet(this).setListener(new DoubleDatePickerBottomSheet.Listener() {
            @Override
            public void onDateSelected(List<Date> dates) {
                final StringBuilder stringBuilder = new StringBuilder();
                for (Date date : dates) {
                    stringBuilder.append(simpleDateFormat.format(date)).append("\n");
                }
                textView.setText(stringBuilder.toString());
            }
        }).display();
    }
}