package com.saint.aoyangbuulid.article;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.article.news.News_Fragment;
import com.saint.aoyangbuulid.article.notice.Notice_Fragment;

/**
 * Created by 志浩 on 2015/11/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Article_Fragment extends Fragment implements View.OnClickListener{
    public ImageButton imageButton_news,imageButton_notice;
    public TextView textView_news,textView_notice;
    private boolean flag=true;
    public Notice_Fragment notice;
    public News_Fragment news;

    //片段管理器
    public FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.frame_article_layout, container, false);

        //实例化
        imageButton_news= (ImageButton) view.findViewById(R.id.image_news);
        imageButton_notice=(ImageButton)view.findViewById(R.id.image_notice);
        textView_news= (TextView) view.findViewById(R.id.textview_news);
        textView_notice= (TextView) view.findViewById(R.id.textview_notice);
        //监听
        textView_news.setOnClickListener(this);
        textView_notice.setOnClickListener(this);
        if (savedInstanceState!=null){
            notice= (Notice_Fragment) fragmentManager.findFragmentByTag("notice");
            news= (News_Fragment) fragmentManager.findFragmentByTag("news");
        }

        setTabSelection(0);
//
        return view;
    }
    private void changeSwitchButton(boolean flag){
        if (flag){
            imageButton_news.setVisibility(View.VISIBLE);
            textView_news.setTextColor(Color.parseColor("#56123230"));
            imageButton_notice.setVisibility(View.INVISIBLE);
            textView_notice.setTextColor(Color.WHITE);
        }else {
            imageButton_news.setVisibility(View.INVISIBLE);
            textView_news.setTextColor(Color.WHITE);
            imageButton_notice.setVisibility(View.VISIBLE);
            textView_notice.setTextColor(Color.parseColor("#56123230"));
        }
    }
//
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textview_news:
                setTabSelection(0);
                break;
            case R.id.textview_notice:
                setTabSelection(1);
                break;
        }
    }
    public void setTabSelection(int index){
        Clean();
        fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        //隐藏所有的fragment:
        hidFragment(transaction);
        switch (index){
            case 0:
                flag = true;
                changeSwitchButton(flag);
                if (news==null){
                    news= new News_Fragment();
                    transaction.add(R.id.fl_article,news);
                }else transaction.show(news);
                break;
            case 1:
                flag = false;
                changeSwitchButton(flag);
                if (notice==null){
                    notice= new Notice_Fragment();
                    transaction.add(R.id.fl_article,notice);
                }else transaction.show(notice);
                break;
        }
        transaction.commit();
    }
    public void hidFragment(FragmentTransaction transaction){
        if (news!=null){
            transaction.hide(news);
        }
        if (notice!=null){
            transaction.hide(notice);
        }

    }
    public void Clean(){
       imageButton_news.setVisibility(View.INVISIBLE);
        imageButton_notice.setVisibility(View.INVISIBLE);

        textView_news.setTextColor(Color.WHITE);
        textView_notice.setTextColor(Color.WHITE);


    }
}
