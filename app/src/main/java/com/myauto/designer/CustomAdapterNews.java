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
 * Created by Designer2 on 03.07.2018.
 */

public class CustomAdapterNews extends ArrayAdapter<DataModelNews> implements View.OnClickListener{

    private ArrayList<DataModelNews> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView txtIco;
        TextView txtName;
        TextView txtBase64;
        TextView txtType;
        TextView txtTel,txtData,txtOtvet,txtInsta;
    }

    public CustomAdapterNews(ArrayList<DataModelNews> data, Context context) {
        super(context, R.layout.recyclerview_row, data);
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
        DataModelNews dataModelNews = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.recyclerview_row, parent, false);
            viewHolder.txtIco = (ImageView) convertView.findViewById(R.id.ico);
            viewHolder.txtBase64 = (TextView) convertView.findViewById(R.id.base64Pic);

            viewHolder.txtName = (TextView) convertView.findViewById(R.id.textHead);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.textDescription);
            viewHolder.txtTel = (TextView) convertView.findViewById(R.id.textMob);
            viewHolder.txtData = (TextView) convertView.findViewById(R.id.textData);

            viewHolder.txtOtvet = (TextView) convertView.findViewById(R.id.textOtvet);
            viewHolder.txtInsta = (TextView) convertView.findViewById(R.id.textInsta);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        String toCount = dataModelNews.getIcon();
        if(toCount.equals("")){

        }else{
            byte[] decodedString = Base64.decode(dataModelNews.getIcon(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            viewHolder.txtIco.setImageBitmap(decodedByte);
        }

        viewHolder.txtBase64.setText(dataModelNews.getImg());

        viewHolder.txtName.setText(dataModelNews.getHead());
        viewHolder.txtType.setText(dataModelNews.getDesc());
        viewHolder.txtTel.setText(dataModelNews.getMob());
        viewHolder.txtData.setText(dataModelNews.getData());

        viewHolder.txtOtvet.setText(dataModelNews.getOtvet());
        viewHolder.txtInsta.setText(dataModelNews.getInsta());

        // Return the completed view to render on screen
        return convertView;
    }


}