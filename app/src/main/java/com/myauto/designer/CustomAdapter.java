package com.myauto.designer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
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
 * Created by Designer2 on 01.12.2017.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtVersion,model;
        ImageView info;
        ImageView Image,Oil_Logo;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_driver_order, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

        switch (v.getId())
        {
            case R.id.item_info:
                Snackbar.make(v, "Дата въезда " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_driver_order, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.LabelListManagerMarka);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            viewHolder.model = (TextView) convertView.findViewById(R.id.Garaj_Model);

            viewHolder.Image = (ImageView) convertView.findViewById(R.id.garaj_Img);
            viewHolder.Oil_Logo = (ImageView) convertView.findViewById(R.id.Garaj_Oil_logo);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtType.setText(dataModel.getType());
        viewHolder.txtVersion.setText(dataModel.getVersion_number());

        if (dataModel.getModel().equals("")) {
            viewHolder.model.setText("не указано");
        }else{
            viewHolder.model.setText(dataModel.getModel());
        }

        if (dataModel.getOil_Logo().equals("")) {

        }else{
            byte[] decodedString2 = Base64.decode(dataModel.getOil_Logo(), Base64.DEFAULT);
            Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
            viewHolder.Oil_Logo.setImageBitmap(decodedByte2);
        }

        if (dataModel.getImage().equals("")) {

        }else{
            byte[] decodedString = Base64.decode(dataModel.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            viewHolder.Image.setImageBitmap(decodedByte);
        }






        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}