package com.myauto.designer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Designer2 on 18.09.2018.
 */

public class CustomAdapterMiniRabOper extends ArrayAdapter<DataModelMiniRabOper> implements View.OnClickListener{

    private ArrayList<DataModelMiniRabOper> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtMincena;
        TextView txtMaxcena;
        TextView txtName;
    }



    public CustomAdapterMiniRabOper(ArrayList<DataModelMiniRabOper> data, Context context) {
        super(context, R.layout.row_mini_rab_oper, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelPush dataModelPush =(DataModelPush)object;




    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelMiniRabOper dataModelMiniRabOper = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_mini_rab_oper, parent, false);
            viewHolder.txtMincena = (TextView) convertView.findViewById(R.id.mini_rab_oper_mincena);
            viewHolder.txtMaxcena = (TextView) convertView.findViewById(R.id.mini_rab_oper_maxcena);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.mini_rab_oper_oper);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtMincena.setText(dataModelMiniRabOper.getMinCena());
        viewHolder.txtMaxcena.setText(dataModelMiniRabOper.getMaxCena());
        viewHolder.txtName.setText(dataModelMiniRabOper.getName());
        // Return the completed view to render on screen
        return convertView;
    }


}