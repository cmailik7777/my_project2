package com.myauto.designer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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


public class rab_oper extends AppCompatActivity {

    ArrayList<DataModelRabOper> dataModelRabOpers;
    ListView listView;
    private static CustomAdapterRabOper adapter;


    private getRab_Oper getRabOper;

    ProgressBar pb;
    TextView ifLabel,all_count;
    String num;
    String count;
    RelativeLayout relativeLayout5;

    String IPSTRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rab_oper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pb = (ProgressBar)findViewById(R.id.rab_oper_ProgressBar);
        ifLabel = (TextView)findViewById(R.id.rab_oper_ifLabel);
        listView = (ListView)findViewById(R.id.rab_oper_listView);

        all_count = (TextView)findViewById(R.id.rab_oper_count_all);

        relativeLayout5=(RelativeLayout)findViewById(R.id.relativeLayout5);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        Log.e("num",num);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent ope = new Intent(rab_oper.this, sozdat_rab_oper.class);
                ope.putExtra("num", num);
                ope.putExtra("who", "manager");
                startActivity(ope);
            }
        });

        startRab_Oper();



    }

    public void startRab_Oper(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.VISIBLE);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                final String pin = prefs.getString("pin", null);

                //-------------------------------------------------------------------------------------------------------------------------------------------
                getRabOper = new getRab_Oper();
                getRabOper.start(mob, pin,num);

                try {
                    getRabOper.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
                //--------------------------------------------------------------------------------------------------------------------------------------------

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String result=getRabOper.getResult();
                        try {
                            dataModelRabOpers = new ArrayList<>();


                            JSONObject jsonObject = new JSONObject(result);
                            String empty = jsonObject.getString("empty");
                            if (empty.equals("no")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ifLabel.setVisibility(View.VISIBLE);
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ifLabel.setVisibility(View.INVISIBLE);

                                    }
                                });

                                /*	"empty": "ok",
                                        "res": [{
                                            "brigade": "A0407",
                                            "operations": "РљРѕРЅС‚СЂРѕР»СЊ СЂР°Р±РѕС‚С‹ Р°РІС‚РѕРјР°РіРЅРёС‚РѕР»С‹",
                                            "time": "0",
                                            "amountbz": "5В 000",
                                            "amountskidka": "0",
                                            "amountuchet": "5В 000",
                                            "amount": "5В 000",
                                            "data": "10.03.2018 8:41:28",
                                            "responsible": "Р“СѓСЃР°СЂРѕРІ Р”РµРЅРёСЃ",
                                            "category": "Р­Р»РµРєС‚СЂРёС‡РµСЃРєРёРµ СЂР°Р±РѕС‚С‹"
                                        },
                               */

                                JSONArray jsonArray = jsonObject.getJSONArray("res");
                                count = "";

                                int j = 1;
                                for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                                    String brigade = jsonArray.getJSONObject(i).getString("brigade");
                                    if (brigade.isEmpty()){
                                        brigade="Пусто";
                                    }
                                    Log.e("brigade ",brigade);

                                    String operations = jsonArray.getJSONObject(i).getString("operations");
                                    if (operations.isEmpty()){
                                        operations="Пусто";
                                    }
                                    Log.e("operations ",operations);
                                    String data = jsonArray.getJSONObject(i).getString("data");
                                    if (data.isEmpty()){
                                        data="Пусто";
                                    }
                                    Log.e("data ",data);
                                    String responsible = jsonArray.getJSONObject(i).getString("responsible");
                                    if (responsible.isEmpty()){
                                        responsible="Пусто";
                                    }
                                    Log.e("responsible ",responsible);
                                    String category = jsonArray.getJSONObject(i).getString("category");
                                    if (category.isEmpty()){
                                        category="Пусто";
                                    }
                                    Log.e("category ",category);
                                    String amount = jsonArray.getJSONObject(i).getString("amount");
                                    if (amount.isEmpty()){
                                        amount="Пусто";
                                    }
                                    Log.e("amount ",amount);
                                    count = String.valueOf(j);

                                    dataModelRabOpers.add(new DataModelRabOper(brigade,responsible,category,amount,operations,data,count));
                                    adapter= new CustomAdapterRabOper(dataModelRabOpers,getApplicationContext());

                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            //Intent ope = new Intent(mmrpit.this, Gos.class);
                                            //ope.putExtra("result", result);
                                            //ope.putExtra("GosNomer", tv.getText().toString());
                                            //startActivity(ope);
                                        }
                                    });

                                    j=j+1;
                                }
                            }
                            Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
                        } catch (Exception e) {
                            Log.e("Fail 3", e.toString());
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                all_count.setText(count);
                            }
                        });
                        pb.setVisibility(View.INVISIBLE);
                        relativeLayout5.setVisibility(View.VISIBLE);

                    }
                });



            }
        }).start();
    }

    public class getRab_Oper extends Thread {

        String MobS;
        String PinS,numS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", numS));

            Log.e("num",numS);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getMyWork.php");
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

        public void start(String mobp, String pinp, String num) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.numS = num;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
