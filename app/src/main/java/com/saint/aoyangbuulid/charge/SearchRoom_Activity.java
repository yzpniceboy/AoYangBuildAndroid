package com.saint.aoyangbuulid.charge;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzh on 15-11-16.
 */
public class SearchRoom_Activity extends BaseActivity implements View.OnClickListener {
    public ImageButton button_send;
    public List list_roomdata;
    public ListView listView;
    public CheckBox box;
    public TextView text;
    public String roomnumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_rooms_layout);

        button_send= (ImageButton) findViewById(R.id.send);
        listView= (ListView) findViewById(R.id.room);
        button_send.setOnClickListener(this);


        list_roomdata=new ArrayList();
        for (int i=10;i<21;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("rooms",i+"号房间");
            list_roomdata.add(map);

        }

        SimpleAdapter adapter=new SimpleAdapter(this,list_roomdata,R.layout.searchroom_layout,new String[]{"rooms"},new int[]{R.id.text_room});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                box= (CheckBox) view.findViewById(R.id.checkbox_room);
                box.setChecked(true);
                text= (TextView) view.findViewById(R.id.text_room);
                roomnumber=text.getText().toString();

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.send){
                Intent intentroom=new Intent();
                intentroom.putExtra("room",roomnumber);
                intentroom.setClass(SearchRoom_Activity.this, Room_Reservation_Activity.class);
                startActivity(intentroom);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

