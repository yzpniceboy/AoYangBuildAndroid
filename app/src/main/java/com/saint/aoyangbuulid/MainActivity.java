package com.saint.aoyangbuulid;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saint.aoyangbuulid.article.Article_Fragment;
import com.saint.aoyangbuulid.contact.Contact_Fragment;
import com.saint.aoyangbuulid.login.Login_Activity;
import com.saint.aoyangbuulid.mine.Mine_Fragment;
import com.saint.aoyangbuulid.reserve.Reserve_Fragment;

import java.util.Timer;
import java.util.TimerTask;
//对底部进行操作
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends FragmentActivity implements View.OnClickListener {

//    private static final String EXITACTION = "action.exit";
//    private ExitReceiver exitReceiver = new ExitReceiver();
    private RelativeLayout relative_artice,relative_reserve
            ,relative_mine,relative_contact;

    private ImageButton imageartice_button,imagereserve_button
            ,imagemine_button,imagecontact_button;

    //文章界面
    public Article_Fragment article;
    public Mine_Fragment mine;
    public Contact_Fragment contact;
    public Reserve_Fragment reserve;
//    public Notice_Fragment notice;
    private  boolean isExit=false;
    private  TimerTask task=null;
    private  Timer timer=null;
    //片段管理器
    public FragmentManager fragmentManager=getFragmentManager();
    public TextView textView_mine,textView_article,textView_contact,textView_reserve;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(EXITACTION);
//        registerReceiver(exitReceiver, filter);
        initButton();
        //默认
        setTabSelection(0);

/**
 * 消除fragment的重叠的方法:
 * 先去判断savedInstanceState是否为null，如果不为null,则表示里面有保存这四个fragment。
 * 则不再重新去add这四个fragment，而是通过Tag从前保存的数据中直接去读取。
 * 另外一个思路是，直接在包含Fragment的Activity中复写：
 *     public void onSaveInstanceState(Bundle outState) {
 //    TODO Auto-generated method stub
 //    Log.v("LH", "onSaveInstanceState"+outState);
 //    super.onSaveInstanceState(outState);   //将这一行注释掉，阻止activity保存fragment的状态
// }*/
        if (savedInstanceState!=null){
            article= (Article_Fragment) fragmentManager.findFragmentByTag("article");
            mine= (Mine_Fragment) fragmentManager.findFragmentByTag("mine");
            contact= (Contact_Fragment) fragmentManager.findFragmentByTag("contact");
            reserve= (Reserve_Fragment) fragmentManager.findFragmentByTag("reserve");
        }
        //可能要将它注释掉


    }
    public void initButton(){

        relative_artice= (RelativeLayout) findViewById(R.id.relative_article);
        relative_reserve= (RelativeLayout) findViewById(R.id.relative_reserve);
        relative_mine= (RelativeLayout) findViewById(R.id.relative_mine);
        relative_contact= (RelativeLayout) findViewById(R.id.relative_contact);

        imageartice_button= (ImageButton) findViewById(R.id.imageartice_button);
        imagereserve_button= (ImageButton) findViewById(R.id.imagereserve_button);
        imagemine_button= (ImageButton) findViewById(R.id.imagemine_button);
        imagecontact_button= (ImageButton) findViewById(R.id.imagecontact_button);

        textView_article= (TextView) findViewById(R.id.textview_article);
        textView_reserve= (TextView) findViewById(R.id.textview_reserve);
        textView_mine= (TextView) findViewById(R.id.textview_mine);
        textView_contact= (TextView) findViewById(R.id.textview_contact);

        relative_artice.setOnClickListener(this);
        relative_reserve.setOnClickListener(this);
        relative_mine.setOnClickListener(this);
        relative_contact.setOnClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_article:
                setTabSelection(0);
                break;
            case R.id.relative_reserve:
                setTabSelection(1);
                break;
            case R.id.relative_mine:
                setTabSelection(2);
                break;
            case R.id.relative_contact:
                setTabSelection(3);
                break;
        }
    }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void setTabSelection(int index){
            Clean();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            //隐藏所有的fragment:
            hidFragment(transaction);
            switch (index){
                case 0:
                    imageartice_button.setImageResource(R.mipmap.home_icon);
                    textView_article.setTextColor(Color.parseColor("#0099cc"));
                    if (article==null){
                        article=new Article_Fragment();
                        transaction.add(R.id.framelayout,article,"article");
                    }else transaction.show(article);
                    break;
                case 1:
                    imagereserve_button.setImageResource(R.mipmap.reservation_icon);
                    textView_reserve.setTextColor(Color.parseColor("#0099cc"));
                    if (reserve==null){
                        reserve=new Reserve_Fragment();
                        transaction.add(R.id.framelayout,reserve,"reserve");
                    }else transaction.show(reserve);
                    break;
                case 2:
                    imagemine_button.setImageResource(R.mipmap.user_icon);
                    textView_mine.setTextColor(Color.parseColor("#0099cc"));
                    if (mine==null){
                        mine=new Mine_Fragment();
                        transaction.add(R.id.framelayout,mine,"mine");
                    }else
                    transaction.show(mine);

                    break;
                case 3:
                    imagecontact_button.setImageResource(R.mipmap.contact_icon);
                    textView_contact.setTextColor(Color.parseColor("#0099cc"));
                    SharedPreferences sp=getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
                        if (contact==null){
                            contact=new Contact_Fragment();
                            transaction.add(R.id.framelayout,contact,"contact");
                        }else{
                            transaction.show(contact);
                        }

                    break;
            }
            transaction.commit();
        }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void hidFragment(FragmentTransaction transaction){
        if (article!=null){
            transaction.hide(article);
        }
        if (contact!=null){
            transaction.hide(contact);
        }
        if (mine!=null){
            transaction.hide(mine);
        }
        if (reserve!=null){
            transaction.hide(reserve);
        }
    }
        public void Clean(){
            imageartice_button.setImageResource(R.mipmap.home_icon_not);
            imagereserve_button.setImageResource(R.mipmap.reservation_icon_not);
            imagemine_button.setImageResource(R.mipmap.user_icon_not);
            imagecontact_button.setImageResource(R.mipmap.contact_icon_not);
            textView_contact.setTextColor(Color.parseColor("#82858b"));
            textView_mine.setTextColor(Color.parseColor("#82858b"));
            textView_reserve.setTextColor(Color.parseColor("#82858b"));
            textView_article.setTextColor(Color.parseColor("#82858b"));

        }
    /***
     *actvity 与 fragment 联动
     */

   /**
    * 当你按下home 没有销毁activity 时 调用此方法*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e("MainActivity:"+"========================>","SaveInstanceState");
    }

    @Override
    public void onBackPressed() {
        if (isExit){
            MainActivity.this.finish();
        }else{
            isExit=true;
            Toast.makeText(MainActivity.this, "再按一次退出智慧园区", Toast.LENGTH_SHORT)
                    .show();
             task=new TimerTask() {
                @Override
                public void run() {
                    isExit=false;
                }
            };
            timer=new Timer();
            timer.schedule(task,2000);
        }
    }


}
