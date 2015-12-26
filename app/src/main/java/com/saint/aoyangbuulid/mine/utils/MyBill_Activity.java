package com.saint.aoyangbuulid.mine.utils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.login.Login_Activity;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zzh on 15-12-24.
 */
public class MyBill_Activity extends BaseActivity {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myview_layout);
        text= (TextView) findViewById(R.id.text_along);
        getJson();


    }
    public void getJson(){
        String url= Constant.SERVER_URL+"/wp-json/pods/bill";
        AsyncHttpClient client=new AsyncHttpClient();
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        client.setBasicAuth(sp.getString("passed",""),sp.getString("phone",""));
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.print(response);
                text.setText(response+"");
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
            MyBill_Activity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
