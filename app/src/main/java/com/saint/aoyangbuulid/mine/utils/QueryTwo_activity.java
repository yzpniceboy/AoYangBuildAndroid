package com.saint.aoyangbuulid.mine.utils;

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
import com.saint.aoyangbuulid.login.Login_Activity;
import com.saint.aoyangbuulid.mine.adapter.QueryTwoAdapter;
import com.saint.aoyangbuulid.article.news.mylistview.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.AuthScope;

/**
 * Created by zzh on 15-12-12.
 */
public class QueryTwo_activity extends BaseActivity {
    public QueryTwoAdapter adapter;
    String company_name;
    public XListView list_view;
    List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter=new QueryTwoAdapter(QueryTwo_activity.this,list);
            list_view.setAdapter(adapter);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.querytwo_layout);
        getJSON();
        list_view= (XListView) findViewById(R.id.list_two_query);
        list_view.setPullLoadEnable(true);
        list_view.setXListViewListener(new XListView.IXListViewListener() {
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

        adapter=new QueryTwoAdapter(QueryTwo_activity.this,list);
        list_view.setAdapter(adapter);
    }
    /**
     *普通员工看自己书否申请成功
     * 查看提交申请*/
    public void getJSON(){
        String url= Constant.SERVER_URL+"/wp-json/pods/application";
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);

        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(sp.getString("phone",""),sp.getString("passed",""), AuthScope.ANY);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.print(response);
                Iterator<String> it = response.keys();
                while (it.hasNext()) {
                    String k = it.next().toString();
                    String v = response.optString(k);
                    try {
                        JSONObject ob_v = new JSONObject(v);
                        //返回审核状态
                        String approved = ob_v.optString("approved");
                        String user = ob_v.optString("user");
                        String company = ob_v.optString("company");
                        JSONObject ob_company = new JSONObject(company);
                        Iterator<String> iterator = ob_company.keys();
                        while (iterator.hasNext()) {
                            String k_data = iterator.next().toString();
                            String v_data = ob_company.optString(k_data);
                            System.out.print(v_data);
                            JSONObject ob_data = new JSONObject(v_data);
                            company_name = ob_data.optString("name");
                        }
                        JSONObject ob_user = new JSONObject(user);
                        String display_name = ob_user.optString("display_name");
                        System.out.print(display_name);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("display_name", display_name);
                        map.put("company_name", company_name);
                        map.put("approved", approved);
                        list.add(map);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    private void onLoad(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        String time=dateFormat.format(new java.util.Date());
        list_view.stopRefresh();
        list_view.stopLoadMore();
        list_view.setRefreshTime(time);
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
            QueryTwo_activity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
