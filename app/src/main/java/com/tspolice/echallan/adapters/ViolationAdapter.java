package com.tspolice.echallan.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.tspolice.echallan.R;
import com.tspolice.echallan.activities.SpotChallanActivity;
import com.tspolice.echallan.models.MultiSelectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srinivas on 11/17/2018.
 */

public class ViolationAdapter extends ArrayAdapter<MultiSelectModel> {


    private ArrayList<MultiSelectModel> multiSelectModelArrayList;
    private ViewHolder viewHolder;
    private Context context;

    public ViolationAdapter(Context context, ArrayList<MultiSelectModel> objects) {
        super(context, R.layout.item_vltn, objects);
        this.multiSelectModelArrayList = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MultiSelectModel multiSelectModel = multiSelectModelArrayList.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_vltn, parent, false);
            viewHolder.vltn_Sec = convertView.findViewById(R.id.vltn_Sec);
            viewHolder.vltn_Dis = convertView.findViewById(R.id.vltn_Dis);
            viewHolder.vltn_Amnt = convertView.findViewById(R.id.amount);
            convertView.setTag(viewHolder);
            if (position % 2 == 0) {
                convertView.setBackgroundColor(Color.parseColor("#f9f9f9"));
            } else {
                convertView.setBackgroundColor(Color.parseColor("#d7d7d7"));
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (multiSelectModel != null) {
            viewHolder.vltn_Sec.setText(multiSelectModel.getVltnSec());
            viewHolder.vltn_Dis.setText(multiSelectModel.getVltnDis());
            viewHolder.vltn_Amnt.setText(String.valueOf(multiSelectModel.getFine_min()));
        }
        viewHolder.vltn_Amnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiSelectModelArrayList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "List Clicked", Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        AppCompatTextView vltn_Sec, vltn_Dis, vltn_Amnt;
    }


}
