package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class Tovar extends AppCompatActivity {

    ArrayList<DataModelTovar> dataModelsTovar;
    ListView listView;
    private static CustomAdapterTovar adapter;


    String NaimenOper;
    String KolOper;
    String VidOper;
    String SummaOper;
    ProgressDialog progressDialog;
    String checkprog;

    String remZ;
    private tovar tovar1;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        remZ = intent.getStringExtra("RemZ");
        setTitle(remZ);
        progressBar = (ProgressBar)findViewById(R.id.tovar_Progressbar);
        listView = (ListView)findViewById(R.id.listTovar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //-------------------------------------------------------------------------------------------------------------------------------------------

                SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                String pin = prefs.getString("pin", null);
                tovar1 = new tovar();

                tovar1.start(remZ,mob,pin);

                try {
                    tovar1.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
                //--------------------------------------------------------------------------------------------------------------------------------------------


            }
        }).start();

    }

    public class tovar extends Thread {

        String remZ;
        String MobS,PinS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run(){

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(4);

            NameValuerPair.add(new BasicNameValuePair("num",remZ));
            NameValuerPair.add(new BasicNameValuePair("mob",MobS));
            NameValuerPair.add(new BasicNameValuePair("pin",PinS));

            Log.e("remZ",remZ);


            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getRepairTable.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection succes");
            } catch (Exception e){
                Log.e("Fail 1", e.toString());

            }
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "\n");
                }
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "[}][,][{]");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection succes " + result);
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }
            try {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            int IDi = 1;
                            //{"rab":[{"operation":"Р‘РµСЃРєРѕРЅС‚Р°РєС‚РЅР°СЏ РјРѕР№РєР° РґРІРёРіР°С‚РµР»СЏ РІС‹РїРѕР»РЅРёС‚СЊ", "count":"1", "summa":"5В 000"}

                            //"tov":[{"operation":"РђРїРїР»РёРєР°С‚РѕСЂ-С‚Р°РјРїРѕРЅ /CAR SYSTEM 139.325", "count":"1", "summa":"60"},
                            dataModelsTovar= new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("rab");

                            if (jsonArray.length() > 0 && jsonArray!=null){
                                String operationN = jsonArray.getJSONObject(0).getString("operation");
                                String summaS = jsonArray.getJSONObject(0).getString("summa");

                                String pareInt = String.valueOf(IDi);
                                dataModelsTovar.add(new DataModelTovar(operationN, null, "Выполнены рабочие операции",summaS+" тг.",summaS,pareInt));
                                adapter= new CustomAdapterTovar(dataModelsTovar,getApplicationContext());
                                listView.setAdapter(adapter);

                                for(int i = 1; i < jsonArray.length(); i++) {
                                    IDi = IDi +1;
                                    String pareInt2 = String.valueOf(IDi);
                                    String operation = jsonArray.getJSONObject(i).getString("operation");
                                    Log.e("operation ", operation);
                                    String count = jsonArray.getJSONObject(i).getString("count");
                                    Log.e("count ", count);
                                    String summa = jsonArray.getJSONObject(i).getString("summa");
                                    Log.e("summa ", summa);

                                    dataModelsTovar.add(new DataModelTovar(operation, null, null,summa+" тг.",summa,pareInt2));
                                    adapter= new CustomAdapterTovar(dataModelsTovar,getApplicationContext());
                                    listView.setAdapter(adapter);

                                }
                            }else{

                            }

                            IDi = 0;
                            JSONArray jsonArrayTov = jsonObject.getJSONArray("tov");
                            if (jsonArrayTov.length() > 0 && jsonArrayTov!=null){

                                IDi = IDi +1;
                                String pareInt3 = String.valueOf(IDi);
                                String operationTT = jsonArrayTov.getJSONObject(0).getString("operation");
                                String countTT = jsonArrayTov.getJSONObject(0).getString("count");
                                String summaTT = jsonArrayTov.getJSONObject(0).getString("summa");
                                String edTT = jsonArrayTov.getJSONObject(0).getString("ed");

                                dataModelsTovar.add(new DataModelTovar(operationTT, countTT+" "+edTT, "Запасные части",summaTT+" тг.",summaTT,pareInt3));
                                adapter= new CustomAdapterTovar(dataModelsTovar,getApplicationContext());
                                listView.setAdapter(adapter);


                                for(int i = 1; i < jsonArrayTov.length(); i++) {
                                    IDi = IDi +1;
                                    String pareInt4 = String.valueOf(IDi);
                                    String operation = jsonArrayTov.getJSONObject(i).getString("operation");
                                    Log.e("operation ", operation);
                                    String count = jsonArrayTov.getJSONObject(i).getString("count");
                                    Log.e("count ", count);
                                    String summa = jsonArrayTov.getJSONObject(i).getString("summa");
                                    String edT = jsonArrayTov.getJSONObject(i).getString("ed");
                                    Log.e("summa ", summa);

                                    dataModelsTovar.add(new DataModelTovar(operation, count+" "+edT, null,summa+" тг.",summa,pareInt4));
                                    adapter= new CustomAdapterTovar(dataModelsTovar,getApplicationContext());
                                    listView.setAdapter(adapter);

                                }
                            }else{

                            }

                        } catch (Exception e) {
                            Log.e("Fail 4", e.toString());
                        }
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });


            } catch (Exception e){
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp, String mob, String pin){
            this.remZ = mobp;
            this.MobS = mob;
            this.PinS = pin;
            this.start();
        }


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
        Log.e("-----Tovar-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Tovar-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Tovar-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Tovar-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----Tovar-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Tovar-----", "onRestart");
    }

}
