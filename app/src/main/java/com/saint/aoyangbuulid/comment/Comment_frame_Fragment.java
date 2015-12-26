package com.saint.aoyangbuulid.comment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saint.aoyangbuulid.R;

import java.util.List;
import java.util.Map;

/**
 * Created by zzh on 15-11-5.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Comment_frame_Fragment extends Fragment {
    public List<Map<String,Object>> list;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.commont_layout_frame,container,false);
        return view;
    }
}
