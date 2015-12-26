package com.saint.aoyangbuulid.mypulldownlistview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.saint.aoyangbuulid.R;

public class MainActivity extends Activity {
    final String TAG = "MainActivity";
    static String[] adapterData = new String[] { "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z" };
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pulldownlistview_layout);
        mContext = this;
        final com.saint.aoyangbuulid.mypulldownlistview.PullDownListView pullDownListView = (com.saint.aoyangbuulid.mypulldownlistview.PullDownListView) this
                .findViewById(R.id.list_right);
        final com.saint.aoyangbuulid.mypulldownlistview.YProgressView progressView = (com.saint.aoyangbuulid.mypulldownlistview.YProgressView) this
                .findViewById(R.id.progressView);
        final EyeView eyeView = (EyeView) this.findViewById(R.id.eyeView);

        pullDownListView.getListView().setAdapter(mAdapter);

        pullDownListView.setOnPullHeightChangeListener(new com.saint.aoyangbuulid.mypulldownlistview.PullDownListView.OnPullHeightChangeListener(){

            @Override
            public void onTopHeightChange(int headerHeight,
                                          int pullHeight) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this,"下拉",Toast.LENGTH_SHORT).show();
                float progress = (float) pullHeight
                        / (float) headerHeight;

                if(progress<0.5){
                    progress = 0.0f;
                }else{
                    progress = (progress-0.5f)/0.5f;
                }


                if (progress > 1.0f) {
                    progress = 1.0f;
                }

                if (!pullDownListView.isRefreshing()) {
                    eyeView.setProgress(progress);
                }
            }

            @Override
            public void onBottomHeightChange(int footerHeight,
                                             int pullHeight) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this,"上拉",Toast.LENGTH_SHORT).show();

                float progress = (float) pullHeight
                        / (float) footerHeight;

                if(progress<0.5){
                    progress = 0.0f;
                }else{
                    progress = (progress-0.5f)/0.5f;
                }

                if (progress > 1.0f) {
                    progress = 1.0f;
                }

                if (!pullDownListView.isRefreshing()) {
                    progressView.setProgress(progress);
                }

            }

            @Override
            public void onRefreshing(final boolean isTop) {
                // TODO Auto-generated method stub
                if (isTop) {
                    eyeView.startAnimate();
                } else {
                    progressView.startAnimate();
                }

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pullDownListView.pullUp();
                        if (isTop) {
                            eyeView.stopAnimate();
                        } else {
                            progressView.stopAnimate();
                        }
                    }

                }, 3000);
            }

        });

        pullDownListView.getListView().setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        // TODO Auto-generated method stub

                    }

                });

    }

    private BaseAdapter mAdapter = new BaseAdapter(){

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return adapterData.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return adapterData[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView textView = new TextView(mContext);
            textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,dp2px(mContext,50)));
            textView.setText(adapterData[position]);
            textView.setTextSize(20);
            textView.setTextColor(0xff000000);
            textView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            textView.setPadding(50, 0, 0, 0);

            return textView;
        }

    };

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}

