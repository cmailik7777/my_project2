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
public class CustomAdapterListManager extends ArrayAdapter<DataModelListManager> implements View.OnClickListener{

    private ArrayList<DataModelListManager> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {

        TextView txtTextNameManag;

    }



    public CustomAdapterListManager(ArrayList<DataModelListManager> data, Context context) {
        super(context, R.layout.row_manager_list, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelListManager dataModelListManager =(DataModelListManager)object;




    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelListManager dataModelListManager = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_manager_list, parent, false);
            viewHolder.txtTextNameManag = (TextView) convertView.findViewById(R.id.NameManag);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtTextNameManag.setText(dataModelListManager.getTextNameManag());

        // Return the completed view to render on screen
        return convertView;
    }


}
