package com.myauto.designer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapterLablesListDetail extends ArrayAdapter<DataModelLabelListDetail> implements View.OnClickListener{

    private ArrayList<DataModelLabelListDetail> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtLablesListDetailDescription;
        TextView txtLablesListDetailAuthor;

        TextView txtLablesListDetailChecked;
    }



    public CustomAdapterLablesListDetail(ArrayList<DataModelLabelListDetail> data, Context context) {
        super(context, R.layout.row_item_label_list_detail, data);
        this.dataSet = data;
        this.mContext=context;
    }


    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelLabelListDetail dataModelLabelListDetail =(DataModelLabelListDetail)object;

      /*  switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " +dataModelLabelListDetail.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }*/
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelLabelListDetail dataModelLabelListDetail = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_label_list_detail, parent, false);
            viewHolder.txtLablesListDetailDescription = (TextView) convertView.findViewById(R.id.LablesListDetailDescription);
            viewHolder.txtLablesListDetailAuthor = (TextView) convertView.findViewById(R.id.LablesListDetailAuthor);

            viewHolder.txtLablesListDetailChecked=(TextView)convertView.findViewById(R.id.LablesListDetailChecked);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtLablesListDetailDescription.setText(dataModelLabelListDetail.getLablesListDetailDescription());
        viewHolder.txtLablesListDetailAuthor.setText(dataModelLabelListDetail.getLablesListDetailAuthor());
        viewHolder.txtLablesListDetailChecked.setText(dataModelLabelListDetail.getLablesListDetailChecked());

        String toCount = viewHolder.txtLablesListDetailChecked.getText().toString();

        if(toCount.equals("client")){
            View positiveButton = convertView.findViewById(R.id.change);
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams)positiveButton.getLayoutParams();
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            positiveButton.setLayoutParams(layoutParams);
        } else if(toCount.equals("manager")){
            View positiveButton = convertView.findViewById(R.id.change);
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams)positiveButton.getLayoutParams();
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            positiveButton.setLayoutParams(layoutParams);
        }

        return convertView;
    }


}
