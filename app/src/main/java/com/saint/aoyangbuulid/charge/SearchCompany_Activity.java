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
import android.widget.TextView;

import com.saint.aoyangbuulid.BaseActivity;
import com.saint.aoyangbuulid.R;
import com.saint.aoyangbuulid.charge.adapter.SearchCompany_Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzh on 15-11-12.
 */
public class SearchCompany_Activity extends BaseActivity {
    public ListView list_company;
    public List list_data;
    public ImageButton button_company;
    public TextView textview_company;
    public SearchCompany_Adapter adapter;
    public CheckBox checkbox;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_company_layout);

        button_company= (ImageButton) findViewById(R.id.button_company);
        list_company= (ListView) findViewById(R.id.list_searchcompany);


        button_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.button_company){
                    Intent intent=new Intent();
                    intent.putExtra("companyname",name);
                    intent.setClass(SearchCompany_Activity.this,Company_Reservation_Activity.class);
                    startActivity(intent);
                }
            }
        });







        list_data=new ArrayList();
        for (int i=0;i<20;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("company","苏州**********公司");
            list_data.add(map);
        }
        adapter=new SearchCompany_Adapter(this,list_data);
        list_company.setAdapter(adapter);

        list_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkbox = (CheckBox) view.findViewById(R.id.check_company);
                checkbox.setChecked(true);
                textview_company= (TextView) view.findViewById(R.id.text_company);
                name=textview_company.getText().toString();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater  inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            Intent intent=new Intent();
            intent.setClass(SearchCompany_Activity.this,Company_Reservation_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
