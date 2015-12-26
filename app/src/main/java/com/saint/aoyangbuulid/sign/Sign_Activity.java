package com.saint.aoyangbuulid.sign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.login.Login_Activity;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zzh on 15-11-9.
 */
public class Sign_Activity extends Activity implements View.OnClickListener {
    public ImageButton imageButton_exit, imageButton_getcode, imagebutton, imageButton_registered;
    public CheckBox checkBox;
    public TextView text;
    public Timer timer = null;
    public TimerTask task = null;
    int i = 5;
    public EditText et_phone, et_passed, et_passedagain, et_name;
    public String phone, passed, repassed, name, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_layout);
        //实例化
        et_name = (EditText) findViewById(R.id.edittext_choice_phonenumberext_code);
        et_phone = (EditText) findViewById(R.id.edittext_choice_phonenumber);
        et_passed = (EditText) findViewById(R.id.edittext_choice_passed);
        et_passedagain = (EditText) findViewById(R.id.edittext_passedagain);
        imageButton_registered = (ImageButton) findViewById(R.id.imagebutton_registered);
        imageButton_exit = (ImageButton) findViewById(R.id.image_exitthree);
//        imageButton_getcode = (ImageButton) findViewById(R.id.imagebutton_getcode);
        checkBox = (CheckBox) findViewById(R.id.checkbox_remarks);
//        text = (TextView) findViewById(R.id.text_gettime);

        //监听
        imageButton_exit.setOnClickListener(this);
//        imageButton_getcode.setOnClickListener(this);
        imageButton_registered.setOnClickListener(this);
//        text.setVisibility(View.INVISIBLE);



    }

    public android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            //更新主UI
            text.setText("还剩" + msg.arg1 + "s");
            StartTime();
            if (msg.arg1 == 0) {
                imageButton_getcode.setVisibility(View.VISIBLE);
                text.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_exitthree:
                finish();
                break;
//            case R.id.imagebutton_getcode:
//                imageButton_getcode.setVisibility(View.INVISIBLE);
//                text.setVisibility(View.VISIBLE);
//                StartTime();
//                break;
            case R.id.imagebutton_registered:
                Intent intent=getIntent();
                role=intent.getStringExtra("role");
                SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("roles",role);
                phone = et_phone.getText().toString();
                passed = et_passed.getText().toString();
                name = et_name.getText().toString();
                repassed = et_passedagain.getText().toString();

                try {
                    postJSON();
                } catch (UnsupportedEncodingException e) {
                   Toast.makeText(Sign_Activity.this,"no",Toast.LENGTH_SHORT).show();
                }
                //比较两次密码输入是否一致
                if (passed.equals(repassed)) {
                    intent = new Intent();
                    intent.putExtra("phone", phone);
                    intent.putExtra("passed", passed);
                    intent.setClass(Sign_Activity.this, Login_Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void StartTime() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (i-- >= 1) {
                    //获取消息
                    Message msg = handler.obtainMessage();
                    //
                    msg.arg1 = i;
                    handler.sendMessage(msg);
                } else {
                    timer.cancel();
                    i = 5;
                }

            }
        };
        timer.schedule(task, 1000);
    }

    public void postJSON() throws UnsupportedEncodingException {
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("user_name",name);
        editor.commit();
        AsyncHttpClient client=new AsyncHttpClient();
        String url= Constant.SERVER_URL+"/wp-json/users/register";
        RequestParams params=new RequestParams();
        params.add("data[user_login]", phone);
        params.add("data[user_pass]", passed);
        params.add("data[nickname]", name);
        params.add("data[display_name]", name);
        params.add("data[role]", role);
        client.post(url,  params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
    }

}