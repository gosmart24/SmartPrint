package com.samtech.smartprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CyberTech on 6/4/2013.
 */
public class HistoryAdapter extends BaseAdapter {

    ArrayList<HistoryModel> list;
    LayoutInflater inflater;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> objects) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HistoryModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row, parent, false);

            holder.typetv = (TextView) convertView.findViewById(R.id.type);
            holder.amounttv = (TextView) convertView.findViewById(R.id.amount);
            holder.datetv = (TextView) convertView.findViewById(R.id.date);
            holder.icontv = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.typetv.setText(list.get(position).getHistoryType());
        holder.amounttv.setText("N" + list.get(position).getHistoryAmount());
        holder.datetv.setText(list.get(position).getHistoryDate());
        String types = (list.get(position).getHistoryType());
        if (types.equalsIgnoreCase("Debit")) {
            holder.icontv.setImageResource(R.mipmap.debit);
        } else if (types.equalsIgnoreCase("Credit")) {
            holder.icontv.setImageResource(R.mipmap.credit);
        } else if (types.equalsIgnoreCase("Airtime")) {
            holder.icontv.setImageResource(R.mipmap.airtime);
        } else if (types.equalsIgnoreCase("Bills")) {
            holder.icontv.setImageResource(R.mipmap.bills);
        } else if (types.equalsIgnoreCase("Transfer")) {
            holder.icontv.setImageResource(R.mipmap.transfer);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView typetv;
        TextView amounttv;
        TextView datetv;
        ImageView icontv;
    }
}
