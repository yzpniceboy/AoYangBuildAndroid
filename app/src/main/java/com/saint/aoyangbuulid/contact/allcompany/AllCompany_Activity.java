package com.saint.aoyangbuulid.contact.allcompany;

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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.article.news.mylistview.XListView;
import com.saint.aoyangbuulid.contact.adapter.AllCompany_Adapter;
import com.saint.aoyangbuulid.login.Login_Activity;

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
 * Created by zzh on 15-12-18.
 */
public class AllCompany_Activity extends BaseActivity {
    private XListView view_company;
    private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    private AllCompany_Adapter adapter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter=new AllCompany_Adapter(AllCompany_Activity.this,list);
            view_company.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allcompany_layout);

        view_company= (XListView) findViewById(R.id.all_company_list);
        view_company.setPullLoadEnable(true);
        getCompanyJSON();

        view_company.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCompanyJSON();
                        onLoad();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCompanyJSON();
                        onLoad();
                    }
                }, 2000);
            }
        });
        view_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView list_data= (ListView) parent;
                Map<String,Object> m_data= (Map<String, Object>) list_data.getItemAtPosition(position);
                Integer id_data= (Integer) m_data.get("company_id");
                Intent intent=new Intent(AllCompany_Activity.this,Allcompany_Member.class);
                intent.putExtra("id",id_data);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                AllCompany_Activity.this.finish();


            }
        });
        adapter=new AllCompany_Adapter(AllCompany_Activity.this,list);
        view_company.setAdapter(adapter);
    }
    public  void  getCompanyJSON(){
        String url= Constant.SERVER_URL+"/wp-json/pods/company";
        AsyncHttpClient client=new AsyncHttpClient();
        final SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                list.clear();
                Iterator<String> iterator = response.keys();
                System.out.print(iterator);
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String v = response.optString(key);
                    System.out.print(v);
                    try {
                        JSONObject object = new JSONObject(v);
                        String name = object.optString("name");
                        int company_id = object.optInt("id");
                        String phone=object.optString("phone");
                        if (!phone.equals(JSONObject.NULL)){
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("name", name);
                            map.put("phone",phone);
                            map.put("company_id", company_id);
                            list.add(map);
                        }else {
                            phone="暂未设置";
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("name", name);
                            map.put("phone",phone);
                            map.put("company_id", company_id);
                            list.add(map);
                        }
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
        view_company.stopRefresh();
        view_company.stopLoadMore();
        view_company.setRefreshTime(time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
