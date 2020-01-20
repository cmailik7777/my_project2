package com.myauto.designer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Designer2 on 31.10.2018.
 */

public class Custom extends ArrayAdapter {

    private ArrayList dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }

    public Custom(ArrayList data, Context context) {
        super(context, R.layout.row_choice_brig, data);
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Data getItem(int position) {
        return (Data) dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_choice_brig, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.rowTextView);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Data item = getItem(position);


        viewHolder.txtName.setText(item.name);
        viewHolder.checkBox.setChecked(item.checked);


        return result;
    }
}