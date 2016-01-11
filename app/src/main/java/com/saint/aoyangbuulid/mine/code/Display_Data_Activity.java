package com.saint.aoyangbuulid.mine.code;

import android.app.ProgressDialog;
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
import com.saint.aoyangbuulid.login.Login_Activity;
import com.saint.aoyangbuulid.mine.adapter.MipcaAdapterCapture;

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
 * Created by zzh on 15-12-19.
 */
public class Display_Data_Activity extends BaseActivity {
    XListView view;
    String data;
    private ProgressDialog loadingDialog;

    private List<Map<String , Object>>  list=new ArrayList<Map<String ,Object>>();
    public MipcaAdapterCapture adapterCapture;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapterCapture=new MipcaAdapterCapture(Display_Data_Activity.this,list);
            view.setAdapter(adapterCapture);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_company);
        loadingDialog= new ProgressDialog(Display_Data_Activity.this);
        loadingDialog.setMessage("正在获取最新列表...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        view= (XListView) findViewById(R.id.list_company);
        Intent  intent=getIntent();
        data=intent.getStringExtra("key_data");
        getJSON();
        adapterCapture=new MipcaAdapterCapture(Display_Data_Activity.this,list);
        view.setAdapter(adapterCapture);
        view.setPullLoadEnable(true);
        view.setPullRefreshEnable(true);
        view.setXListViewListener(new XListView.IXListViewListener() {
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
                }, 2000);

            }
        });
    }

    public void getJSON(){
        final SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);

        String url= Constant.SERVER_URL+"/wp-json/pods/user/"+data;
        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""), AuthScope.ANY);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                loadingDialog.dismiss();
                list.clear();
                System.out.print(response);
                String user_name = response.optString("display_name");
                System.out.print(user_name);
                String company_data = response.optString("user_company");
                try {
                    JSONObject object = new JSONObject(company_data);
                    Iterator<String> iterator = object.keys();
                    while (iterator.hasNext()) {
                        String k = iterator.next().toString();
                        String v = object.optString(k);
                        JSONObject ob_company = new JSONObject(v);
                        String company_name = ob_company.optString("name");
                        System.out.print(company_name);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", user_name);
                        map.put("company", company_name);
                        list.add(map);
                        adapterCapture.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(Display_Data_Activity.this, "查无此人", Toast.LENGTH_SHORT).show();



            }
        });
    }
    private void onLoad(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        String time=dateFormat.format(new java.util.Date());
        view.stopRefresh();
        view.stopLoadMore();
        view.setRefreshTime(time);
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
            Display_Data_Activity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
