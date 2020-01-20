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
 * Created by Designer2 on 10.07.2018.
 */

public class CustomAdapterManagMenu extends ArrayAdapter<DataModelManagMenu> implements View.OnClickListener{

    private ArrayList<DataModelManagMenu> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtMenu;
    }



    public CustomAdapterManagMenu(ArrayList<DataModelManagMenu> data, Context context) {
        super(context, R.layout.row_item_manag_menu, data);
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
        DataModelManagMenu dataModelManagMenu = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_manag_menu, parent, false);
            viewHolder.txtMenu = (TextView) convertView.findViewById(R.id.Menu);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtMenu.setText(dataModelManagMenu.getMenu());
        // Return the completed view to render on screen
        return convertView;
    }


}