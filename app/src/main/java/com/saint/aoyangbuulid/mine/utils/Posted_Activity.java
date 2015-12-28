package com.saint.aoyangbuulid.mine.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.login.Login_Activity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zzh on 15-12-28.
 */
public class Posted_Activity extends BaseActivity {
    TextView text_title,text_date,text_content;
    String finished ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posted_main);
        text_content= (TextView) findViewById(R.id.text_content);
        text_date= (TextView) findViewById(R.id.text_time);
        text_title= (TextView) findViewById(R.id.text_title);

        Intent intent=getIntent();
        finished=intent.getStringExtra("data[finished]");
        getJson();
    }
    public void getJson(){
        String url= Constant.SERVER_URL+"/wp-json/pods/bill";
        AsyncHttpClient client=new AsyncHttpClient();
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""));
        RequestParams params=new RequestParams();
        params.add("data[finished]", finished);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Iterator<String> iterator = response.keys();
                while (iterator.hasNext()) {
                    String k = iterator.next().toString();
                    String v = response.optString(k);
                    try {
                        JSONObject object = new JSONObject(v);
                        String amount = object.optString("amount");
                        String title = object.optString("post_title");
                        String content = object.optString("post_content");
                        String date = object.optString("post_date");
                        text_content.setText(content);
                        text_date.setText(date);
                        text_title.setText(title);
                    } catch (JSONException e) {
//                        e.printStackTrace();
                    }
                }
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
            Intent intent=new Intent(Posted_Activity.this,MyBill_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
}
