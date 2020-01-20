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
 * Created by Designer2 on 19.06.2018.
 */

public class CustomAdapterMyAd extends ArrayAdapter<DataModelMyAd> implements View.OnClickListener{

    private ArrayList<DataModelMyAd> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {

        TextView txtId;
        TextView txtDate;
        TextView txtHeader;
        TextView txtText;
        TextView txtCost;
        TextView txtCity;
        TextView txtBrend;
        TextView txtArticle;
        TextView txtStatys;
        ImageView pic1,pic2,pic3;
    }



    public CustomAdapterMyAd(ArrayList<DataModelMyAd> data, Context context) {
        super(context, R.layout.row_itemgn, data);
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
        DataModelMyAd dataModelMyAd = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_my_ad, parent, false);

            TextView txtId;
            TextView txtDate;
            TextView txtHeader;
            TextView txtText;
            TextView txtCost;
            TextView txtCity;
            TextView txtBrend;
            TextView txtArticle;
            TextView txtStatys;
            ImageView pic1,pic2,pic3;



            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.myAd_Data);
            viewHolder.txtHeader = (TextView) convertView.findViewById(R.id.myAd_Head);
            viewHolder.txtText = (TextView) convertView.findViewById(R.id.myAd_text);

            viewHolder.txtCost = (TextView) convertView.findViewById(R.id.myAd_Cost);
            viewHolder.txtCity = (TextView) convertView.findViewById(R.id.myAd_City);
            viewHolder.txtBrend = (TextView) convertView.findViewById(R.id.myAd_Brend);
            viewHolder.txtArticle = (TextView) convertView.findViewById(R.id.myAd_Article);
            viewHolder.txtStatys = (TextView) convertView.findViewById(R.id.myAd_Statys);

            viewHolder.pic1 = (ImageView) convertView.findViewById(R.id.myAd_pic1);
            viewHolder.pic2 = (ImageView) convertView.findViewById(R.id.myAd_pic2);
            viewHolder.pic3 = (ImageView) convertView.findViewById(R.id.myAd_pic3);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtDate.setText(dataModelMyAd.getDate());
        viewHolder.txtHeader.setText(dataModelMyAd.getHeader());
        viewHolder.txtText.setText(dataModelMyAd.getText());
        viewHolder.txtCost.setText(dataModelMyAd.getCost());

        viewHolder.txtCost.setText(dataModelMyAd.getCost());
        viewHolder.txtCity.setText(dataModelMyAd.getCity());
        viewHolder.txtBrend.setText(dataModelMyAd.getBrend());
        viewHolder.txtArticle.setText(dataModelMyAd.getArticle());
        viewHolder.txtStatys.setText(dataModelMyAd.getStatys());

        if (dataModelMyAd.getPic1().equals("")) {

        }else{
            byte[] decodedString2 = Base64.decode(dataModelMyAd.getPic1(), Base64.DEFAULT);
            Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
            viewHolder.pic1.setImageBitmap(decodedByte2);
        }

        if (dataModelMyAd.getPic2().equals("")) {

        }else{
            byte[] decodedString2 = Base64.decode(dataModelMyAd.getPic2(), Base64.DEFAULT);
            Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
            viewHolder.pic2.setImageBitmap(decodedByte2);
        }

        if (dataModelMyAd.getPic3().equals("")) {

        }else{
            byte[] decodedString2 = Base64.decode(dataModelMyAd.getPic3(), Base64.DEFAULT);
            Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
            viewHolder.pic3.setImageBitmap(decodedByte2);
        }
        // Return the completed view to render on screen
        return convertView;
    }


}
