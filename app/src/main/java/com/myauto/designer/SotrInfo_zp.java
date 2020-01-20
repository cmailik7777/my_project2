package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class SotrInfo_zp extends AppCompatActivity {

    ArrayList<DataModalSotr_Info_Doc> dataModalSotr_info_docs;
    ListView listView;
    private static CustomAdapterSotr_Info_Doc adapter;

    String IPSTRING;

    private InfoZp infoZp;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sotr_info_zp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.sotr_info_zp_listview);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefs.getString("ip", null);

    }

    class goInfoDoc extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SotrInfo_zp.this);
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
            infoZp = new InfoZp();
            infoZp.start(mob, pin);

            try {
                infoZp.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=infoZp.getResult();
            try {
                /*
                    	{
                        "empty": "ok",
                        "res": [{
                            "num": "ALM004498",
                            "dok": "РќР°С‡РёСЃР»РµРЅРёРµ Р—Рџ ALM004498 РѕС‚ 17.05.2016 17:08:00"
                        }, {
                            "num": "ALM004485",
                            "dok": "РќР°С‡РёСЃР»РµРЅРёРµ Р—Рџ ALM004485 РѕС‚ 17.05.2016 16:44:04"
                        }]
                    }
                }

                */
                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){

                }else {
                    /*JSONArray jsonArray = jsonObject.getJSONArray("res");
                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        String text = jsonArray.getJSONObject(i).getString("text");
                        Log.e("text ",text);
                        String date = jsonArray.getJSONObject(i).getString("date");
                        Log.e("date ",date);

                        dataModelPushes.add(new DataModelPush(text,date));
                        adapter= new CustomAdapterPush(dataModelPushes,getApplicationContext());

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
                    }*/
                    Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
                }
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }

    public class InfoZp extends Thread {

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

}
