package com.myauto.designer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SeeAd extends AppCompatActivity {

    TextView head,desc,otvet,mob;
    ImageView banner;
    Button insta,call,message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        banner = (ImageView) findViewById(R.id.imageView14);

        head = (TextView)findViewById(R.id.see_ad_head);
        desc = (TextView)findViewById(R.id.see_ad_desc);
        otvet = (TextView)findViewById(R.id.see_ad_otvet);
        mob = (TextView)findViewById(R.id.see_ad_mob);

/*        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        setTitle("");

        Intent intent = getIntent();

        String headS = intent.getStringExtra("head");
        String textS = intent.getStringExtra("desc");
        String imageS = intent.getStringExtra("image");

        String otvetS = intent.getStringExtra("otvet");
        String mobS = intent.getStringExtra("mob");


        byte[] decodedString = Base64.decode(imageS, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        banner.setImageBitmap(decodedByte);

        head.setText(headS);
        desc.setText(textS);
        otvet.setText(otvetS);
        mob.setText(mobS);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----SeeAd-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----SeeAd-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----SeeAd-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----SeeAd-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----SeeAd-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----SeeAd-----", "onRestart");
    }
}
