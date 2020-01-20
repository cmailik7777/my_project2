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
import android.widget.AdapterView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Service extends AppCompatActivity {

    ArrayList<DataModelOrgService> dataModelOrgServices;
    ListView listView;
    private static CustomAdapterOrgService adapter;

    ArrayList<DataModelMyService> dataModelMyServices;
    ListView listViewManagers;
    private static CustomAdapterMyService adapterManagers;


String res,resManagers,orgName;
    private MyService Myservice;
    private MyServiceOrg Myserviceorg;
    ProgressDialog progressDialog;
    ProgressBar Loading_Service;
    TextView text_Service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Loading_Service = (ProgressBar)findViewById(R.id.Loading_Service);
        listView = (ListView) findViewById(R.id.listview_org_service);
        listViewManagers = (ListView)findViewById(R.id.list_Service);
        text_Service = (TextView)findViewById(R.id.text_Service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                String pin = prefs.getString("pin", null);
                Myserviceorg = new MyServiceOrg();
                Myserviceorg.start(mob, pin);

                try {
                    Myserviceorg.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }

                res = Myserviceorg.resOrg();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //{"services":[{"id":"03", "name":"G MOTORS"}, {"id":"01", "name":"MEGA MOTORS"}]}
                            dataModelOrgServices = new ArrayList<>();

                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray jsonArray = jsonObject.getJSONArray("services");

                            for(int i = 0; i < jsonArray.length(); i++){
                                String OrgName = jsonArray.getJSONObject(i).getString("name");
                                Log.e("OrgName ",OrgName);

                                dataModelOrgServices.add(new DataModelOrgService(OrgName));
                                adapter= new CustomAdapterOrgService(dataModelOrgServices,getApplicationContext());
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView tv = (TextView)view.findViewById(R.id.org_service_org);
                                        orgName = tv.getText().toString();


                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                text_Service.setVisibility(View.INVISIBLE);
                                                Loading_Service.setVisibility(View.VISIBLE);
                                                listView.setVisibility(View.INVISIBLE);
                                            }
                                        });

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                                SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
                                                String mob = prefs.getString("mob", null);
                                                String pin = prefs.getString("pin", null);
                                                Myservice = new MyService();
                                                Myservice.start(mob, pin,orgName);

                                                try {
                                                    Myservice.join();
                                                } catch (InterruptedException ie) {
                                                    Log.e("pass 0", ie.getMessage());
                                                }

                                                resManagers = Myservice.resOrg();

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        //{"managers":[{"name":"РљР№", "hr":"РњРµё", "phone":"+7 771 552 8575", "pic":
                                                        try {
                                                            dataModelMyServices = new ArrayList<>();

                                                            JSONObject jsonObject = new JSONObject(resManagers);
                                                            JSONArray jsonArray = jsonObject.getJSONArray("managers");

                                                            for(int i = 0; i < jsonArray.length(); i++) {
                                                                String name = jsonArray.getJSONObject(i).getString("name");
                                                                Log.e("name ", name);

                                                                String hr = jsonArray.getJSONObject(i).getString("hr");
                                                                Log.e("hr ", hr);

                                                                String phone = jsonArray.getJSONObject(i).getString("phone");
                                                                Log.e("phone ", phone);

                                                                String pic = jsonArray.getJSONObject(i).getString("pic");
                                                                Log.e("pic ", pic);

                                                                dataModelMyServices.add(new DataModelMyService(pic,name,hr,phone));
                                                                adapterManagers = new CustomAdapterMyService(dataModelMyServices, getApplicationContext());
                                                                listViewManagers.setAdapter(adapterManagers);
                                                            }
                                                            text_Service.setVisibility(View.INVISIBLE);
                                                            Loading_Service.setVisibility(View.INVISIBLE);
                                                            listView.setVisibility(View.INVISIBLE);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Loading_Service.setVisibility(View.INVISIBLE);
                                                        }
                                                    }
                                                });
                                            }
                                        }).start();





                                    }
                                });
                            }
                            text_Service.setVisibility(View.VISIBLE);
                            Loading_Service.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Loading_Service.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }).start();

        //{"services":[{"id":"03", "name":"G MOTORS"}, {"id":"01", "name":"MEGA MOTORS"}]}
        //{"managers":[{"name":"РќРµР·Р°РјСѓС‚Р‚РёРЅ", "hr":"РњРєР»Рё", "phone":"+7 705 666 2702", "pic"
    }

    public class MyService extends Thread {

        String MobS;
        String PinS;
        String org;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("nameService", org));

            Log.e("NameValuerPair mob: ",MobS);
            Log.e("NameValuerPair pin: ",PinS);
            Log.e("NameValu nameService: ",org);
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getManagers.php");
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service.this);
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
                //{"managers":[{"name":"РљР№", "hr":"РњРµё", "phone":"+7 771 552 8575", "pic":

                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service.this);
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


        public void start(String mobp, String pinp, String orgName) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.org = orgName;
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
        Log.e("-----Service-----", "onPause");
    }
    @Override
    protected void onResume(){
        super.onResume();




        Log.e("-----Service-----", "onResume");
    }


    public class MyServiceOrg extends Thread {

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
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getMyServices.php");
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service.this);
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
                //{"managers":[{"name":"РљР№", "hr":"РњРµё", "phone":"+7 771 552 8575", "pic":

                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service.this);
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
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Service-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Service-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----Service-----", "onStart");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Service-----", "onRestart");
    }

}
