package com.myauto.designer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class trucksolutionsZAKAZ extends AppCompatActivity {

    ArrayList<DataModelTruckSolutionsZAKAZ> dataModelTruckSolutionseZAKAZs;
    ListView listView;
    private static CustomAdapterTruckSolutionsZAKAZ adapter;


    String truckOO;
    String truckOG;
    String truckOA;
    String truckOL;
    String truckPO;
    String truckPG;
    String truckPA;
    String truckPL;

    private String MobS;

    private zapros Zapros1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_trucksolutionszakaz);


        listView = (ListView) findViewById(R.id.listTruckZakaz);

        Intent intent = getIntent();

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        String Nomert = intent.getStringExtra("Nomert");

        setTitle(Nomert);

        Log.e("Massive CHECK", " == " + Nomert);
        Toast.makeText(getApplicationContext(), "Переданный гос номер  " + Nomert,
                Toast.LENGTH_SHORT).show();


        //-------------------------------------------------------------------------------------------------------------------------------------------
        MobS = Nomert;
        Zapros1 = new zapros();
        Zapros1.start(MobS);

        try {
            Zapros1.join();
        } catch (InterruptedException ie) {
            Log.e("pass 0", ie.getMessage());
        }
        //--------------------------------------------------------------------------------------------------------------------------------------------
    }


    public class zapros extends Thread {

        String MobS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("nomerGruz", MobS));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/androidTruckSolutionsZakaz.php");
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
                dataModelTruckSolutionseZAKAZs = new ArrayList<>();
                JSONArray json_data = new JSONArray(result);
                for (int i = 0; i < json_data.length(); i++) { // Цикл где в стринг result пришло от базы
                    truckOO = json_data.getJSONArray(i).get(0).toString(); // gn string равна данной строке 6
                    truckOG = json_data.getJSONArray(i).get(1).toString(); // gn string равна данной строке 6
                    truckOA = json_data.getJSONArray(i).get(2).toString();// ma string равна данной строке 8
                    truckOL = json_data.getJSONArray(i).get(3).toString();// vin string равна данной строке 9
                    truckPO = json_data.getJSONArray(i).get(4).toString(); // gn string равна данной строке 6
                    truckPG = json_data.getJSONArray(i).get(5).toString(); // gn string равна данной строке 6
                    truckPA = json_data.getJSONArray(i).get(6).toString();// ma string равна данной строке 8
                    truckPL = json_data.getJSONArray(i).get(7).toString();// vin string равна данной строке 9

                    dataModelTruckSolutionseZAKAZs.add(new DataModelTruckSolutionsZAKAZ(truckOO, truckOG, truckOA, truckOL, truckPO, truckPG, truckPA, truckPL, truckPL));
                    adapter = new CustomAdapterTruckSolutionsZAKAZ(dataModelTruckSolutionseZAKAZs, getApplicationContext());
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //TextView tv = (TextView) view.findViewById(R.id.truckOO);
                            //Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                            //Intent ope = new Intent(Gos.this, Tovar.class);
                            //ope.putExtra("RemZ", tv.getText().toString());
                            //startActivity(ope);
                        }
                    });

                }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp) {

            this.MobS = mobp;
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
