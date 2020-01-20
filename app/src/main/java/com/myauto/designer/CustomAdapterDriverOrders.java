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
 * Created by Designer2 on 31.07.2018.
 */

public class CustomAdapterDriverOrders extends ArrayAdapter<DataModelDriverOrders> implements View.OnClickListener{

    private ArrayList<DataModelDriverOrders> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtPref1;
        ImageView txtIcon1;

        TextView txtPref2;
        ImageView txtIcon2;

        TextView txtPref3;
        ImageView txtIcon3;

        TextView txtPref4;
        ImageView txtIcon4;

        TextView txtPref5;
        ImageView txtIcon5;

        TextView txtPref6;
        ImageView txtIcon6;

        TextView txtOrderdate;
        TextView txtOrderNum;
    }



    public CustomAdapterDriverOrders(ArrayList<DataModelDriverOrders> data, Context context) {
        super(context, R.layout.list_group, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        /*int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelGN dataModelGN =(DataModelGN)object;




        switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " + dataModelGN.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }*/


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelDriverOrders dataModelDriverOrders = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_group, parent, false);

            viewHolder.txtPref1 = (TextView) convertView.findViewById(R.id.driver_order_pref1);
            viewHolder.txtIcon1 = (ImageView) convertView.findViewById(R.id.driver_order_icon1);

            viewHolder.txtPref2 = (TextView) convertView.findViewById(R.id.driver_order_pref2);
            viewHolder.txtIcon2 = (ImageView) convertView.findViewById(R.id.driver_order_icon2);

            viewHolder.txtPref3 = (TextView) convertView.findViewById(R.id.driver_order_pref3);
            viewHolder.txtIcon3 = (ImageView) convertView.findViewById(R.id.driver_order_icon3);

            viewHolder.txtPref4 = (TextView) convertView.findViewById(R.id.driver_order_pref4);
            viewHolder.txtIcon4 = (ImageView) convertView.findViewById(R.id.driver_order_icon4);

            viewHolder.txtPref5 = (TextView) convertView.findViewById(R.id.driver_order_pref5);
            viewHolder.txtIcon5 = (ImageView) convertView.findViewById(R.id.driver_order_icon5);

            viewHolder.txtPref6 = (TextView) convertView.findViewById(R.id.driver_order_pref6);
            viewHolder.txtIcon6 = (ImageView) convertView.findViewById(R.id.driver_order_icon6);

            viewHolder.txtOrderdate = (TextView) convertView.findViewById(R.id.driver_order_date);
            viewHolder.txtOrderNum = (TextView) convertView.findViewById(R.id.driver_order_race);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if (dataModelDriverOrders.getPref1().equals("")) {}else{
            viewHolder.txtPref1.setText(dataModelDriverOrders.getPref1());
            if (dataModelDriverOrders.getIcon1().equals("")) {}else{
                byte[] decodedString2 = Base64.decode(dataModelDriverOrders.getIcon1(), Base64.DEFAULT);
                Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
                viewHolder.txtIcon1.setImageBitmap(decodedByte2);
            }
        }
        if (dataModelDriverOrders.getPref2().equals("")) {
            viewHolder.txtPref2.setVisibility(View.INVISIBLE);
            viewHolder.txtIcon2.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.txtPref2.setText(dataModelDriverOrders.getPref2());
            if (dataModelDriverOrders.getIcon1().equals("")) {}else{
                byte[] decodedString2 = Base64.decode(dataModelDriverOrders.getIcon2(), Base64.DEFAULT);
                Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
                viewHolder.txtIcon2.setImageBitmap(decodedByte2);
            }
        }
        if (dataModelDriverOrders.getPref3().equals("")) {
            viewHolder.txtPref3.setVisibility(View.INVISIBLE);
            viewHolder.txtIcon3.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.txtPref3.setText(dataModelDriverOrders.getPref3());
            if (dataModelDriverOrders.getIcon1().equals("")) {}else{
                byte[] decodedString2 = Base64.decode(dataModelDriverOrders.getIcon3(), Base64.DEFAULT);
                Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
                viewHolder.txtIcon3.setImageBitmap(decodedByte2);
            }
        }
        if (dataModelDriverOrders.getPref4().equals("")) {
            viewHolder.txtPref4.setVisibility(View.INVISIBLE);
            viewHolder.txtIcon4.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.txtPref4.setText(dataModelDriverOrders.getPref4());
            if (dataModelDriverOrders.getIcon1().equals("")) {}else{
                byte[] decodedString2 = Base64.decode(dataModelDriverOrders.getIcon4(), Base64.DEFAULT);
                Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
                viewHolder.txtIcon4.setImageBitmap(decodedByte2);
            }
        }
        if (dataModelDriverOrders.getPref5().equals("")) {
            viewHolder.txtPref5.setVisibility(View.INVISIBLE);
            viewHolder.txtIcon5.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.txtPref5.setText(dataModelDriverOrders.getPref5());
            if (dataModelDriverOrders.getIcon1().equals("")) {}else{
                byte[] decodedString2 = Base64.decode(dataModelDriverOrders.getIcon5(), Base64.DEFAULT);
                Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
                viewHolder.txtIcon5.setImageBitmap(decodedByte2);
            }
        }
        if (dataModelDriverOrders.getPref6().equals("")) {
            viewHolder.txtPref6.setVisibility(View.INVISIBLE);
            viewHolder.txtIcon6.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.txtPref6.setText(dataModelDriverOrders.getPref6());
            if (dataModelDriverOrders.getIcon1().equals("")) {}else{
                byte[] decodedString2 = Base64.decode(dataModelDriverOrders.getIcon6(), Base64.DEFAULT);
                Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
                viewHolder.txtIcon6.setImageBitmap(decodedByte2);
            }
        }

        viewHolder.txtOrderNum.setText(dataModelDriverOrders.getOrderNum());
        viewHolder.txtOrderdate.setText(dataModelDriverOrders.getOrderDate());

        /*String toCountgetDatz = dataModelGN.getDatz();
        if(toCountgetDatz.equals("")){
            viewHolder.textDatz.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.textDatz.setVisibility(View.VISIBLE);
        }*/

        // Return the completed view to render on screen
        return convertView;
    }


}

