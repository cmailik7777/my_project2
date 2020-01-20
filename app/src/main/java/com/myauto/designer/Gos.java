package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class Gos extends AppCompatActivity {

    ArrayList<DataModelGN> dataModelGNs;
    ListView listView;
    private static CustomAdapterGN adapter;


    String MobS,PinS,result,Vin;

    String gn;
    String ma;
    String vin;
    String gnT;
    String org;
    String remZ;
    String Manag;
    String dato;
    String datc;
    String sum;
    String Gosnm;
    String staty;
    String marka;
    String vincode;
    String tel;
    String GosNomer;
    String checkprog;

    ProgressDialog progressDialog;

    private Zapros Zapros1;
    ProgressBar progressBar;
    TextView textView33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final TextView oil_paramT = (TextView)findViewById(R.id.Gos_Oil_Type);
        final TextView oil_last_dateT = (TextView)findViewById(R.id.Gos_Oil_Date);
        final TextView oil_last_kmT = (TextView)findViewById(R.id.Gos_Oil_Km);
        final TextView oil_next_dateT = (TextView)findViewById(R.id.Gos_Oil_Next_Date);
        final TextView oil_next_kmT = (TextView)findViewById(R.id.Gos_Oil_Next_Km);
        final ImageView oil_logoT = (ImageView) findViewById(R.id.Gos_Oil_Logo);
        textView33 = (TextView)findViewById(R.id.textView33);

        progressBar = (ProgressBar)findViewById(R.id.gn_Progressbar);
        listView=(ListView)findViewById(R.id.listGN);
        Intent intent = getIntent();
        String Gos = intent.getStringExtra("Gos");
        setTitle(Gos);
        Vin = intent.getStringExtra("Vin");

        new Thread(new Runnable() {
            @Override
            public void run() {
                //oil_name":"MOBIL", "oil_param":"5W30", "oil_last_date":"06.09.2017", "oil_last_km":"138В 095", "oil_next_date":"- * * * -", "oil_next_km":"148В 095", "oil_logo"
                SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                String pin = prefs.getString("pin", null);
                MobS = mob;
                PinS = pin;
                Zapros1 = new Zapros();
                Zapros1.start(MobS, PinS,Vin);

                try {
                    Zapros1.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
                result = Zapros1.resOrg();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //oil_name":"MOBIL", "oil_param":"5W30", "oil_last_date":"06.09.2017", "oil_last_km":"138В 095", "oil_next_date":"- * * * -", "oil_next_km":"148В 095", "oil_logo"
                            JSONObject jsonObject = new JSONObject(result);
                            String oil_param = jsonObject.getString("oil_param");
                            oil_paramT.setText(oil_param);
                            Log.e("json", "Результат: " + oil_param);

                            String oil_last_date = jsonObject.getString("oil_last_date");
                            oil_last_dateT.setText(oil_last_date);
                            Log.e("json", "Результат: " + oil_last_date);

                            String oil_last_km = jsonObject.getString("oil_last_km");
                            oil_last_kmT.setText(oil_last_km+" км");
                            Log.e("json", "Результат: " + oil_last_km);

                            String oil_next_date = jsonObject.getString("oil_next_date");
                            oil_next_dateT.setText(oil_next_date);
                            Log.e("json", "Результат: " + oil_next_date);

                            String oil_next_km = jsonObject.getString("oil_next_km");
                            oil_next_kmT.setText(oil_next_km+" км");
                            Log.e("json", "Результат: " + oil_next_km);

                            String oil_logo = jsonObject.getString("oil_logo");
                            byte[] decodedString2 = Base64.decode(oil_logo, Base64.DEFAULT);
                            Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0,decodedString2.length);
                            oil_logoT.setImageBitmap(decodedByte2);
                        } catch (Exception e) {
                            Log.e("Fail 3", e.toString());
                        }
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dataModelGNs = new ArrayList<>();

                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("RepairOrders");

                            for(int i = 0; i < jsonArray.length(); i++){
                                org = jsonArray.getJSONObject(i).getString("org");
                                Log.e("org ",org);
                                remZ = jsonArray.getJSONObject(i).getString("num");
                                Log.e("num ",remZ);
                                Manag = jsonArray.getJSONObject(i).getString("manager");
                                Log.e("manager ",Manag);
                                tel = jsonArray.getJSONObject(i).getString("phone");
                                Log.e("phone ",tel);
                                sum = jsonArray.getJSONObject(i).getString("sum");
                                Log.e("sum ",sum);
                                dato = jsonArray.getJSONObject(i).getString("d_open");
                                Log.e("d_open ",dato);
                                datc = jsonArray.getJSONObject(i).getString("d_close");
                                Log.e("d_close ",datc);
                                staty = jsonArray.getJSONObject(i).getString("status");
                                Log.e("status ",staty);

                                dataModelGNs.add(new DataModelGN(org, remZ, Manag,tel,dato,datc,sum,staty,staty));
                                adapter= new CustomAdapterGN(dataModelGNs,getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView tv = (TextView) view.findViewById(R.id.remz);
                                        //Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                                        Intent ope = new Intent(com.myauto.designer.Gos.this, Tovar.class);
                                        ope.putExtra("RemZ", tv.getText().toString());
                                        startActivity(ope);
                                    }
                                });
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            textView33.setVisibility(View.VISIBLE);
                        } catch (Exception e){
                            Log.e("Fail 3", e.toString());
                        }
                    }
                });
            }
        }).start();

        //"RepairOrders": [{"org":"MB SERVICE", "manager":"Р‘РµР»С‹С… РњРёС…Р°РёР»", "phone":"+7 777 028 0463",
        // "sum":"438В 665", "num":"MBS0053842", "d_open":"11.04.2018 10:46:30",
        // "d_close":"24.05.2018 13:10:31", "status":"Р—Р°РєСЂС‹С‚"},

    }


    public class Zapros extends Thread {

        String MobS;
        String PinS;
        String Vin;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("vin", Vin));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getRepairOrders.php");
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


        public void start(String mobp, String pinp, String vin) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.Vin = vin;
            this.start();
        }

        public String resOrg() {
            return result;
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
        Log.e("-----Gos-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Gos-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Gos-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(checkprog=="1"){
            progressDialog.dismiss();
            checkprog="0";
        }
        Log.e("-----Gos-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----Gos-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Gos-----", "onRestart");
    }

}
