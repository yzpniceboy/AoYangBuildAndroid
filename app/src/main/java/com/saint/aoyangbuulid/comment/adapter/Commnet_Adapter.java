package com.saint.aoyangbuulid.comment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;

import java.util.List;

/**
 * Created by zzh on 15-11-5.
 */
public class Commnet_Adapter extends BaseAdapter {
    public Context context;
    public List list;
    public Commnet_Adapter(Context context,List list){
        this.context=context;
        this.list=list;
    }
    final int VIEW_TYPE=2;
    final int VIEW_1=0;
    final int VIEW_2=1;

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return VIEW_1;
        }else
        return VIEW_2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type=getItemViewType(position);
        Holder holder=null;
        if (convertView==null){
            switch (type){
                case VIEW_1:
                    holder=new Holder();
                    convertView= LayoutInflater.from(context).inflate(R.layout.comment_listone,null);
                    holder.textviewone= (TextView) convertView.findViewById(R.id.commnet_textone);
                    convertView.setTag(holder);
                    break;
                case VIEW_2:
                    holder=new Holder();
                    convertView=LayoutInflater.from(context).inflate(R.layout.comment_listtwo,null);
                    holder.imageView_people= (ImageView) convertView.findViewById(R.id.imageview_people);
                    holder.imageButton_left= (ImageButton) convertView.findViewById(R.id.imagebutton_left);
                    holder.imageButton_right= (ImageButton) convertView.findViewById(R.id.imagebutton_right);
                    holder.textView_name= (TextView) convertView.findViewById(R.id.textview_name);
                    holder.textView_time= (TextView) convertView.findViewById(R.id.textview_time);
                    holder.textView_number_left= (TextView) convertView.findViewById(R.id.comment_text_number_left);
                    holder.textView_number_right= (TextView) convertView.findViewById(R.id.commnet_text_number_right);
                    holder.text_comment= (TextView) convertView.findViewById(R.id.text_nr);
                    convertView.setTag(holder);
                    break;
            }
        }else{
        holder=(Holder)convertView.getTag();
        }
        switch (type){
            case VIEW_1:
                holder.textviewone.setText("    是不是很好玩啊一起来玩吧你个傻逼哈哈啊哈哈哈啊哈哈哈阿哈哈啊哈哈哈哈哈");
                break;
            case VIEW_2:
                holder.imageView_people.setImageResource(R.mipmap.image);
                holder.imageButton_left.setImageResource(R.mipmap.comment_icon_not);
                final Holder finalHolder = holder;
                holder.imageButton_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalHolder.imageButton_left.setImageResource(R.mipmap.comment_icon);
                    }
                });
                holder.imageButton_right.setImageResource(R.mipmap.point_like_icon_not);
                holder.imageButton_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalHolder.imageButton_right.setImageResource(R.mipmap.point_like_icon);


                    }
                });
                holder.textView_name.setText("usename");
                holder.textView_time.setText("12分钟前");
                holder.textView_number_left.setText("63");
                holder.textView_number_right.setText("33");
                break;
        }
        return convertView;
    }



    public class Holder{
        public TextView textviewone,text_comment,textView_name
                ,textView_time,textView_number_left
                ,textView_number_right;
        public ImageView imageView_people;
        public ImageButton imageButton_left,imageButton_right;
    }

}
