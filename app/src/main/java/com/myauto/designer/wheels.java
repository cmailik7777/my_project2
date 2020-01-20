package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class wheels extends AppCompatActivity {

    ArrayList<DataModelWheels> dataModelWheelses;
    ListView listView;
    private static CustomAdapterWheels adapter;

    private Wheels wheels;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    TextView wheels_textCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressBar = (ProgressBar)findViewById(R.id.wheels_Progressbar);
        listView = (ListView) findViewById(R.id.list_Wheels);
        wheels_textCenter = (TextView)findViewById(R.id.wheels_textCenter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                String pin = prefs.getString("pin", null);
                wheels = new Wheels();
                wheels.start(mob, pin);

                try {
                    wheels.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();




    }

    public class Wheels extends Thread {

        String MobS;
        String PinS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getWheels.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection succes");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(com.myauto.designer.wheels.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Произошла ошибка \n" +
                                        "fail 1")
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
                });
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

            } catch (final Exception e) {
                Log.e("Fail 2", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(com.myauto.designer.wheels.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Произошла ошибка \n" +
                                        "fail 2")
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
                });
            }
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    try {
                        //{"wheels":[{"date":"10.04.2018", "gos":"602DNA02", "description":"Р—єР°", "brand":"Yokohama", "size":"215/60/R16", "count":"4", "count_month":"2", "sum":"1В 000"} ]}
                        dataModelWheelses = new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("wheels");

                        if (jsonArray!=null && jsonArray.length() > 0){
                            for(int i = 0; i < jsonArray.length(); i++){
                                String date = jsonArray.getJSONObject(i).getString("date");
                                Log.e("date ",date);
                                String gos = jsonArray.getJSONObject(i).getString("gos");
                                Log.e("gos ",gos);
                                String description = jsonArray.getJSONObject(i).getString("description");
                                Log.e("description ",description);
                                String brand = jsonArray.getJSONObject(i).getString("brand");
                                Log.e("brand ",brand);
                                String size = jsonArray.getJSONObject(i).getString("size");
                                Log.e("size ",size);
                                String count = jsonArray.getJSONObject(i).getString("count");
                                Log.e("count ",count);
                                String count_month = jsonArray.getJSONObject(i).getString("count_month");
                                Log.e("count_month ",count_month);
                                String sum = jsonArray.getJSONObject(i).getString("sum");
                                Log.e("sum ",sum);

                                dataModelWheelses.add(new DataModelWheels(date,gos,description,brand,size,count,count_month,sum));
                                adapter= new CustomAdapterWheels(dataModelWheelses,getApplicationContext());
                                listView.setAdapter(adapter);
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            wheels_textCenter.setText("В данный момент вы не пользовались данной услугой...");
                            progressBar.setVisibility(View.INVISIBLE);
                        }


                    } catch (Exception e) {
                        Log.e("Fail 4", e.toString());
                    }

                    }
                });
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(com.myauto.designer.wheels.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Произошла ошибка \n" +
                                        "fail 3")
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
                });
            }
        }


        public void start(String mobp, String pinp) {
            this.MobS = mobp;
            this.PinS = pinp;
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
        Log.e("-----Wheels-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Wheels-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Wheels-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Wheels-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----Wheels-----", "onStart");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Wheels-----", "onRestart");
    }

}
