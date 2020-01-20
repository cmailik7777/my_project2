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
public class CustomAdapterTovar extends ArrayAdapter<DataModelTovar> implements View.OnClickListener{

    private ArrayList<DataModelTovar> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNaim;
        TextView txtKol;
        TextView txtVid;
        TextView txtSummatovara;

        TextView txtId;

    }



    public CustomAdapterTovar(ArrayList<DataModelTovar> data, Context context) {
        super(context, R.layout.row_itemtovar, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelTovar dataModelTovar =(DataModelTovar)object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelTovar dataModelTovar = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_itemtovar, parent, false);
            viewHolder.txtNaim = (TextView) convertView.findViewById(R.id.Tovar_Name);
            viewHolder.txtKol = (TextView) convertView.findViewById(R.id.Tovar_Cols);
            viewHolder.txtVid = (TextView) convertView.findViewById(R.id.Tovar_Head);
            viewHolder.txtSummatovara = (TextView) convertView.findViewById(R.id.Tovar_Summa);
            viewHolder.txtId = (TextView) convertView.findViewById(R.id.idPos);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtNaim.setText(dataModelTovar.getNaim());

        String toCount2 = dataModelTovar.getKol();

        if (toCount2!=null){
            viewHolder.txtKol.getLayoutParams().height = 150;
        }else {
            viewHolder.txtKol.getLayoutParams().height = 0;
        }
        viewHolder.txtKol.setText(dataModelTovar.getKol());


        String toCount = dataModelTovar.getVid();

        if (toCount!=null){
            viewHolder.txtVid.getLayoutParams().height = 150;
        }else {
            viewHolder.txtVid.getLayoutParams().height = 0;
        }
        viewHolder.txtVid.setText(dataModelTovar.getVid());





        viewHolder.txtId.setText(dataModelTovar.getId());

        viewHolder.txtSummatovara.setText(dataModelTovar.getSummatovara());

        return convertView;
    }


}
