package com.myauto.designer;

import android.content.Context;
import android.graphics.Color;
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
public class CustomAdapterLablesList extends ArrayAdapter<DataModelLabelList> implements View.OnClickListener{

    private ArrayList<DataModelLabelList> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtIdList;

        TextView txtLabelListMarka;
        TextView txtLabelListGos;
        TextView txtLabelListStatys;
        TextView txtLabelListManager;
        TextView txtLabelListTel;

        TextView tttManag,tttTel;
    }



    public CustomAdapterLablesList(ArrayList<DataModelLabelList> data, Context context) {
        super(context, R.layout.row_item_label_list, data);
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
        DataModelLabelList dataModelLabelList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_label_list, parent, false);

            viewHolder.txtIdList = (TextView) convertView.findViewById(R.id.idList);
            viewHolder.txtLabelListMarka = (TextView) convertView.findViewById(R.id.LabelListManagerGos);
            viewHolder.txtLabelListGos = (TextView) convertView.findViewById(R.id.LabelListGos);
            viewHolder.txtLabelListStatys = (TextView) convertView.findViewById(R.id.LabelListStatys);
            viewHolder.txtLabelListManager = (TextView) convertView.findViewById(R.id.LabelListManager);
            viewHolder.txtLabelListTel = (TextView) convertView.findViewById(R.id.LabelListTel);

            viewHolder.tttManag=(TextView)convertView.findViewById(R.id.LabelManager);
            viewHolder.tttTel=(TextView)convertView.findViewById(R.id.LabelTel);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtIdList.setText(dataModelLabelList.getIdList());
        viewHolder.txtLabelListMarka.setText(dataModelLabelList.getLabelListMarka());
        viewHolder.txtLabelListGos.setText(dataModelLabelList.getLabelListGos());
        viewHolder.txtLabelListStatys.setText(dataModelLabelList.getLabelListStatys());//менеджер
        viewHolder.txtLabelListManager.setText(dataModelLabelList.getLabelListManager());//телефон
        viewHolder.txtLabelListTel.setText(dataModelLabelList.getLabelListTel());//no or YES


        String toCount = viewHolder.txtLabelListStatys.getText().toString();

        if(toCount.equals("empty")){
            viewHolder.tttManag.setVisibility(View.GONE);
            viewHolder.txtLabelListStatys.setVisibility(View.GONE);
        }else {
            viewHolder.tttManag.setVisibility(View.VISIBLE);
            viewHolder.txtLabelListStatys.setVisibility(View.VISIBLE);
        }

        String toCount2 = viewHolder.txtLabelListManager.getText().toString();

        if(toCount2.equals("empty")){
            viewHolder.tttTel.setVisibility(View.GONE);
            viewHolder.txtLabelListManager.setVisibility(View.GONE);
        }else {
            viewHolder.tttTel.setVisibility(View.VISIBLE);
            viewHolder.txtLabelListManager.setVisibility(View.VISIBLE);
        }

        String toCount3 = viewHolder.txtLabelListTel.getText().toString();

        if(toCount3.equals("NO")){
            viewHolder.txtLabelListTel.setText("В расмотрении...");
            viewHolder.txtLabelListTel.setTextColor(Color.parseColor("#a7a7a7"));
        } else if (toCount3.equals("YES")){
            viewHolder.txtLabelListTel.setText("Прочитанно");
            viewHolder.txtLabelListTel.setTextColor(Color.parseColor("#11bf04"));
        }

        return convertView;
    }


}
