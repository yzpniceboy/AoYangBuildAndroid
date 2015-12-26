package com.saint.aoyangbuulid.reserve;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.charge.Company_Reservation_Activity;
import com.saint.aoyangbuulid.charge.Room_Reservation_Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 志浩 on 2015/11/2.
 */
//服务定阅
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Reserve_Fragment extends Fragment{
    public ImageButton button;
    public GridView grid_image;
    public List list;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.reserve_layout,container,false);
        grid_image= (GridView) view.findViewById(R.id.gridview);

        list=new ArrayList();
        String[]  name={"水费","电费","宽带费","物业费","会议室预定费","健身房预定费"};
        Integer[] image={R.mipmap.water_icon,R.mipmap.wlectricity_icon,R.mipmap.broadband_fee_icon,R.mipmap.property_costs_icon,R.mipmap.the_meeting_room_reservation_fee_icon,R.mipmap.the_gym_reservation_fee_icon};
        int len=image.length;
        for (int i=0;i<len;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("image",image[i]);
            map.put("name",name[i]);
            list.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(getActivity(),list,R.layout.gridview_list
                ,new String[]{"image","name"},new int[]{R.id.image_grall,R.id.name});
        grid_image.setAdapter(adapter);

        grid_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    Intent intent=new Intent();
                    intent.setClass(getActivity(), Company_Reservation_Activity.class);
                    startActivity(intent);
                }else if (position==1){
                    Intent intent=new Intent();
                    intent.setClass(getActivity(), Company_Reservation_Activity.class);
                    startActivity(intent);

                }else if (position==2){
                    Intent intent=new Intent();
                    intent.setClass(getActivity(), Company_Reservation_Activity.class);
                    startActivity(intent);
                }else if (position==3){
                    Intent intent=new Intent();
                    intent.setClass(getActivity(), Company_Reservation_Activity.class);
                    startActivity(intent);
                }else if (position==4){
                    Intent intent=new Intent();
                    intent.setClass(getActivity(), Room_Reservation_Activity.class);
                    startActivity(intent);

                }else if (position==5){
                    Intent intent=new Intent();
                    intent.setClass(getActivity(), Room_Reservation_Activity.class);
                    startActivity(intent);

                }
            }
        });

        return view;
    }


}
