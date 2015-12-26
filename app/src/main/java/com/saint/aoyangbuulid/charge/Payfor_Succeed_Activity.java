package com.saint.aoyangbuulid.charge;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;

import com.saint.aoyangbuulid.BaseActivity;

/**
 * Created by zzh on 15-11-18.
 */
public class Payfor_Succeed_Activity extends BaseActivity {
    public  String date,roomnumber;
    public TextView text_date,textrooms;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_layout);

        text_date= (TextView) findViewById(R.id.date);
        textrooms= (TextView) findViewById(R.id.text_roomnumber);

            Intent intent=getIntent();
            date=intent.getStringExtra("timedate");
            roomnumber=intent.getStringExtra("rooms");
            textrooms.setText("预定房间：         "+roomnumber);
            text_date.setText("预定时间：         "+date);

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
