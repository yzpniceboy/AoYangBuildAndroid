package com.saint.aoyangbuulid.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;

import java.util.List;
import java.util.Map;

/**
 * Created by zzh on 15-12-27.
 */
public class PostAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> list;
    public  PostAdapter(Context context,List list){
        this.context=context;
        this.list=list;
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
        Holder holder=null;
        if (convertView==null){
            holder=new Holder();
            convertView= LayoutInflater.from(context).inflate(R.layout.post_main,null);
            holder.text_title= (TextView) convertView.findViewById(R.id.text_title);
            holder.text_date= (TextView) convertView.findViewById(R.id.text_time);
            holder.text_amount= (TextView) convertView.findViewById(R.id.text_money);
            holder.text_content= (TextView) convertView.findViewById(R.id.text_content);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.text_id=String.valueOf(list.get(position).get("id"));
        holder.text_title.setText(String.valueOf(list.get(position).get("title")));
        holder.text_amount.setText(String.valueOf(list.get(position).get("amount"))+"元");
        holder.text_date.setText(String.valueOf(list.get(position).get("date")));
        holder.text_content.setText(String.valueOf(list.get(position).get("content")));
        return convertView;
    }
    public class Holder{
        TextView text_amount;
        TextView text_title;
        TextView text_content;
        TextView text_date;
        String text_id;
    }
}
