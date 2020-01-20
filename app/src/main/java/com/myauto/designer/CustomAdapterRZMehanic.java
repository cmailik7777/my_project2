package com.myauto.designer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Designer2 on 15.10.2018.
 */

public class CustomAdapterRZMehanic extends ArrayAdapter<DataModelRZMehanic> implements View.OnClickListener{

    private ArrayList<DataModelRZMehanic> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtManager;
        TextView txtNum;
        TextView txtData;
        TextView txtGos;


    }



    public CustomAdapterRZMehanic(ArrayList<DataModelRZMehanic> data, Context context) {
        super(context, R.layout.row_rz_mehanic, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelGN dataModelGN =(DataModelGN)object;



    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelRZMehanic dataModelRZMehanic = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_rz_mehanic, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.listview_rz_name);
            viewHolder.txtManager = (TextView) convertView.findViewById(R.id.listview_rz_manag);
            viewHolder.txtNum = (TextView) convertView.findViewById(R.id.listview_rz_num);
            viewHolder.txtData = (TextView) convertView.findViewById(R.id.listview_rz_data);
            viewHolder.txtGos = (TextView) convertView.findViewById(R.id.listview_rz_gos);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        String test = dataModelRZMehanic.getName();
        if (test.equals("Ремонтные заказы")){
            viewHolder.txtName.setTextColor(Color.parseColor("#8a8a8a"));
        }else if (test.equals("Мои заказы")){
            viewHolder.txtName.setTextColor(Color.parseColor("#5fba7d"));
        }
        viewHolder.txtName.setText(dataModelRZMehanic.getName());
        viewHolder.txtManager.setText(dataModelRZMehanic.getManag());
        viewHolder.txtNum.setText(dataModelRZMehanic.getNum());
        viewHolder.txtData.setText(dataModelRZMehanic.getData());
        viewHolder.txtGos.setText(dataModelRZMehanic.getGos());

        return convertView;
    }


}
