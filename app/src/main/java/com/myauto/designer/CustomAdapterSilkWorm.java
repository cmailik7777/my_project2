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
public class CustomAdapterSilkWorm extends ArrayAdapter<DataModelSilkWorm> implements View.OnClickListener{

    private ArrayList<DataModelSilkWorm> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNzakSilk;
        TextView txtVidprodSilk;
        TextView txtCenaSilk;
        TextView txtKolichestvoSilk;
        TextView txtSummaSilk;
        TextView txtDatoSilk;
        TextView txtDatzSilk;

        ImageView info;
    }



    public CustomAdapterSilkWorm(ArrayList<DataModelSilkWorm> data, Context context) {
        super(context, R.layout.row_itemsilkworm, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelSilkWorm dataModelSilkWorm =(DataModelSilkWorm)object;




        switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " + dataModelSilkWorm.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelSilkWorm dataModelSilkWorm = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_itemsilkworm, parent, false);
            viewHolder.txtNzakSilk = (TextView) convertView.findViewById(R.id.nzakSilk);
            viewHolder.txtVidprodSilk = (TextView) convertView.findViewById(R.id.vidprodSilk);
            viewHolder.txtCenaSilk = (TextView) convertView.findViewById(R.id.cenaSilk);
            viewHolder.txtKolichestvoSilk = (TextView) convertView.findViewById(R.id.kolichestvoSilk);
            viewHolder.txtSummaSilk = (TextView) convertView.findViewById(R.id.summaSilk);
            viewHolder.txtDatoSilk = (TextView) convertView.findViewById(R.id.datoSilk);
            viewHolder.txtDatzSilk = (TextView) convertView.findViewById(R.id.datzSilk);

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


        viewHolder.txtNzakSilk.setText(dataModelSilkWorm.getNzakSilk());
        viewHolder.txtVidprodSilk.setText(dataModelSilkWorm.getVidprodSilk());
        viewHolder.txtCenaSilk.setText(dataModelSilkWorm.getCenaSilk());
        viewHolder.txtKolichestvoSilk.setText(dataModelSilkWorm.getKolichestvoSilk());
        viewHolder.txtSummaSilk.setText(dataModelSilkWorm.getSummaSilk());
        viewHolder.txtDatoSilk.setText(dataModelSilkWorm.getDatoSilk());
        viewHolder.txtDatzSilk.setText(dataModelSilkWorm.getDatzSilk());

        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


}
