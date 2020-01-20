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
 * Created by Designer2 on 11.06.2018.
 */

public class CustomAdapterWheels  extends ArrayAdapter<DataModelWheels> implements View.OnClickListener{

    private ArrayList<DataModelWheels> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtDate;
        TextView txtGos;
        TextView txtDescription;
        TextView txtBrand;
        TextView txtSize;
        TextView txtCount;
        TextView txtCount_month;
        TextView txtSum;
        //{"wheels":[{"date":"10.04.2018", "gos":"602DNA02", "description":"Р—РёєР°", "brand":"Yokohama", "size":"215/60/R16", "count":"4", "count_month":"2", "sum":"1В 000"} ]}
    }



    public CustomAdapterWheels(ArrayList<DataModelWheels> data, Context context) {
        super(context, R.layout.row_item_wheels, data);
        this.dataSet = data;
        this.mContext=context;
    }


    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelLabelList dataModelLabelList =(DataModelLabelList)object;

      /*  switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " +dataModelLabelList.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }*/
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelWheels dataModelWheels = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_wheels, parent, false);


            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.Wheels_Date);
            viewHolder.txtGos = (TextView) convertView.findViewById(R.id.Wheels_Gos);
            viewHolder.txtDescription= (TextView) convertView.findViewById(R.id.Wheels_Desc);
            viewHolder.txtBrand= (TextView) convertView.findViewById(R.id.Wheels_Brand);
            viewHolder.txtSize= (TextView) convertView.findViewById(R.id.Wheels_Size);
            viewHolder.txtCount= (TextView) convertView.findViewById(R.id.Wheels_Count);
            viewHolder.txtCount_month= (TextView) convertView.findViewById(R.id.Wheels_Count_Mounth);
            viewHolder.txtSum= (TextView) convertView.findViewById(R.id.Wheels_Summa);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtDate.setText(dataModelWheels.getDate());
        viewHolder.txtGos.setText(dataModelWheels.getGos());
        viewHolder.txtDescription.setText(dataModelWheels.getDescription());
        viewHolder.txtBrand.setText(dataModelWheels.getBrand());
        viewHolder.txtSize.setText(dataModelWheels.getSize());
        viewHolder.txtCount.setText(dataModelWheels.getCount());
        viewHolder.txtCount_month.setText(dataModelWheels.getCount_month());
        viewHolder.txtSum.setText(dataModelWheels.getSum());


        return convertView;
    }
}
