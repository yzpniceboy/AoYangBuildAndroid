package com.saint.aoyangbuulid.mine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zzh on 15-11-24.
 */
public class XCRoundImageView extends ImageView {
    private Paint paint ;
    //在代码中使用
    public XCRoundImageView(Context context) {
        this(context,null);
    }
    //在布局中使用
    public XCRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XCRoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();

    } /**绘制圆形图片*/


    //绘制onDraw方法
    @Override
    protected void onDraw(Canvas canvas) {
    //获取资源
        Drawable drawable = getDrawable();
        if (null != drawable) {
            //使用BitmapDrawable类的getBitmap来获取位图
            /**
             *  Drawable d = XXX;

             BitmapDrawable  bd =(BitmapDrawable)d;

             Bitmap  b =bd.getBitmap();
             */
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = getCircleBitmap(bitmap, 14);//第二个参数为像素
            /**
             * left rectSrc
             * top rectDest*/
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            paint.reset();
            /**
             * canvas.drawBitmap(bitmao,left,top,paint)*/
            canvas.drawBitmap(b, rectSrc, rectDest, paint);

        } else {
            super.onDraw(canvas);
        }
    } /** * 获取圆形图片方法
     * @param bitmap
     * @param pixels
     * @return Bitmap
     * @author zzh */
    private Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        //给Paint加上抗锯齿标志
        paint.setAntiAlias(true);
       /**
        * 第一个参数； 透明 (0-255）
        * 第二个参数； 红色（0-255）
        * 第三个参数；绿色（0-255）
        * 第四个参数；蓝色（0-255）
        * */
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        int x = bitmap.getWidth();
        //画圆
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        //PorterDuff.Mode.SRC_IN 取两层绘制交集,显示上层,
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;


    }
}
