package com.github.florent37.sample.singledateandtimepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.Date;
import java.util.List;

public class MainActivityWithDoublePicker extends AppCompatActivity {

    @Bind(R.id.clickMe) TextView clickMe;

    DoubleDatePickerDialog doubleDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_double_picker);
        ButterKnife.bind(this);

        doubleDatePicker = new DoubleDatePickerDialog(this).setListener(new DoubleDatePickerDialog.Listener() {
            @Override
            public void onDateSelected(List<Date> dates) {
                for(Date date : dates){
                    display(date.toString());
                }
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
