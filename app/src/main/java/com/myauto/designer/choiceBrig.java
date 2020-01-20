package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Arrays;

import static com.myauto.designer.CustomAdapterChoiceBrig.ListBRIGStatic;
import static com.myauto.designer.Login.MY_PREFS_NAME;


public class choiceBrig extends AppCompatActivity {

    //ListView listView;
    String num;

    String[] spBrig;

    Spinner brig_spinner;
    Button search,regis;
    TextView caseBrig;


    ProgressDialog progressDialog;

    String IPSTRING;

    private zaprosBrigSotr zaprosBrigSotr;
    private goReg goReg;

    ArrayList<DataModelChoiceBrig> dataModelChoiceBrigs;
    ListView listView;
    private static CustomAdapterChoiceBrig adapter;

    String brigS;

    String dataIntent;
    String codeIntent,massivS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_brig);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        brig_spinner = (Spinner)findViewById(R.id.choice_brig_spinner);
        search = (Button)findViewById(R.id.choice_brig_search);
        caseBrig = (TextView) findViewById(R.id.textView125);
        regis = (Button)findViewById(R.id.button18);

        listView = (ListView)findViewById(R.id.choice_brig_listview);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        String jsonBrigada = intent.getStringExtra("jsonBrigada");
        String brig = intent.getStringExtra("brig");
        dataIntent = intent.getStringExtra("data");
        codeIntent = intent.getStringExtra("cod");
        setTitle(num);

        if (jsonBrigada!=null){
            //то что искать бригады и выбор бригады
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    caseBrig.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    regis.setVisibility(View.INVISIBLE);
                }
            });
        }
        if(brig!=null){
            //автоматом сразу ищет по бригаде

            // поиск убрать
            spBrig = new String[1];
            spBrig[0] = brig;

            // адаптер
            Log.e("--Нажал нет?---", " добавил "+ Arrays.toString(spBrig)+" массив ");
            // адаптер
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(choiceBrig.this, android.R.layout.simple_spinner_item, spBrig);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            brig_spinner.setAdapter(adapter2);
            // заголовок
            brig_spinner.setPrompt("Выберите бригаду");
            // устанавливаем обработчик нажатия
            brig_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    //String selectedSpinManag = brig_spinner.getSelectedItem().toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            brigS = brig_spinner.getSelectedItem().toString();

            getBrig letTast = new getBrig();
            letTast.execute();
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brigS = brig_spinner.getSelectedItem().toString();

                getBrig letTast = new getBrig();
                letTast.execute();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        caseBrig.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        regis.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("List", String.valueOf(ListBRIGStatic));
                if(ListBRIGStatic.size() == 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(choiceBrig.this);
                            builder.setTitle("Внимание!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Для регистрации необходимо выбрать сотрудников")
                                    .setCancelable(false)
                                    .setNegativeButton("Ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }else{
                    String mass = "";
                    for (int i = 0; i < ListBRIGStatic.size(); i++) {
                        mass = mass + ListBRIGStatic.get(i) + "|";
                    }
                    massivS = mass.substring(0, mass.length()-1);
                    Log.e("Все коты: ", massivS);
                    //getRegistrationRabOper.php

                    brigS = brig_spinner.getSelectedItem().toString();

                    regis letTast = new regis();
                    letTast.execute();
                }

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    class regis extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(choiceBrig.this);
            progressDialog.setMessage("Идёт загрузка...");
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
            goReg = new goReg();
            goReg.start(mob, pin, brigS);

            try {
                goReg.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=goReg.getResult();
            try {
                JSONObject jsonObject = new JSONObject(result);
                final String res = jsonObject.getString("res");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(choiceBrig.this);
                        builder.setTitle("Внимание!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage(res)
                                .setCancelable(false)
                                .setNegativeButton("Ок",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                finish();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }); //
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }

    public class goReg extends Thread{

        /*
            "empty": "ok",
            "res": [{
                "brig": "B0404"

        */

        String MobS;
        String PinS;
        String brigS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(15);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("brig", brigS));
            NameValuerPair.add(new BasicNameValuePair("num", num));
            NameValuerPair.add(new BasicNameValuePair("massiv", massivS));
            NameValuerPair.add(new BasicNameValuePair("data", dataIntent));
            NameValuerPair.add(new BasicNameValuePair("cod", codeIntent));


            Log.e("mob", MobS);
            Log.e("PinS", PinS);
            Log.e("brig", brigS);
            Log.e("num", num);
            Log.e("massiv", massivS);
            Log.e("data", dataIntent);
            Log.e("cod", codeIntent);
            /*  $arr_params['mob'] 			= $mob;
            $arr_params['pin'] 			= $_POST['pin'];
            $arr_params['brig'] 		= $_POST['brig'];
            $arr_params['num'] 			= $_POST['num'];
            $arr_params['massiv'] 		= $_POST['massiv'];
            $arr_params['data'] 		= $_POST['data'];
            $arr_params['cod'] 		    = $_POST['cod']*/

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getRegistrationRabOper.php");
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

        public void start(String mobp, String pinp, String brig) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.brigS = brig;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }


    class getBrig extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(choiceBrig.this);
            progressDialog.setMessage("Идёт загрузка...");
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
            zaprosBrigSotr = new zaprosBrigSotr();
            zaprosBrigSotr.start(mob, pin, brigS);

            try {
                zaprosBrigSotr.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=zaprosBrigSotr.getResult();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){

                }else{
                    /*
                    * {
                       {"empty":"ok","brigada":[{"cod":"000001148", "sotr":"Гавенко Всеволод"},{"cod":"000000001", "sotr":"Никитин Дмитрий"}]}
                    }*/
                    JSONArray jsonArray = jsonObject.getJSONArray("brigada");
                    dataModelChoiceBrigs = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        final String cod = jsonArray.getJSONObject(i).getString("cod");
                        Log.e("cod ",cod);

                        final String sotr = jsonArray.getJSONObject(i).getString("sotr");
                        Log.e("sotr ",sotr);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataModelChoiceBrigs.add(new DataModelChoiceBrig(sotr,cod));
                                adapter= new CustomAdapterChoiceBrig(dataModelChoiceBrigs,getApplicationContext());

                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

    public class zaprosBrigSotr extends Thread {

        /*
            "empty": "ok",
            "res": [{
                "brig": "B0404"

        */

        String MobS;
        String PinS;
        String brigS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(3);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("brig", brigS));

            Log.e("mob", MobS);
            Log.e("PinS", PinS);
            Log.e("brig", brigS);
            //getRZProduct.php

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getInfoBrigada.php");
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

        public void start(String mobp, String pinp, String brig) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.brigS = brig;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

}