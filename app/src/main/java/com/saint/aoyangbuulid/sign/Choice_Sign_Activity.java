package com.saint.aoyangbuulid.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.login.Login_Activity;

/**
 * Created by zzh on 15-11-9.
 * 777777777777777
 */
public class Choice_Sign_Activity extends BaseActivity implements View.OnClickListener{
    String role=null;
    public ImageButton imageButton_personal
            ,imageButton_enterprise
            ,imageButton_login
            ,imageButton_exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_sign_layout);
        getSupportActionBar().hide();
        imageButton_enterprise= (ImageButton) findViewById(R.id.imagebutton_enterprise);
        imageButton_personal= (ImageButton) findViewById(R.id.imagebutton_personal);
        imageButton_login= (ImageButton) findViewById(R.id.imagebutton_logintwo);
        imageButton_exit= (ImageButton) findViewById(R.id.image_exittwo);

        imageButton_enterprise.setOnClickListener(this);
        imageButton_login.setOnClickListener(this);
        imageButton_personal.setOnClickListener(this);
        imageButton_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagebutton_logintwo:
                Intent intent=new Intent();
                intent.setClass(Choice_Sign_Activity.this, Login_Activity.class);
                startActivity(intent);
                break;
            case R.id.imagebutton_personal:
                intent=new Intent();
                intent.putExtra("role","subscriber");
                intent.setClass(Choice_Sign_Activity.this, Sign_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                break;
            case R.id.imagebutton_enterprise:
                intent=new Intent();
                intent.putExtra("role","contributor");
                intent.setClass(Choice_Sign_Activity.this, Sign_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                break;
            case R.id.image_exittwo:
                Choice_Sign_Activity.this.finish();
                break;
        }
    }
}
