package com.myauto.designer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class garaj extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private String MobS;
    private String PinS;

    private zapros Zapros1;

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;

    String gn;
    String ma;
    String vin;
    String gnT;
    String org;
    String resulttest;
    String remZ;
    String dato;
    String datc;
    String sum;
    String Gosnm;
    String staty;
    String marka;
    String vincode;
    String tel;
    String Manag;

String result;

    ProgressBar LoadingGaraj;

    ProgressDialog progressDialog;
    String checkprog;
    JSONArray Gosnomer;

    int Numboftabs = 2;
    Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    View holderView, contentView;

    AlertDialog.Builder ad;
    Context context;
    String pin,mob;
    TextView CheckID;
    String che;
    MenuItem target;


    // идентификатор диалогового окна AlertDialog с кнопками
    private final int IDD_THREE_BUTTONS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garaj);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Ваш гараж");
        listView = (ListView) findViewById(R.id.list);

        CheckID=(TextView)findViewById(R.id.checkID);

        //старый drawer
        /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        listView.setDividerHeight(0);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        holderView = findViewById(R.id.holderGaraj);
        contentView = findViewById(R.id.contentGaraj);
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

        LoadingGaraj = (ProgressBar)findViewById(R.id.LoadingGaraj);
        LoadingGaraj.setVisibility(View.VISIBLE);
        CheckID.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                mob = prefs.getString("mob", null);
                pin = prefs.getString("pin", null);
                String name = prefs.getString("Name", null);
                String id = prefs.getString("Id", null);

                String dont = prefs.getString("Dont", null);
                if (mob != null) {
                   /* if (dont.equals("1")){
                        showDialog(IDD_THREE_BUTTONS);
                    }*/
                    // Toast.makeText(getApplicationContext(), "Добро пожаловать *"+ login+"*",
                    //  Toast.LENGTH_SHORT).show();






                MobS = mob;
                PinS = pin;
                Zapros1 = new zapros();
                Zapros1.start(MobS, PinS);

                try {
                    Zapros1.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
                result=Zapros1.resOrg();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /*{"garage": [{"vin":"WBAHB51000GH06434", "gos":"159KHZ05", "mark":"BMW", "model":"", "model_logo":"", "oil_name":"FUCHS", "oil_logo"*/
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("garage");

                            dataModels = new ArrayList<>();
                            Log.e("jsonArray", "Результат: jsonArray " + jsonArray);

                            for(int i = 0; i < jsonArray.length(); i++){
                                vin = jsonArray.getJSONObject(i).getString("vin");
                                Log.e("vin ",vin);
                                gn = jsonArray.getJSONObject(i).getString("gos");
                                Log.e("gn ",gn);
                                ma = jsonArray.getJSONObject(i).getString("mark");
                                Log.e("ma ",ma);
                                dato = jsonArray.getJSONObject(i).getString("model");
                                Log.e("dato ",dato);

                                String Image = jsonArray.getJSONObject(i).getString("model_logo");
                                Log.e("model_logo ",Image);
                                String OIl_logo = jsonArray.getJSONObject(i).getString("oil_logo");
                                Log.e("oil_logo ",OIl_logo);

                                dataModels.add(new DataModel(vin, gn, ma, ma,dato, Image, OIl_logo));
                                adapter = new CustomAdapter(dataModels, getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView tv = (TextView) view.findViewById(R.id.LabelListManagerMarka);
                                        TextView tv2 = (TextView) view.findViewById(R.id.type);
                                        //Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                                        Intent ope = new Intent(garaj.this, Gos.class);
                                        ope.putExtra("Vin", tv.getText().toString());
                                        ope.putExtra("Gos", tv2.getText().toString());
                                        startActivity(ope);
                                    }
                                });

                            }
                        } catch (Exception e) {
                            Log.e("Fail 3", e.toString());
                        }

                        LoadingGaraj.setVisibility(View.INVISIBLE);
                        CheckID.setVisibility(View.INVISIBLE);
                    }
                });
                }else {

                    CheckID.setText("Для просмотра вашего гаража вам необходимо войти...");
                    CheckID.setVisibility(View.VISIBLE);
                    LoadingGaraj.setVisibility(View.INVISIBLE);
                }


            }
        }).start();












//ad.show();





        //--------------------------------------------------------------------------------------------------------------------------------------------

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case IDD_THREE_BUTTONS:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder .setMessage("Вы можете записать пожелания или он же будет будущий ваш ремотный заказ.")
                        .setTitle("Знаете ли вы?")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("Закрыть",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                })


                        .setNegativeButton("Не показывать",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        String donT = "0";
                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString("Dont", donT);
                                        editor.apply();
                                    }
                                });

                return builder.create();
            default:
                return null;
        }
    }


    public class zapros extends Thread {

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
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getGarage.php");
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
        //getMenuInflater().inflate(R.menu.garaj, menu);
        //MenuItem item = menu.findItem(R.id.garaj_add);
        //MenuItem item2 = menu.findItem(R.id.garaj_list);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.garaj_add) {
            Log.e("Gosnomer"," "+Gosnomer);

            Intent ope = new Intent(garaj.this, Lables.class);
            ope.putExtra("Gosnomer", String.valueOf(Gosnomer));
            startActivity(ope);

        } else if(id == R.id.garaj_list){
            Intent intentV = new Intent(this, LablesList.class);
            startActivity(intentV);
       }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_garaj) {


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
            Intent intentP = new Intent(this, MyServiceActivity.class);
            startActivity(intentP);
            finish();

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


    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----Garaj-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Garaj-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Garaj-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Garaj-----", "onStop");
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

        Log.e("-----Garaj-----", "onStart");
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
        Log.e("-----Garaj-----", "onRestart");
    }

}
