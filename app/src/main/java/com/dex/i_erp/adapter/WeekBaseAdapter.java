package com.dex.i_erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dex.i_erp.data.WeekData;
import com.dexter.dex_erp.R;

import java.util.ArrayList;

public class WeekBaseAdapter extends BaseAdapter {
    private static ArrayList<WeekData> searchArrayList;

    private LayoutInflater mInflater;

    public WeekBaseAdapter(Context context, ArrayList<WeekData> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_week, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView
                    .findViewById(R.id.txt_weekname);
            holder.txtCityState = (TextView) convertView
                    .findViewById(R.id.txt_weekhour);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtCityState.setText(searchArrayList.get(position).getHours());

        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtCityState;

    }
}