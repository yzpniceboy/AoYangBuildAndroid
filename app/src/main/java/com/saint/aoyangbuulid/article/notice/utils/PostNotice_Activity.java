package com.saint.aoyangbuulid.article.notice.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
 * Created by zzh on 15-12-4.
 * 现在不用
 */
public class PostNotice_Activity extends BaseActivity  {

    private EditText text_comments;

    private ImageButton button_send;

    public WebView web_ontice;

    String notice_content,news_id,new_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notice_web_layout);

        //实例化
        text_comments= (EditText) findViewById(R.id.edittext_send);
        button_send= (ImageButton) findViewById(R.id.send_button);
        web_ontice= (WebView) findViewById(R.id.web_notice);

        WebSettings set = web_ontice.getSettings();
        set.setDefaultTextEncodingName("UTF -8");

        final Intent intent=getIntent();
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        notice_content=intent.getStringExtra("notice_content");
        if (notice_content==null){
            new_data=sp.getString("content_notice","");
        }else {
            new_data=notice_content;
        }

        web_ontice.loadData(Constant.HTML_CSS+new_data,"text/html;charset=UTF-8",null);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (text_comments.getText().toString().length()!=0) {

                    PostJson();

                    Intent in = new Intent(PostNotice_Activity.this, GetNotice_Activity.class);

                    startActivity(in);

                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

//                    PostNotice_Activity.this.finish();

                }else {
                    Toast.makeText(PostNotice_Activity.this, "评论不能为空", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_notice, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                Intent intent=new Intent(PostNotice_Activity.this, MainActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
                PostNotice_Activity.this.finish();
                break;
        }

        if (item.getItemId()==R.id.comment){
            Intent intent=new Intent(PostNotice_Activity.this,GetNotice_Activity.class);
            intent.putExtra("id", news_id);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            PostNotice_Activity.this.finish();

        }
        return super.onOptionsItemSelected(item);

    }
    public void  PostJson(){

        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);

        AsyncHttpClient client=new AsyncHttpClient();

        client.setBasicAuth(sp.getString("phone",""),sp.getString("passed",""), AuthScope.ANY);

        String url=Constant.SERVER_URL+"/wp-json/posts/"+sp.getString("news_id","")+"/comments";

        RequestParams params=new RequestParams();

        String text=text_comments.getText().toString();

        params.add("data[content]",text);

        params.add("data[author]",sp.getString("nickname",""));

        client.post(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

            }
        });
    }
}
