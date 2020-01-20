package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.iid.FirebaseInstanceId;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.myauto.designer.Login.MY_PREFS_NAME;
import static com.myauto.designer.R.id.holder;
import static com.myauto.designer.R.id.nav_garaj;
import static com.myauto.designer.R.id.nav_service;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    View holderView, contentView;
    MenuItem target;

    ProgressDialog progressDialog;

    private zapros Zapros1;
    private zaprosVersion zaprosVersion;

    private zaprosNews ZaprosNews;
    ProgressBar progressBar;

    ArrayList<DataModelNews> dataModelNewses;
    ListView listView;
    private static CustomAdapterNews adapter;


    public static final int CONNECTION_TIMEOUT = 100000;
    public static final int READ_TIMEOUT = 150000;

    private static final int RECEIVE_BOOT_COMPLETED = 0;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterRec;

    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.RecHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





//caNhLUMLFe8:APA91bG5yOPYHviGyWCMVmi37yypkeVHwnRybSr0A7DRQYFDUDwYizqFkdzPZehOg-O9CTVaNztjntdhRHh74X6IlPLiknikSL5aF-h9ssixYZudM-obgmZDCZNGrx4vRnYeoLPm5hclqwZeWK4PLpqVNyBCIcqNEQ
        Log.e("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());

        setTitle("");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        progressBar = (ProgressBar)findViewById(R.id.Home_Progressbar);
        listView = (ListView) findViewById(R.id.listview_home);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);




        Menu menu =navigationView.getMenu();

        target = menu.findItem(R.id.nav_manager);

        holderView = findViewById(holder);
        contentView = findViewById(R.id.content);
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

         final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        /*login = prefs.getString("Login", null);
        String password = prefs.getString("Password", null);
        String name = prefs.getString("Name", null);
        String id = prefs.getString("Id", null);
        String sotr = prefs.getString("Sotr", null);*/

        String manager_service = prefs.getString("manager_service", null);

        if (manager_service==null){
            hideItem();
        }

        final String mob = prefs.getString("mob", null);
        final String pin = prefs.getString("pin", null);

        if (mob==null){
            Log.e("","");
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String sotr = prefs.getString("manager_service", null);
                    String token = FirebaseInstanceId.getInstance().getToken();
                    Log.e("Firebase", "token "+ token);

                    Zapros1 = new zapros();
                    Zapros1.start(mob, pin,token);

                    try {
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }
                }
            }).start();
        }
        startNews lettask = new startNews();
        lettask.execute();
    }


    private void hideItem() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_manager).setVisible(false);
    }


    class startNews extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String mob = prefs.getString("mob", null);
            String pin = prefs.getString("pin", null);

            ZaprosNews = new zaprosNews();
            ZaprosNews.start(mob, pin);

            try {
                ZaprosNews.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }
            String result = ZaprosNews.getResult();
            try {
             /*	"news": [{
                    "id": "00000000000000000015",
                    "otvetstven": "РђР»РµРєСЃРµРµРІ РЎРµСЂРіРµР№",
                    "instagram": "g_motors_auto",
                    "data": "10.10.2018 12:02:50",
                    "dopmob": "+7 77 777 7441",
                    "mob": "+7 777 777 4379",
                    "caption": "РЎРєРѕСЂРѕ Р·РёРјР°!",
                    "description": "",
                    "text": "РљРѕРјРїР°РЅРёСЏ G Motors РЅР°РїРѕРјРёРЅР°РµС‚ СЃРІРѕРёРј РєР»РёРµРЅС‚Р°Рј Рѕ РЅРµРѕР±С…РѕРґРёРјРѕСЃС‚Рё РїСЂРѕС…РѕР¶РґРµРЅРёСЏ РїР»Р°РЅРѕРІРѕРіРѕ РўРћ СЃ СЂР°СЃС€РёСЂРµРЅРЅС‹РјРё РјРµСЂР°РјРё РїРѕ РїСЂРµРґРѕС‚РІСЂР°С‰РµРЅРёСЋ С…Р°СЂР°РєС‚РµСЂРЅС‹С… Р·РёРјРЅРёС… СЃР»РѕР¶РЅРѕСЃС‚РµР№. РЎРІРѕРµРІСЂРµРјРµРЅРЅР°СЏ Р·Р°РјРµРЅР° С‚РѕРїР»РёРІРЅС‹С… Рё РІРѕР·РґСѓС€РЅС‹С… С„РёР»СЊС‚СЂРѕРІ,РїСЂРѕРІРµСЂРєР° СЂР°Р±РѕС‚РѕСЃРїРѕСЃРѕР±РЅРѕСЃС‚Рё СЃРёСЃС‚РµРј РѕР±Р»РµРіС‡РµРЅРёСЏ Р·Р°РїСѓСЃРєР° РґРІРёРіР°С‚РµР»СЏ,Р·Р°РјРµРЅР° РѕС…Р»Р°Р¶РґР°СЋС‰РµР№ Р¶РёРґРєРѕСЃС‚Рё Рё РїСЂРѕРІРµСЂРєР° РђРљР‘ РїРѕРјРѕР¶РµС‚ Р’Р°Рј РёР·Р±РµР¶Р°С‚СЊ РїСЂРѕСЃС‚РѕСЏ С‚РµС…РЅРёРєРё РІ С…РѕР»РѕРґРЅРѕРµ РІСЂРµРјСЏ РіРѕРґР°.",
                    "ico": "/",
                    "pic": ""
                },*/
                listItems = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("news");
                for(int j = 0; j < jsonArray.length(); j++) {
                    JSONObject o = jsonArray.getJSONObject(j);
                    ListItem item = new ListItem(
                            o.getString("caption"),
                            o.getString("text"),
                            o.getString("ico"),
                            o.getString("pic"),
                            o.getString("mob"),
                            o.getString("otvetstven"),
                            o.getString("instagram"),
                            o.getString("data")
                    );


                    /*final String caption = jsonArray.getJSONObject(j).getString("caption");
                    Log.e("caption ",caption);
                    final String description = jsonArray.getJSONObject(j).getString("description");
                    Log.e("description ",description);

                    final String text = jsonArray.getJSONObject(j).getString("text");
                    Log.e("text ",text);

                    final String mobS = jsonArray.getJSONObject(j).getString("mob");
                    Log.e("mob ",mobS);

                    final String otvet = jsonArray.getJSONObject(j).getString("otvetstven");
                    Log.e("otvet ",otvet);

                    final String instagram = jsonArray.getJSONObject(j).getString("instagram");
                    Log.e("instagram ",instagram);

                    final String data = jsonArray.getJSONObject(j).getString("data");
                    Log.e("data ",data);

                    final String ico = jsonArray.getJSONObject(j).getString("ico");
                    Log.e("ico ",ico);
                    final String pic = jsonArray.getJSONObject(j).getString("pic");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dataModelNewses.add(new DataModelNews(caption, text, ico,text,pic,mobS,otvet,instagram,data));
                            adapter= new CustomAdapterNews(dataModelNewses,getApplicationContext());
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    TextView TVhead = (TextView) view.findViewById(R.id.textHead);
                                    TextView TVtext = (TextView) view.findViewById(R.id.textDescription);
                                    TextView TVBase64Pic = (TextView) view.findViewById(R.id.base64Pic);
                                    ImageView Iico = (ImageView) view.findViewById(R.id.ico);

                                    TextView totvet = (TextView) view.findViewById(R.id.textOtvet);
                                    TextView tmob = (TextView) view.findViewById(R.id.textMob);//textInsta

                                    TextView tinsta = (TextView) view.findViewById(R.id.textInsta);//textInsta

                                    Intent ope = new Intent(Home.this, detail_news.class);
                                    ope.putExtra("head", TVhead.getText().toString());
                                    ope.putExtra("desc", TVtext.getText().toString());
                                    ope.putExtra("image", TVBase64Pic.getText().toString());

                                    ope.putExtra("otvet", totvet.getText().toString());
                                    ope.putExtra("mob", tmob.getText().toString());

                                    ope.putExtra("insta", tinsta.getText().toString());
                                    startActivity(ope);
                                }
                            });
                        }
                    });*/

                    listItems.add(item);
                }

                adapterRec = new MyAdapter(listItems, getApplicationContext());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapterRec);
                    }
                });



            } catch (Exception e){
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }

    public class zapros extends Thread {
        String MobS;
        String PinS;
        String TokenS;

        String PostIDP = "empty";
        String PostLOGINP = "empty";
        String PostMOBP = "empty";
        String PostPP = "empty";
        String PostCodeP = "empty";
        String PostFP = "empty";
        String PostIP = "empty";
        String PostOP = "empty";
        String PostAvaP = "empty";
        String PostdrP = "empty";
        String PostRoleP = "empty";
        String PostDriverP = "empty";
        String PostErrorP = "";
        String PostMehanicP = "";
        String PostModeratorS = "";

        InputStream is = null;
        String result = null;
        String line = null;


        public void run(){

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("mob",MobS));
            NameValuerPair.add(new BasicNameValuePair("pin",PinS));
            NameValuerPair.add(new BasicNameValuePair("tokenAPNS",TokenS));

            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/logon.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection succes");
            } catch (Exception e){
                Log.e("Fail 1", e.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Ошибка подключения! Пожалуйста попробуйте позже.")
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

            try{

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "\n");
                }
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "[}][,][{]");
                }
                is.close();
                result = sb.toString();

                Log.e("pass 2", "connection succes" + result);
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Ошибка подключения! " +
                                        "Пожалуйста попробуйте позже.")
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
                JSONObject jsonObject = new JSONObject(result);

                /*
                	"id": "000000001",
                    "name": "РќРёРєРёС‚РёРЅ Р”РјРёС‚СЂРёР№",
                    "code": "000000001",
                    "f": "РќРёРєРёС‚РёРЅ",
                    "i": "Р”РјРёС‚СЂРёР№",
                    "o": "",
                    "dr": "01.01.0001 0:00:00",
                    "ava": "/9
                */

                PostErrorP = jsonObject.getString("ERROR");
                if (PostErrorP.equals("empty")){ // не правильный пин ИЛИ моб
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences.Editor editor = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("id", null);
                            editor.putString("name", null);

                            editor.putString("mob", null);
                            editor.putString("pin", null);

                            editor.putString("f", null);
                            editor.putString("i", null);
                            editor.putString("o", null);
                            editor.putString("dr", null);

                            editor.putString("ava", null);
                            editor.putString("manager_service", null);
                            editor.putString("driver", null);
                            editor.putString("mehanic", null);
                            editor.putString("moderator", null);

                            editor.putString("code", null);

                            editor.apply();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setTitle("Окно предупреждения!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Ваши данные были изменены!" +
                                            "\nПожалуйста повторите авторизацию.")
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
                }else{

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();


                    PostIDP = jsonObject.getString("id");
                    Log.e("json", "Результат: " + PostIDP);

                    PostLOGINP = jsonObject.getString("name");
                    Log.e("json", "Результат: " + PostLOGINP);

                    PostCodeP = jsonObject.getString("code");
                    Log.e("json", "Результат: " + PostCodeP);

                    PostFP = jsonObject.getString("f");
                    if (PostFP.equals("")){
                        PostFP = "";
                    }
                    Log.e("json", "Результат: " + PostFP);

                    PostIP = jsonObject.getString("i");
                    if (PostIP.equals("")){
                        PostIP = "";
                    }
                    Log.e("json", "Результат: " + PostFP);

                    PostOP = jsonObject.getString("o");
                    if (PostOP.equals("")){
                        PostOP = "";
                    }
                    Log.e("json", "Результат: " + PostOP);

                    PostdrP = jsonObject.getString("dr");
                    if (PostdrP.equals("01.01.0001 0:00:00")){
                        PostdrP = "";
                    }
                    Log.e("json", "Результат: " + PostdrP);

                    PostAvaP = jsonObject.getString("ava");
                    if (PostAvaP.equals("")){
                        PostAvaP = null;
                    }
                    Log.e("json", "Результат: " + PostAvaP);
                    /*"roles": [{
                        "role": "manager_service"
                    }],*/

                    JSONArray jsonArray = jsonObject.getJSONArray("roles");
                    Log.e("jsonArray", "Результат: jsonArray " + jsonArray);


                    if (jsonArray.length() > 0 && jsonArray!=null){
                        Log.e("lol", "if (jsonArray.length() > 0 && jsonArray!=null)");
                        for(int i = 0; i < jsonArray.length(); i++){
                            String role = jsonArray.getJSONObject(i).getString("role");
                            Log.e("lol", "Результат: role " + role);

                            if(role.equals("manager_service")){
                                PostRoleP = role;
                                Log.e("lol", "Результат: PostRoleP " + PostRoleP);
                            }else{
                                if(PostRoleP.equals("manager_service")){

                                }else {
                                    PostRoleP = null;
                                }

                            }

                            if(role.equals("driver")){
                                PostDriverP = role;
                                Log.e("lol", "Результат: PostDriverP " + PostDriverP);
                            }else{
                                if(PostDriverP.equals("driver")){

                                }else {
                                    PostDriverP = null;
                                }
                            }

                            if(role.equals("mehanic")){
                                PostMehanicP = role;
                                Log.e("lol", "Результат: PostMehanicP " + PostMehanicP);
                            }else{
                                if(PostMehanicP.equals("mehanic")){

                                }else {
                                    PostMehanicP = null;
                                }
                            }

                            if(role.equals("moderator")){
                                PostModeratorS = role;
                                Log.e("lol", "Результат: PostModeratorS " + PostModeratorS);
                            }else{
                                if(PostModeratorS.equals("moderator")){

                                }else {
                                    PostModeratorS = null;
                                }
                            }

                        }
                    }else {
                        PostRoleP = null;
                        PostDriverP = null;
                        PostMehanicP = null;

                        Log.e("gogo", "if (jsonArray.length() < 0 && jsonArray=null)");
                        Log.e("gogo", "Результат: PostDriverP " + PostDriverP);
                        Log.e("gogo", "Результат: PostRoleP " + PostRoleP);
                    }

                    editor.putString("id", PostIDP);
                    editor.putString("name", PostLOGINP);

                    editor.putString("f", PostFP);
                    editor.putString("i", PostIP);
                    editor.putString("o", PostOP);
                    editor.putString("dr", PostdrP);

                    editor.putString("manager_service", PostRoleP);
                    editor.putString("driver", PostDriverP);
                    editor.putString("mehanic", PostMehanicP);

                    editor.putString("ava", PostAvaP);

                    editor.putString("code", PostCodeP);
                    editor.apply();


                }

               /* PostIDP = json_data.getJSONObject("name");
                PostLOGINP = json_data.getJSONArray(0).get(1).toString();
                PostMOBP = json_data.getJSONArray(0).get(2).toString();
                PostPP = json_data.getJSONArray(0).get(3).toString();
                PostSotrName = json_data.getJSONArray(0).get(4).toString();
                PostSotr = json_data.getJSONArray(0).get(5).toString();
                Log.e("pass 3", PostSotr);*/
            } catch (Exception e){
                Log.e("Fail 3", e.toString());

            }




        }


        public void start(String mobp, String pinp, String token) {

            this.MobS = mobp;
            this.PinS = pinp;
            this.TokenS = token;
            this.start();


        }

    }

    public class zaprosNews extends Thread {

            String MobS;
            String PinS;

            InputStream is = null;
            String result = null;
            String line = null;

        public void run(){
            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("mob",MobS));
            NameValuerPair.add(new BasicNameValuePair("pin",PinS));

            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/newsMyAuto.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection succes");
            } catch (Exception e){
                Log.e("Fail 1", e.toString());
            }

            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "\n");
                }
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "[}][,][{]");
                }
                is.close();
                result = sb.toString();

                Log.e("pass 2", "connection succes" + result);
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }
            try {
                Log.e("pass 3", "" + result);
            } catch (Exception e){
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp, String pinp) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.start();

        }

        public String getResult() {
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == nav_garaj) {
            Intent intentV = new Intent(this, garaj.class);
            startActivity(intentV);

        } else if (id == R.id.nav_main) {

        } else if (id == R.id.nav_push) {
            Intent intentV = new Intent(this, push.class);
            startActivity(intentV);
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

        } else if (id == nav_service) {
            Intent intentP = new Intent(this, MyServiceActivity.class);
            startActivity(intentP);

        } else if (id == R.id.nav_prog) {
            Intent intentP = new Intent(this, Info.class);
            startActivity(intentP);

        } else if (id == R.id.nav_manager) {
            Intent intentP = new Intent(this, WorkTable.class);
            startActivity(intentP);

        } else if (id == R.id.nav_shop) {
            Intent intentP = new Intent(this, market.class);
            startActivity(intentP);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----MainActivity-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----MainActivity-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----MainActivity-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----MainActivity-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();

        //new AsyncRetrieve().execute();

       /* progressDialog = new ProgressDialog(Home.this);
        progressDialog.setMessage("Идет проверка обновлнения....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String vers = "1,17";
                zaprosVersion = new zaprosVersion();
                zaprosVersion.start(vers);

                try {
                    zaprosVersion.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();*/



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

                //nav_garaj nav_push nav_service nav_shop
            }
        });
        Log.e("-----MainActivity-----", "onStart");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----MainActivity-----", "onRestart");
    }

    public class zaprosVersion extends Thread {

        String vers;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run(){

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("vers",vers));
            Log.e("BasicNameValuePair vers", vers);

            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/nd/getAndroidVersion.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection succes");
            } catch (Exception e){
                Log.e("Fail 1", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });

            }

            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "\n");
                }
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "[}][,][{]");
                }
                is.close();
                result = sb.toString();

                Log.e("pass 2", "connection succes" + result);
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                final String version = jsonObject.getString("res");
                if (version.equals("ok")) {
                    Log.e("Version check"," Yes 1.17 version");
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                            builder.setTitle("Обновление")
                                    .setIcon(R.drawable.ic_launcher_round)
                                    .setMessage("Вышло новое обновление приложения!\n \nТекущая версия 1.17\n")
                                    .setCancelable(false)
                                    .setNegativeButton("Отмена",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            })
                                    .setPositiveButton("Обновить",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    intent.setData(Uri.parse("market://details?id=com.myauto.designer"));
                                                    startActivity(intent);
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            } catch (Exception e){
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
        }


        public void start(String mobp) {

            this.vers = mobp;
            this.start();

        }
    }








    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        // This method does not interact with UI, You need to pass result to onPostExecute to display
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://gps-monitor.kz/oleg/androidVersion.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        // this method will interact with UI, display result sent from doInBackground method
        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("1.15")) {
                Log.e("Version check"," Yes 1.15 version");
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle("Обновление")
                                .setIcon(R.drawable.ic_launcher_round)
                                .setMessage("Вышло новое обновление приложения!\n \nТекущая версия 1.15\nНовая версия приложения "+result)
                                .setCancelable(false)
                                .setNegativeButton("Отмена",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        })
                                .setPositiveButton("Обновить",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse("market://details?id=com.myauto.designer"));
                                                startActivity(intent);
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        }
    }




}
