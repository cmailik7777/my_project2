package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class drEndBeginning extends AppCompatActivity {

    TextView txtMarka,txtModel,txtGos,txtColor,txtId;
    EditText odometr;
    ImageView txtPic;

    ProgressDialog progressDialog;

    private Zapros Zapros1;
    private Zapros2 Zapros2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_end_beginning);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        txtMarka = (TextView)findViewById(R.id.dr_endMarka);
        txtModel = (TextView)findViewById(R.id.dr_endModel);
        txtGos = (TextView)findViewById(R.id.dr_endGos);
        txtColor = (TextView)findViewById(R.id.dr_endColor);
        txtId = (TextView)findViewById(R.id.dr_endId);

        txtPic = (ImageView)findViewById(R.id.dr_endPic);




        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(drEndBeginning.this);
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
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/dr_getSmena.php");
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

                            JSONObject jsonObject = new JSONObject(result);
                            String res = jsonObject.getString("res");

                            if(res.equals("no")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(drEndBeginning.this);
                                builder.setTitle("Окно предупреждения")
                                        .setIcon(R.drawable.iconlogo)
                                        .setMessage("В данный момент у вас отсутсвуют открытая смена. Необходимо cначало открыть смену")
                                        .setCancelable(false)
                                        .setNegativeButton("Отмена",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        finish();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }else{
                                JSONArray jsonArray = jsonObject.getJSONArray("res");
                                for(int i = 0; i < jsonArray.length(); i++){
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

                                    txtId.setText(id);
                                    txtGos.setText(nomer);
                                    txtMarka.setText(marka);
                                    txtModel.setText(model);
                                    txtColor.setText(color);

                                    byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                                    txtPic.setImageBitmap(decodedByte);

                                }
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

    public void end(View view){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(drEndBeginning.this);
                LayoutInflater inflater = drEndBeginning.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                final String km = edt.getText().toString();
                final String id = txtId.getText().toString();


                dialogBuilder.setTitle("Конец рабочего дня");
                dialogBuilder.setIcon(R.drawable.iconlogo);
                dialogBuilder.setMessage("Выбраннная машина \n" +
                        "Марка: " +txtMarka.getText().toString()+"\n"+
                        "Гос. номер: " +txtGos.getText().toString()+"\n"+
                        "Цвет: " +txtColor.getText().toString()+"\n"+
                        "Введите показатель одометра для того чтобы начать смену");
                dialogBuilder.setPositiveButton("Закончить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String kmS = edt.getText().toString();
                        if (kmS.equals("")){
                            kmIsEmpty();
                        }else{
                            endBegin(km,id);
                        }
                    }
                });
                dialogBuilder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });




        /*String km = odometr.getText().toString();
        if (km.equals("")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(drEndBeginning.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(drEndBeginning.this);
                    builder.setTitle("Окно предупреждения")
                            .setIcon(R.drawable.iconlogo)
                            .setMessage("Вы действительно хотите закончить смену на данной машине? "+txtMarka.getText().toString())
                            .setCancelable(false)
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            endBegin();

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }*/


    }

    public void kmIsEmpty(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(drEndBeginning.this);
                builder.setTitle("Окно предупреждения")
                        .setIcon(R.drawable.iconlogo)
                        .setMessage("Введите одометр!")
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
    }

    public void endBegin(final String kmS, final String idAutoS){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(drEndBeginning.this);
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
                String idAuto = txtId.getText().toString();
                String odometrS = odometr.getText().toString();
                Zapros2 = new Zapros2();
                Zapros2.start(mob, pin,idAutoS,kmS);

                try {
                    Zapros2.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();
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
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/dr_closeSmena.php");
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(drEndBeginning.this);
                                        builder.setTitle("Окно предупреждения")
                                                .setIcon(R.drawable.iconlogo)
                                                .setMessage("Смена успешно закрыта")
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(drEndBeginning.this);
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
