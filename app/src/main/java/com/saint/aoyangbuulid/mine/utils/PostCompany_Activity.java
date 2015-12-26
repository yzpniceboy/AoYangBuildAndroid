package com.saint.aoyangbuulid.mine.utils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.login.Login_Activity;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.AuthScope;

/**
 * Created by zzh on 15-12-8.
 */
public class PostCompany_Activity extends BaseActivity implements View.OnClickListener{
    EditText text_company,text_phone,text_brief;
    Button button_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postcompany);
        text_company= (EditText) findViewById(R.id.postcompany);
        text_phone= (EditText) findViewById(R.id.edit_phone);
        text_brief= (EditText) findViewById(R.id.edit_brief);
        button_post= (Button) findViewById(R.id.button_posst);
        button_post.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        postCompanyJson();

        finish();
    }
    /**新建一个公司*/
    public  void  postCompanyJson(){
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
        String p=sp.getString("phone",0+"");
        String w=sp.getString("passed",0+"");
        String url= Constant.SERVER_URL+"/wp-json/pods/company";
        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(p, w, AuthScope.ANY);
        RequestParams params=new RequestParams();
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("company_name",text_company.getText().toString());
        editor.commit();
        params.add("name",text_company.getText().toString());
        params.add("phone",text_phone.getText().toString());
        params.add("brief",text_brief.getText().toString());
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.print(response);

            }
        });
    }


}
