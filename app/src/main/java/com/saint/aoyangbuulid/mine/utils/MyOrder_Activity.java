package com.saint.aoyangbuulid.mine.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.article.news.mylistview.XListView;
import com.saint.aoyangbuulid.mine.adapter.MyOrderAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzh on 15-12-24.
 */
public class MyOrder_Activity  extends BaseActivity  {
    private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    XListView view_list;
    public MyOrderAdapter adapter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter=new MyOrderAdapter(MyOrder_Activity.this,list);
            view_list.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder);
        initData();
        view_list= (XListView) findViewById(R.id.myorder);

        view_list.setPullLoadEnable(true);

        view_list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        onLoad();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        onLoad();
                    }
                }, 2000);
            }
        });
        adapter=new MyOrderAdapter(MyOrder_Activity.this,list);
        view_list.setAdapter(adapter);



    }
    private void onLoad(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        String time=dateFormat.format(new java.util.Date());
        view_list.stopRefresh();
        view_list.stopLoadMore();
        view_list.setRefreshTime(time);
    }

    public void initData(){
        list.clear();
        for (int i=0;i<2;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("name","会议室");
            map.put("phone","未设置");
            map.put("people","未设置");
            list.add(map);
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
            MyOrder_Activity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
