package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class rzMehanic_detail extends AppCompatActivity {

    ArrayList<DataModelRZMehanic_rab_Detail> dataModelRZMehanic_rab_details;
    ListView listView;
    private static CustomAdapterRZMehanic_rab_Detail adapter;

    ProgressDialog progressDialog;

    private getRabotMehanic getRabotMehanic;

    String num,IPSTRING;

    Button add;
    JSONArray jsonBrigada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rz_mehanic_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        Log.e("num",num);

        setTitle(num);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //-------------------------ListView------------------------------------------------------
        listView = (ListView) findViewById(R.id.rz_mehanic_detail_listview);
        //-------------------------//ListView----------------------------------------------------

        add = (Button)findViewById(R.id.rz_mehanic_detail_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ope = new Intent(rzMehanic_detail.this, main_menu_Mehanic.class);
                ope.putExtra("num", num);
                startActivity(ope);
            }
        });

        getMehanicRabot letTast = new getMehanicRabot();
        letTast.execute();

    }

    class getMehanicRabot extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(rzMehanic_detail.this);
            progressDialog.setMessage("Поиск выполненых работ по "+num);
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
            //-------------------------------------------------------------------------------------------------------------------------------------------
            getRabotMehanic = new getRabotMehanic();
            getRabotMehanic.start(mob, pin, num);

            try {
                getRabotMehanic.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getRabotMehanic.getResult();
            try {
                dataModelRZMehanic_rab_details = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){

                }else{
                    /*
                    * {
                        "empty": "ok",
                        "brigada":[{"brigada":"A0106"},{"brigada":"A0401"}],"res"
                        "res": [{
                            "oper":"Реставрация 2-х дисков, шиномонтаж, балансировка, сварка, прокатка,  выполнить",
                            "brig":"A0110",
                            "time":"0",
                            "ispoln":"",
                            "categ":"Шиномонтаж, балансировка, ремонт шин, геометрия",
                            "cod":"000102328",
                            "data":"02.07.2018 14:47:50",
                            "otvetstven":"Нагиленко Виктор"}
                    }*/

                    jsonBrigada = jsonObject.getJSONArray("brigada");

                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        String oper = jsonArray.getJSONObject(i).getString("oper");
                        if (oper.isEmpty()){
                            oper=" ";
                        }
                        Log.e("oper ",oper);

                        String brig = jsonArray.getJSONObject(i).getString("brig");
                        if (brig.isEmpty()){
                            brig=" ";
                        }
                        Log.e("brig ",brig);

                        String time = jsonArray.getJSONObject(i).getString("time");
                        if (time.isEmpty()){
                            time=" ";
                        }
                        Log.e("time ",time);
                        String categ = jsonArray.getJSONObject(i).getString("categ");
                        if (categ.isEmpty()){
                            categ=" ";
                        }
                        Log.e("categ ",categ);
                        String otvetstven = jsonArray.getJSONObject(i).getString("otvetstven");
                        if (otvetstven.isEmpty()){
                            otvetstven=" ";
                        }
                        Log.e("otvetstven ",otvetstven);

                        String ispoln = jsonArray.getJSONObject(i).getString("ispoln");
                        if (ispoln.isEmpty()){
                            ispoln=" ";
                        }
                        Log.e("ispoln ",ispoln);

                        String data = jsonArray.getJSONObject(i).getString("data");
                        if (data.isEmpty()){
                            data=" ";
                        }
                        Log.e("data ",data);
                        String cod = jsonArray.getJSONObject(i).getString("cod");
                        if (cod.isEmpty()){
                            cod=" ";
                        }
                        Log.e("cod ",cod);

                        final String finalBrig = brig;
                        final String finalTime = time;
                        final String finalOtvetstven = otvetstven;
                        final String finalCateg = categ;
                        final String finalOper1 = oper;
                        final String finalCod = cod;
                        final String finalIspoln = ispoln;
                        final String finalData = data;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataModelRZMehanic_rab_details.add(new DataModelRZMehanic_rab_Detail(finalBrig, finalTime, finalOtvetstven, finalCateg, finalOper1, finalCod, finalIspoln, finalData));
                                adapter= new CustomAdapterRZMehanic_rab_Detail(dataModelRZMehanic_rab_details,getApplicationContext());

                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView tv = (TextView)view.findViewById(R.id.rab_oper_mehanic_brig);
                                        TextView data = (TextView)view.findViewById(R.id.rab_oper_mehanic_data);
                                        TextView code = (TextView)view.findViewById(R.id.rab_oper_mehanic_cod);
                                        String brigLV = tv.getText().toString();


                                        /*if (checkBox.isChecked()){
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    "if", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }else{
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    "else", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }*/

                                          try {
                                                for (int i = 0; i < jsonBrigada.length(); i++) {
                                                    String brigada = jsonBrigada.getJSONObject(i).getString("brigada");
                                                    Log.e("brigada ",brigada);
                                                    if (brigada.equals(brigLV)){
                                                        Intent ope = new Intent(rzMehanic_detail.this, choiceBrig.class);
                                                        ope.putExtra("num", num);
                                                        ope.putExtra("jsonBrigada", " ");
                                                        ope.putExtra("brig", brigada);

                                                        ope.putExtra("data", data.getText().toString());
                                                        ope.putExtra("cod", code.getText().toString());
                                                        startActivity(ope);

                                                        Log.e("brigada.equals(brigLV)",brigada);
                                                    }else if (brigLV.equals(" ")){
                                                        Intent ope = new Intent(rzMehanic_detail.this, choiceBrig.class);
                                                        ope.putExtra("num", num);
                                                        ope.putExtra("jsonBrigada", String.valueOf(jsonBrigada));
                                                        ope.putExtra("brig", " ");

                                                        ope.putExtra("data", data.getText().toString());
                                                        ope.putExtra("cod", code.getText().toString());
                                                        startActivity(ope);

                                                        Log.e("brigLV.equals(\" \")",brigLV);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                    }
                                });
                            }
                        });
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

    public class getRabotMehanic extends Thread {

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

            Log.e("mob", MobS);
            Log.e("PinS", PinS);
            Log.e("NumS", NumS);
            //getRZProduct.php

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getRabOperMehanik.php");
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
