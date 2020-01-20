package com.myauto.designer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class market extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<DataModelMarket> dataModelMarkets;
    ListView listView;
    private static CustomAdapterMarket adapter;


    public NavigationView navigationView;
    public DrawerLayout drawer;
    View holderView, contentView;
    MenuItem target;
    ProgressBar progressBar;

    public Market market;
    String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_viewMarket);
        navigationView.setNavigationItemSelectedListener(this);
        holderView = findViewById(R.id.holderMarket);
        contentView = findViewById(R.id.contentMarket);
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


        progressBar = (ProgressBar)findViewById(R.id.Market_progressBar);
        listView=(ListView)findViewById(R.id.list_market);


        new Thread(new Runnable() {
            @Override
            public void run() {
                word = "";
                market = new Market();
                market.start(word);

                try {
                    market.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();



    }

    public class Market extends Thread {

        String WordS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("word", WordS));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/searchAd.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection succes");
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                Log.e("Fail 2", e.toString());
            }
            try {
                //{"ad":[{"id":"ALM00000000000000001",
                // "date":"24.05.2018",
                // "header":"0004209920",
                // "text":"РўРѕСЂРєРё",
                // "cost":"4500",
                // "city":"РђС‚С‹",
                // "brend":"ATE",
                // "article":"0004209920",
                // "pic1" "pic2" "pic3"

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dataModelMarkets = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("ad");

                            for(int i = 0; i < jsonArray.length(); i++){
                                String id = jsonArray.getJSONObject(i).getString("id");
                                Log.e("id ",id);
                                String date = jsonArray.getJSONObject(i).getString("date");
                                Log.e("date ",date);
                                String header = jsonArray.getJSONObject(i).getString("header");
                                Log.e("header ",header);
                                String text = jsonArray.getJSONObject(i).getString("text");
                                Log.e("phone ",text);
                                String cost = jsonArray.getJSONObject(i).getString("cost");
                                Log.e("cost ",cost);
                                String city = jsonArray.getJSONObject(i).getString("city");
                                Log.e("city ",city);
                                String brend = jsonArray.getJSONObject(i).getString("brend");
                                Log.e("brend ",brend);
                                String article = jsonArray.getJSONObject(i).getString("article");
                                Log.e("article ",article);

                                String pic1 = jsonArray.getJSONObject(i).getString("pic1");
                                String pic2 = jsonArray.getJSONObject(i).getString("pic2");
                                String pic3 = jsonArray.getJSONObject(i).getString("pic3");

                                dataModelMarkets.add(new DataModelMarket(pic1,pic2,pic3,city,cost+" тг.",article,date,brend,header,text));
                                adapter= new CustomAdapterMarket(dataModelMarkets,getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        ImageView one = (ImageView) view.findViewById(R.id.Market_list_one);
                                        ImageView two = (ImageView) view.findViewById(R.id.Market_list_two);
                                        ImageView three = (ImageView) view.findViewById(R.id.Market_list_three);

                                        one.buildDrawingCache(true);
                                        Bitmap bitmap = one.getDrawingCache(true);

                                        BitmapDrawable drawable = (BitmapDrawable) one.getDrawable();
                                        bitmap = drawable.getBitmap();

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                        byte[] arr = baos.toByteArray();

                                        String resultFOTO = Base64.encodeToString(arr, Base64.DEFAULT);


                                        two.buildDrawingCache(true);
                                        Bitmap bitmap2 = two.getDrawingCache(true);

                                        BitmapDrawable drawable2 = (BitmapDrawable) two.getDrawable();
                                        bitmap2 = drawable2.getBitmap();

                                        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 50, baos2);
                                        byte[] arr2 = baos2.toByteArray();

                                        String resultFOTO2 = Base64.encodeToString(arr2, Base64.DEFAULT);


                                        three.buildDrawingCache(true);
                                        Bitmap bitmap3 = three.getDrawingCache(true);

                                        BitmapDrawable drawable3 = (BitmapDrawable) three.getDrawable();
                                        bitmap3 = drawable3.getBitmap();

                                        ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
                                        bitmap3.compress(Bitmap.CompressFormat.JPEG, 50, baos3);
                                        byte[] arr3 = baos3.toByteArray();

                                        String resultFOTO3 = Base64.encodeToString(arr3, Base64.DEFAULT);


                                        TextView city = (TextView) view.findViewById(R.id.market_list_city);
                                        TextView summa = (TextView) view.findViewById(R.id.market_list_summa);
                                        TextView article = (TextView) view.findViewById(R.id.market_list_article);
                                        TextView date = (TextView) view.findViewById(R.id.market_list_date);
                                        TextView brend = (TextView) view.findViewById(R.id.market_list_brend);
                                        TextView head = (TextView) view.findViewById(R.id.market_list_head);
                                        TextView text = (TextView) view.findViewById(R.id.market_list_text);

                                        Intent ope = new Intent(com.myauto.designer.market.this, detailAd.class);
                                        ope.putExtra("city", city.getText().toString());
                                        ope.putExtra("summa", summa.getText().toString());
                                        ope.putExtra("article", article.getText().toString());
                                        ope.putExtra("date", date.getText().toString());
                                        ope.putExtra("brend", brend.getText().toString());
                                        ope.putExtra("head", head.getText().toString());
                                        ope.putExtra("text", text.getText().toString());
                                        ope.putExtra("one", resultFOTO);
                                        ope.putExtra("two", resultFOTO2);
                                        ope.putExtra("three", resultFOTO3);

                                        startActivity(ope);
                                    }
                                });
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                Log.e("Fail 3", e.toString());
            }
        }

        public void start(String mobp) {
            this.WordS = mobp;
            this.start();
        }

        public String resOrg() {
            return result;
        }

    }

    public void Search(View view){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                EditText editText = (EditText)findViewById(R.id.market_World);
                String word = editText.getText().toString();
                market = new Market();
                market.start(word);

                try {
                    market.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();
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
        getMenuInflater().inflate(R.menu.market, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

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
            Intent intentV = new Intent(this, MyServiceActivity.class);
            startActivity(intentV);
            finish();

        } else if (id == R.id.nav_prog) {
            Intent intentP = new Intent(this, Info.class);
            startActivity(intentP);

        } else if (id == R.id.nav_manager) {
            Intent intentP = new Intent(this, WorkTable.class);
            startActivity(intentP);
            finish();

        } else if (id == R.id.nav_shop) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

        Log.e("-----Market-----", "onStart");
    }
    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_manager).setVisible(false);
    }
}
