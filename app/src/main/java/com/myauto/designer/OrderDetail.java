package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.myauto.designer.Drivers.mobDr;
import static com.myauto.designer.Drivers.pinDr;

public class OrderDetail extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    ArrayList<DataModelOrderDetail> dataModelOrderDetails;
    ListView listView;
    private static CustomAdapterOrderDetail adapter;

    private Zapros Zapros1;
    private Zapros2 zapros2;
    private ZaprosOpen zaprosOpen;
    private ZaprosClose zaprosClose;

    ProgressDialog progressDialog;

    String result;
    ProgressBar progressBar;
    Button btn_drivers;
    String who;
    String bar,stat,numt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = (ListView)findViewById(R.id.listview_order_detail);
        progressBar = (ProgressBar)findViewById(R.id.driver_order_detail_progresbar);
        btn_drivers = (Button)findViewById(R.id.btn_drivers);

        /*Intent intent = getIntent();
        final String numOrder = intent.getStringExtra("numOrder");
        final String numMyOrder = intent.getStringExtra("numMyOrder");
        final String numHistoryOrder = intent.getStringExtra("numHistoryOrder");

        if(numOrder!=null){
            setTitle(numOrder);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        who = "0";
                                        Zapros1 = new Zapros();
                                        Zapros1.start(mobDr, pinDr,numOrder,who);
                                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                                        Zapros1.join();
                                    } catch (InterruptedException ie) {
                                        Log.e("pass 0", ie.getMessage());
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn_drivers.setText("Забрать рейс");
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });

                                    btn_drivers.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            numOrder(numOrder);
                                        }
                                    });


                                }
                            }).start();

        }else if (numMyOrder!=null){
            setTitle(numMyOrder);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_drivers.setVisibility(View.GONE);
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        who = "1";
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numMyOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            }).start();

        }else if (numHistoryOrder!=null){
            setTitle(numHistoryOrder);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_drivers.setVisibility(View.GONE);
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    who = "0";
                    try {
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numHistoryOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();

        }*/

        final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh_OrdersDetail);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listview(); // your code
                pullToRefresh.setRefreshing(false);

                listView.setAdapter(null);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }

    public void listview(){
        Intent intent = getIntent();
        final String numOrder = intent.getStringExtra("numOrder");
        final String numMyOrder = intent.getStringExtra("numMyOrder");
        final String numHistoryOrder = intent.getStringExtra("numHistoryOrder");



        if(numOrder!=null){
            setTitle(numOrder);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        who = "0";
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_drivers.setText("Забрать рейс");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                    btn_drivers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            numOrder(numOrder);
                        }
                    });


                }
            }).start();

        }else if (numMyOrder!=null){
            setTitle(numMyOrder);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_drivers.setVisibility(View.GONE);
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        who = "1";
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numMyOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            }).start();

        }else if (numHistoryOrder!=null){
            setTitle(numHistoryOrder);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_drivers.setVisibility(View.GONE);
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    who = "0";
                    try {
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numHistoryOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();

        }
    }

    public void numOrder (final String num){

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
        builder.setTitle("Окно предупреждения!")
                .setIcon(R.drawable.iconlogo)
                .setMessage("Вы уверены что хотите забрать данный рейс?")
                .setCancelable(false)
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog = new ProgressDialog(OrderDetail.this);
                                        progressDialog.setMessage("Пожалуйста подождите....");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                    }
                                });


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            zapros2 = new Zapros2();
                                            zapros2.start(mobDr, pinDr,num);
                                            Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                                            zapros2.join();
                                        } catch (InterruptedException ie) {
                                            Log.e("pass 0", ie.getMessage());
                                        }
                                        progressDialog.dismiss();
                                    }
                                }).start();

                            }
                        })
                .setPositiveButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void numMyOrderAccept (){
        Toast.makeText(getApplicationContext(), "numMyOrderAccept", Toast.LENGTH_SHORT).show();
    }

    public void numMyOrderGet (){
        Toast.makeText(getApplicationContext(), "numMyOrderGet", Toast.LENGTH_SHORT).show();
    }

    public class Zapros extends Thread {

        String MobS;
        String PinS;
        String NumS,WhoS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", NumS));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/nd/getDriverByNumber.php");
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
                dataModelOrderDetails = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("body");

                    /*[{
                    "num": "ALM000000001",
                    "start_org": "Р¤СѓСЂРјР°РЅРѕРІР°",
                    "start_adress": "Р—РµР»РµРЅС‹Р№ Р±Р°Р·Р°СЂ",
                    "start_cl": "РћР»РµРі",
                    "start_tel": "911",
                    "stop_org": "РљСѓРЅР°РµРІР°",
                    "stop_adress": "1,21",
                    "stop_cl": "Р’Р°Р»РµСЂР°",
                    "stop_tel": "119",
                    "info": "Р’Р·СЏС‚СЊ С‚Р°РїРѕС‡РєРё",
                    "owner": "Mega Motors Astana"
                }]*/

                    for(int i = 0; i < jsonArray.length(); i++){
                        final String num = jsonArray.getJSONObject(i).getString("num");
                        Log.e("Order Detail --- num",num);

                        final String start_org = jsonArray.getJSONObject(i).getString("start_org");
                        Log.e("Order Detail start_org",start_org);

                        final String start_adress = jsonArray.getJSONObject(i).getString("start_adress");
                        Log.e("Order start_adress",start_adress);

                        final String start_cl = jsonArray.getJSONObject(i).getString("start_cl");
                        Log.e("Order start_cl",start_cl);

                        final String start_tel = jsonArray.getJSONObject(i).getString("start_tel");
                        Log.e("Order Detail --- num",start_tel);

                        final String stop_org = jsonArray.getJSONObject(i).getString("stop_org");
                        Log.e("Order Detail --- num",stop_org);

                        final String stop_adress = jsonArray.getJSONObject(i).getString("stop_adress");
                        Log.e("Order Detail --- num",stop_adress);

                        final String stop_cl = jsonArray.getJSONObject(i).getString("stop_cl");
                        Log.e("Order Detail --- num",stop_cl);

                        final String stop_tel = jsonArray.getJSONObject(i).getString("stop_tel");
                        Log.e("Order Detail --- num",stop_tel);

                        final String info = jsonArray.getJSONObject(i).getString("info");
                        Log.e("Order Detail --- num",info);

                        final String owner = jsonArray.getJSONObject(i).getString("owner");
                        Log.e("Order Detail --- num",owner);

                        final String barcode = jsonArray.getJSONObject(i).getString("barcode");
                        Log.e("Order Detail  barcode",barcode);

                        final String statys = jsonArray.getJSONObject(i).getString("status");
                        Log.e("Order Detail  statys",statys);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataModelOrderDetails.add(new DataModelOrderDetail(num,start_org,start_adress,start_cl,start_tel,stop_org,stop_adress,stop_cl,stop_tel,info,owner,WhoS,barcode,statys));
                                adapter= new CustomAdapterOrderDetail(dataModelOrderDetails,getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView textView = (TextView)view.findViewById(R.id.ss_who);
                                        TextView statys = (TextView)view.findViewById(R.id.ss_statys);
                                        TextView barcode = (TextView)view.findViewById(R.id.ss_barcode);
                                        final TextView numtxt = (TextView)view.findViewById(R.id.lblListItem);
                                        final String who = textView.getText().toString();
                                        stat = statys.getText().toString();
                                        bar = barcode.getText().toString();
                                        numt = numtxt.getText().toString();
                                        Log.e("AAAAAAAAAAAAA","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+bar);
                                        if(who.equals("0")){
                                            Log.e("NEA","nihera");
                                        }else{
                                            if(stat.equals("Ожидание")){
                                                if(bar.equals("")){
                                                    progressDialog = new ProgressDialog(OrderDetail.this);
                                                    progressDialog.setMessage("Пожалуйста подождите....");
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.show();
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                zaprosOpen = new ZaprosOpen();
                                                                zaprosOpen.start(mobDr, pinDr,num);
                                                                zaprosOpen.join();
                                                            } catch (InterruptedException ie) {
                                                                Log.e("pass 0", ie.getMessage());
                                                            }
                                                            progressDialog.dismiss();
                                                        }
                                                    }).start();

                                                }else{
                                                    showCameraPreview();
                                                }
                                            }else if(stat.equals("Выполнение")){
                                                if(bar.equals("")){
                                                    progressDialog = new ProgressDialog(OrderDetail.this);
                                                    progressDialog.setMessage("Пожалуйста подождите....");
                                                    progressDialog.setCancelable(false);
                                                    progressDialog.show();
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                zaprosClose = new ZaprosClose();
                                                                zaprosClose.start(mobDr, pinDr,num);
                                                                zaprosClose.join();
                                                            } catch (InterruptedException ie) {
                                                                Log.e("pass 0", ie.getMessage());
                                                            }
                                                            progressDialog.dismiss();
                                                        }
                                                    }).start();

                                                }else{
                                                    showCameraPreview();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp, String pinp, String numOrder, String who) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.NumS = numOrder;
            this.WhoS = who;
            this.start();
        }

        public String resOrg() {
            return result;
        }

    }

    //Взять рейс
    public class Zapros2 extends Thread {

        String MobS;
        String PinS;
        String NumS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", NumS));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/nd/setDriverOrder.php");
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
                JSONObject jsonObject = new JSONObject(result);

                final String rec = jsonObject.getString("res");
                Log.e("json", "Результат: res" + rec);

                if (rec.equals("no")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
                            builder.setTitle("")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Данный рейс уже забрали!")
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finish();
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
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
                            builder.setTitle("Успешно!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Вы успешно забрали рейс!")
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finish();
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp, String pinp, String num) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.NumS = num;
            this.start();
        }

        public String resOrg() {
            return result;
        }

    }

    //забрать груз
    //забрать груз
    public class ZaprosOpen extends Thread {

        String MobS;
        String PinS;
        String NumS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", NumS));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/nd/setDriverStatusOpenOrder.php");
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
                JSONObject jsonObject = new JSONObject(result);

                final String res = jsonObject.getString("res");
                Log.e("json", "Результат: res" + res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
                        builder.setTitle("")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage(res)
                                .setCancelable(false)
                                .setNegativeButton("ок",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                Update();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp, String pinp, String num) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.NumS = num;
            this.start();
        }

        public String resOrg() {
            return result;
        }

    }

    //груз доставлен
    public class ZaprosClose extends Thread {

        String MobS;
        String PinS;
        String NumS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", NumS));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/nd/setDriverStatusCloseOrder.php");
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
                JSONObject jsonObject = new JSONObject(result);

                final String res = jsonObject.getString("res");
                Log.e("json", "Результат: res" + res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
                        builder.setTitle("")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage(res)
                                .setCancelable(false)
                                .setNegativeButton("ок",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                Update();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp, String pinp, String num) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.NumS = num;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startCamera();
            } else{

            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void showCameraPreview() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if(stat.equals("Ожидание")){
                Intent ope = new Intent(OrderDetail.this, QRCode.class);
                ope.putExtra("myOrderBarOpen", numt);
                ope.putExtra("myOrderBarOpenBar", bar);
                startActivity(ope);
            }else if (stat.equals("Выполнение")){
                Intent ope = new Intent(OrderDetail.this, QRCode.class);
                ope.putExtra("myOrderBarClose", numt);
                ope.putExtra("myOrderBarCloseBar", bar);
                startActivity(ope);
            }

        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
        // END_INCLUDE(startCamera)
    }

    /**
     * Requests the {@link android.Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CAMERA)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для исполнения операции с QR кодом, " +
                            "вам необходимо разрешить использовании камеры в приложении My Auto. \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(OrderDetail.this,
                                            new String[]{android.Manifest.permission.CAMERA},
                                            PERMISSION_REQUEST_CAMERA);
                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetail.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для исполнения операции с QR кодом, " +
                            "вам необходимо разрешить использовании камеры в приложении My Auto. \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(OrderDetail.this,
                                            new String[]{android.Manifest.permission.CAMERA},
                                            PERMISSION_REQUEST_CAMERA);
                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void startCamera() {
        Intent ope = new Intent(OrderDetail.this, QRCode.class);
        ope.putExtra("myOrderBar", bar);
        startActivity(ope);
    }

    public void Update(){
        Intent intent = getIntent();
        final String numOrder = intent.getStringExtra("numOrder");
        final String numMyOrder = intent.getStringExtra("numMyOrder");
        final String numHistoryOrder = intent.getStringExtra("numHistoryOrder");

        listView.setAdapter(null);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        if(numOrder!=null){
            setTitle(numOrder);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        who = "0";
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_drivers.setText("Забрать рейс");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                    btn_drivers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            numOrder(numOrder);
                        }
                    });


                }
            }).start();

        }else if (numMyOrder!=null){
            setTitle(numMyOrder);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_drivers.setVisibility(View.GONE);
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        who = "1";
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numMyOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            }).start();

        }else if (numHistoryOrder!=null){
            setTitle(numHistoryOrder);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_drivers.setVisibility(View.GONE);
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    who = "0";
                    try {
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numHistoryOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        final String numOrder = intent.getStringExtra("numOrder");
        final String numMyOrder = intent.getStringExtra("numMyOrder");
        final String numHistoryOrder = intent.getStringExtra("numHistoryOrder");

        listView.setAdapter(null);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        if(numOrder!=null){
            setTitle(numOrder);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        who = "0";
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_drivers.setText("Забрать рейс");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                    btn_drivers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            numOrder(numOrder);
                        }
                    });


                }
            }).start();

        }else if (numMyOrder!=null){
            setTitle(numMyOrder);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_drivers.setVisibility(View.GONE);
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        who = "1";
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numMyOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            }).start();

        }else if (numHistoryOrder!=null){
            setTitle(numHistoryOrder);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btn_drivers.setVisibility(View.GONE);
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    who = "0";
                    try {
                        Zapros1 = new Zapros();
                        Zapros1.start(mobDr, pinDr,numHistoryOrder,who);
                        Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();

        }
    }
}
