package com.saint.aoyangbuulid.mine.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.article.news.mylistview.XListView;
import com.saint.aoyangbuulid.login.Login_Activity;
import com.saint.aoyangbuulid.mine.adapter.PostAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zzh on 15-12-24.
 */
public class MyBill_Activity extends BaseActivity {
    XListView view_list;
    List<Map<String,Object>> list=new ArrayList<>();
    TextView text;
    PostAdapter adapter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter=new PostAdapter(MyBill_Activity.this,list);
            view_list.setAdapter(adapter);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myview_layout);
        view_list= (XListView) findViewById(R.id.list_view);
        view_list.setPullLoadEnable(true);
        view_list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJson();
                        onLoad();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJson();

                        onLoad();
                    }
                }, 2000);
            }
        });

        view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView= (ListView) parent;
                Map<String,Object> map= (Map<String, Object>) listView.getItemAtPosition(position);
                String title= (String) map.get("title");
                String date= (String) map.get("date");
                String amount= (String) map.get("amount");
                String content= (String) map.get("content");
                String bill_id= (String) map.get("id");
                SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("bill_id",bill_id);
                editor.commit();
                Intent intent=new Intent(MyBill_Activity.this,Post_Coment_Activity.class);
                intent.putExtra("key_title",title);
                intent.putExtra("key_date",date);
                intent.putExtra("key_amount",amount);
                intent.putExtra("key_content",content);
//                intent.putExtra("id",bill_id);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        getJson();
        adapter=new PostAdapter(MyBill_Activity.this,list);
        view_list.setAdapter(adapter);

    }
    public void getJson(){
        String url= Constant.SERVER_URL+"/wp-json/pods/bill";
        AsyncHttpClient client=new AsyncHttpClient();
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""));
        RequestParams params=new RequestParams();
        params.add("data[finished]", "0");
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                list.clear();
                System.out.print(response + "");
                Iterator<String> iterator = response.keys();
                while (iterator.hasNext()) {
                    String k = iterator.next().toString();
                    String v = response.optString(k);
                    try {
                        JSONObject object = new JSONObject(v);
                        String bill_id=object.optString("ID");
//                        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
//                        SharedPreferences.Editor editor=sp.edit();
//                        editor.putString("bill_id",bill_id);
//                        editor.commit();
                        String amount = object.optString("amount");
                        String title = object.optString("post_title");
                        String content = object.optString("post_content");
                        String date = object.optString("post_date");
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("amount", amount);
                        map.put("title", title);
                        map.put("content", content);
                        map.put("date", date);
                        map.put("id",bill_id);
                        list.add(map);
                        adapter.notifyDataSetChanged();

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
        inflater.inflate(R.menu.menu_posted, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.posted:
                Intent intent=new Intent(MyBill_Activity.this,Posted_Activity.class);
                intent.putExtra("data[finished]","1");
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                MyBill_Activity.this.finish();

                break;
            case android.R.id.home:

                MyBill_Activity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onLoad(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        String time=dateFormat.format(new java.util.Date());
        view_list.stopRefresh();
        view_list.stopLoadMore();
        view_list.setRefreshTime(time);
    }
}
