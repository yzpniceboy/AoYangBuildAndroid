package com.saint.aoyangbuulid.mine.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.charge.ChargeSubmit_Activity;

/**
 * Created by zzh on 15-12-28.
 */
public class Post_Coment_Activity extends BaseActivity{
    private TextView text_title,text_date,text_content,text_amount;
    String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_content_main);


        text_amount= (TextView) findViewById(R.id.text_money);
        text_content= (TextView) findViewById(R.id.text_content);
        text_title= (TextView) findViewById(R.id.text_title);
        text_date= (TextView) findViewById(R.id.text_time);

        Intent intent=getIntent();
        String title=intent.getStringExtra("key_title");
        String date=intent.getStringExtra("key_date");
        amount=intent.getStringExtra("key_amount");
        String content=intent.getStringExtra("key_content");

        text_content.setText(content);
        text_date.setText(date);
        text_title.setText(title);
        text_amount.setText(amount+"元");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Post_Coment_Activity.this.finish();
                break;
            case R.id.post:
                Intent intent=new Intent(Post_Coment_Activity.this, ChargeSubmit_Activity.class);
                intent.putExtra("amount",amount);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
