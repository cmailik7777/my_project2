package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class MyServiceActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView;
    public DrawerLayout drawer;
    View holderView, contentView;
    MenuItem target;

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

    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_viewService);
        navigationView.setNavigationItemSelectedListener(this);
        holderView = findViewById(R.id.holderService);
        contentView = findViewById(R.id.contentService);
        toolbar.setNavigationIcon(new DrawerArrowDrawable(this));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (drawer.isDrawerOpen(navigationView)) {
                                                         drawer.closeDrawer(navigationView);
                                                     } else {
                                                         drawer.openDrawer(navigationView);
                                                     }
                                                 }
                                             }
        );


        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawer, float slideOffset) {


                                         contentView.setX(navigationView.getWidth() * slideOffset);
                                         RelativeLayout.LayoutParams lp =
                                                 (RelativeLayout.LayoutParams) contentView.getLayoutParams();
                                         lp.height = drawer.getHeight() -
                                                 (int) (drawer.getHeight() * slideOffset * 0.3f);
                                         lp.topMargin = (drawer.getHeight() - lp.height) / 2;
                                         contentView.setLayoutParams(lp);
                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                     }
                                 }
        );

        Menu menu =navigationView.getMenu();

        target = menu.findItem(R.id.nav_manager);

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

                res = Myserviceorg. resOrg();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //{"services":[{"id":"03", "name":"G MOTORS"}, {"id":"01", "name":"MEGA MOTORS"}]}
                            dataModelOrgServices = new ArrayList<>();

                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray jsonArray = jsonObject.getJSONArray("services");

                            if (jsonArray!=null && jsonArray.length() > 0){
                                Log.e("DA DA"," NULL");
                                Log.e("DA DA"," NULL");
                                Log.e("DA DA"," NULL");
                                Log.e("DA DA"," NULL");
                                Log.e("DA DA"," NULL");
                                Log.e("DA DA"," NULL");
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
                                                                    listViewManagers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                        @Override
                                                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                                            final TextView tv = (TextView) view.findViewById(R.id.Service_Tel);
                                                                            final TextView tvName = (TextView) view.findViewById(R.id.Service_Name);
                                                                            final String mobS = tv.getText().toString();
                                                                            runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MyServiceActivity.this);
                                                                                    builder.setTitle("")
                                                                                            .setIcon(R.drawable.ic_launcher_round)
                                                                                            .setMessage(""+tvName.getText().toString())
                                                                                            .setCancelable(true)
                                                                                            .setNegativeButton("Позвонить",
                                                                                                    new DialogInterface.OnClickListener() {
                                                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                                                            comeCall(tv.getText().toString());
                                                                                                        }
                                                                                                    })
                                                                                            .setPositiveButton("Написать",
                                                                                                    new DialogInterface.OnClickListener() {
                                                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                                                            String trim = mobS.replaceAll(" ", "");

                                                                                                            Uri uri = Uri.parse("smsto:" + trim);
                                                                                                            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                                                                                                            i.setPackage("com.whatsapp");
                                                                                                            startActivity(i);
                                                                                                        }
                                                                                                    });
                                                                                    AlertDialog alert = builder.create();
                                                                                    alert.show();
                                                                                }
                                                                            });

                                                                        }
                                                                    });
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
                            }else {
                                text_Service.setVisibility(View.VISIBLE);
                                text_Service.setText("Данные о вашем сервисе отсутсвуют...");
                                Loading_Service.setVisibility(View.INVISIBLE);
                            }



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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_READ_PHONE_STATE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.

            } else {

            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void comeCall(String tel) {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + tel));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_PHONE_STATE)) {
            ActivityCompat.requestPermissions(MyServiceActivity.this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_REQUEST_READ_PHONE_STATE);

        }else{
            ActivityCompat.requestPermissions(MyServiceActivity.this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_REQUEST_READ_PHONE_STATE);
        }
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyServiceActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyServiceActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyServiceActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyServiceActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyServiceActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyServiceActivity.this);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String manager_service = prefs.getString("manager_service", null);
                String driver = prefs.getString("driver", null);
                String mehanic = prefs.getString("mehanic", null);
                String mob = prefs.getString("mob", null);

                if (manager_service==null && driver==null && mehanic==null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_manager).setVisible(false);
                }else if (manager_service!=null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_manager).setVisible(true);
                }else if (driver!=null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_manager).setVisible(true);
                }else if (mehanic!=null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_manager).setVisible(true);
                }

                if(mob==null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_garaj).setVisible(false);

                    Menu nav_Menunav_service = navigationView.getMenu();
                    nav_Menunav_service.findItem(R.id.nav_service).setVisible(false);

                    Menu nav_Menunav_shop = navigationView.getMenu();
                    nav_Menunav_shop.findItem(R.id.nav_shop).setVisible(false);

                    Menu nav_Menunav_push = navigationView.getMenu();
                    nav_Menunav_push.findItem(R.id.nav_push).setVisible(false);
                }else {
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_garaj).setVisible(true);

                    Menu nav_Menunav_service = navigationView.getMenu();
                    nav_Menunav_service.findItem(R.id.nav_service).setVisible(true);

                    Menu nav_Menunav_shop = navigationView.getMenu();
                    nav_Menunav_shop.findItem(R.id.nav_shop).setVisible(true);

                    Menu nav_Menunav_push = navigationView.getMenu();
                    nav_Menunav_push.findItem(R.id.nav_shop).setVisible(true);
                }
            }
        });

        Log.e("-----Service-----", "onStart");
    }
    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_manager).setVisible(false);
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Service-----", "onRestart");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_garaj) {
            Intent intentV = new Intent(this, garaj.class);
            startActivity(intentV);
            finish();

        } else if (id == R.id.nav_main) {
            Intent intentV = new Intent(this, Home.class);
            startActivity(intentV);
            finish();


        } else if (id == R.id.nav_push) {
            Intent intentV = new Intent(this, push.class);
            startActivity(intentV);
            finish();
        } else if (id == R.id.nav_profil) {

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String mob = prefs.getString("mob", null);
            if (mob != null){
                Intent intentV = new Intent(this, Profile2.class);
                startActivity(intentV);
            }else{
                Intent intentV = new Intent(this, Login.class);
                startActivity(intentV);
            }

        } else if (id == R.id.nav_service) {


        } else if (id == R.id.nav_prog) {
            Intent intentP = new Intent(this, Info.class);
            startActivity(intentP);

        } else if (id == R.id.nav_manager) {
            Intent intentP = new Intent(this, WorkTable.class);
            startActivity(intentP);
            finish();

        } else if (id == R.id.nav_shop) {
            Intent intentP = new Intent(this, market.class);
            startActivity(intentP);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
