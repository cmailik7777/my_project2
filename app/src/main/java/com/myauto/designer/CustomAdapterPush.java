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
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapterPush extends ArrayAdapter<DataModelPush> implements View.OnClickListener{

    private ArrayList<DataModelPush> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTextPush;
        TextView txtDataPush;
    }



    public CustomAdapterPush(ArrayList<DataModelPush> data, Context context) {
        super(context, R.layout.row_push, data);
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
        DataModelPush dataModelPush = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_push, parent, false);
            viewHolder.txtTextPush = (TextView) convertView.findViewById(R.id.textPush);
            viewHolder.txtDataPush = (TextView) convertView.findViewById(R.id.dataPush);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtTextPush.setText(dataModelPush.getTextPush());
        viewHolder.txtDataPush.setText(dataModelPush.getDataPush());

        // Return the completed view to render on screen
        return convertView;
    }


}
