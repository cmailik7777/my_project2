package com.myauto.designer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapterFinalRemz extends ArrayAdapter<DataModelFinalRemz> implements View.OnClickListener{

    private ArrayList<DataModelFinalRemz> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {

        TextView txtTextFinalListRemzRemz;
        TextView txtTextFinalListRemzDatao;
        TextView txtTextFinalListRemzDataZ;
        TextView txtTextFinalListRemzVin;

        TextView txtTextFinalListRemzNomerDvig;
        TextView txtTextFinalListRemzGos;
        TextView txtTextFinalListRemzOdometr;
        TextView txtTextFinalListRemzClient;
        TextView txtTextFinalListRemzTel;

        TextView txtTextFinalListRemzNachis;
        TextView txtTextFinalListRemzOplach;
        TextView txtTextFinalListRemzBalanc;
        TextView txtTextFinalListRemzKomment;
        TextView txtTextFinalListRemzKommentManag;

        TextView txtTextFinalListRemzMarka;
        TextView txtTextFinalListRemzAccept;


    }



    public CustomAdapterFinalRemz(ArrayList<DataModelFinalRemz> data, Context context) {
        super(context, R.layout.row_final_remz, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelFinalRemz dataModelFinalRemz =(DataModelFinalRemz)object;




    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelFinalRemz dataModelFinalRemz = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_final_remz, parent, false);
            viewHolder.txtTextFinalListRemzRemz = (TextView) convertView.findViewById(R.id.FinalListRemzRemz);
            viewHolder.txtTextFinalListRemzDatao = (TextView) convertView.findViewById(R.id.FinalListRemzDatao);
            viewHolder.txtTextFinalListRemzDataZ = (TextView) convertView.findViewById(R.id.FinalListRemzDataZ);
            viewHolder.txtTextFinalListRemzVin = (TextView) convertView.findViewById(R.id.FinalListRemzVin);

            viewHolder.txtTextFinalListRemzNomerDvig = (TextView) convertView.findViewById(R.id.FinalListRemzNomerDvig);
            viewHolder.txtTextFinalListRemzGos = (TextView) convertView.findViewById(R.id.FinalListRemzGos);
            viewHolder.txtTextFinalListRemzOdometr = (TextView) convertView.findViewById(R.id.FinalListRemzOdometr);
            viewHolder.txtTextFinalListRemzClient = (TextView) convertView.findViewById(R.id.FinalListRemzClient);
            viewHolder.txtTextFinalListRemzTel = (TextView) convertView.findViewById(R.id.FinalListRemzTel);

            viewHolder.txtTextFinalListRemzNachis = (TextView) convertView.findViewById(R.id.FinalListRemzNachis);
            viewHolder.txtTextFinalListRemzOplach = (TextView) convertView.findViewById(R.id.FinalListRemzOplach);
            viewHolder.txtTextFinalListRemzBalanc = (TextView) convertView.findViewById(R.id.FinalListRemzBalanc);
            viewHolder.txtTextFinalListRemzKomment = (TextView) convertView.findViewById(R.id.FinalListRemzKomment);
            viewHolder.txtTextFinalListRemzKommentManag = (TextView) convertView.findViewById(R.id.FinalListRemzKommentManag);

            viewHolder.txtTextFinalListRemzMarka = (TextView) convertView.findViewById(R.id.FinalListRemzMarka);
            viewHolder.txtTextFinalListRemzAccept = (TextView) convertView.findViewById(R.id.FinalListRemzAccept);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }



        viewHolder.txtTextFinalListRemzRemz.setText(dataModelFinalRemz.getTextFinalListRemzRemz());
        viewHolder.txtTextFinalListRemzDatao.setText(dataModelFinalRemz.getTextFinalListRemzDatao());
        viewHolder.txtTextFinalListRemzDataZ.setText(dataModelFinalRemz.getTextFinalListRemzDataZ());
        viewHolder.txtTextFinalListRemzVin.setText(dataModelFinalRemz.getTextFinalListRemzVin());

        viewHolder.txtTextFinalListRemzNomerDvig.setText(dataModelFinalRemz.getTextFinalListRemzNomerDvig());
        viewHolder.txtTextFinalListRemzGos.setText(dataModelFinalRemz.getTextFinalListRemzGos());
        viewHolder.txtTextFinalListRemzOdometr.setText(dataModelFinalRemz.getTextFinalListRemzOdometr());
        viewHolder.txtTextFinalListRemzClient.setText(dataModelFinalRemz.getTextFinalListRemzClient());
        viewHolder.txtTextFinalListRemzTel.setText(dataModelFinalRemz.getTextFinalListRemzTel());

        viewHolder.txtTextFinalListRemzNachis.setText(dataModelFinalRemz.getTextFinalListRemzNachis());
        viewHolder.txtTextFinalListRemzOplach.setText(dataModelFinalRemz.getTextFinalListRemzOplach());
        viewHolder.txtTextFinalListRemzBalanc.setText(dataModelFinalRemz.getTextFinalListRemzBalanc());
        viewHolder.txtTextFinalListRemzKomment.setText(dataModelFinalRemz.getTextFinalListRemzKomment());
        viewHolder.txtTextFinalListRemzKommentManag.setText(dataModelFinalRemz.getTextFinalListRemzKommentManag());

        viewHolder.txtTextFinalListRemzMarka.setText(dataModelFinalRemz.getTextFinalListRemzMarka());
        viewHolder.txtTextFinalListRemzAccept.setText(dataModelFinalRemz.getTextFinalListRemzAccept());

        // Return the completed view to render on screen
        return convertView;
    }


}
