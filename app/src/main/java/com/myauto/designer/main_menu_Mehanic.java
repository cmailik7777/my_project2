package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class main_menu_Mehanic extends AppCompatActivity {

    String IPSTRING;
    ProgressDialog progressDialog;

    String num;

    TextView dataT,managT,gosT,vinT;

    Button goMyRab,goBrigRab,goZap,goSearch;

    EditText name;

    private infoGet infoGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu__mehanic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");

        setTitle(num);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dataT = (TextView)findViewById(R.id.main_menu_data);
        managT = (TextView)findViewById(R.id.main_menu_manag);
        gosT = (TextView)findViewById(R.id.main_menu_gos);
        vinT = (TextView)findViewById(R.id.main_menu_vin);

        goMyRab = (Button)findViewById(R.id.main_menu_my_rab_btn);
        goBrigRab = (Button)findViewById(R.id.main_menu_brig_rab_btn);
        goZap = (Button)findViewById(R.id.main_menu_zap_btn);
        goSearch = (Button)findViewById(R.id.main_menu_search_btn);

        name = (EditText)findViewById(R.id.main_menu_name_edittext);

        goMyRab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ope = new Intent(main_menu_Mehanic.this, mehanic_my_rab.class);
                ope.putExtra("num", num);
                startActivity(ope);
            }
        });

        goBrigRab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ope = new Intent(main_menu_Mehanic.this, mehanic_brig_rab.class);
                ope.putExtra("num", num);
                startActivity(ope);
            }
        });

        goZap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ope = new Intent(main_menu_Mehanic.this, mehanic_zap_num.class);
                ope.putExtra("num", num);
                startActivity(ope);
            }
        });

        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ope = new Intent(main_menu_Mehanic.this, mehanic_find_rab.class);
                ope.putExtra("num", num);
                ope.putExtra("text", name.getText().toString());
                startActivity(ope);
            }
        });


        infoNum letTast = new infoNum();
        letTast.execute();

    }

    class infoNum extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(main_menu_Mehanic.this);
            progressDialog.setMessage("Загрузка информация по "+num);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String mob = prefs.getString("mob", null);
            String pin = prefs.getString("pin", null);
            //-------------------------------------------------------------------------------------------------------------------------------------------
            infoGet = new infoGet();
            infoGet.start(mob, pin);

            try {
                infoGet.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=infoGet.getResult();
            /*

                {
                    "empty": "ok",
                    "res": [{
                        "num": "ALM0117372",
                        "manager": "Нагиленко Виктор",
                        "data": "02.07.2018 9:20:52",
                        "vin": "222.185 S 500 4-MATIC",
                        "gos": "027RXA02"
                    }]
                }

                */
            try {
                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(main_menu_Mehanic.this);
                            builder.setTitle("Внимание!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Информация по рем. заказу не найдена")
                                    .setCancelable(false)
                                    .setNegativeButton("Ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }); //
                }else{
                    final JSONArray jsonArray = jsonObject.getJSONArray("res");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                                    final String manager = jsonArray.getJSONObject(i).getString("manager");
                                    Log.e("manager ",manager);

                                    managT.setText(manager);

                                    final String data = jsonArray.getJSONObject(i).getString("data");
                                    Log.e("data ",data);

                                    dataT.setText(data);

                                    final String vin = jsonArray.getJSONObject(i).getString("vin");
                                    Log.e("vin ",vin);

                                    vinT.setText(vin);

                                    final String gos = jsonArray.getJSONObject(i).getString("gos");
                                    Log.e("gos ",gos);

                                    gosT.setText(gos);


                                }
                            } catch (Exception e) {
                                Log.e("Fail 5", e.toString());
                            }

                        }
                    });

                }

                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }

    public class infoGet extends Thread {

        /*
            "empty": "ok",
            "res": [{
                "brig": "B0404"

        */

        String MobS;
        String PinS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(15);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", num));


            Log.e("mob", MobS);
            Log.e("PinS", PinS);
            Log.e("num", num);
            /*  $arr_params['mob'] 			= $mob;
            $arr_params['pin'] 			= $_POST['pin'];
            $arr_params['brig'] 		= $_POST['brig'];
            $arr_params['num'] 			= $_POST['num'];
            $arr_params['massiv'] 		= $_POST['massiv'];
            $arr_params['data'] 		= $_POST['data'];
            $arr_params['cod'] 		    = $_POST['cod']*/

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://" + IPSTRING + "/oleg/mobile/test/info_meh_num.php");
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
