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
 * Created by Designer2 on 13.06.2018.
 */

public class CustomAdapterOrgService extends ArrayAdapter<DataModelOrgService> implements View.OnClickListener{

    private ArrayList<DataModelOrgService> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtOrg;
    }



    public CustomAdapterOrgService(ArrayList<DataModelOrgService> data, Context context) {
        super(context, R.layout.row_item_org_service, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelOrgService dataModelOrgService = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_org_service, parent, false);
            viewHolder.txtOrg = (TextView) convertView.findViewById(R.id.org_service_org);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtOrg.setText(dataModelOrgService.getOrg());
        // Return the completed view to render on screen
        return convertView;
    }


}

