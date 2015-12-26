package com.saint.aoyangbuulid.mine.adapter;

import android.content.Context;
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
 * Created by zzh on 15-12-22.
 */
public class MipcaAdapterCapture extends BaseAdapter  {
    public Context context;
    public List<Map<String,Object>> list;
    public  MipcaAdapterCapture(Context context,List list){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.listview_right,null);
            holder.view= (XCRoundImageView) convertView.findViewById(R.id.image_user);
            holder.text_company= (TextView) convertView.findViewById(R.id.text_right_phone);
            holder.text_name= (TextView) convertView.findViewById(R.id.textview_right_name);
            convertView.setTag(holder);
        }else  {
            holder= (Holder) convertView.getTag();
        }

//        holder.view.setImageBitmap();

        holder.view.setImageResource(R.mipmap.image);
        holder.text_company.setText(String.valueOf((list.get(position)).get("company")));
        holder.text_name.setText(String.valueOf((list.get(position)).get("name")));
        return convertView;
    }

    public class Holder{
        XCRoundImageView view;
        TextView text_name,text_company;

    }
}
