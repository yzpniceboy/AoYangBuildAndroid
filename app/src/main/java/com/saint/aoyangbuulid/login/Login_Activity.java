package com.saint.aoyangbuulid.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.saint.aoyangbuulid.MainActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.Utils.Constant;
import com.saint.aoyangbuulid.sign.Choice_Sign_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.AuthScope;

/**
 * Created by zzh on 15-11-9.
 */
public class Login_Activity extends Activity implements View.OnClickListener{
    public EditText et_phonenumber,et_passed;
    public Button bt_sign,bt_forgetpassed;
    public ImageButton imageButton_login,imageButton_exit;
    private TextWatcher phoneTextWatcher;
    //定义SharedPreferences访问模式和文件名称
    public SharedPreferences sp;
    public  static  final  String PREFERENCE_NAME="SaveSetting";
    public  static int Mode=Context.MODE_WORLD_READABLE;
    //用来存放帐号密码
    public String p,w,number;
    private ImageView welcomeimage=null;
    private ProgressDialog loadingDialog;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // 如果调起支付太慢, 可以在这里开启动画, 以progressdialog为例
        loadingDialog = new ProgressDialog(Login_Activity.this);
        loadingDialog.setMessage("正在连接，请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);



        /**实例化：SharedPreferences
         * */
        SharedPreferences sp=getSharedPreferences(PREFERENCE_NAME,Mode);
        //获取实例
        welcomeimage= (ImageView) findViewById(R.id.welcomeimage);
        et_passed= (EditText) findViewById(R.id.edittext_passed);
        et_phonenumber= (EditText) findViewById(R.id.edittext_phonenumber);
        bt_sign= (Button) findViewById(R.id.button_sign);
        bt_forgetpassed= (Button) findViewById(R.id.button_forgetpassed);
        imageButton_login= (ImageButton) findViewById(R.id.imagebutton_login);
        imageButton_exit= (ImageButton) findViewById(R.id.image_exitone);
        //实现监听
        et_passed.setOnClickListener(this);
        et_phonenumber.setOnClickListener(this);
        bt_forgetpassed.setOnClickListener(this);
        bt_sign.setOnClickListener(this);
        imageButton_login.setOnClickListener(this);
        imageButton_exit.setOnClickListener(this);

        //将sp中存放的帐号密码提取出来放入两个输入文本框中
        et_passed.setText(sp.getString("passed", ""));
        et_phonenumber.setText(sp.getString("phone", ""));


        /**
         * 监听输入的手机号码是否正确
         *
         * */

        phoneTextWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                number=et_phonenumber.getText().toString();
                if (isMobile(number)==false){
                    imageButton_login.setClickable(false);
                    et_phonenumber.setTextColor(Color.RED);
                }else {
                    imageButton_login.setClickable(true);
                    et_phonenumber.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        et_phonenumber.addTextChangedListener(phoneTextWatcher);

    }
//点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagebutton_login:
                //登陆
                getJSON();
                break;
            case R.id.button_sign:
               Intent intent = new Intent();
                intent.setClass(Login_Activity.this,Choice_Sign_Activity.class);
                startActivity(intent);
                break;
            case R.id.button_forgetpassed:
                Toast.makeText(Login_Activity.this,"暂无内容",Toast.LENGTH_SHORT).show();
                break;
            case R.id.image_exitone:
                Login_Activity.this.finish();
                Log.e("key:" + "=====================>", "destroy");
                break;

        }
    }
    public void getJSON(){
        String url= Constant.SERVER_URL+"/wp-json/users/me";
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        p=et_phonenumber.getText().toString();
        w=et_passed.getText().toString();

/**UTF-8编码
 * try {
 String UTF= URLEncoder.encode(p + ":" + w, "utf-8");
 Log.e("========================>", UTF);
 } catch (UnsupportedEncodingException e) {
 e.printStackTrace();
 }*/

        client.setBasicAuth(p, w, AuthScope.ANY);
        loadingDialog.show();
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                sp = getSharedPreferences(PREFERENCE_NAME, Mode);
                final SharedPreferences.Editor editor = sp.edit();
                editor.putString("phone", p);
                editor.putString("passed", w);
                editor.putString("response", response + "");
                editor.commit();
                String nick_name = response.optString("nickname");
                String roles = response.optString("roles");
                String meta = response.optString("meta");
                try {
                    JSONObject data = new JSONObject(meta);
                    JSONObject company_data = data.getJSONObject("user_company");
                    System.out.print(company_data);
                    String name = company_data.optString("name");
                    String id = company_data.optString("id");
                    SharedPreferences sp = getSharedPreferences(PREFERENCE_NAME, Mode);
                    SharedPreferences.Editor editor_name = sp.edit();
                    editor_name.putString("company_name", name);
                    editor_name.putString("company_id_mine", id);
                    editor_name.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int user_id = response.optInt("ID");
                sp = getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
                SharedPreferences.Editor editor_id = sp.edit();
                editor_id.putString("user_id", user_id + "");
                editor_id.putString("nickname", nick_name);
                editor_id.putString("roles", roles);
                editor_id.commit();


//                    if (response!=null){
//                        Intent intent=new Intent();
//                        intent.putExtra("phone",p);
//                        intent.putExtra("passed",w);
//                        intent.setClass(Login_Activity.this,MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                    Dialog();
                Intent intent = new Intent();
                intent.putExtra("phone",p);
                    intent.putExtra("passed",w);
                    intent.setClass(Login_Activity.this, MainActivity.class);
                    startActivity(intent);
                    Login_Activity.this.finish();

                }
            });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public  void Dialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(Login_Activity.this,AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("提示")
                .setMessage("您的账号或密码输入有误，请重新输入...");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }
    //判断输入框内容为手机号码
    public static boolean isMobile(String number) {
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(number);
        return  m.matches();
    }

}
