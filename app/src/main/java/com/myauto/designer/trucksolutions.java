package com.myauto.designer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class trucksolutions extends AppCompatActivity {

    ArrayList<DataModelTruckSolutions> dataModelTruckSolutionses;
    ListView listView;
    private static CustomAdapterTruckSolutions adapter;

    private String MobS;
    private String PinS;


    String servicet;
    String nomert;
    String vest;
    String obemt;
    String mestt;
    String summat;

    private zapros Zapros1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trucksolutions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView=(ListView)findViewById(R.id.listTruck);


        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
        String login = prefs.getString("Login", null);
        String password = prefs.getString("Password", null);
        String name = prefs.getString("Name", null);
        String id = prefs.getString("Id", null);
        if (login != null) {
            Toast.makeText(getApplicationContext(), "Добро пожаловать *"+ login+"*",
                    Toast.LENGTH_SHORT).show();
        }

        //-------------------------------------------------------------------------------------------------------------------------------------------
        MobS = login;
        PinS = password;
        Zapros1 = new zapros();
        Zapros1.start(MobS, PinS);

        try {
            Zapros1.join();
        } catch (InterruptedException ie) {
            Log.e("pass 0", ie.getMessage());
        }
        //--------------------------------------------------------------------------------------------------------------------------------------------
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
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/androidTruckSolutions.php");
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
                dataModelTruckSolutionses = new ArrayList<>();
                JSONArray json_data = new JSONArray(result);
                for (int i = 0; i < json_data.length(); i++) { // Цикл где в стринг result пришло от базы
                    servicet = json_data.getJSONArray(i).get(0).toString(); // gn string равна данной строке 6
                    nomert = json_data.getJSONArray(i).get(1).toString(); // gn string равна данной строке 6
                    vest = json_data.getJSONArray(i).get(2).toString();// ma string равна данной строке 8
                    obemt = json_data.getJSONArray(i).get(3).toString();// vin string равна данной строке 9
                    mestt = json_data.getJSONArray(i).get(4).toString(); // gn string равна данной строке 6
                    summat = json_data.getJSONArray(i).get(5).toString(); // gn string равна данной строке 6

                    dataModelTruckSolutionses.add(new DataModelTruckSolutions(servicet, nomert, vest,obemt,mestt, summat,summat));

                    adapter= new CustomAdapterTruckSolutions(dataModelTruckSolutionses,getApplicationContext());
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView tv = (TextView) view.findViewById(R.id.nzaktruck);
                            //Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                            Intent ope = new Intent(trucksolutions.this, trucksolutionsZAKAZ.class);
                            ope.putExtra("result", result);
                            ope.putExtra("Nomert", tv.getText().toString());
                            startActivity(ope);
                        }
                    });

                }
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
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
