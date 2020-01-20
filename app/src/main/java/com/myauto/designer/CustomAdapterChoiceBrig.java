package com.myauto.designer;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Designer2 on 01.11.2018.
 */

public class CustomAdapterChoiceBrig extends ArrayAdapter<DataModelChoiceBrig> implements View.OnClickListener{

    private ArrayList<DataModelChoiceBrig> dataSet;
    Context mContext;
    //String WHOISTHIS;
    //ArrayList<String> List;
    static ArrayList ListBRIGStatic = new ArrayList();
    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtCod;

        CheckBox check;
    }

    public CustomAdapterChoiceBrig(ArrayList<DataModelChoiceBrig> data, Context context) {
        super(context, R.layout.row_choice_brig, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelChoiceBrig dataModelChoiceBrig =(DataModelChoiceBrig)object;

        switch (v.getId()){
            case R.id.checkBoxBrig:
                CheckBox checkBox = (CheckBox)v.findViewById(R.id.checkBoxBrig);
                if (checkBox.isChecked()){
                    Log.e("checkBox","if");
                    String string = dataModelChoiceBrig.getName();
                    ListBRIGStatic.add(string);
                }else{
                    Log.e("checkBox","else");
                    String string = dataModelChoiceBrig.getName();
                    ListBRIGStatic.remove(string);
                }
                Snackbar.make(v, "" + ListBRIGStatic, Snackbar.LENGTH_SHORT)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelChoiceBrig dataModelChoiceBrig = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_choice_brig, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.rowTextView);
            viewHolder.txtCod = (TextView) convertView.findViewById(R.id.rowCod);

            viewHolder.check = (CheckBox) convertView.findViewById(R.id.checkBoxBrig);

            result=convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtName.setText(dataModelChoiceBrig.getName());
        viewHolder.txtCod.setText(dataModelChoiceBrig.getCod());
        viewHolder.check.setOnClickListener(this);
        viewHolder.check.setTag(position);
        return convertView;
    }
}