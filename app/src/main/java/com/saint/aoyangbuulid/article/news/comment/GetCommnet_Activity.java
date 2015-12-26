package com.saint.aoyangbuulid.article.news.comment;

import android.content.Intent;
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
public class GetCommnet_Activity extends BaseActivity {
    public CommentAdapter adapter;
    public XListView new_commen_view;
    public  String id;
    public static final int CHANGE_COMMENT=1;
    public List<Map<String,Object>> list_data=new ArrayList<Map<String,Object>>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CHANGE_COMMENT:
                    adapter=new CommentAdapter(GetCommnet_Activity.this,list_data);
                    new_commen_view.setAdapter(adapter);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_comment);
        new_commen_view= (XListView) findViewById(R.id.new_comment);
        new_commen_view.setPullLoadEnable(true);
        new_commen_view.setXListViewListener(new XListView.IXListViewListener() {
            //下拉
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJSON();
                        onLoad();
                    }
                },2000);
            }
            //上拉
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJSON();
                        onLoad();
                    }
                },2000);
            }
        });


        Intent intent=getIntent();
        id=intent.getStringExtra("figure");
        getJSON();




        adapter=new CommentAdapter(this,list_data);
        new_commen_view.setAdapter(adapter);

    }
    public void getJSON(){
        AsyncHttpClient client=new AsyncHttpClient();
        String news_id=id;
        client.get(Constant.SERVER_URL+"/wp-json/posts/"+news_id+"/comments",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                list_data.clear();
                if (!response.equals(JSONObject.NULL)){
                    for (int i=0;i<response.length();i++){
                        try {
                            Map<String,Object> map=new HashMap<String, Object>();
                            JSONObject object=response.getJSONObject(i);
                            String content_com=object.getString("content");
                            String content_date=object.getString("date");
                            String newdate=content_date.replaceAll("T"," ");
                            JSONObject author_ob=object.getJSONObject("author");
                            String author=author_ob.getString("name");
                            if (!author.equals(JSONObject.NULL)){
                                map.put("content",content_com);
                                map.put("date",newdate);
                                map.put("auther",author);
                                list_data.add(map);
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
    private void onLoad(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        String time=dateFormat.format(new java.util.Date());
        new_commen_view.stopRefresh();
        new_commen_view.stopLoadMore();
        new_commen_view.setRefreshTime(time);
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
            Intent intent=new Intent(GetCommnet_Activity.this,PostComment_Activity.class);
            startActivity(intent);
            GetCommnet_Activity.this.finish();
            overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);

        }
        return super.onOptionsItemSelected(item);

    }
}
