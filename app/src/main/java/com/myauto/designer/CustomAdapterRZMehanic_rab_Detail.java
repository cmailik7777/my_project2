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
 * Created by Designer2 on 16.10.2018.
 */

public class CustomAdapterRZMehanic_rab_Detail extends ArrayAdapter<DataModelRZMehanic_rab_Detail> implements View.OnClickListener{

    private ArrayList<DataModelRZMehanic_rab_Detail> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtBrig;
        TextView txtTime;
        TextView txtOtvet;
        TextView txtCateg;
        TextView txtRab;
        TextView txtCod;
        TextView txtData;
        TextView txtRabIspon;
    }



    public CustomAdapterRZMehanic_rab_Detail(ArrayList<DataModelRZMehanic_rab_Detail> data, Context context) {
        super(context, R.layout.row_list_rab_oper_mehanic, data);
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
        DataModelRZMehanic_rab_Detail dataModelRZMehanic_rab_detail = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_list_rab_oper_mehanic, parent, false);
            viewHolder.txtBrig = (TextView) convertView.findViewById(R.id.rab_oper_mehanic_brig);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.rab_oper_mehanic_time);
            viewHolder.txtOtvet = (TextView) convertView.findViewById(R.id.rab_oper_mehanic_otvet);
            viewHolder.txtCateg = (TextView) convertView.findViewById(R.id.rab_oper_mehanic_categ);
            viewHolder.txtRab = (TextView) convertView.findViewById(R.id.rab_oper_mehanic_rab);


            viewHolder.txtCod = (TextView) convertView.findViewById(R.id.rab_oper_mehanic_cod);
            viewHolder.txtData = (TextView) convertView.findViewById(R.id.rab_oper_mehanic_data);
            viewHolder.txtRabIspon = (TextView) convertView.findViewById(R.id.rab_oper_mehanic_ispoln);


            result=convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtBrig.setText(dataModelRZMehanic_rab_detail.getBrig());
        viewHolder.txtTime.setText(dataModelRZMehanic_rab_detail.getTime());
        viewHolder.txtOtvet.setText(dataModelRZMehanic_rab_detail.getOtvet());
        viewHolder.txtCateg.setText(dataModelRZMehanic_rab_detail.getCateg());
        viewHolder.txtRab.setText(dataModelRZMehanic_rab_detail.getRab());

        viewHolder.txtCod.setText(dataModelRZMehanic_rab_detail.getCod());
        viewHolder.txtData.setText(dataModelRZMehanic_rab_detail.getData());
        viewHolder.txtRabIspon.setText(dataModelRZMehanic_rab_detail.getIspoln());

        return convertView;
    }


}