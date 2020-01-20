package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.myauto.designer.Login.MY_PREFS_NAME;




public class Invent extends AppCompatActivity {


    private ZXingScannerView scannerView;
    private Zapross zapross;
    ProgressDialog progressDialog;
    String IPSTRING,mob,pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);
        mob = prefsIP.getString("mob", null);
        pin = prefsIP.getString("pin", null);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        scannerView.setResultHandler(new Invent.ZXingScannerResultHandler());
        scannerView.startCamera();


    }
    private class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler{
        @Override
        public void handleResult(final Result result) {
            final String resultCode = result.getText();
        //Toast.makeText(QRCode.this, resultCode, Toast.LENGTH_LONG).show();


            AlertDialog.Builder builder = new AlertDialog.Builder(Invent.this);
            builder.setTitle("QRCode распознан!")
                    .setIcon(R.drawable.iconlogo)
                    .setMessage(resultCode+" \nВерно?")
                    .setCancelable(false)
                    .setNegativeButton("Нет",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    scannerView = new ZXingScannerView(Invent.this);
                                    scannerView.setResultHandler(new Invent.ZXingScannerResultHandler());

                                    setContentView(scannerView);
                                    scannerView.startCamera();
                                    dialog.dismiss();
                                }
                            })
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sZapross(resultCode);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            scannerView.stopCamera();
        }
    }

    public void sZapross(final String resultCode){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(Invent.this);
                progressDialog.setMessage("Пожалуйста подождите...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                String pin = prefs.getString("pin", null);

                zapross = new Invent.Zapross();
                zapross.start(mob,pin,resultCode);

                try {
                    zapross.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();
    }
        public class Zapross extends Thread {

            String MobS;
            String PinS;
            String ResultCodeS;

            InputStream is = null;
            String result = null;
            String line = null;


            public void run() {

                ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

                NameValuerPair.add(new BasicNameValuePair("mob", MobS));
                NameValuerPair.add(new BasicNameValuePair("pin", PinS));
                NameValuerPair.add(new BasicNameValuePair("qrCode", ResultCodeS));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/setFilialRepair.php");
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
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Invent.this);
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
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Invent.this);
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
                    //{"wheels":[{"date":"10.04.2018", "gos":"602DNA02", "description":"Р—єР°", "brand":"Yokohama", "size":"215/60/R16", "count":"4", "count_month":"2", "sum":"1В 000"} ]}

                    JSONObject jsonObject = new JSONObject(result);
                    final String res = jsonObject.getString("res");
                    Log.e("json", "Результат ip: " + res);

                    if (res.equals("no")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Invent.this);
                                builder.setTitle("Ошибка!")
                                        .setIcon(R.drawable.iconlogo)
                                        .setMessage("Произошла ошибка")
                                        .setCancelable(false)
                                        .setNegativeButton("ок",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        scannerView = new ZXingScannerView(Invent.this);
                                                        scannerView.setResultHandler(new Invent.ZXingScannerResultHandler());

                                                        setContentView(scannerView);
                                                        scannerView.startCamera();
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(Invent.this);
                                builder.setTitle("Окно предупреждения!")
                                        .setIcon(R.drawable.iconlogo)
                                        .setMessage(res)
                                        .setCancelable(false)
                                        .setNegativeButton("ок",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        scannerView = new ZXingScannerView(Invent.this);
                                                        scannerView.setResultHandler(new Invent.ZXingScannerResultHandler());

                                                        setContentView(scannerView);
                                                        scannerView.startCamera();
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }
                    progressDialog.dismiss();
                    Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
                } catch (Exception e) {
                    Log.e("Fail 3", e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Invent.this);
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


            public void start(String mobp, String pin, String resultCode) {
                this.MobS = mobp;
                this.PinS = pin;
                this.ResultCodeS = resultCode;
                this.start();
            }

            public String resOrg() {
                return result;
            }

        }
    @Override
    protected void onPause(){
        super.onPause();
        scannerView.stopCamera();
        Log.e("-----INVENT-----", "onPause");
    }
        }
