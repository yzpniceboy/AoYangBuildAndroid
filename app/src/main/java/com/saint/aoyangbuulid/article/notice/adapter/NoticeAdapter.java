package com.saint.aoyangbuulid.article.notice.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;

import java.util.List;
import java.util.Map;

/**
 * Created by zzh on 15-12-4.
 */
public class NoticeAdapter extends BaseAdapter {
    public List<Map<String,Object>> list;
    public Context context;
    public NoticeAdapter(Context context,List list){
        this.list=list;
        this.context=context;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.notice_list_comment_layout, null);
            holder.text_company= (TextView) convertView.findViewById(R.id.text_nocompanyname);
            holder.text_content= (TextView) convertView.findViewById(R.id.text_nocontent);
            holder.text_date= (TextView) convertView.findViewById(R.id.text_nodate);
            holder.text_title= (TextView) convertView.findViewById(R.id.text_notitle);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.Content=String.valueOf((list.get(position)).get("content"));
        holder.text_company.setText("澳杨集团");
        holder.new_id= (String) list.get(position).get("new_id");
        holder.text_title.setText(String.valueOf((list.get(position)).get("title")));
        holder.text_date.setText(String.valueOf((list.get(position)).get("date")));
        Spanned data= Html.fromHtml(String.valueOf((list.get(position)).get("excerpt")));
        holder.text_content.setText(data);
        return convertView;
    }
    private  class Holder{
        String Content,new_id;
        TextView text_title,text_date,text_company,text_content;
    }
}
