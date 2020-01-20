package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class rzArticle extends AppCompatActivity {

    ArrayList<DataModelrzArticle> dataModelrzArticles;
    ListView listView;
    private static CustomAdapterrzArticle adapter;

    ProgressDialog progressDialog;

    String num;

    private getArticle getArticle;

    String IPSTRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rz_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        Log.e("num",num);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //-------------------------ListView------------------------------------------------------
        listView = (ListView) findViewById(R.id.rz_article_listview);
        //-------------------------//ListView------------------------------------------------------


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ope = new Intent(rzArticle.this, addrzArticle.class);
                ope.putExtra("num", num);
                startActivity(ope);
            }
        });

        goArticle letTast = new goArticle();
        letTast.execute();





    }


    class goArticle extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(rzArticle.this);
            progressDialog.setMessage("Поиск запчастей по "+num+"...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String mob = prefs.getString("mob", null);
            String pin = prefs.getString("pin", null);
            String org = prefs.getString("org", null);
            //-------------------------------------------------------------------------------------------------------------------------------------------
            getArticle = new getArticle();
            getArticle.start(mob, pin, num);

            try {
                getArticle.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getArticle.getResult();
            try {
                dataModelrzArticles = new ArrayList<>();


                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(rzArticle.this);
                            builder.setTitle("Внимание!")
                                    .setMessage("Запчасти по данному ремонтному заказу "+num+" отсвутвуют!")
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //ifLabel.setVisibility(View.INVISIBLE);

                        }
                    });

                                    /*
                    "empty": "ok",
                    "res": [{
                            "product": "РЎС‚РµРєР»Рѕ Р»РѕР±РѕРІРѕРµ СЃ РѕР±РѕРіСЂРµРІРѕРј (РїРѕР»РЅС‹Р№ РѕР±РѕРіСЂРµРІ) РІ РєР»РµР№ TOYOTA LРЎ J200 /LEXUS 450-570",
                            "art": "56101-60958",
                            "unit": "С€С‚.", шт
                        "base": "",
                            "quantity": "1",
                            "price": "721В 250",
                        "amountskidka": "0",
                        "amountbz": "721В 250",
                            "amount": "721В 250",
                             "data": "18.09.2018 14:06:36",
                            "responsible": "Р“СѓСЃР°СЂРѕРІ Р”РµРЅРёСЃ",
                            "stock": "РћСЃРЅРѕРІРЅРѕР№",
                        "mol": "",
                        "accountability": "РќРµС‚"
                    },


                */

                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    int j = 1;
                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        String product = jsonArray.getJSONObject(i).getString("product");
                        if (product.isEmpty()){
                            product="Пусто";
                        }
                        Log.e("product ",product);

                        String art = jsonArray.getJSONObject(i).getString("art");
                        if (art.isEmpty()){
                            art="Пусто";
                        }
                        Log.e("art ",art);
                        String unit = jsonArray.getJSONObject(i).getString("unit");
                        if (unit.isEmpty()){
                            unit="Пусто";
                        }
                        Log.e("unit ",unit);

                        String quantity = jsonArray.getJSONObject(i).getString("quantity");
                        if (quantity.isEmpty()){
                            quantity="Пусто";
                        }
                        Log.e("quantity ",quantity);

                        String amount = jsonArray.getJSONObject(i).getString("amount");
                        if (amount.isEmpty()){
                            amount="Пусто";
                        }
                        Log.e("amount ",amount);
                        String data = jsonArray.getJSONObject(i).getString("data");
                        if (data.isEmpty()){
                            data="Пусто";
                        }
                        Log.e("data ",data);

                        String responsible = jsonArray.getJSONObject(i).getString("responsible");
                        if (responsible.isEmpty()){
                            responsible="Пусто";
                        }
                        Log.e("responsible ",responsible);

                        String stock = jsonArray.getJSONObject(i).getString("stock");
                        if (stock.isEmpty()){
                            stock="Пусто";
                        }
                        Log.e("stock ",stock);

                        String price = jsonArray.getJSONObject(i).getString("price");
                        if (price.isEmpty()){
                            price="Пусто";
                        }
                        Log.e("price ",price);

                        final String count = String.valueOf(j);

                        final String finalData = data;
                        final String finalUnit = unit;
                        final String finalQuantity = quantity+" ";
                        final String finalResponsible = responsible;
                        final String finalArt = art;
                        final String finalProduct = product;
                        final String finalAmount = amount;
                        final String finalStock = stock;
                        final String finalPrice = price;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataModelrzArticles.add(new DataModelrzArticle(count, finalData, finalArt, finalResponsible, finalQuantity + finalUnit, finalPrice, finalStock, finalAmount, finalProduct));
                                adapter= new CustomAdapterrzArticle(dataModelrzArticles,getApplicationContext());

                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView tv = (TextView)view.findViewById(R.id.mini_rab_oper_oper);
                                       /* Intent ope = new Intent(sozdat_rab_oper.this, detail_sozdat_rab_oper.class);
                                        ope.putExtra("num", num);
                                        ope.putExtra("name", tv.getText().toString());
                                        startActivity(ope);*/
                                    }
                                });
                            }
                        });



                        j=j+1;
                    }
                    //
                }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }

    public class getArticle extends Thread {

        /*
            "empty": "ok",
            "res": [{
                "brig": "B0404"

        */

        String MobS;
        String PinS;
        String NumS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(3);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", NumS));
            //getRZProduct.php

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getRZProduct.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection succes");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "[}][,][{]");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection succes " + result);
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }
            try {
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }

        public void start(String mobp, String pinp, String nump) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.NumS = nump;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
