package com.github.florent37.sample.singledateandtimepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivityWithDoublePicker extends AppCompatActivity {

    @Bind(R.id.clickMe) TextView clickMe;

    DoubleDatePickerDialog doubleDatePicker;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_double_picker);
        ButterKnife.bind(this);

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());

        doubleDatePicker = new DoubleDatePickerDialog(this).setListener(new DoubleDatePickerDialog.Listener() {
            @Override
            public void onDateSelected(List<Date> dates) {
                final StringBuilder stringBuilder = new StringBuilder();
                for(Date date : dates){
                    stringBuilder.append(simpleDateFormat.format(date)).append("\n");
                }
                clickMe.setText(stringBuilder.toString());
            }

        });
    }

    @OnClick(R.id.clickMe)
    public void onClickMe() {
        doubleDatePicker.display();
    }

    private void display(String toDisplay) {
        Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT).show();
    }
}
