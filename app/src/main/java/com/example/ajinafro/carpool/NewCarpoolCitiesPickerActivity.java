package com.example.ajinafro.carpool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.ajinafro.R;
import com.example.ajinafro.models.Carpool;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class NewCarpoolCitiesPickerActivity extends AppCompatActivity {
    private static final String TAG ="CarpoolCitiesPicker" ;
    Carpool carpool;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Calendar calendar;
    private EditText dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_carpool_cities_picker);
        ButterKnife.bind(this);
        timePicker=(TimePicker)findViewById(R.id.newcarpool_timepicker);
        timePicker.setIs24HourView(true);

        dateView = (EditText) findViewById(R.id.newcarpool_datepicker);
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        year = calendar.get(Calendar.YEAR);

        timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(calendar.get(Calendar.MINUTE));
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }
    @OnClick(R.id.newcarpool_backbtn)
    void back(){
        onBackPressed();
    }

    @OnTouch(R.id.newcarpool_datepicker)
    boolean datepick(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_UP){
            setDate();
            return true;
        }return false;
    }
    @OnClick(R.id.newcarpool_donebtn)
    void goto_nextActivity(){
        Date date=new GregorianCalendar(year,month-1,day).getTime();
        Log.d(TAG, "goto_nextActivity: date is"+date.toString());
        Log.d(TAG, "goto_nextActivity: time is "+timePicker.getHour()+":"+timePicker.getMinute());
        Intent nextac=new Intent(getApplicationContext(),NewCarpoolOtherDetailsActivity.class);
        nextac.putExtra("initial_carpool_value", carpool);
        startActivity(nextac);
    }
    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
    }
    private final DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}