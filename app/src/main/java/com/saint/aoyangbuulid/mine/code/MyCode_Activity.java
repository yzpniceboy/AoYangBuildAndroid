package com.saint.aoyangbuulid.mine.code;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.login.Login_Activity;
import com.saint.aoyangbuulid.mine.encoding.EncodingHandler;

/**
 * Created by zzh on 15-12-17.
 */
public class MyCode_Activity extends BaseActivity {
    private ImageView image_code;
    private TextView text_name,text_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycode);
        SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        image_code= (ImageView) findViewById(R.id.image_code);
        text_name= (TextView) findViewById(R.id.code_text);
        text_phone= (TextView) findViewById(R.id.code_phone_text);
        //将id 生成二维码

        try {
//            Bitmap bitmap= EncodingHandler.createQRCode(id, 400, BitmapFactory.decodeResource(getResources(),
//                    Integer.parseInt(sp.getString("image_head", String.valueOf(R.mipmap.aoyang)))));
//            Bitmap bitmap=EncodingHandler.createQRCode(id,400, BitmapFactory.decodeResource(getResources(), R.mipmap.aoyang));
            Bitmap bitmap=EncodingHandler.createQRCode(id,400,null);
            image_code.setImageBitmap(bitmap);

            text_name.setText(sp.getString("nickname",""));
            text_phone.setText(sp.getString("phone",""));
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }
}
