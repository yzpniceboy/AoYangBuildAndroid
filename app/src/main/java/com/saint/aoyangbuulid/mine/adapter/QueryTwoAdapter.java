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
 * Created by zzh on 15-12-12.
 */
public class QueryTwoAdapter extends BaseAdapter {
    public Context context;
    public List<Map<String,Object>> list;
    public QueryTwoAdapter(Context context,List list){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.querytwo,null);
            holder.text_name= (TextView) convertView.findViewById(R.id.text_commonname);
            holder.text_roles= (TextView) convertView.findViewById(R.id.text_roles);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();

        }

        holder.text_name.setText(String.valueOf((list.get(position)).get("company_name")));
        holder.approved= (String) list.get(position).get("approved");
        if (holder.approved.equals("0")){
            holder.text_roles.setText("审核中...");
        }else {
            if (holder.approved.equals("1")){
                holder.text_roles.setText("通过");
            }else {
                holder.text_roles.setText("未通过");
            }
        }
        return convertView;
    }
    public class Holder{
        TextView text_name,text_roles;
        String approved;

    }
}
