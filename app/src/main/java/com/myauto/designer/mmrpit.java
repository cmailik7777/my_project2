package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class mmrpit extends AppCompatActivity {

    ArrayList<DataModelPrint> dataModelPrints;
    ListView listView;
    private static CustomAdapterPrint adapter;


    private String MobS;
    private String PinS;

    String nom;
    String vid;
    String cena;
    String kol;
    String summa;
    String dato;
    String datc;

    TextView textView;

    int pp;
    String ppFinal;

    ProgressDialog progressDialog;

    private zapros Zapros1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmrpit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView=(ListView)findViewById(R.id.listPrint);


        SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
        String login = prefs.getString("Login", null);
        String password = prefs.getString("Password", null);
        String name = prefs.getString("Name", null);
        String id = prefs.getString("Id", null);

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
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/androidMMprint.php");
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
                dataModelPrints = new ArrayList<>();
                JSONArray json_data = new JSONArray(result);



                for (int i = 0; i < json_data.length(); i++) { // Цикл где в стринг result пришло от баз
                    nom = json_data.getJSONArray(i).get(0).toString(); // gn string равна данной строке 6
                    vid = json_data.getJSONArray(i).get(1).toString(); // gn string равна данной строке 6
                    kol = json_data.getJSONArray(i).get(2).toString();// ma string равна данной строке 8
                    cena = json_data.getJSONArray(i).get(3).toString();// vin string равна данной строке 9
                    summa = json_data.getJSONArray(i).get(4).toString(); // gn string равна данной строке 6
                    dato = json_data.getJSONArray(i).get(5).toString(); // gn string равна данной строке 6
                    datc = json_data.getJSONArray(i).get(6).toString();// ma string равна данной строке

                    dataModelPrints.add(new DataModelPrint(nom, vid, kol,cena,summa, dato,datc,datc));

                        adapter= new CustomAdapterPrint(dataModelPrints,getApplicationContext());
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               // TextView tv = (TextView) view.findViewById(R.id.nzak);
                                //Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                                //Intent ope = new Intent(mmrpit.this, Gos.class);
                                //ope.putExtra("result", result);
                                //ope.putExtra("GosNomer", tv.getText().toString());
                                //startActivity(ope);
                            }
                        });

                    pp = i+1;
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
