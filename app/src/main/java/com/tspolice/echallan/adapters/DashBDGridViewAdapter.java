package com.tspolice.echallan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tspolice.echallan.R;

import java.util.ArrayList;



/**
 * Created by Srinivas on 9/19/2018.
 */

public class DashBDGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> appNames_Array;
    private ArrayList<Integer> appIcons_Array;

    public DashBDGridViewAdapter(Context context, ArrayList<String> appNames_Array,
                                 ArrayList<Integer> appIcons_Array) {
        this.context = context;
        this.appNames_Array=appNames_Array;
        this.appIcons_Array=appIcons_Array;
    }

    @Override
    public int getCount() {
        return appNames_Array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.custom_dashbd_grid, null);
        AppCompatTextView txt_appName=(AppCompatTextView)convertView.findViewById(R.id.txt_appName);
        AppCompatImageView img_appIcon=(AppCompatImageView)convertView.findViewById(R.id.img_appIcon);
        img_appIcon.setImageResource(appIcons_Array.get(position));
        txt_appName.setText(appNames_Array.get(position));
        return convertView;
    }
}
