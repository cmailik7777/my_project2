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
import android.widget.ImageView;
import android.widget.TextView;

public class detailAd extends AppCompatActivity {

    ImageView market_list_one;
    ImageView market_list_two;
    ImageView market_list_three;

    TextView city,summa,date,article,brend,head,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        String cityS = intent.getStringExtra("city");
        String summaS = intent.getStringExtra("summa");
        String articleS = intent.getStringExtra("article");
        String dateS = intent.getStringExtra("date");
        String brendS = intent.getStringExtra("brend");
        String headS = intent.getStringExtra("head");
        setTitle(headS);
        String textS = intent.getStringExtra("text");



        market_list_one = (ImageView)findViewById(R.id.MarketDetail_list_one);
        market_list_two = (ImageView)findViewById(R.id.MarketDetail_list_two);
        market_list_three = (ImageView)findViewById(R.id.MarketDetail_list_three);

        city = (TextView) findViewById(R.id.marketDetail_list_city);
        summa = (TextView) findViewById(R.id.marketDetail_list_summa);
        date = (TextView) findViewById(R.id.marketDetail_list_date);
        article = (TextView) findViewById(R.id.marketDetail_list_article);
        brend = (TextView) findViewById(R.id.marketDetail_list_brend);
        head = (TextView) findViewById(R.id.marketDetail_list_head);
        desc = (TextView) findViewById(R.id.marketDetail_list_text);

        String oneS = intent.getStringExtra("one");
        String twoS = intent.getStringExtra("two");
        String threeS = intent.getStringExtra("three");

        byte[] decodedString = Base64.decode(oneS, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        market_list_one.setImageBitmap(decodedByte);

        byte[] decodedString2 = Base64.decode(twoS, Base64.DEFAULT);
        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
        market_list_two.setImageBitmap(decodedByte2);

        byte[] decodedString3 = Base64.decode(threeS, Base64.DEFAULT);
        Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0,decodedString3.length);
        market_list_three.setImageBitmap(decodedByte3);


        city.setText(cityS);
        summa.setText(summaS);
        date.setText(dateS);
        article.setText(articleS);
        brend.setText(brendS);
        head.setText(headS);
        desc.setText(textS);


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
        Log.e("-----DetailAD-----", "onPause");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----DetailAD-----", "onResume");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----DetailAD-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----DetailAD-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----DetailAD-----", "onStart");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----DetailAD-----", "onRestart");
    }

}
