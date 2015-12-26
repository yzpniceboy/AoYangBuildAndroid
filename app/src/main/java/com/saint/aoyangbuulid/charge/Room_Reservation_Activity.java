package com.saint.aoyangbuulid.charge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.reserve.utils.Setingtime_view;

import java.util.Calendar;

/**
 * Created by zzh on 15-11-16.
 */
public class Room_Reservation_Activity extends BaseActivity implements View.OnClickListener{

    public ImageButton button_next;
    public TextView text_time,text_room;
    public SharedPreferences sp;
    public String time,room;
    public int Y,M,D;
    public Calendar c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_reservatin_layout);
        getWindow().setBackgroundDrawable(null);
        //获取系统时间：
         c=Calendar.getInstance();
        Y=c.get(Calendar.YEAR);
        M=c.get(Calendar.MONTH);
        D=c.get(Calendar.DAY_OF_YEAR);

    //初始化

        text_room= (TextView) findViewById(R.id.view_room);
        text_time= (TextView) findViewById(R.id.view_time);
        button_next= (ImageButton) findViewById(R.id.button_next);


        text_room.setOnClickListener(this);
        text_time.setOnClickListener(this);
        button_next.setOnClickListener(this);



        //接受信息


        Intent intentroom=getIntent();
        time=intentroom.getStringExtra("time");
        text_time.setText(time);
        sp=getSharedPreferences("time", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("time", time);
        editor.commit();

        Intent intent=getIntent();
        room=intent.getStringExtra("room");
        text_room.setText(room);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button_next:
                Intent intent=new Intent();
                intent.putExtra("time",time);
                intent.putExtra("room",room);
                intent.setClass(Room_Reservation_Activity.this, ChargeSubmit_Activity.class);
                startActivity(intent);
                break;
            case R.id.view_room:
                intent=new Intent();
                intent.setClass(Room_Reservation_Activity.this, SearchRoom_Activity.class);
                startActivity(intent);



                break;
            case R.id.view_time:


                intent=new Intent();
                intent.setClass(Room_Reservation_Activity.this, Setingtime_view.class);
                startActivity(intent);

                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
//            if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
//                finish();
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
