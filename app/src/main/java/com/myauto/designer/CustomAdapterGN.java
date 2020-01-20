package com.myauto.designer;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
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
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapterGN extends ArrayAdapter<DataModelGN> implements View.OnClickListener{

    private ArrayList<DataModelGN> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtService;
        TextView txtRemz;
        TextView txtManager;
        TextView txtTel;
        TextView txtDato;
        TextView txtDatz,textDatz;
        TextView txtSumma;
        TextView txtStatys;
        ImageView info;


    }



    public CustomAdapterGN(ArrayList<DataModelGN> data, Context context) {
        super(context, R.layout.row_itemgn, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelGN dataModelGN =(DataModelGN)object;




        switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " + dataModelGN.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelGN dataModelGN = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_itemgn, parent, false);
            viewHolder.txtService = (TextView) convertView.findViewById(R.id.service);
            viewHolder.txtRemz = (TextView) convertView.findViewById(R.id.remz);
            viewHolder.txtManager = (TextView) convertView.findViewById(R.id.manager);
            viewHolder.txtTel = (TextView) convertView.findViewById(R.id.tel);

            viewHolder.txtDato = (TextView) convertView.findViewById(R.id.dato);
            viewHolder.txtDatz = (TextView) convertView.findViewById(R.id.datz);
            viewHolder.txtSumma = (TextView) convertView.findViewById(R.id.summa);
            viewHolder.txtStatys = (TextView) convertView.findViewById(R.id.statys);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            viewHolder.textDatz = (TextView) convertView.findViewById(R.id.datz1);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtService.setText(dataModelGN.getService());
        viewHolder.txtRemz.setText(dataModelGN.getRemz());
        viewHolder.txtManager.setText(dataModelGN.getManager());
        viewHolder.txtTel.setText(dataModelGN.getTel());

        viewHolder.txtDato.setText(dataModelGN.getDato());
        viewHolder.txtDatz.setText(dataModelGN.getDatz());
        String toCountgetDatz = dataModelGN.getDatz();
        if(toCountgetDatz.equals("")){
            viewHolder.textDatz.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.textDatz.setVisibility(View.VISIBLE);
        }



        viewHolder.txtSumma.setText(dataModelGN.getSumma());

        viewHolder.txtStatys.setText(dataModelGN.getStatys());
        String toCount = dataModelGN.getStatys();

        if (toCount.equals("Закрыт")){
            viewHolder.txtStatys.setTextColor(Color.parseColor("#fa0000"));
        }else if (toCount.equals("Долговой")){
            viewHolder.txtStatys.setTextColor(Color.parseColor("#e4e73e"));
        }else if (toCount.equals("Открыт")){
            viewHolder.txtStatys.setTextColor(Color.parseColor("#25c105"));
        }

        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


}
