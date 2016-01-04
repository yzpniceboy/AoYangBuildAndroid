package com.saint.aoyangbuulid.welcomeimage;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.MainActivity;
import com.saint.aoyangbuulid.R;

/**
 * Created by zzh on 15-12-17.
 */
public class Welcoming extends BaseActivity {
    private ImageView welcomeimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomeimage);
        welcomeimage= (ImageView) findViewById(R.id.welcomeimage);
        getSupportActionBar().hide();

//        welcome 动画
        AlphaAnimation animation=new AlphaAnimation(0.3f,1.0f);
        animation.setDuration(3000);
        welcomeimage.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                welcomeimage.setImageResource(R.mipmap.welcomepager);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent=new Intent(Welcoming.this, MainActivity.class);
                startActivity(intent);
                //进入动画和退出动画
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                Welcoming.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });






//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent=new Intent(Welcoming.this, Login_Activity.class);
//                startActivity(intent);
//            }
//        },3000);
    }
}
