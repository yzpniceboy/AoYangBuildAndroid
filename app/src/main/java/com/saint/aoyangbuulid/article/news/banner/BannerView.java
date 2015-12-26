package com.saint.aoyangbuulid.article.news.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.saint.aoyangbuulid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zzh on 15-12-2.
 */
public class BannerView extends LinearLayout {
    //默认图片缓存路径
    private String imageCachePath = "imageloader/Cache";

    private Context context;
    private ViewPager viewPager;
    private List<ImageView> imageViews;//图片集合
    private List<View> dots; //点集合
    private TextView tv_title;
    private int currentItem = 0; // 当前图片的索引
    private LinearLayout dotWraper;
    private ScheduledExecutorService scheduledExecutorService;
    //自动播放
    private boolean isAUTO_PALY=true;

    //异步加载图片
    private ImageLoader imgLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    //数据源
    private List<BannerData> data;
    //更新主UI
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);
        }
    };

    private ClickCallback callback;

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.bannerview_layout, this, true);

        if (isAUTO_PALY){
            start();
        }

    }

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.bg)
                .showImageForEmptyUri(R.drawable.bg)
                .showImageOnFail(R.drawable.bg)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();

    }
    private void initControl() {
        initImageLoader();
        imageViews = new ArrayList<ImageView>();
        tv_title = (TextView) findViewById(R.id.tv_title);
        viewPager = (ViewPager) findViewById(R.id.vp);
    }

    private void addDynamicView() {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
        if (null == data || data.size() < 1)
            return;
        for (int i = 0; i < data.size(); i++) {
            ImageView imageView = new ImageView(context);
            // 异步加载图片
            imgLoader.displayImage(data.get(i).getImage(), imageView,
                    options);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
            dots.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void start() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
                TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable {
        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            if (null == data || data.size() < 1)
                return;
            BannerData ad = data.get(position);
            tv_title.setText(ad.getTitle());
            dots.get(oldPosition).setBackgroundResource(R.mipmap.dot_blur);
            dots.get(position).setBackgroundResource(R.mipmap.dot_focus);
//            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
//            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = imageViews.get(position);
            ((ViewPager) container).addView(iv);
            final BannerData dataItem = data.get(position);
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.perform(dataItem.getId(), position);
                    }
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    //启动
    public void startup(List<BannerData> adList) {
        initControl();
        this.data = adList;
        dotWraper = (LinearLayout) findViewById(R.id.dot_wraper);
        dots = new ArrayList<View>();
        for (int i = 0; i < adList.size(); i++) {
            View dot = LayoutInflater.from(context).inflate(R.layout.banner_dot, this, false);
            dots.add(dot);//将圆点加入LIST<VIEW>
            dotWraper.addView(dot);
        }
        addDynamicView();
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        //start();
    }

    //停止此控件的轮播
    public void shutdown() {
        if (scheduledExecutorService != null)
            scheduledExecutorService.shutdown();
    }

    public interface ClickCallback {
        public void perform(int id, int position);
    }

    public String getImageCachePath() {
        return imageCachePath;
    }

    public void setImageCachePath(String imageCachePath) {
        this.imageCachePath = imageCachePath;
    }

    public void setCallback(ClickCallback callback) {
        this.callback = callback;
    }
}
