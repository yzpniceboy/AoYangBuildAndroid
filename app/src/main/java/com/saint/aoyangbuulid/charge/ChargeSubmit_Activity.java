package com.saint.aoyangbuulid.charge;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.saint.aoyangbuulid.R;

import com.saint.aoyangbuulid.BaseActivity;

/**
 * Created by zzh on 15-11-16.
 */
public class ChargeSubmit_Activity extends BaseActivity {
    public ImageButton button_end;
    public EditText text;
    public String money;
    public  String time,room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeend);

        Intent intent=getIntent();
        room=intent.getStringExtra("room");
        time=intent.getStringExtra("time");


        button_end = (ImageButton) findViewById(R.id.button_end);
        text = (EditText) findViewById(R.id.edittext_money);
        money = text.getText().toString();
        if (money!=null){
            button_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("timedate",time);
                    intent.putExtra("rooms",room);
                    intent.setClass(ChargeSubmit_Activity.this, Payfor_Succeed_Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

                }
            });
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
        if (item.getItemId()  ==  android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
