package com.saint.aoyangbuulid.article.news;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.article.news.adapter.MyAdapter;
import com.saint.aoyangbuulid.article.news.banner.BannerData;
import com.saint.aoyangbuulid.article.news.banner.BannerView;
import com.saint.aoyangbuulid.article.news.comment.PostComment_Activity;
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
import cz.msebera.android.httpclient.auth.AuthScope;

/**
 * Created by zzh on 15-12-15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class News_Fragment extends Fragment {
    public XListView view_listView;
    public List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    public MyAdapter myAdapter;
    public BannerView bannerView;
    public BannerData d;
    public List<BannerData> data=new ArrayList<BannerData>();
    private int pagenumber=1;
    private static final int CHANGE_PAGE=1;
    public String content,title,time,image,news_id=null;
    private  SharedPreferences sp;
    private String p,w;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CHANGE_PAGE:
                    int data=msg.arg1;
                    pagenumber=data;
                    getNewsJSON();
                    myAdapter=new MyAdapter(getActivity(),list);
                    view_listView.setAdapter(myAdapter);
                    break;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.news_layout_main,container,false);
        getLoginJSON();

        view_listView= (XListView) view.findViewById(R.id.listview_main);
        getNewsJSON();

        view_listView.setPullLoadEnable(true);

/**
 * 给listview 增加一个Header*/

        View view_header=LayoutInflater.from(getActivity()).inflate(R.layout.activity_account, view_listView, false);
        bannerView = (BannerView) view_header.findViewById(R.id.banner);
        bannerView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.MATCH_PARENT));
        bannerView.setCallback(new BannerView.ClickCallback() {
            @Override
            public void perform(int id, int position) {
                Intent hot_intent = new Intent();
                int figure = data.get(position).getId();
                System.out.print(figure);
                hot_intent.putExtra("figure_id", figure);
                hot_intent.putExtra("content", data.get(position).getHot_content());
                hot_intent.setClass(getActivity(), PostComment_Activity.class);
                startActivity(hot_intent);
            }
        });
        view_listView.setXListViewListener(new XListView.IXListViewListener() {

            //实现下拉数据的加载
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int data = pagenumber + 1;
                        Message message = Message.obtain(mHandler, 1);
                        message.arg1 = data;
                        mHandler.sendMessage(message);
                        onLoad();
                    }
                }, 2000);

            }

            // onLoadMore中实现上拉加载更多的数据加载
            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int data = pagenumber + 1;
                        Message message = Message.obtain(mHandler, 1);
                        message.arg1 = data;
                        mHandler.sendMessage(message);
                        onLoad();
                    }
                }, 2000);
            }
        });
        view_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取被点击的item所对应的数据
                ListView view_list = (ListView) parent;
                Map<String, Object> m = (Map<String, Object>) view_list.getItemAtPosition(position);
                String cont = (String) m.get("content");
                SharedPreferences sp = getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("hot_data", cont);
                editor.commit();
                news_id = (String) m.get("new_id");
                Intent intent = new Intent(getActivity(), PostComment_Activity.class);
                intent.putExtra("content", cont);
                intent.putExtra("news_id", news_id);
                startActivity(intent);

            }
        });
        getimageJSON();
        myAdapter=new MyAdapter(getActivity(),list);
        view_listView.setAdapter(myAdapter);
        return view;

    }
    /**解析JSON
     * 文章列表*/
    public void getNewsJSON(){
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.add("page", String.valueOf(pagenumber));
        client.get("http://112.80.40.185/wp-json/posts?filter[category_name]=news", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
//                list.clear();
                int len = response.length();
                for (int i = 0; i < len; i++) {
                    try {
                        Map<String, Object> map = new HashMap<String, Object>();
                        JSONObject object = response.optJSONObject(i);
                        title = object.optString("title");
                        time = object.optString("date");
                        String newtime=time.replaceAll("T"," ");
                        content = object.optString("content");
                        news_id = object.optString("ID");
                        map.put("title", title);
                        map.put("date", newtime);
                        map.put("content", content);
                        map.put("new_id", news_id);
                        list.add(map);
                        if (object.getJSONObject("featured_image").equals(JSONObject.NULL)) {
                        } else {
                            JSONObject feature = object.optJSONObject("featured_image");
                            image = feature.optString("source");
                            map.put("image", image);
                        }
                        myAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
//                        Toast.makeText(getActivity(), "出错拉!!!!!!", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        });
    }
    private void onLoad(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
        String time=dateFormat.format(new java.util.Date());
        view_listView.stopRefresh();
        view_listView.stopLoadMore();
        view_listView.setRefreshTime(time);
    }
    //幻灯片
    public void getimageJSON(){
        Log.e("imageJson:"+"===============>","JSONS");
        final AsyncHttpClient imageclient=new AsyncHttpClient();
        imageclient.get(Constant.SERVER_URL + "/wp-json/posts?filter[category_name]=slider", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    for (int m = 0; m < response.length(); m++) {
                        JSONObject object = response.optJSONObject(m);

                        JSONObject feature = object.optJSONObject("featured_image");
                        //判断图片资源是否为空
                        if (!feature.optString("source").equals(JSONObject.NULL)) {
                            d = new BannerData();
                            d.setImage(feature.getString("source"));
                            d.setTitle(object.getString("title"));
                            d.setId(object.getInt("ID"));
                            d.setHot_content(object.optString("content"));
                            data.add(d);
                        }
                    }
                    bannerView.startup(data);
                    view_listView.addHeaderView(bannerView, null, true);
                    view_listView.setAdapter(myAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void getLoginJSON(){
        String url= Constant.SERVER_URL+"/wp-json/users/me";
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        p=sp.getString("phone", "");
        w=sp.getString("passed","");

/**UTF-8编码
 * try {
 String UTF= URLEncoder.encode(p + ":" + w, "utf-8");
 Log.e("========================>", UTF);
 } catch (UnsupportedEncodingException e) {
 e.printStackTrace();
 }*/

        client.setBasicAuth(p, w, AuthScope.ANY);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Dialog();
            }
        });
    }
    public  void Dialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("提示")
                .setMessage("您的账号或密码输入有误，请重新输入...");

        dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
                SharedPreferences.Editor editor=sp.edit();
                editor.clear();
                editor.commit();
                Intent intent=new Intent(getActivity(),Login_Activity.class);
                startActivity(intent);
                getActivity().finish();
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }


}
