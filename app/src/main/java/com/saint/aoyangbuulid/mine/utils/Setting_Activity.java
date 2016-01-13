package com.saint.aoyangbuulid.mine.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.login.Login_Activity;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.AuthScope;

/**
 * Created by zzh on 15-11-26.
 */
public class Setting_Activity extends BaseActivity implements View.OnClickListener{

    public TextView textView_user,textView_phone,textView_passed;
    public String user_name,phonenumber;
    public EditText et_user;
    public ImageButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_seeting_layout);
        initDate();
        Intent intent=getIntent();
        textView_passed.setText(intent.getStringExtra("passed"));
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        String text=sp.getString("new_nickname","");
        textView_user.setText(text);
    }

    public void initDate(){
        textView_passed= (TextView) findViewById(R.id.change_passedword);
        textView_phone= (TextView) findViewById(R.id.change_phone);
        textView_user= (TextView) findViewById(R.id.change_user);
        button= (ImageButton) findViewById(R.id.btn_sure);
        textView_user.setOnClickListener(this);
        textView_phone.setOnClickListener(this);
        textView_passed.setOnClickListener(this);
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.change_user:
                    showuserDialog();
                    break;
                case R.id.change_passedword:
                    Intent intent=new Intent();
                    intent.setClass(Setting_Activity.this,ChangePassed_Activity.class);
                    startActivity(intent);

                    break;
                case R.id.change_phone:
                    showphoneDialog();
                    break;
                case R.id.btn_sure:
                    postJSON();
                    finish();
                    break;
            }
    }
    public void showuserDialog() {
        LayoutInflater inflater=LayoutInflater.from(Setting_Activity.this);
        View view=inflater.inflate(R.layout.changuser_layout, null);
        et_user= (EditText) view.findViewById(R.id.et_user);
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(Setting_Activity.this)
                .setTitle("更改昵称")
                .setView(view);

                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user_name = et_user.getText().toString();
                        textView_user.setText(user_name);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog.create().show();
    }
    /**
     * 同上*/
    public void showphoneDialog(){
        LayoutInflater inflaterphone=LayoutInflater.from(Setting_Activity.this);
        View p_view=inflaterphone.inflate(R.layout.changuser_layout, null);
        et_user= (EditText) p_view.findViewById(R.id.et_user);
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(Setting_Activity.this)
                .setTitle("更改手机号码")
                .setView(p_view);

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                phonenumber=et_user.getText().toString();
                textView_phone.setText(phonenumber);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create().show();
    }
    public void postJSON(){
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);

        String url= Constant.SERVER_URL+"/wp-json/pods/user/"+sp.getString("user_id","");
        AsyncHttpClient  client  =new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.add("nickname",textView_user.getText().toString());

//        params.add("mobile",);
        params.add("phone", textView_phone.getText().toString());
        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""), AuthScope.ANY);
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            Setting_Activity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


