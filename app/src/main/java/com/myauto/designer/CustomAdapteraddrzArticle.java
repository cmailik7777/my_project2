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
 * Created by Designer2 on 20.09.2018.
 */

public class CustomAdapteraddrzArticle extends ArrayAdapter<DataModeladdrzArticle> implements View.OnClickListener{

    private ArrayList<DataModeladdrzArticle> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtCount;
        TextView txtNomen;
        TextView txtPrice;
        TextView txtOrg;
        TextView txtCol;
        TextView txtNeedS,txtNeedI;
    }



    public CustomAdapteraddrzArticle(ArrayList<DataModeladdrzArticle> data, Context context) {
        super(context, R.layout.row_add_rz_article, data);
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
        DataModeladdrzArticle dataModeladdrzArticle = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_add_rz_article, parent, false);
            viewHolder.txtCount = (TextView) convertView.findViewById(R.id.add_rz_article_count);
            viewHolder.txtNomen = (TextView) convertView.findViewById(R.id.add_rz_article_nomen);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.add_rz_article_price);
            viewHolder.txtOrg = (TextView) convertView.findViewById(R.id.add_rz_article_org);
            viewHolder.txtCol = (TextView) convertView.findViewById(R.id.add_rz_article_col);
            viewHolder.txtNeedS = (TextView) convertView.findViewById(R.id.add_rz_article_needS);
            viewHolder.txtNeedI = (TextView) convertView.findViewById(R.id.add_rz_article_needI);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtCount.setText(dataModeladdrzArticle.getCount());
        viewHolder.txtNomen.setText(dataModeladdrzArticle.getNomen());
        viewHolder.txtPrice.setText(dataModeladdrzArticle.getPrice());
        viewHolder.txtOrg.setText(dataModeladdrzArticle.getOrg());
        viewHolder.txtCol.setText(dataModeladdrzArticle.getCol());
        viewHolder.txtNeedS.setText(dataModeladdrzArticle.getNeedS());
        viewHolder.txtNeedI.setText(dataModeladdrzArticle.getNeedI());

        // Return the completed view to render on screen
        return convertView;
    }


}