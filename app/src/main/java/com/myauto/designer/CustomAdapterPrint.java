package com.myauto.designer;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapterPrint extends ArrayAdapter<DataModelPrint> implements View.OnClickListener{

    private ArrayList<DataModelPrint> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNzak;
        TextView txtVidprod;
        TextView txtCena;
        TextView txtKolichestvo;
        TextView txtSummaprint;
        TextView txtDatoprint;
        TextView txtDatzprint;

        ImageView info;
    }



    public CustomAdapterPrint(ArrayList<DataModelPrint> data, Context context) {
        super(context, R.layout.row_itemprint, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelPrint dataModelPrint =(DataModelPrint)object;




        switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " + dataModelPrint.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelPrint dataModelPrint = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_itemprint, parent, false);
            viewHolder.txtNzak = (TextView) convertView.findViewById(R.id.nzak);
            viewHolder.txtVidprod = (TextView) convertView.findViewById(R.id.vidprod);
            viewHolder.txtCena = (TextView) convertView.findViewById(R.id.cena);
            viewHolder.txtKolichestvo = (TextView) convertView.findViewById(R.id.kolichestvo);
            viewHolder.txtSummaprint = (TextView) convertView.findViewById(R.id.summaprint);
            viewHolder.txtDatoprint = (TextView) convertView.findViewById(R.id.datoprint);
            viewHolder.txtDatzprint = (TextView) convertView.findViewById(R.id.datzprint);

            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtNzak.setText(dataModelPrint.getNzak());
        viewHolder.txtVidprod.setText(dataModelPrint.getVidprod());
        viewHolder.txtCena.setText(dataModelPrint.getCena());
        viewHolder.txtKolichestvo.setText(dataModelPrint.getKolichestvo());
        viewHolder.txtSummaprint.setText(dataModelPrint.getSummaprint());
        viewHolder.txtDatoprint.setText(dataModelPrint.getDatoprint());
        viewHolder.txtDatzprint.setText(dataModelPrint.getDatzprint());

        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


}
