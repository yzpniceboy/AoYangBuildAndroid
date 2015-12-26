package com.saint.aoyangbuulid.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;

import java.util.List;
import java.util.Map;

/**
 * Created by zzh on 15-12-10.
 */
public class Query_Adpater extends BaseAdapter {
    public List<Map<String,Object>> list;
    public Context context;
    public Query_Adpater(Context context,List list){
        this.context=context;
        this.list=list;
    }
    int approved_id;

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

            convertView= LayoutInflater.from(context).inflate(R.layout.query_display_layout,null);
//            holder.btn_yes= (Button) convertView.findViewById(R.id.bt_yes);
//            holder.btn_no=(Button)convertView.findViewById(R.id.bt_no);
            holder.company_name= (TextView) convertView.findViewById(R.id.text_query);
            holder.user_name= (TextView) convertView.findViewById(R.id.text_name);
//
//            holder.btn_yes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    approved_id=1;
//
//                }
//            });
//            holder.btn_no.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    approved_id=-1;
//
//                }
//            });

            convertView.setTag(holder);

        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.approved_data=approved_id;
        holder.user_name.setText(String.valueOf((list.get(position)).get("user_name")));
        holder.company_name.setText(String.valueOf((list.get(position)).get("name")));
        return convertView;
    }
    public class Holder{
        int approved_data;
        Button btn_yes,btn_no;
        TextView company_name,user_name;
    }

}
