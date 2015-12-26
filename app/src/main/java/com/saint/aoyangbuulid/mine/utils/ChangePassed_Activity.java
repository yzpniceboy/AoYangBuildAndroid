package com.saint.aoyangbuulid.mine.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.saint.aoyangbuulid.R;

import com.saint.aoyangbuulid.BaseActivity;

/**
 * Created by zzh on 15-11-27.
 */
public class ChangePassed_Activity extends BaseActivity {
    public EditText pass,passed;
    public String p,pa;
    public ImageButton button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassed_layout);
        pass= (EditText) findViewById(R.id.et_passed);
        passed= (EditText) findViewById(R.id.et_passedagain);
        button= (ImageButton) findViewById(R.id.button_sure);
        button.setEnabled(false);

        p=pass.getText().toString();
        pa=passed.getText().toString();
        if (pa.equals(p)) {
            button.setEnabled(true);
        }else {
            Toast.makeText(ChangePassed_Activity.this,"两次输入不一致",Toast.LENGTH_SHORT).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.putExtra("passed",pass.getText().toString());
                intent.setClass(ChangePassed_Activity.this,Setting_Activity .class);
                startActivity(intent);


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            ChangePassed_Activity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
