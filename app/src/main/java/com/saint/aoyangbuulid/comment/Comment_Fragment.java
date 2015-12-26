package com.saint.aoyangbuulid.comment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.comment.adapter.Commnet_Adapter;

import java.util.List;


/**
 * Created by zzh on 15-11-5.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Comment_Fragment extends Fragment {
    public Commnet_Adapter adapter;
    public List<String> list_comment;
    public ListView list_data;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.comment_layout, container, false);
        getActivity().getWindow().setBackgroundDrawable(null);

        list_data= (ListView) view.findViewById(R.id.listview_comment);
        adapter=new Commnet_Adapter(getActivity(),list_comment);
        list_data.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.love:
                Toast.makeText(getActivity(),"收藏成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Toast.makeText(getActivity(),"分享成功",Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
