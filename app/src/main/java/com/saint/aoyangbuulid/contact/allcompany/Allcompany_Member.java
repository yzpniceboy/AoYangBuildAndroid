package com.saint.aoyangbuulid.contact.allcompany;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.article.news.mylistview.XListView;
import com.saint.aoyangbuulid.contact.adapter.CompanyMember_Adapter;
import com.saint.aoyangbuulid.login.Login_Activity;

import org.json.JSONArray;
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
 * Created by zzh on 15-12-18.
 */
public class Allcompany_Member extends BaseActivity {
    public CompanyMember_Adapter adapter;
    private XListView view_member;
    private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                adapter=new CompanyMember_Adapter(Allcompany_Member.this,list);
                view_member.setAdapter(adapter);
        }
    };
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allcompany_layout);
        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);
        view_member= (XListView) findViewById(R.id.all_company_list);
        view_member.setPullLoadEnable(true);
        view_member.setPullRefreshEnable(true);
        view_member.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCompanyMember();
                        onLoad();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCompanyMember();
                        onLoad();
                    }
                }, 2000);
            }
        });
        getCompanyMember();
        adapter=new CompanyMember_Adapter(Allcompany_Member.this,list);
        view_member.setAdapter(adapter);

    }
    public void getCompanyMember(){
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
        System.out.print(id);
        String url= Constant.SERVER_URL+"/wp-json/pods/company/"+id;
        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""), AuthScope.ANY);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String user = response.optString("users");
                list.clear();
                try {
                    JSONObject ob_user = new JSONObject(user);
                    Iterator<String> iterator = ob_user.keys();
                    while (iterator.hasNext()) {
                        String k = iterator.next().toString();
                        String v = ob_user.optString(k);
                        System.out.print(v);
                        JSONObject ob_v = new JSONObject(v);
                        String display = ob_v.optString("display_name");
                        System.out.print(display);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("dispaly_name", display);
                        list.add(map);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Allcompany_Member.this,"暂无此此功能",Toast.LENGTH_SHORT).show();                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
    private void onLoad(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        String time=dateFormat.format(new java.util.Date());
        view_member.stopRefresh();
        view_member.stopLoadMore();
        view_member.setRefreshTime(time);
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
            Intent intent=new Intent(Allcompany_Member.this,AllCompany_Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            Allcompany_Member.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
