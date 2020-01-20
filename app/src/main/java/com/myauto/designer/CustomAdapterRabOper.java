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

public class CustomAdapterRabOper extends ArrayAdapter<DataModelRabOper> implements View.OnClickListener{

    private ArrayList<DataModelRabOper> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtBrigada;
        TextView txtDate;
        TextView txtOtvet;
        TextView txtCategory;
        TextView txtAmount;
        TextView txtRab;
        TextView txtCount;
    }



    public CustomAdapterRabOper(ArrayList<DataModelRabOper> data, Context context) {
        super(context, R.layout.row_rab_oper, data);
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
        DataModelRabOper dataModelRabOper = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_rab_oper, parent, false);
            viewHolder.txtBrigada = (TextView) convertView.findViewById(R.id.rab_oper_brigada);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.rab_oper_date);
            viewHolder.txtOtvet = (TextView) convertView.findViewById(R.id.rab_oper_otvet);
            viewHolder.txtCategory = (TextView) convertView.findViewById(R.id.rab_oper_category);
            viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.rab_oper_amount);
            viewHolder.txtRab = (TextView) convertView.findViewById(R.id.rab_oper_oper);

            viewHolder.txtCount = (TextView) convertView.findViewById(R.id.rab_oper_count);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtBrigada.setText(dataModelRabOper.getBrigada());
        viewHolder.txtDate.setText(dataModelRabOper.getData());
        viewHolder.txtOtvet.setText(dataModelRabOper.getOtvet());
        viewHolder.txtCategory.setText(dataModelRabOper.getCategory());
        viewHolder.txtAmount.setText(dataModelRabOper.getAmount());
        viewHolder.txtRab.setText(dataModelRabOper.getOper());
        viewHolder.txtCount.setText(dataModelRabOper.getCount());

        //viewHolder.txtTextPush.setText(dataModelRabOper.getTextPush());
        //viewHolder.txtDataPush.setText(dataModelRabOper.getDataPush());

        // Return the completed view to render on screen
        return convertView;
    }


}
