package com.saint.aoyangbuulid.article.notice;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.article.news.mylistview.XListView;
import com.saint.aoyangbuulid.article.notice.adapter.NoticeAdapter;
import com.saint.aoyangbuulid.article.notice.utils.PostNotice_Activity;
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
 * Created by zzh on 15-12-8.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Notice_Fragment extends Fragment {
    public XListView view_notice;
    public List<Map<String,Object>> list_notice=new ArrayList<Map<String,Object>>();
    public NoticeAdapter mNoticeAdapter;
    public String no_title,no_content,no_date,no_excerpt,no_id;
    private ProgressDialog loadingDialog;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mNoticeAdapter = new NoticeAdapter(getActivity(),list_notice);
            view_notice.setAdapter(mNoticeAdapter);
        }
    };
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.notice,container,false);

        loadingDialog=new ProgressDialog(getActivity());
        loadingDialog.setMessage("正在获取最新列表...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        view_notice= (XListView) view.findViewById(R.id.listview_notice);
        getNoticeJSON();
        view_notice.setPullLoadEnable(true);
        view_notice.setPullRefreshEnable(true);
        view_notice.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNoticeJSON();
                        onLoad();
                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNoticeJSON();
                        onLoad();
                    }
                },2000);
            }
        });

        view_notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView li_notice = (ListView) parent;
                Map<String, Object> map = (Map<String, Object>) li_notice.getItemAtPosition(position);
                String content_notice = (String) map.get("content");
                String news_id= (String) map.get("new_id");
                SharedPreferences sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("content_notice",content_notice);
                editor.putString("news_id",news_id);
                editor.commit();
                Intent intent = new Intent(getActivity(), PostNotice_Activity.class);
                intent.putExtra("notice_content", content_notice);
                intent.putExtra("news_id",news_id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

            }
        });
        mNoticeAdapter=new NoticeAdapter(getActivity(),list_notice);
        view_notice.setAdapter(mNoticeAdapter);
        return view;
    }

    public void getNoticeJSON(){

        AsyncHttpClient notice_client=new AsyncHttpClient();
        notice_client.get(Constant.SERVER_URL+"/wp-json/posts?filter[category_name]=notice",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                loadingDialog.dismiss();
                list_notice.clear();
                for (int n=0;n<response.length();n++){
                    try {
                        JSONObject no_object=response.getJSONObject(n);
                        no_id=no_object.optString("ID");
                        no_title=no_object.optString("title");
                        no_content=no_object.optString("content");
                        no_date=no_object.optString("date");
                        String new_nodate=no_date.replaceAll("T"," ");
                        no_excerpt=no_object.optString("excerpt");
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put("title",no_title);
                        map.put("content",no_content);
                        map.put("date",new_nodate);
                        map.put("excerpt",no_excerpt);
                        map.put("new_id",no_id);
                        list_notice.add(map);
                        mNoticeAdapter.notifyDataSetChanged();
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
        view_notice.stopRefresh();
        view_notice.stopLoadMore();
        view_notice.setRefreshTime(time);
    }
}
