package com.myauto.designer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Designer2 on 22.10.2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<ListItem> listitems;
    private Context context;

    public MyAdapter(List<ListItem> listItems, Context context){
        this.listitems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int postion){
        final ListItem listItem = listitems.get(postion);

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());

        holder.txtBase64.setText(listItem.getImg());
        holder.txtTel.setText(listItem.getMob());
        holder.txtData.setText(listItem.getData());
        holder.txtOtvet.setText(listItem.getOtvet());
        holder.txtInsta.setText(listItem.getInsta());



        Picasso.get().load(listItem.getImageUrl()).into(holder.imageView);


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("data ",listItem.getImg());
                Intent intent = new Intent (view.getContext(), detail_news.class);
                intent.putExtra("head", listItem.getHead());
                intent.putExtra("desc", listItem.getDesc());
                intent.putExtra("image", listItem.getImg());

                intent.putExtra("otvet", listItem.getOtvet());
                intent.putExtra("mob", listItem.getMob());

                intent.putExtra("insta", listItem.getInsta());
                //context.startActivity(ope);


                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){ return listitems.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;

        TextView txtBase64;
        TextView txtTel,txtData,txtOtvet,txtInsta;


        public ImageView imageView;
        public RelativeLayout relativeLayout;

	public ViewHolder(View itemView){
            super(itemView);
            textViewHead = (TextView) itemView.findViewById(R.id.textHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textDescription);

        txtBase64 = (TextView) itemView.findViewById(R.id.base64Pic);
        txtTel = (TextView) itemView.findViewById(R.id.textMob);
        txtData = (TextView) itemView.findViewById(R.id.textData);
        txtOtvet = (TextView) itemView.findViewById(R.id.textOtvet);
        txtInsta = (TextView) itemView.findViewById(R.id.textInsta);

            imageView = (ImageView) itemView.findViewById(R.id.ico);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.newsID);

        }
    }

}
