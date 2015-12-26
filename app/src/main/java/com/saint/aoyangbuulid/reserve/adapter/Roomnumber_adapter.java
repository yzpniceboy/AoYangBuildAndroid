package com.saint.aoyangbuulid.reserve.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;

import java.util.List;

/**
 * Created by zzh on 15-11-17.
 */
public class Roomnumber_adapter extends BaseAdapter {
    public List list;
    public Context context;
    public Roomnumber_adapter(Context context,List list){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.searchroom_layout,null);
            holder.mCheckBox= (CheckBox) convertView.findViewById(R.id.checkbox_room);
            holder.mTextView= (TextView) convertView.findViewById(R.id.text_room);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.mTextView.setText("");
        return convertView;
    }
    public class Holder{
        public TextView mTextView;
        public CheckBox mCheckBox;
    }
}
