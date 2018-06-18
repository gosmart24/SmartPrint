package com.samtech.smartprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by CyberTech on 6/4/2013.
 * SAMUEL ADAKOLE
 */
public class QuickHelpAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<String> ListStorage;


    public QuickHelpAdapter(Context context, List<String> Storage) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListStorage = Storage;
    }

    @Override
    public int getCount() {
        return ListStorage.size();
    }

    @Override
    public String getItem(int position) {
        return ListStorage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.helpxml, parent, false);

            holder.tv_help = (TextView) convertView.findViewById(R.id.tv_helpDis);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_help.setText(ListStorage.get(position));
        return convertView;
    }

    private class Holder {
        TextView tv_help;
    }
}
