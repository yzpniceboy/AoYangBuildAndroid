package com.saint.aoyangbuulid.charge.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;

import java.util.List;

/**
 * Created by zzh on 15-11-12.
 */
public class SearchCompany_Adapter extends BaseAdapter {
    public String message;
    public Context context;
    public List list_data;
    public SearchCompany_Adapter(Context context, List list){
        this.context=context;
        this.list_data=list;

    }
    @Override
    public int getCount() {
        return list_data.size();
    }

    @Override
    public Object getItem(int position) {
        return list_data.get(position);
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
            convertView= LayoutInflater.from(context).inflate(R.layout.searchcompany_display,null);
            holder.textview_name= (TextView) convertView.findViewById(R.id.text_company);
            holder.checkbox_company= (CheckBox) convertView.findViewById(R.id.check_company);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.textview_name.setText("苏州**********公司");
        final Holder finalHolder = holder;
        holder.checkbox_company.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    message=finalHolder.textview_name.getText().toString();
                    Handler handler=new Handler();


                }
            }
        });
        return convertView;
    }
    public class Holder{
        public CheckBox checkbox_company;
        public TextView textview_name;
    }
}
