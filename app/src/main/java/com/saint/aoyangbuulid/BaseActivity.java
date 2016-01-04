package com.saint.aoyangbuulid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by zzh on 15-11-17.
 */
public class BaseActivity extends ActionBarActivity {
    private  static final  String EXITACTION="action.exit";
    private  ExitReceiver exitReceiver=new ExitReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IntentFilter filter=new IntentFilter();
        filter.addAction(EXITACTION);
        registerReceiver(exitReceiver,filter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(exitReceiver);
    }

    @Override
    public void onBackPressed() {

    }
    private class ExitReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            BaseActivity.this.finish();
        }
    }
}
