package com.saint.aoyangbuulid.article.news.comment;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.saint.aoyangbuulid.MainActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.login.Login_Activity;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.AuthScope;


/**
 * Created by 志浩 on 2015/10/30.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PostComment_Activity extends BaseActivity {
    public ImageButton send_button;
    public EditText text_send;
    public FragmentManager fragmentManager=getFragmentManager();
    public String view_content,title,hot_data,notice_content,news_id,figure_id,new_data;
    private WebView web_content;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commont_layout);

        web_content= (WebView) findViewById(R.id.article_text);
        WebSettings set = web_content.getSettings();
        set.setDefaultTextEncodingName("UTF -8");

        //显示左边返回头功能
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //获取传递的数值
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        Intent intent=getIntent();
        hot_data=intent.getStringExtra("content");
        if (hot_data==null){
            new_data=sp.getString("hot_data","");
        }else {
            new_data=hot_data;
        }
        view_content=intent.getStringExtra("content");
        news_id=intent.getStringExtra("news_id");
        notice_content=intent.getStringExtra("notice_content");
        figure_id=intent.getIntExtra("figure_id",0)+"";


        /**限制文章内容格式constant.HTML_CSS*/
       // web_content.loadData(Constant.HTML_CSS+view_content,"text/html; charset=UTF-8",null);
        web_content.loadData(Constant.HTML_CSS+new_data,"text/html;charset=UTF-8",null);


        send_button= (ImageButton) findViewById(R.id.send_button);
        text_send= (EditText) findViewById(R.id.edittext_send);
            //跳转并且获取传出文本框的数据
            send_button.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onClick(View v) {
                    if (text_send.getText().toString().length()!=0){
                        postCommentJSON();
                        Intent in = new Intent(PostComment_Activity.this, GetCommnet_Activity.class);
                        in.putExtra("id", news_id);
                        in.putExtra("figure", figure_id);
                        startActivity(in);
                        overridePendingTransition(R.anim.push_left_in,
                                R.anim.push_left_out);
                        PostComment_Activity.this.finish();

                    }else {
                        Toast.makeText(PostComment_Activity.this,"评论不能不能为空",Toast.LENGTH_SHORT).show();

                    }

                }
            });


    }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater=getMenuInflater();
                inflater.inflate(R.menu.menu_notice,menu);
                return super.onCreateOptionsMenu(menu);
    }
//为menu item 点击事件
            @Override
            public boolean  onOptionsItemSelected(MenuItem item) {

                //查看评论

                switch (item.getItemId()){
                    case R.id.comment:
                        Intent intent=new Intent(PostComment_Activity.this,GetCommnet_Activity.class);
                        intent.putExtra("id", news_id);
                        intent.putExtra("figure", figure_id);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in,
                                R.anim.push_left_out);
                        PostComment_Activity.this.finish();

                        break;
                    case android.R.id.home:
                        intent=new Intent(PostComment_Activity.this, MainActivity.class);
                        startActivity(intent);
//                        overridePendingTransition(R.anim.push_right_in,
//                                R.anim.push_right_out);
                        PostComment_Activity.this.finish();



                        break;
                }

                return super.onOptionsItemSelected(item);
    }


    public  void postCommentJSON(){
        final String text=text_send.getText().toString();
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
        if (news_id==null){
            news_id=figure_id;
        }
        String url=Constant.SERVER_URL+"/wp-json/posts/"+news_id+"/comments";
        AsyncHttpClient client=new AsyncHttpClient();
        client.setBasicAuth(sp.getString("phone", ""), sp.getString("passed", ""), AuthScope.ANY);
        RequestParams params=new RequestParams();
        params.add("data[content]",text);
        params.add("data[author]", sp.getString("nickname",""));
        client.post(url,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
    }
}
