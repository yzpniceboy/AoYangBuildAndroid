package com.saint.aoyangbuulid.charge;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;

/**
 * Created by zzh on 15-11-16.
 */
public class ChargeSubmit_Activity extends BaseActivity {
    public ImageButton button_end;
    public TextView text_money;
    public  String money;
    private CheckBox ck_pay,ck_wechat,ck_cash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeend);

        Intent intent=getIntent();
        money=intent.getStringExtra("amount");

        initData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()  ==  android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void initData(){
        button_end = (ImageButton) findViewById(R.id.button_end);
        text_money = (TextView) findViewById(R.id.text_money);
        ck_cash= (CheckBox) findViewById(R.id.check_cash);
        ck_pay= (CheckBox) findViewById(R.id.check_pay);
        ck_wechat= (CheckBox) findViewById(R.id.check_wechat);
        text_money.setText(money);

        ck_cash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ck_wechat.setClickable(false);
                    ck_pay.setClickable(false);
                } else {
                    ck_wechat.setClickable(true);
                    ck_pay.setClickable(true);
                }
            }
        });
        ck_pay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ck_cash.setClickable(false);
                    ck_wechat.setClickable(false);
                }else {
                    ck_cash.setClickable(true);
                    ck_wechat.setClickable(true);
                }
            }
        });
        ck_wechat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ck_pay.setClickable(false);
                    ck_cash.setClickable(false);
                }else {
                    ck_pay.setClickable(true);
                    ck_cash.setClickable(true);
                }
            }
        });

    }


}
