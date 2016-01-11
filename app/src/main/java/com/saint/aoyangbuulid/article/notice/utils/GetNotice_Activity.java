package com.saint.aoyangbuulid.article.notice.utils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.article.news.adapter.CommentAdapter;
import com.saint.aoyangbuulid.article.news.mylistview.XListView;
import com.saint.aoyangbuulid.login.Login_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zzh on 15-12-5.
 */
public class GetNotice_Activity extends BaseActivity {
    public String id;
    private XListView view_list;
    private ProgressDialog loadingDialog;

    public CommentAdapter adapter;
    private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter=new CommentAdapter(GetNotice_Activity.this,list);
            view_list.setAdapter(adapter);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_comment);

        loadingDialog=new ProgressDialog(GetNotice_Activity.this);
        loadingDialog.setMessage("正在获取最新列表...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        getJSON();
        view_list= (XListView) findViewById(R.id.new_comment);

        view_list.setPullLoadEnable(true);

        view_list.setPullRefreshEnable(true);

        view_list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJSON();


                        onLoad();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJSON();


                        onLoad();
                    }
                }, 2000);
            }
        });


        adapter=new CommentAdapter(GetNotice_Activity.this,list);
        view_list.setAdapter(adapter);


    }
    private void onLoad(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        String time=dateFormat.format(new java.util.Date());
        view_list.stopRefresh();
        view_list.stopLoadMore();
        view_list.setRefreshTime(time);
    }
    public void getJSON(){
        AsyncHttpClient client=new AsyncHttpClient();
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        client.get(Constant.SERVER_URL+"/wp-json/posts/" + sp.getString("news_id","") + "/comments", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                loadingDialog.dismiss();
                System.out.print(response);
                list.clear();
                if (!response.equals(JSONObject.NULL)) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Map<String, Object> map = new HashMap<String, Object>();
                            JSONObject object = response.getJSONObject(i);
                            String content_com = object.getString("content");
                            String content_date = object.getString("date");
                            String newdate=content_date.replaceAll("T"," ");

                            JSONObject author_ob = object.getJSONObject("author");
                            String author = author_ob.getString("name");

                            if (!author.equals(JSONObject.NULL)) {
                                map.put("content", content_com);
                                map.put("date", newdate);
                                map.put("auther", author);
                                list.add(map);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
            Intent intent=new Intent(GetNotice_Activity.this,PostNotice_Activity.class);
            startActivity(intent);
            GetNotice_Activity.this.finish();

//            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return super.onOptionsItemSelected(item);

    }
}
