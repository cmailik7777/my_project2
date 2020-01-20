package com.myauto.designer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class MyAd extends AppCompatActivity {


    ProgressBar progressBar;
    TextView myAd_text;

    ArrayList<DataModelMyAd> dataModelMyAds;
    ListView listView;
    private static CustomAdapterMyAd adapter;

    public MyAds myAds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressBar = (ProgressBar)findViewById(R.id.myAd_Progressbar);
        myAd_text = (TextView)findViewById(R.id.myAd_text);

        listView=(ListView)findViewById(R.id.myAd_listview);


        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs2.getString("mob", null);
                String pin = prefs2.getString("pin", null);
                myAds = new MyAds();
                myAds.start(mob,pin);

                try {
                    myAds.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();

    }

    public class MyAds extends Thread {

        String PinS,MobS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getMyAd.php");
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
                //{"my_ad":[
                // {"id":"ALM00000000000000002",
                // "date":"24.05.2018",
                // "header":"0001802609",
                // "text":"Р¤РёР»СЊС‚СЂ С‚РѕРїР»РёРІРЅС‹Р№",
                // "cost":"111",
                // "city":"РђСЃС‚Р°РЅР°",
                // "brend":"BOSCH",
                // "article":"0001802609",
                // "cancel":"РќРµС‚",
                // "checkup":"Р”Р°",
                // "pic1":"",
                // "pic2":"",
                // "pic3":""}

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dataModelMyAds = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("my_ad");

                            for(int i = 0; i < jsonArray.length(); i++){
                                String id = jsonArray.getJSONObject(i).getString("id");
                                Log.e("id ",id);
                                String date = jsonArray.getJSONObject(i).getString("date");
                                Log.e("date ",date);
                                String header = jsonArray.getJSONObject(i).getString("header");
                                Log.e("header ",header);
                                String text = jsonArray.getJSONObject(i).getString("text");
                                Log.e("text ",text);
                                String cost = jsonArray.getJSONObject(i).getString("cost");
                                Log.e("cost ",cost);
                                String city = jsonArray.getJSONObject(i).getString("city");
                                Log.e("city ",city);
                                String brend = jsonArray.getJSONObject(i).getString("brend");
                                Log.e("brend ",brend);
                                String article = jsonArray.getJSONObject(i).getString("article");
                                Log.e("article ",article);

                                String cancel = jsonArray.getJSONObject(i).getString("cancel");
                                Log.e("cancel ",cancel);

                                String checkup = jsonArray.getJSONObject(i).getString("checkup");
                                Log.e("checkup ",checkup);
                                
                                String status = "";
                                if(cancel.equals("Да")){
                                    status = "Отклонён";
                                }else if(checkup.equals("Да")){
                                    status = "В ожидании публикации";
                                }else if (checkup.equals("Нет")){
                                    status = "Опубликован";
                                }

                                String pic1 = jsonArray.getJSONObject(i).getString("pic1");
                                String pic2 = jsonArray.getJSONObject(i).getString("pic2");
                                String pic3 = jsonArray.getJSONObject(i).getString("pic3");

                                dataModelMyAds.add(new DataModelMyAd(id,date,header,text,cost+" тг.","г. "+city,brend,article,status,pic1,pic2,pic3));
                                adapter= new CustomAdapterMyAd(dataModelMyAds,getApplicationContext());
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

                                        Intent ope = new Intent(MyAd.this, detailAd.class);
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

        public void start(String mobp, String pin) {
            this.MobS = mobp;
            this.PinS = pin;
            this.start();
        }

        public String resOrg() {
            return result;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.myad_add, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        int id = item.getItemId();

        if (id == R.id.myAd_Add) {
            Intent intentV = new Intent(this, myad_add.class);
            startActivity(intentV);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----MyAd-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----MyAd-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----MyAd-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----MyAd-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----MyAd-----", "onStart");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----MyAd-----", "onRestart");
    }

}
