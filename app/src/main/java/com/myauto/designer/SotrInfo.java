package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class SotrInfo extends AppCompatActivity {

    TextView org,podr,code,dolj,brig,proc;
    TextView procLabel,brigLabel;

    Button info_zp;

    String IPSTRING;

    ProgressDialog progressDialog;

    private PersonalInfo personalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sotr_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        org = (TextView)findViewById(R.id.sotr_info_org);
        podr = (TextView)findViewById(R.id.sotr_info_podraz);
        code = (TextView)findViewById(R.id.sotr_info_sotrcode);
        dolj = (TextView)findViewById(R.id.sotr_info_dolj);
        brig = (TextView)findViewById(R.id.sotr_info_brig);
        proc = (TextView)findViewById(R.id.sotr_info_proc);

        procLabel = (TextView)findViewById(R.id.sotr_info_procLABEL);
        brigLabel = (TextView)findViewById(R.id.sotr_info_brigLABEL);

        info_zp = (Button) findViewById(R.id.sotr_info_zp);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("name", null);
        final String sotr = prefs.getString("code", null);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                code.setText(sotr);
            }
        });
        IPSTRING = prefs.getString("ip", null);
        setTitle(name);

        info_zp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        getPersonalInfo letTast = new getPersonalInfo();
        letTast.execute();
    }

    class getPersonalInfo extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SotrInfo.this);
            progressDialog.setMessage("Пожалуйста подождите...");
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
            personalInfo = new PersonalInfo();
            personalInfo.start(mob, pin);

            try {
                personalInfo.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=personalInfo.getResult();
            try {
                /*
                    	"empty": "ok",
                        "res": [{
                            "org": "TEST MOTORS",
                            "podr": "IT РґРµРїР°СЂС‚Р°РјРµРЅС‚",
                            "dolj": "РћРїРµСЂР°С‚РѕСЂ Р­Р’Рњ",
                            "brig": "",
                            "proc": "0"
                        }]
                }

                */
                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){

                }else {
                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    final String orgS = jsonArray.getJSONObject(0).getString("org");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            org.setText(orgS);
                        }
                    });

                    final String podrS = jsonArray.getJSONObject(0).getString("podr");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            podr.setText(podrS);
                        }
                    });

                    final String doljS = jsonArray.getJSONObject(0).getString("dolj");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dolj.setText(doljS);
                        }
                    });

                    final String brigS = jsonArray.getJSONObject(0).getString("brig");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (brigS!=null){
                                brig.setVisibility(View.INVISIBLE);
                                brigLabel.setVisibility(View.INVISIBLE);
                            }else{
                                brig.setVisibility(View.VISIBLE);
                                brigLabel.setVisibility(View.VISIBLE);
                                brig.setText(brigS);
                            }

                        }
                    });
                    final String procS = jsonArray.getJSONObject(0).getString("proc");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (procS.equals("0")){
                                proc.setVisibility(View.INVISIBLE);
                                procLabel.setVisibility(View.INVISIBLE);

                            }else{
                                proc.setVisibility(View.VISIBLE);
                                procLabel.setVisibility(View.VISIBLE);
                                proc.setText(procS);
                            }
                        }
                    });

                    Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
                }
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }

    public class PersonalInfo extends Thread {

        String MobS;
        String PinS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            //PersonalRoom.php?mob=+7%20777%20534%207677&pin=6020

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/nd/PersonalRoom.php");
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
