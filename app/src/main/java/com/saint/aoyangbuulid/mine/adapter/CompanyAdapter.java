package com.saint.aoyangbuulid.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.saint.aoyangbuulid.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzh on 15-12-9.
 */
public class CompanyAdapter extends BaseAdapter  {
        public List<Map<String,Object>> list;
        public Context context;
        public  CompanyAdapter(Context context,List list){
            this.context=context;
            this.list=list;

    }
    Map<Integer, Boolean> isCheckMap =  new HashMap<Integer, Boolean>();


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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if (convertView==null){
            holder=new Holder();
            convertView= LayoutInflater.from(context).inflate(R.layout.select_company_display,null);
            holder.text_company= (TextView) convertView.findViewById(R.id.select_company);
            holder.ck= (CheckBox) convertView.findViewById(R.id.ck_company);


            holder.ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    int radiaoId = Integer.parseInt(buttonView.getTag().toString());
                    if(isChecked)
                    {
                        //将选中的放入hashmap中
                        isCheckMap.put(position, isChecked);
                    }
                    else
                    {
                        //取消选中的则剔除
                        isCheckMap.remove(position);
                    }
                }
            });
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }

//        holder.ck.setChecked(isCheckMap.get(position) == null ? false : true);
        holder.id= (int) (list.get(position)).get("company_id");
        holder.text_company.setText(String.valueOf((list.get(position)).get("name")));
        if(isCheckMap!=null && isCheckMap.containsKey(position))
        {
            holder.ck.setChecked(isCheckMap.get(position));
        }
        else
        {
            holder.ck.setChecked(false);
        }
        return convertView ;
    }
    public class Holder{
        int id;
        TextView text_company;
        CheckBox ck;
    }

}
