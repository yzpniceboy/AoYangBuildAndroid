package com.saint.aoyangbuulid.mine.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.login.Login_Activity;
import com.saint.aoyangbuulid.mine.adapter.Query_Adpater;
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
 * Created by zzh on 15-12-10.
 */
public class Query_activity extends BaseActivity {
    public int data;
    public XListView view_list;
    private ProgressDialog loadingDialog;
    public Query_Adpater adapter;
    public List<Map<String,Object>> list=new ArrayList<Map<String ,Object>>();
    TextView text_null;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter=new Query_Adpater(Query_activity.this,list);
            view_list.setAdapter(adapter);
        }
    };
    String approved_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_layout);
        text_null= (TextView) findViewById(R.id.text_null);

        loadingDialog=new ProgressDialog(Query_activity.this,ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage("正在获取最新列表...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);
        loadingDialog.show();
        getJSON();
        view_list= (XListView) findViewById(R.id.list_query);
        view_list.setPullLoadEnable(true);
        view_list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJSON();

                        onLoad();
                    }
                },1000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJSON();

                        onLoad();
                    }
                },1000);
            }
        });
        adapter=new Query_Adpater(Query_activity.this,list);
        view_list.setAdapter(adapter);

        view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder dialog=new AlertDialog.Builder(Query_activity.this)
                        .setTitle("审查")
                        .setMessage("是否同意加入本公司");
                dialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data = 1;
                        putJSON();
                        Query_activity.this.finish();
                    }
                });
                dialog.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data = -1;
                        putJSON();
                        Query_activity.this.finish();

                    }
                });
                dialog.create().show();

            }


        });
    }
    private void onLoad(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        String time=dateFormat.format(new java.util.Date());
        view_list.stopRefresh();
        view_list.stopLoadMore();
        view_list.setRefreshTime(time);
    }
    /**查询提交的申请*/
    public void getJSON(){
        final SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
        String url= Constant.SERVER_URL+"/wp-json/pods/application";
        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""), AuthScope.ANY);
        RequestParams params=new RequestParams();

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                text_null.setVisibility(View.GONE);
                view_list.setVisibility(View.VISIBLE);
                loadingDialog.dismiss();
                list.clear();
                Iterator<String> it = response.keys();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    String v = response.optString(key);
                    try {
                        JSONObject object = new JSONObject(v);
                        approved_id = object.optString("id");
                        String user_info = object.optString("user");
                        JSONObject user = new JSONObject(user_info);
                        String user_name = user.optString("display_name");
                        String company = object.getString("company");
                        JSONObject data = new JSONObject(company);
                        Iterator<String> iterator = data.keys();
                        while (iterator.hasNext()) {
                            String key_data = iterator.next().toString();
                            String v_data = data.optString(key_data);
                            JSONObject js = new JSONObject(v_data);
                            String company_name = js.getString("name");
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("name", company_name);
                            map.put("user_name", user_name);
                            list.add(map);
                            adapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        if (text_null.getVisibility()==View.VISIBLE){

            text_null.setText("暂无申请");
            loadingDialog.dismiss();
        }

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
              finish();
        }
        return super.onOptionsItemSelected(item);

    }
/**审批申请*/
    public void putJSON(){
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        String url = Constant.SERVER_URL+"/wp-json/pods/application/"+ approved_id;
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("approved", data + "");
        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""), AuthScope.ANY);
        client.put(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                loadingDialog.dismiss();
                System.out.print(response);

            }
        });
    }

}
