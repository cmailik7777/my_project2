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
 * Created by Designer2 on 07.11.2018.
 */

public class CustomAdapterMehanicBrigRab extends ArrayAdapter<DataModelMehanicBrigRab> implements View.OnClickListener{

    private ArrayList<DataModelMehanicBrigRab> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;


    }



    public CustomAdapterMehanicBrigRab(ArrayList<DataModelMehanicBrigRab> data, Context context) {
        super(context, R.layout.row_mehanic_my_rab, data);
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
        DataModelMehanicBrigRab dataModelMehanicBrigRab = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_mehanic_my_rab, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.mehanic_my_rab_name);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtName.setText(dataModelMehanicBrigRab.getName());
        return convertView;
    }


}