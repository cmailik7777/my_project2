package com.myauto.designer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
 * Created by Designer2 on 11.07.2018.
 */
                /*

                "res": [{
                    "set": "0",
                    "id": "000000002",
                    "nomer": "565CMB02",
                    "marka": "Mitsubishi",
                    "model": "[200] DELICA SPACE GEAR/CARGO (PA-PF# )",
                    "color": "Р·РµР»РµРЅС‹Р№",
                    "pic":

                 */

public class CustomAdapterDrBeginning extends ArrayAdapter<DataModelDrBeginning> implements View.OnClickListener{

    private ArrayList<DataModelDrBeginning> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtGos;
        TextView txtId;
        TextView txtMarka;
        TextView txtModel;
        TextView txtColor;

        ImageView pic;


    }



    public CustomAdapterDrBeginning(ArrayList<DataModelDrBeginning> data, Context context) {
        super(context, R.layout.row_item_dr_beginning, data);
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
        DataModelDrBeginning dataModelDrBeginning = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_dr_beginning, parent, false);

            viewHolder.txtId = (TextView) convertView.findViewById(R.id.dr_beginning_id);

            viewHolder.txtMarka = (TextView) convertView.findViewById(R.id.dr_beginning_marka);
            viewHolder.txtGos = (TextView) convertView.findViewById(R.id.dr_beginning_gos);
            viewHolder.txtModel = (TextView) convertView.findViewById(R.id.dr_beginning_model);
            viewHolder.txtColor = (TextView) convertView.findViewById(R.id.dr_beginning_color);

            viewHolder.pic = (ImageView) convertView.findViewById(R.id.dr_beginning_pic);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtId.setText(dataModelDrBeginning.getId());

        viewHolder.txtMarka.setText(dataModelDrBeginning.getMarka());
        viewHolder.txtGos.setText(dataModelDrBeginning.getNomer());
        viewHolder.txtModel.setText(dataModelDrBeginning.getModel());
        viewHolder.txtColor.setText(dataModelDrBeginning.getColor());
        if (dataModelDrBeginning.getPic().equals("")) {

        }else{
            byte[] decodedString = Base64.decode(dataModelDrBeginning.getPic(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            viewHolder.pic.setImageBitmap(decodedByte);
        }

        // Return the completed view to render on screen
        return convertView;
    }


}