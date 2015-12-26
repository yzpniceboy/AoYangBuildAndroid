package com.saint.aoyangbuulid.article.news.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.mine.utils.XCRoundImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by zzh on 15-12-5.
 */
public class CommentAdapter extends BaseAdapter {
    public Context context;
    public List<Map<String,Object>> list;
    public CommentAdapter(Context context,List list){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.new_comment_dis_layout,null);
            holder.header= (XCRoundImageView) convertView.findViewById(R.id.user_image);
            holder.text_content= (TextView) convertView.findViewById(R.id.text_content);
            holder.text_date= (TextView) convertView.findViewById(R.id.comment_date);
            holder.text_header= (TextView) convertView.findViewById(R.id.text_user);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.header.setImageResource(R.mipmap.image);
        holder.text_date.setText(String.valueOf((list.get(position)).get("date")));
        holder.text_header.setText(String.valueOf((list.get(position)).get("auther")));
        Spanned content=Html.fromHtml(String.valueOf((list.get(position)).get("content")));
        holder.text_content.setText(content);
        return convertView;
    }
    public class Holder{
        XCRoundImageView header;
        TextView text_date,text_content,text_header;

    }
}
