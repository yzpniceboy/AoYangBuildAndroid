package com.saint.aoyangbuulid.contact.adapter;

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
 * Created by zzh on 15-12-18.
 */
public class CompanyMember_Adapter extends BaseAdapter {
    public List<Map<String ,Object>> list;
    public Context context;
    public CompanyMember_Adapter(Context context,List list){
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
            holder.user_image= (XCRoundImageView) convertView.findViewById(R.id.image_user);
            holder.text_name= (TextView) convertView.findViewById(R.id.textview_right_name);
            holder.text_phone= (TextView) convertView.findViewById(R.id.text_right_phone);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.text_phone.setText(String.valueOf(list.get(position).get("phone")));
        holder.text_name.setText(String.valueOf((list.get(position)).get("dispaly_name")));
        return convertView;
    }
    public class Holder{
        XCRoundImageView user_image;
        TextView text_name,text_phone;
    }
}
