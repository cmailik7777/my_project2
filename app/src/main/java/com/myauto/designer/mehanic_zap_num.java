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

public class mehanic_zap_num extends AppCompatActivity {

    String IPSTRING,nameOper,namePS, timePS,numPS,categPS;
    ProgressDialog progressDialog;

    private getMyRab getMyRab;
    private infoOper infoOper;

    private createRabotmehanic createRabotmehanic;

    ArrayList<DataModelMehanicZapNum> dataModelMehanicZapNa;
    ListView listView;
    private static CustomAdapterMehanicZapNum adapter;

    String num;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mehanic_zap_num);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        setTitle(num);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = (ListView)findViewById(R.id.mehanic_zap_listview);

        getMehanicRabot letTast = new getMehanicRabot();
        letTast.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private class getMehanicRabot extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mehanic_zap_num.this);
            progressDialog.setMessage("Поиск запчастей по "+num);
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
            getMyRab = new getMyRab();
            getMyRab.start(mob, pin, num);

            try {
                getMyRab.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getMyRab.getResult();
            try {
                dataModelMehanicZapNa = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){

                }else{
                    /*	"empty": "ok",
                        "res": [{
                            "zapchast": "Воздушный фильтр двигателя MB M157 278"
                        }, {
                            "zapchast": "Комплект задних тормозных колодок автомобиля МВ AMG / W222 AMG 6.3 / AMG 6.5 /"
                        }, {*/
                    dataModelMehanicZapNa = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        final String zapchast = jsonArray.getJSONObject(i).getString("zapchast");
                        Log.e("zapchast ",zapchast);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataModelMehanicZapNa.add(new DataModelMehanicZapNum(zapchast));
                                adapter= new CustomAdapterMehanicZapNum(dataModelMehanicZapNa,getApplicationContext());

                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView tv = (TextView)view.findViewById(R.id.mehanic_my_rab_name);
                                        text = tv.getText().toString();
                                        Intent ope = new Intent(mehanic_zap_num.this, detail_sozdat_rab_oper.class);
                                        ope.putExtra("num", num);
                                        ope.putExtra("name", tv.getText().toString());
                                        ope.putExtra("who", "mehanic");
                                        startActivity(ope);

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

    private class getMyRab extends Thread {
        String MobS;
        String PinS;
        String numS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(3);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", numS));

            Log.e("mob", MobS);
            Log.e("PinS", PinS);
            Log.e("numS", numS);
            //getRZProduct.php

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/zapchastNaRZ.php");
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

        public void start(String mobp, String pinp, String numS) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.numS = numS;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

    private class info extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mehanic_zap_num.this);
            progressDialog.setMessage("Поиск работы: "+text);
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
            infoOper = new infoOper();
            infoOper.start(mob, pin, text);

            try {
                infoOper.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=infoOper.getResult();
            try {

                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){

                }else{

                    /*
                    *
                    * {"empty":"ok","res":[{"text":"Выпускной коллектор двигателя M113, снять и поставить", "fix":"no", "categ":"", "value":"15 000", "time":"0", "vid":"", "group":""}]}
                    *
                    *
                    * */
                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    for (int i = 0; i < 1; i++) { // Цикл где в стринг result пришло от баз
                        namePS = jsonArray.getJSONObject(i).getString("text");
                        Log.e("text ",namePS);

                        final String fixS = jsonArray.getJSONObject(i).getString("fix");
                        Log.e("fix ",fixS);

                        categPS = jsonArray.getJSONObject(i).getString("categ");
                        Log.e("categ ",categPS);

                        final String valueS = jsonArray.getJSONObject(i).getString("value");
                        Log.e("value ",valueS);

                        timePS = jsonArray.getJSONObject(i).getString("time");
                        Log.e("time ",timePS);

                        final String vidS = jsonArray.getJSONObject(i).getString("vid");
                        Log.e("vid ",vidS);

                        final String groupS = jsonArray.getJSONObject(i).getString("group");
                        Log.e("group ",groupS);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mehanic_zap_num.this);
                            builder.setTitle("Окно предупреждения!")
                                    .setMessage(Html.fromHtml("Вы действительно уверены что хотите создать рабочую операцию на данный ремотный заказ? <b>"+num+"</b>" +
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
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }
    private class infoOper extends Thread {

            /*
                "empty": "ok",
                "res": [{
                    "brig": "B0404"

            */

        String MobS;
        String PinS;
        String textS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(3);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("text", textS));

            Log.e("mob", MobS);
            Log.e("PinS", PinS);
            Log.e("textS", textS);
            //getRZProduct.php

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

        public void start(String mobp, String pinp, String texts) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.textS = texts;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

    class mehanicCreateRabot extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mehanic_zap_num.this);
            progressDialog.setMessage("Идёт вынос работы "+namePS);
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
            createRabotmehanic.start(mob, pin, namePS, timePS,num,categPS);

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(mehanic_zap_num.this);
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
            NameValuerPair.add(new BasicNameValuePair("num", num));
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
