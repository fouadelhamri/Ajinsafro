package com.example.ajinafro.carpool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ajinafro.R;
import com.example.ajinafro.models.Carpool;
import com.example.ajinafro.utils.CityPickerActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class NewCarpoolCitiesPickerActivity extends AppCompatActivity {
    private static final String TAG ="CarpoolCitiesPicker" ;
    private static final int CITY_PICKER =888 ;
    private static final int CITY_PICKER2 =881 ;
    private static final int CARPOOL_SHARED=880;
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


    @BindView(R.id.searchcarpool_startcity2)
    EditText startcity;
    @BindView(R.id.newcarpool_endcity2)
    EditText endcity;

    @OnClick(R.id.newcarpool_endcity2)
    void cityPicker2(){
        Intent intent=new Intent(NewCarpoolCitiesPickerActivity.this, CityPickerActivity.class);
        startActivityForResult(intent,CITY_PICKER2);
    }


    @OnClick(R.id.searchcarpool_startcity2)
    void cityPicker(){
        Intent intent=new Intent(NewCarpoolCitiesPickerActivity.this,CityPickerActivity.class);
        startActivityForResult(intent,CITY_PICKER);
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

        Log.d(TAG, "goto_nextActivity: date is"+date.getTime());
        Log.d(TAG, "goto_nextActivity: time is "+timePicker.getHour()+":"+timePicker.getMinute());
        Log.d(TAG, "goto_nextActivity: time is "+timePicker.toString());

        boolean validate = true;
        String start_city=startcity.getText().toString();
        String end_city=endcity.getText().toString();

        if(!isTimingValide(date,timePicker.getHour(),timePicker.getMinute()) || start_city.isEmpty() || end_city.isEmpty() ){
            validate= false;
        }
        if(validate) {
            Intent nextac = new Intent(getApplicationContext(), NewCarpoolOtherDetailsActivity.class);
            nextac.putExtra("startcity",start_city);
            nextac.putExtra("endcity", end_city);
            nextac.putExtra("datecarpool", date.getTime());
            nextac.putExtra("timecarpoolHour", +timePicker.getHour());
            nextac.putExtra("timecarpoolMinute", +timePicker.getMinute());
            nextac.putExtra("initial_carpool_value", carpool);
            startActivityForResult(nextac,CARPOOL_SHARED);
        }
        else{
            Toast.makeText(getApplicationContext(),"veillez remplir la ville de depart et la ville d'arrivee",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isTimingValide(Date date, int hour, int minute) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeZone(TimeZone.getDefault());
        int calendar_hours=calendar2.get(Calendar.HOUR_OF_DAY);
        int calendar_mins=calendar2.get(Calendar.MINUTE);
        calendar2.set(
                        calendar2.get(Calendar.YEAR),
                        calendar2.get(Calendar.MONTH),
                        calendar2.get(Calendar.DAY_OF_MONTH)
                        ,00
                        ,00
                        ,00
                    );
        if(date.toString().equalsIgnoreCase(calendar2.getTime().toString()) ){
            if(hour<calendar_hours || hour==calendar_hours && minute<=calendar_mins){
                Log.d(TAG, "idTimingValide: invalide time");
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
    }
    private final DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,int arg1, int arg2, int arg3) {
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    arg0.setMinDate(System.currentTimeMillis() - 1000);
                    showDate(arg1, arg2+1, arg3);
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            Calendar today = Calendar.getInstance();
            long now = today.getTimeInMillis();
            DatePickerDialog datep = new DatePickerDialog(this,
                    myDateListener, year, month - 1, day);
            datep.getDatePicker().setMinDate(now);

            return datep;
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        this.year=year;
        this.month=month;
        this.day=day;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CITY_PICKER){
            if(resultCode == Activity.RESULT_OK){
                startcity.setText(data.getExtras().getString("selectedCity") );
            }
        }
        if(requestCode==CITY_PICKER2){
            if(resultCode == Activity.RESULT_OK){
                endcity.setText(data.getExtras().getString("selectedCity") );
            }
        }
        if (requestCode==CARPOOL_SHARED){
            Log.d(TAG, "onActivityResult: FINISH ");
                finish();
        }

    }

}