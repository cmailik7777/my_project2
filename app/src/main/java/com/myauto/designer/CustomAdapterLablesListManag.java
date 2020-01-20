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
public class CustomAdapterLablesListManag extends ArrayAdapter<DataModelLabelListManag> implements View.OnClickListener{

    private ArrayList<DataModelLabelListManag> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtLabelListManagerNZ;
        TextView txtLabelListManagerMarka;
        TextView txtLabelListManagerGos;

        TextView txtLabelListManagerTel;
        TextView txtLabelListManagerClient;
        TextView txtLabelListManagerDescription;

    }



    public CustomAdapterLablesListManag(ArrayList<DataModelLabelListManag> data, Context context) {
        super(context, R.layout.row_item_label_list_manag, data);
        this.dataSet = data;
        this.mContext=context;
    }


    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelLabelListManag dataModelLabelListManag =(DataModelLabelListManag)object;

      /*  switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " +dataModelLabelListManag.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }*/
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelLabelListManag dataModelLabelListManag = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_label_list_manag, parent, false);
            viewHolder.txtLabelListManagerNZ = (TextView) convertView.findViewById(R.id.LabelListManagerNZ);
            viewHolder.txtLabelListManagerMarka = (TextView) convertView.findViewById(R.id.LabelListManagerMarka);
            viewHolder.txtLabelListManagerGos = (TextView) convertView.findViewById(R.id.LabelListManagerGos);

            viewHolder.txtLabelListManagerTel = (TextView)convertView.findViewById(R.id.LabelListManagerTel);
            viewHolder.txtLabelListManagerClient = (TextView) convertView.findViewById(R.id.LabelListManagerClient);
            viewHolder.txtLabelListManagerDescription = (TextView)convertView.findViewById(R.id.LabelListManagerDescription);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtLabelListManagerNZ.setText(dataModelLabelListManag.getLabelListManagerNZ());
        viewHolder.txtLabelListManagerMarka.setText(dataModelLabelListManag.getLabelListManagerMarka());
        viewHolder.txtLabelListManagerGos.setText(dataModelLabelListManag.getLabelListManagerGos());

        viewHolder.txtLabelListManagerTel.setText(dataModelLabelListManag.getLabelListManagerTel());
        viewHolder.txtLabelListManagerClient.setText(dataModelLabelListManag.getLabelListManagerClient());
        viewHolder.txtLabelListManagerDescription.setText(dataModelLabelListManag.getLabelListManagerDescription());

        /*String toCount = viewHolder.txtCount.getText().toString();

        if(toCount.equals("empty")){
            viewHolder.txtCount.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.txtCount.setVisibility(View.VISIBLE);
        }*/


        return convertView;
    }


}
