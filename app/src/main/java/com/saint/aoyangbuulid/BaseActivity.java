package com.saint.aoyangbuulid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by zzh on 15-11-17.
 */
public class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()==android.R.id.home){
//            overridePendingTransition(R.anim.push_left_in,
//                    R.anim.push_left_out);
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onBackPressed() {

    }
}
