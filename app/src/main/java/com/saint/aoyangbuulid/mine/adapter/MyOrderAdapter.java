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
 * Created by zzh on 15-12-24.
 */
public class MyOrderAdapter extends BaseAdapter  {
    private List<Map<String,Object>> list;
    private Context context;
    public MyOrderAdapter(Context context,List list){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.older_layout,null);
            holder.text_rooms= (TextView) convertView.findViewById(R.id.rooms_name);
            holder.text_phone= (TextView) convertView.findViewById(R.id.phone);
            holder.text_people= (TextView) convertView.findViewById(R.id.people);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.text_phone.setText(String.valueOf((list.get(position)).get("phone")));
        holder.text_rooms.setText(String.valueOf((list.get(position)).get("name")));
        holder.text_people.setText(String.valueOf((list.get(position)).get("people")));
        return convertView;
    }
    private class Holder{
        TextView text_phone,text_people,text_rooms;

    }
}
