package com.saint.aoyangbuulid.contact;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.article.news.mylistview.XListView;
import com.saint.aoyangbuulid.contact.adapter.CompanyMember_Adapter;
import com.saint.aoyangbuulid.contact.allcompany.AllCompany_Activity;
import com.saint.aoyangbuulid.login.Login_Activity;
import com.saint.aoyangbuulid.mine.utils.Select_Company_Activity;

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
 * Created by 志浩 on 2015/11/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class Contact_Fragment extends Fragment  {
    private ImageButton button_navi;
    private XListView view_list;
    TextView text_null;
    public CompanyMember_Adapter adapter;
    private static final  int UPDATA=1;
    private ProgressDialog loadingDialog;
    public List<Map<String,Object>> list_right=new ArrayList<Map<String,Object>>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA:
                    adapter= new CompanyMember_Adapter(getActivity(),list_right);
                    view_list.setAdapter(adapter);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.contact_cepartment_layout, container, false);


        getCompanyMember();

        loadingDialog=new ProgressDialog(getActivity());
        loadingDialog.setMessage("正在获取最新列表...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        view_list= (XListView) view.findViewById(R.id.list_view);
        view_list.setPullLoadEnable(true);
        view_list.setPullRefreshEnable(true);
        button_navi= (ImageButton) view.findViewById(R.id.button_navi);
        button_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllCompany_Activity.class);
                startActivity(intent);


            }
     });
        view_list.setXListViewListener(new XListView.IXListViewListener() {
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
       view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ListView list = (ListView) parent;
               Map<String, Object> map = (Map<String, Object>) list.getItemAtPosition(position);
               String phonenumber = (String) map.get("phone");
               Intent intent = new Intent();
               intent.setAction("android.intent.action.CALL");
               intent.setData(Uri.parse("tel:" + phonenumber));
               startActivity(intent);
           }
       });
        adapter= new CompanyMember_Adapter(getActivity(),list_right);
        view_list.setAdapter(adapter);
        return view;
    }
    public void getCompanyMember(){
        SharedPreferences sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
        String url= Constant.SERVER_URL+"/wp-json/pods/company/"+sp.getString("company_id_mine","");
        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""), AuthScope.ANY);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                view_list.setVisibility(View.VISIBLE);
                loadingDialog.dismiss();
                System.out.print(response);
                list_right.clear();
                try {
                    String user = response.optString("users");
                    System.out.print(user);
                    JSONObject ob_user = new JSONObject(user);
                    Iterator<String> iterator = ob_user.keys();
                    while (iterator.hasNext()) {
                        String k = iterator.next().toString();
                        String v = ob_user.optString(k);
                        System.out.print(v);
                        JSONObject ob_v = new JSONObject(v);
                        String display = ob_v.optString("display_name");
                        String phone=ob_v.optString("phone");
                        if (phone.equals("")){
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("dispaly_name", display);
                            map.put("phone","暂未设置");
                            list_right.add(map);
                        }else {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("dispaly_name", display);
                            map.put("phone",phone);
                            list_right.add(map);
                        }

                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
                if (sp.getString("company_name","").equals("")||sp.getString("company_name","").equals(null)) {
                    DialogDemo();
                }
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
    private void Dialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity())
                .setMessage("您暂时还不属于任何一家公司,赶加入入吧!!!!!")
                .setCancelable(false)
                .setTitle("提示");
        dialog.setPositiveButton("加入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), Select_Company_Activity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.create().show();

    }

    private  void  DialogDemo(){
        android.support.v7.app.AlertDialog.Builder dialog= new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setMessage("您暂时还不属于任何一家公司,赶加入入吧!!!!!")
                .setCancelable(false)
                .setTitle("提示");
        dialog.setPositiveButton("加入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), Select_Company_Activity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                dialog.dismiss();
            }
        });
        dialog.create().show();

    }
}
