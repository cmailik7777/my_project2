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
 * Created by Designer2 on 19.09.2018.
 */

public class CustomAdapterrzArticle extends ArrayAdapter<DataModelrzArticle> implements View.OnClickListener{

    private ArrayList<DataModelrzArticle> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtCount;
        TextView txtDate;
        TextView txtArt;
        TextView txtCol;
        TextView txtSumma;
        TextView txtStock;
        TextView txtAmount;
        TextView txtNomen;
        TextView txtOtvet;

    }



    public CustomAdapterrzArticle(ArrayList<DataModelrzArticle> data, Context context) {
        super(context, R.layout.row_rz_article, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelPush dataModelPush =(DataModelPush)object;




    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelrzArticle dataModelrzArticle = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            TextView txtCount;
            TextView txtDate;
            TextView txtArt;
            TextView txtCol;
            TextView txtSumma;
            TextView txtStock;
            TextView txtAmount;
            TextView txtNomen;
            TextView txtOtvet;

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_rz_article, parent, false);
            viewHolder.txtCount = (TextView) convertView.findViewById(R.id.rz_article_count);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.rz_article_date);
            viewHolder.txtArt = (TextView) convertView.findViewById(R.id.rz_article_art);
            viewHolder.txtCol = (TextView) convertView.findViewById(R.id.rz_article_col);
            viewHolder.txtSumma = (TextView) convertView.findViewById(R.id.rz_article_summa);
            viewHolder.txtStock = (TextView) convertView.findViewById(R.id.rz_article_stock);
            viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.rz_article_amount);
            viewHolder.txtNomen = (TextView) convertView.findViewById(R.id.rz_article_nomen);
            viewHolder.txtOtvet = (TextView) convertView.findViewById(R.id.rz_article_otvet);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtCount.setText(dataModelrzArticle.getCount());
        viewHolder.txtDate.setText(dataModelrzArticle.getDate());
        viewHolder.txtArt.setText(dataModelrzArticle.getArticle());
        viewHolder.txtCol.setText(dataModelrzArticle.getKol());
        viewHolder.txtSumma.setText(dataModelrzArticle.getSumma());
        viewHolder.txtStock.setText(dataModelrzArticle.getStock());
        viewHolder.txtAmount.setText(dataModelrzArticle.getAmount());
        viewHolder.txtNomen.setText(dataModelrzArticle.getNomen());
        viewHolder.txtOtvet.setText(dataModelrzArticle.getOtvet());

        // Return the completed view to render on screen
        return convertView;
    }


}