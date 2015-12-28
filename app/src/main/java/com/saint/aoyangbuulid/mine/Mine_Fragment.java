package com.saint.aoyangbuulid.mine;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.login.Login_Activity;
import com.saint.aoyangbuulid.mine.code.Dispaly_data_activity;
import com.saint.aoyangbuulid.mine.code.MipcaActivityCapture;
import com.saint.aoyangbuulid.mine.code.MyCode_Activity;
import com.saint.aoyangbuulid.mine.utils.MyBill_Activity;
import com.saint.aoyangbuulid.mine.utils.MyOrder_Activity;
import com.saint.aoyangbuulid.mine.utils.QueryTwo_activity;
import com.saint.aoyangbuulid.mine.utils.Query_activity;
import com.saint.aoyangbuulid.mine.utils.Select_Company_Activity;
import com.saint.aoyangbuulid.mine.utils.Setting_Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 志浩 on 2015/11/2.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Mine_Fragment extends Fragment implements View.OnClickListener {
    private static final int RESULT_OK =-1 ;
    public ImageButton imageButton_setmine,button_query,button_user;
    public TextView textView_loginmine,text_company,text_scan,text_my_user,text_account_balance;
    public ImageView image_round,image_scan,image_mycode,image_right_scan,image_mymoney,image_order;
    public Bitmap head;//头像Bitmap
    private static String path="/sdcard/myHead/";//sd路径
    public AlertDialog.Builder dialog;
    public SharedPreferences sp;
    public String text=null;private final static int SCANNIN_GREQUEST_CODE = 5;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.mine_layout,container,false);


        //实例化控件
        button_user= (ImageButton) view.findViewById(R.id.button_user);
        text_my_user= (TextView) view.findViewById(R.id.text_my_user);
        text_account_balance= (TextView) view.findViewById(R.id.account_balance);
        image_order= (ImageView) view.findViewById(R.id.image_order);
        image_mymoney= (ImageView) view.findViewById(R.id.image_mymoney);
        image_right_scan= (ImageView) view.findViewById(R.id.image_right_scan);
        text_scan= (TextView) view.findViewById(R.id.text_scan);
        image_scan= (ImageView) view.findViewById(R.id.image_scan);
        image_mycode= (ImageView) view.findViewById(R.id.image_mycode);
        text_company= (TextView) view.findViewById(R.id.text_company_name);



        sp=getActivity().getSharedPreferences("image",Context.MODE_PRIVATE);
        textView_loginmine= (TextView) view.findViewById(R.id.textview_loginmine);
        imageButton_setmine= (ImageButton) view.findViewById(R.id.imagebutton_setmine);
        image_round= (ImageView) view.findViewById(R.id.imageview_round);
        button_query= (ImageButton) view.findViewById(R.id.image_query);
        //设置监听
        image_mymoney.setOnClickListener(this);
        imageButton_setmine.setOnClickListener(this);
        textView_loginmine.setOnClickListener(this);
        image_round.setOnClickListener(this);
        text_company.setOnClickListener(this);
        button_query.setOnClickListener(this);
        image_mycode.setOnClickListener(this);
        image_order.setOnClickListener(this);


        sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);

        if (sp.getString("roles","").equals("[\"guard\"]")){
            image_scan.setVisibility(View.VISIBLE);
            text_scan.setVisibility(View.VISIBLE);
            image_right_scan.setVisibility(View.VISIBLE);
//            image_mymoney.setClickable(false);
//            image_order.setClickable(false);
            image_scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MipcaActivityCapture.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                }
            });
        }
        sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
        text_company.setText(sp.getString("company_name","创建/加入公司"));



        /**nickname 异常 不知道是不是数据为空的问题 还是sp 没有存储
         *         获取昵称
         */
        sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
        textView_loginmine.setText(sp.getString("phone",""));

        try {
            FileInputStream fis=new FileInputStream(path+ "head.jpg");
            Bitmap bt = BitmapFactory.decodeStream(fis);//从Sd中找头像，转换成Bitmap
            if(bt!=null){
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bt);//转换成drawable
                image_round.setImageDrawable(drawable);
            }else{
/**
 * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
 *
 */
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return view;

}
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_mymoney:
                if (sp.getString("roles","").equals("[\"guard\"]")){
                    dialog();
                }else {
                    Intent intent=new Intent();
                    intent.setClass(getActivity(),MyBill_Activity.class);
                    startActivity(intent);
                }


                //我的账户
                break;
            case R.id.image_order:
                if (sp.getString("roles","").equals("[\"guard\"]")){
                    dialog();
                }else {
                    Intent intent=new Intent();
                    intent.setClass(getActivity(),MyOrder_Activity.class);
                    startActivity(intent);
                }

                break;
            case R.id.imagebutton_setmine:
                SetDialog();
                break;
            case R.id.imageview_round:
                showDialog();
                break;
            case R.id.text_company_name:
                sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
                text=sp.getString("roles", "");
                if (text.equals("[\"contributor\"]")){
                    text_company.setClickable(false);
            }else {
                    text_company.setClickable(true);
                    Intent intent=new Intent();
                    intent.setClass(getActivity(),Select_Company_Activity.class);
                    startActivity(intent);
                }

                break;
            case R.id.image_query:
                sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
                text=sp.getString("roles", "");
                String roles="[\"contributor\"]";
                if (text.equals(roles)){
                    Intent intent=new Intent(getActivity(),Query_activity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(getActivity(),QueryTwo_activity.class);
                    startActivity(intent);
                }

                break;

            case R.id.image_mycode:
                SharedPreferences sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME, Login_Activity.Mode);
                Intent intent=new Intent(getActivity(),MyCode_Activity.class);
                intent.putExtra("id",sp.getString("user_id",""));
                startActivity(intent);
                break;
        }
    }
    public void showDialog(){

        String []items={"拍照","从本地获取"};
         dialog=new AlertDialog.Builder(getActivity())
                .setTitle("更改头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                        "head.jpg")));
                                startActivityForResult(intent, 2);//采用ForResult打开
                                break;
                            case 1:
                                 intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent, 1);
                                break;
                        }
                    }
                });
        dialog.create().show();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));//裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");


                    if(head!=null){
/**
 * 上传服务器代码
 */
                        setPicToView(head);//保存在SD卡中
                        image_round.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String result=bundle.getString("result");
                    Intent intent=new Intent(getActivity(), Dispaly_data_activity.class);
                    intent.putExtra("key_data", result);
                    startActivity(intent);

                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 调用系统的裁剪
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
// aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
// outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }


    //保存到SD卡
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }

        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName =path + "head.jpg";//图片名字
        try {
            b = new FileOutputStream(fileName);
            /**
             * 图片压缩*/
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, b);// 把数据写入文件 50是压缩率表示压缩（100-50）%
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
//关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
         public  void  SetDialog(){
             final LayoutInflater inflater=LayoutInflater.from(getActivity());
             View view=inflater.inflate(R.layout.settingchange, null);
             TextView text_change= (TextView) view.findViewById(R.id.userinfo);
             TextView text_exit= (TextView) view.findViewById(R.id.exit);

             AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT)
                     .setTitle("设置")
                     .setView(view);

             text_change.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setClass(getActivity(),Setting_Activity.class);
                        startActivity(intent);
                 }
             });
             text_exit.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent=new Intent();
                     intent.setClass(getActivity(), Login_Activity.class);
                     SharedPreferences sp=getActivity().getSharedPreferences(Login_Activity.PREFERENCE_NAME,Login_Activity.Mode);
                     SharedPreferences.Editor editor=sp.edit();
                     editor.clear();
                     editor.commit();
                     startActivity(intent);
                 }
             });
             dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                 }
             });
            dialog.create().show();
         }
    public void data(){
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View view_data=inflater.inflate(R.layout.myview_layout,null);

        final AlertDialog.Builder data=new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("信息")
                .setView(view_data);
        data.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        data.create().show();
    }
    private void dialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("提示")
                .setMessage("您当前用户权限不够!!!!!!");
        dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

}
