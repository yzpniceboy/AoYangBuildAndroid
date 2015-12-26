package com.saint.aoyangbuulid.article.news.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.saint.aoyangbuulid.R;

import java.util.List;
import java.util.Map;


/**
 * Created by 志浩 on 2015/10/30.
 */
//
public class MyAdapter extends BaseAdapter {

    //5种layout 模式
    final int VIEW_TYPE=5;
    final int VIEW_1=0;
    final int VIEW_2=1;
    final int VIEW_3=2;
    final int VIEW_4=3;
    final int VIEW_5=4;

    public Context context;
    public List<Map<String,Object>> list;
    private DisplayImageOptions options;
//    Map<Integer, Boolean> isCheckMap =  new HashMap<Integer, Boolean>();

    public MyAdapter(Context context,List list){
        this.context=context;
        this.list=list;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.image)
//                .showImageForEmptyUri(R.drawable.image)
//                .showImageOnFail(R.drawable.image)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
//                .build();
    }
    public ImageLoader imageLoader=ImageLoader.getInstance();
    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
            return VIEW_2;


    }
    @Override
    public int getViewTypeCount() {
        return 4;
    }
    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
//        int type=getItemViewType(position);

        //if (convertView==null){
            holder = new Holder();
            convertView =LayoutInflater.from(context).inflate(R.layout.listview_view02, null);
            holder.text_date= (TextView) convertView.findViewById(R.id.text_date);
            holder.text_title = (TextView) convertView.findViewById(R.id.text_title);
            holder.image_news = (ImageView) convertView.findViewById(R.id.image_news);
            convertView.setTag(holder);

//        }else {
//            holder=(Holder)convertView.getTag();
//        }

        Map<String,Object> m = list.get(position);
        holder.content = String.valueOf(m.get("content"));
        holder.text_title.setText(String.valueOf(m.get("title")));
        holder.text_date.setText(String.valueOf(m.get("date")));
        if(String.valueOf(m.get("image")) != null) {
            if(!String.valueOf(m.get("image")).equals("") && !String.valueOf(m.get("image")).equals("null")) {
                imageLoader.displayImage((String.valueOf(m.get("image"))), holder.image_news);
                holder.image_news.setTag(String.valueOf(m.get("image")));
                Log.d("image:", position + "|" + holder.image_news.getTag());
            }
        }
        return convertView;
    }



    public class Holder{
        String content,id;
        TextView text_title,text_date;
        ImageView image_news;
    }


}
