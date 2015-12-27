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
public class AllCompany_Adapter extends BaseAdapter {
    public List<Map<String,Object>> list;
    public Context context;
    public AllCompany_Adapter(Context context,List list){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.companylist_layout,null);
            holder.text_company_name= (TextView) convertView.findViewById(R.id.text_company_name);
            holder.text_company_phone= (TextView) convertView.findViewById(R.id.text_compnay_phone);
            holder.image_company= (XCRoundImageView) convertView.findViewById(R.id.image_company);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.id= (int) list.get(position).get("company_id");
        holder.text_company_name.setText(String.valueOf((list.get(position)).get("name")));
        holder.image_company.setImageResource(R.mipmap.company_logo3x);
        holder.text_company_phone.setText(String.valueOf(list.get(position).get("phone")));
        return convertView;
    }
    public class Holder{
        int  id;
        TextView text_company_name,text_company_phone;
        XCRoundImageView image_company;
    }
}
