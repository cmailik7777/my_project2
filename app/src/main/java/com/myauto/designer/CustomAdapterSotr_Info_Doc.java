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
 * Created by Designer2 on 24.09.2018.
 */

public class CustomAdapterSotr_Info_Doc extends ArrayAdapter<DataModalSotr_Info_Doc> implements View.OnClickListener{

    private ArrayList<DataModalSotr_Info_Doc> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNum;
        TextView txtDoc;
    }



    public CustomAdapterSotr_Info_Doc(ArrayList<DataModalSotr_Info_Doc> data, Context context) {
        super(context, R.layout.row_sotr_info_doc, data);
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
        DataModalSotr_Info_Doc dataModalSotr_info_doc = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_sotr_info_doc, parent, false);
            viewHolder.txtNum = (TextView) convertView.findViewById(R.id.sotr_info_doc_num);
            viewHolder.txtDoc = (TextView) convertView.findViewById(R.id.sotr_info_doc_doc);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtNum.setText(dataModalSotr_info_doc.getNum());
        viewHolder.txtDoc.setText(dataModalSotr_info_doc.getDoc());

        // Return the completed view to render on screen
        return convertView;
    }


}