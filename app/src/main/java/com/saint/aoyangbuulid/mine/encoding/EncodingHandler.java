package com.saint.aoyangbuulid.mine.encoding;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by zzh on 15-12-17.
 */
public final class EncodingHandler {
    private static final int BLACK = 0xff000000;

    //
    public static Bitmap createQRCode(String str, int widthAndHeight, Bitmap logo) throws WriterException {
//        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        BitMatrix matrix = new MultiFormatWriter().encode(str,
//                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
//        int width = matrix.getWidth();
//        int height = matrix.getHeight();
//        int[] pixels = new int[width * height];
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if (matrix.get(x, y)) {
//                    pixels[y * width + x] = BLACK;
//                }
//            }
//        }
//        bitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        return bitmap;
//    }
//    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
//        if (src == null) {
//            return null;
//        }
//
//        if (logo == null) {
//            return src;
//        }
//
//        //获取图片的宽高
//        int srcWidth = src.getWidth();
//        int srcHeight = src.getHeight();
//        int logoWidth = logo.getWidth();
//        int logoHeight = logo.getHeight();
//
//        if (srcWidth == 0 || srcHeight == 0) {
//            return null;
//        }
//
//        if (logoWidth == 0 || logoHeight == 0) {
//            return src;
//        }
//
//        //logo大小为二维码整体大小的1/5
//        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
//        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
//        try {
//            Canvas canvas = new Canvas(bitmap);
//            canvas.drawBitmap(src, 0, 0, null);
//            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
//            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
//
//            canvas.save(Canvas.ALL_SAVE_FLAG);
//            canvas.restore();
//        } catch (Exception e) {
//            bitmap = null;
//            e.getStackTrace();
//        }
//
//        return bitmap;
//    }

        try {
            Bitmap scaleLogo = getScaleLogo(logo,widthAndHeight,widthAndHeight);
            int offsetX = 0;
            int offsetY = 0;
            if(scaleLogo != null){
                offsetX = (widthAndHeight - scaleLogo.getWidth())/2;
                offsetY = (widthAndHeight - scaleLogo.getHeight())/2;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(str,
                    BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
            int[] pixels = new int[widthAndHeight * widthAndHeight];
            for (int y = 0; y < widthAndHeight; y++) {
                for (int x = 0; x < widthAndHeight; x++) {
                    //判断是否在logo图片中
                    if(offsetX != 0 && offsetY != 0 && x >= offsetX && x < offsetX+scaleLogo.getWidth() && y>= offsetY && y < offsetY+scaleLogo.getHeight()){
                        int pixel = scaleLogo.getPixel(x-offsetX,y-offsetY);
                        //如果logo像素是透明则写入二维码信息
                        if(pixel == 0){
                            if(bitMatrix.get(x, y)){
                                pixel = 0xff000000;
                            }else{
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * widthAndHeight + x] = pixel;

                    }else{
                        if (bitMatrix.get(x, y)) {
                            pixels[y * widthAndHeight + x] = 0xff000000;
                        } else {
                            pixels[y * widthAndHeight + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(widthAndHeight, widthAndHeight,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthAndHeight, 0, 0, widthAndHeight, widthAndHeight);
            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缩放logo到二维码的1/5
     * @param logo
     * @param w
     * @param h
     * @return
     */
    private static Bitmap getScaleLogo(Bitmap logo,int w,int h){
        if(logo == null){
            return null;
        }

        android.graphics.Matrix matrix = new android.graphics.Matrix();
        float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 / logo.getHeight());
        matrix.postScale(scaleFactor,scaleFactor);

        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
        return result;
    }


}
