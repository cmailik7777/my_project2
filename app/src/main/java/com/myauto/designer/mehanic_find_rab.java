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
import android.text.Html;
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

public class mehanic_find_rab extends AppCompatActivity {

    String IPSTRING,numI,textI;

    private getSearch getSearch;

    ProgressDialog progressDialog;

    ArrayList<DataModelMehanicFindRab> dataModelMehanicFindRabs;
    ListView listView;
    private static CustomAdapterMehanicFindRab adapter;

    String nameOper,namePS, timePS,numPS,categPS;


    private createRabotmehanic createRabotmehanic;
    private getinfoOper getinfoOper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mehanic_find_rab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        Intent intent = getIntent();
        numI = intent.getStringExtra("num");
        textI = intent.getStringExtra("text");
        setTitle(textI);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = (ListView)findViewById(R.id.mehanic_find_rab_listview);

        Searching letTask = new Searching();
        letTask.execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private class Searching extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mehanic_find_rab.this);
            progressDialog.setMessage("Идёт поиск: "+textI);
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
            getSearch = new getSearch();
            getSearch.start(mob, pin, org,"Все","Всеми",textI);

            try {
                getSearch.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getSearch.getResult();
            try {
                dataModelMehanicFindRabs = new ArrayList<>();


                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //ifLabel.setVisibility(View.VISIBLE);
                            //progressDialog.dismiss();
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
                                        "pricemin": "0",
                                        "pricemax": "1В 500",
                                        "oper": " Р”РёР°РіРЅРѕСЃС‚РёРєР° РѕР±РІРѕРґРЅС‹С… СЂРѕР»РёРєРѕРІ, РІС‹РїРѕР»РЅРёС‚СЊ"


                                */

                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        String pricemin = jsonArray.getJSONObject(i).getString("pricemin");
                        if (pricemin.isEmpty()){
                            pricemin="Пусто";
                        }
                        Log.e("pricemin ",pricemin);

                        String pricemax = jsonArray.getJSONObject(i).getString("pricemax");
                        if (pricemax.isEmpty()){
                            pricemax="Пусто";
                        }
                        Log.e("pricemax ",pricemax);
                        String oper = jsonArray.getJSONObject(i).getString("oper");
                        if (oper.isEmpty()){
                            oper="Пусто";
                        }
                        Log.e("oper ",oper);

                        final String finalPricemin = pricemin;
                        final String finalPricemax = pricemax;
                        final String finalOper = oper;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataModelMehanicFindRabs.add(new DataModelMehanicFindRab(finalOper,finalPricemin,finalPricemax));
                                adapter= new CustomAdapterMehanicFindRab(dataModelMehanicFindRabs,getApplicationContext());

                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView tv = (TextView)view.findViewById(R.id.mehanic_find_rab_name);
                                        nameOper = tv.getText().toString();
                                        infoOper letTask = new infoOper();
                                        letTask.execute();
                                            /*TextView tv = (TextView)view.findViewById(R.id.mini_rab_oper_oper);
                                            nameOper = tv.getText().toString();
                                            infoOper letTask = new infoOper();
                                            letTask.execute();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(sozdat_rab_oper.this);
                                                                builder.setTitle("Окно предупреждения!")
                                                                        .setMessage(Html.fromHtml("Вы действительно уверены что хотите создать рабочую операцию на данный ремотный заказ? <b>"+num+"</b>" +
                                                                                "<br> Бригада: <b>"+brS+"</b>" +
                                                                                "<br> Работа: <b>"+name.getText().toString()+"</b>" +
                                                                                "<br> Категория ремонта: <b>"+categS+"</b>" +
                                                                                "<br> Стоимость: <b>"+value.getText().toString()+"</b>" +
                                                                                "<br> Норма времени: <b>"+time.getText().toString()+"</b>" +
                                                                                "<br> Фиксированная цена: <b>"+testFix+"</b>"))
                                                                        .setCancelable(false)
                                                                        .setNegativeButton("Отмена",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                })
                                                                        .setPositiveButton("Да",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        dialog.dismiss();
                                                                                        CatTask catTask = new CatTask();
                                                                                        catTask.execute();
                                                                                        // CreateRab();
                                                                                    }
                                                                                });
                                                                AlertDialog alert = builder.create();
                                                                alert.show();
                                                }
                                            });*/


                                    }
                                });
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                    //
                }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 6", e.toString());
            }
            return null;
        }
    }

    private class getSearch extends Thread {

        String MobS;
        String PinS,orgS,groupS,managS,textS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("org", orgS));
            NameValuerPair.add(new BasicNameValuePair("group", groupS));
            NameValuerPair.add(new BasicNameValuePair("priem", managS));
            NameValuerPair.add(new BasicNameValuePair("text", textS));

            /*    	$arr_params['mob'] 	= $mob;
                    $arr_params['pin'] 		= $_GET['pin'];
                    $arr_params['org'] 		= $_GET['org'];
                    $arr_params['group'] 	= $_GET['group'];
                    $arr_params['priem'] 	= $_GET['priem'];
                    $arr_params['text'] 		= $_GET['text'];*/

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getDostOper.php");
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

        public void start(String mobp, String pinp, String org, String selectedgroup, String selectedmanag, String text) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.orgS = org;
            this.groupS = selectedgroup;
            this.managS = selectedmanag;
            this.textS = text;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

    class infoOper extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mehanic_find_rab.this);
            progressDialog.setMessage("Идёт загрузка");
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
            getinfoOper = new getinfoOper();
            getinfoOper.start(mob, pin, nameOper);

            try {
                getinfoOper.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getinfoOper.getResult();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){

                }else{
                 /*
                    "empty": "ok",
                    "res": [{
                        "text": "Адаптация фары и диагностика электронных систем",
                        "fix": "no",
                        "categ": "Электрические работы",
                        "value": "25 000",
                        "time": "0",
                        "group": ""
                    }]
                }
                */
                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    for (int i = 0; i < 1; i++) { // Цикл где в стринг result пришло от баз
                        namePS = jsonArray.getJSONObject(i).getString("text");
                        if (namePS.isEmpty()){
                            namePS=" ";
                        }
                        Log.e("text ",namePS);
                        categPS = jsonArray.getJSONObject(i).getString("categ");
                        if (categPS.isEmpty()){
                            categPS=" ";
                        }
                        Log.e("categ ",categPS);

                        timePS = jsonArray.getJSONObject(i).getString("time");
                        if (timePS.isEmpty()){
                            timePS=" ";
                        }
                        Log.e("timePS ",timePS);



                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mehanic_find_rab.this);
                            builder.setTitle("Окно предупреждения!")
                                    .setMessage(Html.fromHtml("Вы действительно уверены что хотите создать рабочую операцию на данный ремотный заказ? <b>"+numI+"</b>" +
                                            "<br> Работа: <b>"+namePS+"</b>" +
                                            "<br><br> Категория ремонта: <b>"+categPS+"</b>" +
                                            "<br> Норма времени: <b>"+timePS+"</b>"))
                                    .setCancelable(false)
                                    .setNegativeButton("Отмена",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            })
                                    .setPositiveButton("Да",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                    mehanicCreateRabot letTask = new mehanicCreateRabot();
                                                    letTask.execute();
                                                    // CreateRab();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 5", e.toString());
            }


            return null;
        }
    }

    class mehanicCreateRabot extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mehanic_find_rab.this);
            progressDialog.setMessage("Идёт вынос работы");
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
            createRabotmehanic = new createRabotmehanic();
            createRabotmehanic.start(mob, pin, namePS, timePS,numI,categPS);

            try {
                createRabotmehanic.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=createRabotmehanic.getResult();
            try {
                JSONObject jsonObject = new JSONObject(result);
                final String res = jsonObject.getString("res");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mehanic_find_rab.this);
                        builder.setTitle(" ")
                                .setMessage(Html.fromHtml("<b>"+res+"</b>"))
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

                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 6", e.toString());
            }
            return null;
        }
    }

    public class getinfoOper extends Thread {

        String MobS;
        String PinS,OperS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(20);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("text", OperS));

            /*    	    	    	$arr_params['mob'] 	= $mob;
	$arr_params['pin'] 		= $_GET['pin'];
	$arr_params['text'] 		= $_GET['text'];*/

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getInfoOper.php");
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

        public void start(String mobp, String pinp, String name) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.OperS = name;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

    public class createRabotmehanic extends Thread {

        String MobS;
        String PinS,nameS,timeS,numS,categS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("name", nameS));
            NameValuerPair.add(new BasicNameValuePair("time", timeS));
            NameValuerPair.add(new BasicNameValuePair("num", numI));
            NameValuerPair.add(new BasicNameValuePair("categ", categS));

            /*    	    	$arr_params['mob'] 	= $mob;
                            $arr_params['pin'] 		= $_POST['pin'];
                            $arr_params['name'] 	= $_POST['name'];
                            $arr_params['time'] 	= $_POST['time'];
                            $arr_params['num'] 	= $_POST['num'];
                            $arr_params['categ'] 	= $_POST['categ'];*/

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getSozdatRabOperMehanik.php");
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

        public void start(String mobp, String pinp, String name, String time, String num, String categ) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.nameS = name;
            this.timeS = time;
            this.numS = num;
            this.categS = categ;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }


}
