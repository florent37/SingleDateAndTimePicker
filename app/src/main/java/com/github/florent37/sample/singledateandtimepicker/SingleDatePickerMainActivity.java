package com.github.florent37.sample.singledateandtimepicker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;

public class SingleDatePickerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_date_picker_activity_main);

        final SingleDateAndTimePicker singleDateAndTimePicker = findViewById(R.id.single_day_picker);
        final SingleDateAndTimePicker singleDateAndTimePicker2 = findViewById(R.id.single_day_picker2);
        // Example for setting default selected date to yesterday
//        Calendar instance = Calendar.getInstance();
//        instance.add(Calendar.DATE, -1 );
//        singleDateAndTimePicker.setDefaultDate(instance.getTime());
        SingleDateAndTimePicker.OnDateChangedListener changeListener = (displayed, date) -> display(displayed);
        singleDateAndTimePicker.addOnDateChangedListener(changeListener);
        singleDateAndTimePicker2.addOnDateChangedListener(changeListener);

        //singleDateAndTimePicker.setTypeface(Typeface.DEFAULT);
        singleDateAndTimePicker2.setTypeface(ResourcesCompat.getFont(this, R.font.dinot_regular));

        findViewById(R.id.toggleEnabled).setOnClickListener(v -> {
            singleDateAndTimePicker.setEnabled(!singleDateAndTimePicker.isEnabled());
            singleDateAndTimePicker2.setEnabled(!singleDateAndTimePicker2.isEnabled());
        });
    }

    private void display(String toDisplay) {
        Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT).show();
    }
}
