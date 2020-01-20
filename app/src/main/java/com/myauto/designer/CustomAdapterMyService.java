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
 * Created by Designer2 on 11.06.2018.
 */

public class CustomAdapterMyService extends ArrayAdapter<DataModelMyService> implements View.OnClickListener{

    private ArrayList<DataModelMyService> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView txtPhoto;
        TextView txtName;
        TextView txtType;
        TextView txtTel;
    }



    public CustomAdapterMyService(ArrayList<DataModelMyService> data, Context context) {
        super(context, R.layout.row_item_service, data);
        this.dataSet = data;
        this.mContext=context;
    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelGN dataModelGN =(DataModelGN)object;



    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelMyService dataModelMyService = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_service, parent, false);
            viewHolder.txtPhoto = (ImageView) convertView.findViewById(R.id.Service_photo);

            viewHolder.txtName = (TextView) convertView.findViewById(R.id.Service_Name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.Service_Type);
            viewHolder.txtTel = (TextView) convertView.findViewById(R.id.Service_Tel);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if(dataModelMyService.getPhoto().equals("")){

        }else{
            byte[] decodedString = Base64.decode(dataModelMyService.getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            viewHolder.txtPhoto.setImageBitmap(decodedByte);
        }

        viewHolder.txtName.setText(dataModelMyService.getName());
        viewHolder.txtType.setText(dataModelMyService.getType());
        viewHolder.txtTel.setText(dataModelMyService.getTel());







        // Return the completed view to render on screen
        return convertView;
    }


}
