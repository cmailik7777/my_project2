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
import android.widget.Button;
import android.widget.EditText;
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

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class rzMehanic extends AppCompatActivity {

    ArrayList<DataModelRZMehanic> dataModelRZMehanics;
    ListView listView;
    private static CustomAdapterRZMehanic adapter;

    ProgressDialog progressDialog;

    Button search;
    EditText gosT;

    private getGos getgos;
    private getAllmehanic getAllmehanic;
    String gos,IPSTRING,num;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rz_mehanic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //-------------------------ListView------------------------------------------------------
        listView = (ListView) findViewById(R.id.rz_mehanic_listview);
        //-------------------------//ListView----------------------------------------------------
        progressBar = (ProgressBar)findViewById(R.id.rz_mehanic_progressbar);

        getAll letTas2t = new getAll();
        letTas2t.execute();

        search = (Button)findViewById(R.id.rz_mehanic_search);
        gosT = (EditText) findViewById(R.id.rz_mehanic_gos);
        FloatingActionButton fabGos = (FloatingActionButton) findViewById(R.id.rz_mehanic_foto_gos);

        fabGos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentP = new Intent(rzMehanic.this, Show_Cam_Mehanic.class);
                startActivity(intentP);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String org = prefsIP.getString("org", null);
                String pref = "";
                //AST 0039697
                if (gosT.getText().toString().matches("[0-9]+")){
                    Log.e("поиск по рз ", "sa");
                    if (org.equals("MEGA MOTORS")){
                        pref = "ALM";
                    } else if (org.equals("G MOTORS")){
                        pref = "GMS";
                    } else if (org.equals("AVANGARD")){
                        pref = "AVG";
                    } else if (org.equals("INTERSERVICE")){
                        pref = "INT";
                    } else if (org.equals("KRAFT SERVICE")){
                        pref = "KRF";
                    } else if (org.equals("MB SERVICE")){
                        pref = "MBS";
                    } else if (org.equals("MEGA MOTORS ASIA")){
                        pref = "ASA";
                    } else if (org.equals("MEGA MOTORS ASTANA")){
                        pref = "AST";
                    } else if (org.equals("MEGA MOTORS SEMEY")){
                        pref = "SEM";
                    } else if (org.equals("MM MBW")){
                        pref = "BMW";
                    } else if (org.equals("CORD")){
                        pref = "CRD";
                    }

                    int len = gosT.getText().length();
                    if (len == 1){
                        num = pref+"000000"+gosT.getText().toString();
                        Log.e("num ", num);
                    } else if (len == 2) {
                        num = pref+"00000"+gosT.getText().toString();
                        Log.e("num ", num);
                    } else if (len == 3) {
                        num = pref+"0000"+gosT.getText().toString();
                        Log.e("num ", num);
                    } else if (len == 4) {
                        num = pref+"000"+gosT.getText().toString();
                        Log.e("num ", num);
                    } else if (len == 5) {
                        num = pref+"00"+gosT.getText().toString();
                        Log.e("num ", num);
                    } else if (len == 6) {
                        num = pref+"0"+gosT.getText().toString();
                        Log.e("num ", num);
                    } else if (len == 7) {
                        num = pref+""+gosT.getText().toString();
                        Log.e("num ", num);
                    }
                    gos = "";
                    goGos letTast = new goGos();
                    letTast.execute();
                }else {
                    num = "";
                    Log.e("ВЫПОЛНЯЮ ПОИСК ПО ГОС ", gosT.getText().toString());
                    gos = gosT.getText().toString();
                    goGos letTast = new goGos();
                    letTast.execute();
                }
            }
        });

        Intent intent = getIntent();
        final String GOS = intent.getStringExtra("GOSCAM");
        if (GOS!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gosT.setText(GOS);
                }
            });
            num = "";
            gos = gosT.getText().toString();
            goGos letTast = new goGos();
            letTast.execute();
        }


    }


    class goGos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(rzMehanic.this);
            progressDialog.setMessage("Поиск ремонтных заказов...");
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
            getgos = new getGos();
            getgos.start(mob, pin, gos, org, num);

            try {
                getgos.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getgos.getResult();
            try {
                dataModelRZMehanics = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){
                    String emptyRZ = jsonObject.getString("emptyRz");
                    if (emptyRZ.equals("no")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(rzMehanic.this);
                                builder.setTitle("")
                                        .setMessage("Ремонтные заказы отсуствуют. Обратитесь к мастеру приёмщику.")
                                        .setCancelable(false)
                                        .setPositiveButton("ок",
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
                        /*{
                            "empty": "no",
                            "emptyRz": "ok",
                            "rz": [{
                                "num": "ALM0082025",
                                "manager": "Караев Алексей",
                                "data": "21.12.2015 14:31:33",
                                "gos": "442LFA02"
                            }, {
                                "num": "ALM0086434",
                                "manager": "Садыков Сергей",
                                "data": "27.04.2016 11:28:42",
                                "gos": "442LFA02"
                            }]
                        }*/
                        JSONArray jsonArray = jsonObject.getJSONArray("rz");
                        for (int i = 0; i < 1; i++) { // Цикл где в стринг result пришло от баз
                            String num = jsonArray.getJSONObject(i).getString("num");
                            if (num.isEmpty()){
                                num=" ";
                            }
                            Log.e("num ",num);

                            String manager = jsonArray.getJSONObject(i).getString("manager");
                            if (manager.isEmpty()){
                                manager=" ";
                            }
                            Log.e("manager ",manager);

                            String data = jsonArray.getJSONObject(i).getString("data");
                            if (data.isEmpty()){
                                data=" ";
                            }
                            Log.e("data ",data);

                            String gos = jsonArray.getJSONObject(i).getString("gos");
                            if (gos.isEmpty()){
                                gos=" ";
                            }
                            Log.e("gos ",gos);


                            final String finalNum = num;
                            final String finalManager = manager;
                            final String finalData = data;
                            final String finalGos = gos;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataModelRZMehanics.add(new DataModelRZMehanic("Ремонтные заказы", finalManager, finalNum, finalData, finalGos));
                                    adapter= new CustomAdapterRZMehanic(dataModelRZMehanics,getApplicationContext());

                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            TextView tvNum = (TextView)view.findViewById(R.id.listview_rz_num);
                                            Intent ope = new Intent(rzMehanic.this, rzMehanic_detail.class);
                                            ope.putExtra("num", tvNum.getText().toString());
                                            startActivity(ope);
                                        }
                                    });
                                }
                            });
                        }
                        for (int i = 1; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                            String num = jsonArray.getJSONObject(i).getString("num");
                            if (num.isEmpty()){
                                num=" ";
                            }
                            Log.e("num ",num);

                            String manager = jsonArray.getJSONObject(i).getString("manager");
                            if (manager.isEmpty()){
                                manager=" ";
                            }
                            Log.e("manager ",manager);

                            String data = jsonArray.getJSONObject(i).getString("data");
                            if (data.isEmpty()){
                                data=" ";
                            }
                            Log.e("data ",data);

                            String gos = jsonArray.getJSONObject(i).getString("gos");
                            if (gos.isEmpty()){
                                gos=" ";
                            }
                            Log.e("gos ",gos);


                            final String finalNum = num;
                            final String finalManager = manager;
                            final String finalData = data;
                            final String finalGos = gos;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataModelRZMehanics.add(new DataModelRZMehanic(" ", finalManager, finalNum, finalData, finalGos));
                                    adapter= new CustomAdapterRZMehanic(dataModelRZMehanics,getApplicationContext());

                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            TextView tvNum = (TextView)view.findViewById(R.id.listview_rz_num);
                                            Intent ope = new Intent(rzMehanic.this, rzMehanic_detail.class);
                                            ope.putExtra("num", tvNum.getText().toString());
                                            startActivity(ope);
                                        }
                                    });
                                }
                            });
                        }
                    }
                }else{

                    String emptyRZ = jsonObject.getString("emptyRz");
                    if (emptyRZ.equals("no")) {

                    }else{
                        /*{
                            "empty": "no",
                            "emptyRz": "ok",
                            "rz": [{
                                "num": "ALM0082025",
                                "manager": "Караев Алексей",
                                "data": "21.12.2015 14:31:33",
                                "gos": "442LFA02"
                            }, {
                                "num": "ALM0086434",
                                "manager": "Садыков Сергей",
                                "data": "27.04.2016 11:28:42",
                                "gos": "442LFA02"
                            }]
                        }*/


                        JSONArray jsonArray2 = jsonObject.getJSONArray("rz");
                        for (int i = 0; i < 1; i++) { // Цикл где в стринг result пришло от баз
                            String num = jsonArray2.getJSONObject(i).getString("num");
                            if (num.isEmpty()){
                                num=" ";
                            }
                            Log.e("num ",num);

                            String manager = jsonArray2.getJSONObject(i).getString("manager");
                            if (manager.isEmpty()){
                                manager=" ";
                            }
                            Log.e("manager ",manager);

                            String data = jsonArray2.getJSONObject(i).getString("data");
                            if (data.isEmpty()){
                                data=" ";
                            }
                            Log.e("data ",data);

                            String gos = jsonArray2.getJSONObject(i).getString("gos");
                            if (gos.isEmpty()){
                                gos=" ";
                            }
                            Log.e("gos ",gos);


                            final String finalNum = num;
                            final String finalManager = manager;
                            final String finalData = data;
                            final String finalGos = gos;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataModelRZMehanics.add(new DataModelRZMehanic("Ремонтные заказы", finalManager, finalNum, finalData, finalGos));
                                    adapter= new CustomAdapterRZMehanic(dataModelRZMehanics,getApplicationContext());

                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            TextView tvNum = (TextView)view.findViewById(R.id.listview_rz_num);
                                            Intent ope = new Intent(rzMehanic.this, rzMehanic_detail.class);
                                            ope.putExtra("num", tvNum.getText().toString());
                                            startActivity(ope);
                                        }
                                    });
                                }
                            });
                        }
                        for (int i = 1; i < jsonArray2.length(); i++) { // Цикл где в стринг result пришло от баз
                            String num = jsonArray2.getJSONObject(i).getString("num");
                            if (num.isEmpty()){
                                num=" ";
                            }
                            Log.e("num ",num);

                            String manager = jsonArray2.getJSONObject(i).getString("manager");
                            if (manager.isEmpty()){
                                manager=" ";
                            }
                            Log.e("manager ",manager);

                            String data = jsonArray2.getJSONObject(i).getString("data");
                            if (data.isEmpty()){
                                data=" ";
                            }
                            Log.e("data ",data);

                            String gos = jsonArray2.getJSONObject(i).getString("gos");
                            if (gos.isEmpty()){
                                gos=" ";
                            }
                            Log.e("gos ",gos);


                            final String finalNum = num;
                            final String finalManager = manager;
                            final String finalData = data;
                            final String finalGos = gos;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataModelRZMehanics.add(new DataModelRZMehanic(" ", finalManager, finalNum, finalData, finalGos));
                                    adapter= new CustomAdapterRZMehanic(dataModelRZMehanics,getApplicationContext());

                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            TextView tvNum = (TextView)view.findViewById(R.id.listview_rz_num);
                                            Intent ope = new Intent(rzMehanic.this, rzMehanic_detail.class);
                                            ope.putExtra("num", tvNum.getText().toString());
                                            startActivity(ope);
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }

    public class getGos extends Thread {

        /*
            "empty": "ok",
            "res": [{
                "brig": "B0404"

        */

        String MobS;
        String PinS;
        String GosS,OrgS,NumS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(6);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("nomer", GosS));
            NameValuerPair.add(new BasicNameValuePair("org", "TEST MOTORS"));
            NameValuerPair.add(new BasicNameValuePair("num", NumS));

            Log.e("mob", MobS);
            Log.e("PinS", PinS);
            Log.e("nomer", GosS);
            Log.e("OrgS", OrgS);
            Log.e("NumS", NumS);
            //getRZProduct.php

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/nd/getRzMehanik.php");
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

        public void start(String mobp, String pinp, String nump, String org, String num) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.GosS = nump;
            this.OrgS = org;
            this.NumS = num;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

    class getAll extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String mob = prefs.getString("mob", null);
            String pin = prefs.getString("pin", null);
            String org = prefs.getString("org", null);
            //-------------------------------------------------------------------------------------------------------------------------------------------
            getAllmehanic = new getAllmehanic();
            getAllmehanic.start(mob, pin, org);

            try {
                getAllmehanic.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getAllmehanic.getResult();
            try {
                dataModelRZMehanics = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                if (!jsonObject.isNull("res")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(rzMehanic.this);
                            builder.setTitle("Внимание!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Вы не состоите в бригаде!")
                                    .setCancelable(false)
                                    .setNegativeButton("Выход",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }else{
                    String empty = jsonObject.getString("empty");
                    if (empty.equals("no")){

                    }else{
                        JSONArray jsonArray = jsonObject.getJSONArray("zak");
                        for (int i = 0; i < 1; i++) { // Цикл где в стринг result пришло от баз
                            String num = jsonArray.getJSONObject(i).getString("num");
                            if (num.isEmpty()){
                                num=" ";
                            }
                            Log.e("num ",num);

                            String manager = jsonArray.getJSONObject(i).getString("manager");
                            if (manager.isEmpty()){
                                manager=" ";
                            }
                            Log.e("manager ",manager);

                            String data = jsonArray.getJSONObject(i).getString("data");
                            if (data.isEmpty()){
                                data=" ";
                            }
                            Log.e("data ",data);

                            String gos = jsonArray.getJSONObject(i).getString("gos");
                            if (gos.isEmpty()){
                                gos=" ";
                            }
                            Log.e("gos ",gos);


                            final String finalNum = num;
                            final String finalManager = manager;
                            final String finalData = data;
                            final String finalGos = gos;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataModelRZMehanics.add(new DataModelRZMehanic("Мои заказы", finalManager, finalNum, finalData, finalGos));
                                    adapter= new CustomAdapterRZMehanic(dataModelRZMehanics,getApplicationContext());

                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            TextView tvNum = (TextView)view.findViewById(R.id.listview_rz_num);
                                            Intent ope = new Intent(rzMehanic.this, rzMehanic_detail.class);
                                            ope.putExtra("num", tvNum.getText().toString());
                                            startActivity(ope);
                                        }
                                    });
                                }
                            });
                        }
                        for (int i = 1; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                            String num = jsonArray.getJSONObject(i).getString("num");
                            if (num.isEmpty()){
                                num=" ";
                            }
                            Log.e("num ",num);

                            String manager = jsonArray.getJSONObject(i).getString("manager");
                            if (manager.isEmpty()){
                                manager=" ";
                            }
                            Log.e("manager ",manager);

                            String data = jsonArray.getJSONObject(i).getString("data");
                            if (data.isEmpty()){
                                data=" ";
                            }
                            Log.e("data ",data);

                            String gos = jsonArray.getJSONObject(i).getString("gos");
                            if (gos.isEmpty()){
                                gos=" ";
                            }
                            Log.e("gos ",gos);


                            final String finalNum = num;
                            final String finalManager = manager;
                            final String finalData = data;
                            final String finalGos = gos;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataModelRZMehanics.add(new DataModelRZMehanic(" ", finalManager, finalNum, finalData, finalGos));
                                    adapter= new CustomAdapterRZMehanic(dataModelRZMehanics,getApplicationContext());

                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            TextView tvNum = (TextView)view.findViewById(R.id.listview_rz_num);
                                            Intent ope = new Intent(rzMehanic.this, rzMehanic_detail.class);
                                            ope.putExtra("num", tvNum.getText().toString());
                                            startActivity(ope);
                                        }
                                    });
                                }
                            });
                        }
                    }
                }


                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }

    public class getAllmehanic extends Thread {

        /*
            "empty": "ok",
            "res": [{
                "brig": "B0404"

        */

        String MobS;
        String PinS;
        String OrgS;

        InputStream is = null;
        String result = null;
        String line = null;

    public void run() {

        ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(6);

        NameValuerPair.add(new BasicNameValuePair("mob", MobS));
        NameValuerPair.add(new BasicNameValuePair("pin", PinS));
        NameValuerPair.add(new BasicNameValuePair("org", OrgS));

        Log.e("mob", MobS);
        Log.e("OrgS", OrgS);
        //getRZProduct.php

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/setRzMehanikAll.php");
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

    public void start(String mobp, String pinp, String org) {
        this.MobS = mobp;
        this.PinS = pinp;
        this.OrgS = org;
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
