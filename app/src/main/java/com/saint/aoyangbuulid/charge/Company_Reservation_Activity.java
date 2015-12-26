package com.saint.aoyangbuulid.charge;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;

import com.saint.aoyangbuulid.BaseActivity;

/**
 * Created by zzh on 15-11-11.
 */
public class Company_Reservation_Activity extends BaseActivity implements View.OnClickListener{
    public TextView textview_company,textview_rooms;
    public ImageButton button_next;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_resevatin);
        getWindow().setBackgroundDrawable(null);
        textview_company= (TextView) findViewById(R.id.textview_company);
        textview_rooms= (TextView) findViewById(R.id.textview_rooms);
        button_next= (ImageButton) findViewById(R.id.imagebutton_next);


        textview_company.setOnClickListener(this);
        textview_rooms.setOnClickListener(this);
        button_next.setOnClickListener(this);



        Intent intent=getIntent();
        String companyname=intent.getStringExtra("companyname");
        textview_company.setText(companyname);
    }

    @Override
    public void onClick(View v) {
                switch (v.getId()){
                    case R.id.textview_company:
                        Intent intent=new Intent();
                        intent.setClass(Company_Reservation_Activity.this,SearchCompany_Activity.class);
                        startActivity(intent);
                        break;

                    case R.id.imagebutton_next:
                        intent=new Intent();
                        intent.setClass(Company_Reservation_Activity.this, ChargeSubmit_Activity.class);
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
}
