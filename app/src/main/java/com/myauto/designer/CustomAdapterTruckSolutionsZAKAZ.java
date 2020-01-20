package com.myauto.designer;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapterTruckSolutionsZAKAZ extends ArrayAdapter<DataModelTruckSolutionsZAKAZ> implements View.OnClickListener{

    private ArrayList<DataModelTruckSolutionsZAKAZ> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTruckOO;
        TextView txtTruckOG;
        TextView txtTruckOA;
        TextView txtTruckOL;

        TextView txtTruckPO;
        TextView txtTruckPG;
        TextView txtTruckPA;
        TextView txtTruckPL;

        ImageView info;
    }



    public CustomAdapterTruckSolutionsZAKAZ(ArrayList<DataModelTruckSolutionsZAKAZ> data, Context context) {
        super(context, R.layout.row_itemtrucksolutions, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelTruckSolutionsZAKAZ dataModelTruckSolutionsZAKAZ =(DataModelTruckSolutionsZAKAZ)object;




        switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " + dataModelTruckSolutionsZAKAZ.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelTruckSolutionsZAKAZ dataModelTruckSolutionsZAKAZ = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_itemtrucksolutionszakaz, parent, false);
            viewHolder.txtTruckOO = (TextView) convertView.findViewById(R.id.truckOO);
            viewHolder.txtTruckOG = (TextView) convertView.findViewById(R.id.truckOG);
            viewHolder.txtTruckOA = (TextView) convertView.findViewById(R.id.truckOA);
            viewHolder.txtTruckOL = (TextView) convertView.findViewById(R.id.truckOL);

            viewHolder.txtTruckPO = (TextView) convertView.findViewById(R.id.truckPO);
            viewHolder.txtTruckPG = (TextView) convertView.findViewById(R.id.truckPG);
            viewHolder.txtTruckPA = (TextView) convertView.findViewById(R.id.truckPA);
            viewHolder.txtTruckPL = (TextView) convertView.findViewById(R.id.truckPL);

            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtTruckOO.setText(dataModelTruckSolutionsZAKAZ.getTruckOO());
        viewHolder.txtTruckOG.setText(dataModelTruckSolutionsZAKAZ.getTruckOG());
        viewHolder.txtTruckOA.setText(dataModelTruckSolutionsZAKAZ.getTruckOA());
        viewHolder.txtTruckOL.setText(dataModelTruckSolutionsZAKAZ.getTruckOL());

        viewHolder.txtTruckPO.setText(dataModelTruckSolutionsZAKAZ.getTruckPO());
        viewHolder.txtTruckPG.setText(dataModelTruckSolutionsZAKAZ.getTruckPG());
        viewHolder.txtTruckPA.setText(dataModelTruckSolutionsZAKAZ.getTruckPA());
        viewHolder.txtTruckPL.setText(dataModelTruckSolutionsZAKAZ.getTruckPL());

        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


}
