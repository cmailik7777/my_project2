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
 * Created by Designer2 on 18.06.2018.
 */

public class CustomAdapterMarket extends ArrayAdapter<DataModelMarket> implements View.OnClickListener{

    private ArrayList<DataModelMarket> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView imgOne;
        ImageView imgTwo;
        ImageView imgThree;
        TextView txtCity;
        TextView txtSumma;
        TextView txtArticle;
        TextView txtDate;
        TextView txtBrend;
        TextView txtHead;
        TextView txtDesc;
    }



    public CustomAdapterMarket(ArrayList<DataModelMarket> data, Context context) {
        super(context, R.layout.row_market, data);
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
        DataModelMarket dataModelMarket = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_market, parent, false);
            viewHolder.imgOne = (ImageView) convertView.findViewById(R.id.Market_list_one);
            viewHolder.imgTwo = (ImageView) convertView.findViewById(R.id.Market_list_two);
            viewHolder.imgThree = (ImageView) convertView.findViewById(R.id.Market_list_three);

            viewHolder.txtCity = (TextView) convertView.findViewById(R.id.market_list_city);
            viewHolder.txtSumma = (TextView) convertView.findViewById(R.id.market_list_summa);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.market_list_date);
            viewHolder.txtBrend = (TextView) convertView.findViewById(R.id.market_list_brend);
            viewHolder.txtHead = (TextView) convertView.findViewById(R.id.market_list_head);
            viewHolder.txtDesc = (TextView) convertView.findViewById(R.id.market_list_text);
            viewHolder.txtArticle = (TextView) convertView.findViewById(R.id.market_list_article);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if(dataModelMarket.getImgOne().equals("")){

        }else{
            byte[] decodedString = Base64.decode(dataModelMarket.getImgOne(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            viewHolder.imgOne.setImageBitmap(decodedByte);
        }

        if(dataModelMarket.getImgTwo().equals("")){

        }else{
            byte[] decodedString = Base64.decode(dataModelMarket.getImgTwo(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            viewHolder.imgTwo.setImageBitmap(decodedByte);
        }

        if(dataModelMarket.getImgThree().equals("")){

        }else{
            byte[] decodedString = Base64.decode(dataModelMarket.getImgThree(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            viewHolder.imgThree.setImageBitmap(decodedByte);
        }


        viewHolder.txtCity.setText(dataModelMarket.getCity());
        viewHolder.txtSumma.setText(dataModelMarket.getSumma());
        viewHolder.txtDate.setText(dataModelMarket.getDate());
        viewHolder.txtBrend.setText(dataModelMarket.getBrend());
        viewHolder.txtHead.setText(dataModelMarket.getHead());
        viewHolder.txtDesc.setText(dataModelMarket.getDesc());
        viewHolder.txtArticle.setText(dataModelMarket.getArticle());
        // Return the completed view to render on screen
        return convertView;
    }


}