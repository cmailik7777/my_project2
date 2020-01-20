package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class drBeginning extends AppCompatActivity {

    ProgressDialog progressDialog;

    private Zapros Zapros1;
    private Zapros2 Zapros2;

    ArrayList<DataModelDrBeginning> dataModelDrBeginnings;
    ListView listView;
    private static CustomAdapterDrBeginning adapter;

    TextView markaDR,gosDR,modelDR,colorDR,idDR;
    EditText odometr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_beginning);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle("Начало работы");

        listView = (ListView)findViewById(R.id.listView_dr_beginning);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(drBeginning.this);
                progressDialog.setMessage("Пожалуйста подождите....");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });




        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                String pin = prefs.getString("pin", null);
                Zapros1 = new Zapros();
                Zapros1.start(mob, pin);

                try {
                    Zapros1.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();




    }

    public class Zapros extends Thread {

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
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/dr_getFreeAuto.php");
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
                /*

                "res": [{
                    "set": "0",
                    "id": "000000002",
                    "nomer": "565CMB02",
                    "marka": "Mitsubishi",
                    "model": "[200] DELICA SPACE GEAR/CARGO (PA-PF# )",
                    "color": "Р·РµР»РµРЅС‹Р№",
                    "pic":

                 */

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dataModelDrBeginnings = new ArrayList<>();

                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("res");

                            for(int i = 0; i < jsonArray.length(); i++){
                                String set = "";
                                String id = jsonArray.getJSONObject(i).getString("id");
                                Log.e("id ",id);
                                String nomer = jsonArray.getJSONObject(i).getString("nomer");
                                Log.e("nomer ",nomer);
                                String marka = jsonArray.getJSONObject(i).getString("marka");
                                Log.e("marka ",marka);
                                String model = jsonArray.getJSONObject(i).getString("model");
                                Log.e("model ",model);
                                String color = jsonArray.getJSONObject(i).getString("color");
                                Log.e("color ",color);
                                String pic = jsonArray.getJSONObject(i).getString("pic");

                                dataModelDrBeginnings.add(new DataModelDrBeginning(set,id,nomer,marka,model,color,pic));
                                adapter= new CustomAdapterDrBeginning(dataModelDrBeginnings,getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        final TextView marka = (TextView) view.findViewById(R.id.dr_beginning_marka);
                                        final TextView model = (TextView) view.findViewById(R.id.dr_beginning_model);
                                        final TextView gos = (TextView) view.findViewById(R.id.dr_beginning_gos);
                                        final TextView color = (TextView) view.findViewById(R.id.dr_beginning_color);
                                        final TextView idS = (TextView) view.findViewById(R.id.dr_beginning_id);
                                        //Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();

                                        final String markaS = marka.getText().toString();
                                        final String modelS = model.getText().toString();
                                        final String gosS = gos.getText().toString();
                                        final String colorS = color.getText().toString();
                                        final String idSS = idS.getText().toString();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(drBeginning.this);
                                                LayoutInflater inflater = drBeginning.this.getLayoutInflater();
                                                final View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
                                                dialogBuilder.setView(dialogView);

                                                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);


                                                dialogBuilder.setTitle("Начало рабочего дня");
                                                dialogBuilder.setIcon(R.drawable.iconlogo);
                                                dialogBuilder.setMessage("Выбраннная машина \n" +
                                                        "Марка: " +markaS+"\n"+
                                                        "Гос. номер: " +gosS+"\n"+
                                                        "Цвет: " +colorS+"\n"+
                                                        "Введите показатель одометра для того чтобы начать смену");
                                                dialogBuilder.setPositiveButton("Приступить", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        String kmS = edt.getText().toString();
                                                        smena(kmS,idSS);
                                                    }
                                                });
                                                dialogBuilder.setNegativeButton("Выбрать другую", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                AlertDialog b = dialogBuilder.create();
                                                b.show();





                                            }
                                        });
                                    }
                                });
                            }
                            progressDialog.dismiss();
                        } catch (Exception e){
                            Log.e("Fail 3", e.toString());
                        }
                    }
                });


                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
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


    public void smena(final String km, final String idAuto){


        if(km.equals("")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(drBeginning.this);
                    builder.setTitle("Окно предупреждения")
                            .setIcon(R.drawable.iconlogo)
                            .setMessage("Введите обязательно показатель одометра!")
                            .setCancelable(false)
                            .setNegativeButton("Хорошо",
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(drBeginning.this);
                    progressDialog.setMessage("Пожалуйста подождите....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
                    String mob = prefs.getString("mob", null);
                    String pin = prefs.getString("pin", null);
                    Zapros2 = new Zapros2();
                    Zapros2.start(mob, pin,idAuto,km);

                    try {
                        Zapros2.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }
                }
            }).start();
        }



    }

    public class Zapros2 extends Thread {

        String MobS;
        String PinS;
        String idAuto;
        String odometrS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            Log.e("pass 1", "BasicNameValuePair mob" + MobS);
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            Log.e("pass 1", "BasicNameValuePair PinS" + PinS);
            NameValuerPair.add(new BasicNameValuePair("idAuto", idAuto));
            Log.e("pass 1", "BasicNameValuePair idAuto" + idAuto);
            NameValuerPair.add(new BasicNameValuePair("odometr", odometrS));
            Log.e("pass 1", "BasicNameValuePair odometrS" + odometrS);
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/dr_setSmena.php");
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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject jsonObject = new JSONObject(result);
                            String res = jsonObject.getString("res");

                            if (res.equals("ok")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(drBeginning.this);
                                        builder.setTitle("Окно предупреждения")
                                                .setIcon(R.drawable.iconlogo)
                                                .setMessage("Смена успешно принята")
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
                                });

                            }else if (res.equals("no")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(drBeginning.this);
                                        builder.setTitle("Ошибка")
                                                .setIcon(R.drawable.iconlogo)
                                                .setMessage("Произошла ошибка. Попробуйте повторить ввод позже")
                                                .setCancelable(false)
                                                .setNegativeButton("Хорошо",
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
                            progressDialog.dismiss();
                        } catch (Exception e){
                            Log.e("Fail 3", e.toString());
                        }
                    }
                });


                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp, String pinp, String idAuto, String odometrS) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.idAuto = idAuto;
            this.odometrS = odometrS;
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

}
