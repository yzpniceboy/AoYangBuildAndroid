package com.saint.aoyangbuulid.reserve.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.charge.Room_Reservation_Activity;

import java.util.Calendar;

/**
 * Created by zzh on 15-11-20.
 */
public class Setingtime_view extends BaseActivity {
    public DatePicker datePicker;
    public TimePicker timepicker;
    public ImageButton button;
    public  int Y,M,D,H,m;
    public final  int ID=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingtime_layout);


        final Calendar calendar=Calendar.getInstance();
         Y=calendar.get(Calendar.YEAR);
         M=calendar.get(Calendar.MONTH);
         D=calendar.get(Calendar.DAY_OF_MONTH);
         H=calendar.get(Calendar.HOUR_OF_DAY);
         m=calendar.get(Calendar.MINUTE);
        datePicker= (DatePicker) findViewById(R.id.datepicker);

        datePicker.init(Y, M, D, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year,(monthOfYear+1),dayOfMonth);
                Y=year;
                M=monthOfYear;
                D=dayOfMonth;
            }
        });

        timepicker= (TimePicker) findViewById(R.id.timepicker);
        timepicker.setIs24HourView(true);
        timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(hourOfDay,minute);
                H=hourOfDay;
                m=minute;

            }
        });

        button= (ImageButton) findViewById(R.id.send_time);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Setingtime_view.this, Room_Reservation_Activity.class);
                intent.putExtra("time",Y+"-"+(M+1)+"-"+D+"-"+H+"-"+m);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
