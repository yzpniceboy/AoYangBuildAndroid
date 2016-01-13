package com.saint.aoyangbuulid.mine.utils;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.article.news.mylistview.XListView;
import com.saint.aoyangbuulid.login.Login_Activity;
import com.saint.aoyangbuulid.mine.adapter.CompanyAdapter;

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
 * Created by zzh on 15-12-9.
 */
public class Select_Company_Activity extends BaseActivity {
    public XListView view_company;
    private ProgressDialog loadingDialog;
    public List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    public CompanyAdapter adapter;
    String company_name;
    String  text_data;
    private boolean isAUTO_Refresh=true;
    public static  final  int CHANG_REFRESH=1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CHANG_REFRESH:
                    adapter=new CompanyAdapter(Select_Company_Activity.this,list);
                    view_company.setAdapter(adapter);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_company);
        loadingDialog=new ProgressDialog(Select_Company_Activity.this,ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage("正在获取最新列表...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        view_company= (XListView) findViewById(R.id.list_company);
        view_company.setPullLoadEnable(true);
        getCompanyJSON();
        if (isAUTO_Refresh){
            Refresh();
        }
        adapter=new CompanyAdapter(this,list);
        view_company.setAdapter(adapter);
        view_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView list_company = (ListView) parent;
                Map<String, Object> map = (Map<String, Object>) list_company.getItemAtPosition(position);
                company_name = (String) map.get("name");
                final int company_data = (int) map.get("company_id");
                final CheckBox ck = (CheckBox) view.findViewById(R.id.ck_company);
                ck.setChecked(true);
                final View view_info = LayoutInflater.from(Select_Company_Activity.this).inflate(R.layout.join_info, null,true);

                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(Select_Company_Activity.this)
                        .setTitle("申请")
                        .setCancelable(false)
                        .setMessage("申请加入" + company_name + "?")
                        .setView(view_info);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText et_data= (EditText) view_info.findViewById(R.id.et_data);
                        text_data = et_data.getText().toString();
                       //申请加入公司
                        String url = Constant.SERVER_URL + "/wp-json/pods/application";
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        SharedPreferences sp = getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
                        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""), AuthScope.ANY);
                        params.add("company", String.valueOf(company_data));
                        params.add("user", sp.getString("user_id", ""));
                        System.out.print(text_data);
                        params.add("introduction", text_data);
                        client.post(url, params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Log.e("postCompany:"+"===============>","onSuccess");
                                Select_Company_Activity.this.finish();


                            }
                        });

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ck.setChecked(false);
                        Select_Company_Activity.this.finish();
                        dialog.dismiss();
                    }
                });
                dialog.create().show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_createcompany, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Select_Company_Activity.this.finish();
                break;
            case R.id.add_company:
                Intent intent=new Intent();
                intent.setClass(Select_Company_Activity.this,PostCompany_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    /**查询所有公司*/
    public  void  getCompanyJSON(){
        String url= Constant.SERVER_URL+"/wp-json/pods/company";
        AsyncHttpClient client=new AsyncHttpClient();
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        client.setBasicAuth(sp.getString("phone",""),sp.getString("passed",""));
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                loadingDialog.dismiss();
                list.clear();
                System.out.print(response);
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
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("name", name);
                        map.put("company_id", company_id);
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
        view_company.stopRefresh();
        view_company.stopLoadMore();
        view_company.setRefreshTime(time);
    }


        private void Refresh(){

            view_company.setXListViewListener(new XListView.IXListViewListener() {
                @Override
                public void onRefresh() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getCompanyJSON();
                            onLoad();

                        }
                    },2000);
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
        }

}
