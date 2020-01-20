package com.myauto.designer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Designer2 on 01.08.2018.
 */

public class CustomAdapterOrderDetail extends ArrayAdapter<DataModelOrderDetail> implements View.OnClickListener{

    private ArrayList<DataModelOrderDetail> dataSet;
    Context mContext;
    private zaprosOpenDriverOrders zaprosOpenDriverOrders;
    private static final int PERMISSION_REQUEST_CAMERA = 0;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNum;
        TextView txtss_owner;
        TextView txtstart_Org;
        TextView txtstart_Adress;
        TextView txtstart_Tel;
        TextView txtstart_Lico;

        TextView txtstop_Org;
        TextView txtstop_Adress;
        TextView txtstop_Tel;
        TextView txtstop_Lico;
        TextView txtBarcode;
        TextView txtss_statys;
        TextView txtss_who;
        TextView ss_header;

        TextView txtInfo;

        TextView txtNeedQR;




    }



    public CustomAdapterOrderDetail(ArrayList<DataModelOrderDetail> data, Context context) {
        super(context, R.layout.list_drivers_orders, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {

        /*int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelOrderDetail dataModelOrderDetail =(DataModelOrderDetail)object;


        switch (v.getId()){
            case R.id.get_gruz:
                String toCount = dataModelOrderDetail.getWho();
                if(toCount.equals("")){
                    try {
                        zaprosOpenDriverOrders = new zaprosOpenDriverOrders();
                        zaprosOpenDriverOrders.start(mobDr, pinDr,dataModelOrderDetail.getNum());
                        Log.e("dataModelOrderDetailNum",dataModelOrderDetail.getNum());
                        zaprosOpenDriverOrders.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    final String names = zaprosOpenDriverOrders.resOrg();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage(names)
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }).start();

                }else{

                }

                    Snackbar.make(v, "bar " + dataModelOrderDetail.getBarcode(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                break;
        }*/

    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelOrderDetail dataModelOrderDetail = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_drivers_orders, parent, false);

            viewHolder.txtNum = (TextView) convertView.findViewById(R.id.lblListItem);
            viewHolder.txtss_owner = (TextView) convertView.findViewById(R.id.ss_owner);
            viewHolder.txtInfo = (TextView) convertView.findViewById(R.id.ss_info);

            viewHolder.txtstart_Org = (TextView) convertView.findViewById(R.id.start_Org);
            viewHolder.txtstart_Adress = (TextView) convertView.findViewById(R.id.start_Adress);
            viewHolder.txtstart_Tel = (TextView) convertView.findViewById(R.id.start_Tel);
            viewHolder.txtstart_Lico = (TextView) convertView.findViewById(R.id.start_Lico);

            viewHolder.txtstop_Org = (TextView) convertView.findViewById(R.id.stop_Org);
            viewHolder.txtstop_Adress = (TextView) convertView.findViewById(R.id.stop_Adress);
            viewHolder.txtstop_Tel = (TextView) convertView.findViewById(R.id.stop_Tel);
            viewHolder.txtstop_Lico = (TextView) convertView.findViewById(R.id.stop_Lico);
            viewHolder.txtss_statys = (TextView) convertView.findViewById(R.id.ss_statys);

            viewHolder.txtss_who = (TextView) convertView.findViewById(R.id.ss_who);
            viewHolder.txtBarcode = (TextView) convertView.findViewById(R.id.ss_barcode);
            viewHolder.txtNeedQR = (TextView) convertView.findViewById(R.id.need_qr);

            viewHolder.ss_header = (TextView) convertView.findViewById(R.id.ss_header);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        String toCount = dataModelOrderDetail.getWho();
        String stat = dataModelOrderDetail.getStatys();
        if (toCount.equals("0")){
            viewHolder.ss_header.setVisibility(View.GONE);
        }else{
            viewHolder.ss_header.setVisibility(View.VISIBLE);
            if (stat.equals("Выполнение")){
                viewHolder.ss_header.setText("Нажмите на заявку чтобы ее завершить");
            }
            if (stat.equals("Выполнена")){
                viewHolder.ss_header.setVisibility(View.GONE);
            }
        }


        if (stat.equals("Выполнение")){
            viewHolder.txtss_statys.setTextColor(Color.parseColor("#ffa200"));
        }
        if (stat.equals("Выполнена")){
            viewHolder.txtss_statys.setTextColor(Color.parseColor("#2bab00"));
        }
        if (stat.equals("Ожидание")){
            viewHolder.txtss_statys.setTextColor(Color.parseColor("#8800ff"));
        }


        viewHolder.txtss_who.setText(dataModelOrderDetail.getWho());
        viewHolder.txtBarcode.setText(dataModelOrderDetail.getBarcode());

        if(dataModelOrderDetail.getBarcode().equals("")){
            viewHolder.txtNeedQR.setVisibility(View.GONE);
        }else {
            viewHolder.txtNeedQR.setVisibility(View.VISIBLE);
        }

        viewHolder.txtss_statys.setText(dataModelOrderDetail.getStatys());

        viewHolder.txtNum.setText(dataModelOrderDetail.getNum());
        viewHolder.txtss_owner.setText(dataModelOrderDetail.getOwner());
        viewHolder.txtInfo.setText(dataModelOrderDetail.getInfo());

        viewHolder.txtstart_Org.setText(dataModelOrderDetail.getStart_org());
        viewHolder.txtstart_Adress.setText(dataModelOrderDetail.getStart_adress());
        viewHolder.txtstart_Tel.setText(dataModelOrderDetail.getStart_tel());
        viewHolder.txtstart_Lico.setText(dataModelOrderDetail.getStop_cl());

        viewHolder.txtstop_Org.setText(dataModelOrderDetail.getStop_org());
        viewHolder.txtstop_Adress.setText(dataModelOrderDetail.getStop_adress());
        viewHolder.txtstop_Tel.setText(dataModelOrderDetail.getStop_tel());
        viewHolder.txtstop_Lico.setText(dataModelOrderDetail.getStop_cl());


        // Return the completed view to render on screen
        return convertView;
    }




}