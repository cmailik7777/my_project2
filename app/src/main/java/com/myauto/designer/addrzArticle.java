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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class addrzArticle extends AppCompatActivity {

    EditText edit_search;
    Button search;
    ArrayList<DataModeladdrzArticle> dataModeladdrzArticles;
    ListView listView;
    private static CustomAdapteraddrzArticle adapter;

    private GetNomenclature getNomenclature;

    ProgressDialog progressDialog;

    String art,num;

    String IPSTRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrz_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        Log.e("num",num);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        edit_search = (EditText) findViewById(R.id.add_rz_article_search);
        search = (Button) findViewById(R.id.add_rz_article_btn_search);
        listView = (ListView) findViewById(R.id.add_rz_article_listview);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                art = edit_search.getText().toString();
                getNomen lettask = new getNomen();
                lettask.execute();
            }
        });


    }

    class getNomen extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final String[] art = new String[1];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    art[0] = edit_search.getText().toString();
                }
            });
            progressDialog = new ProgressDialog(addrzArticle.this);
            progressDialog.setMessage("Поиск по артикулу \n"+art[0]+"");
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
            getNomenclature = new GetNomenclature();
            getNomenclature.start(mob, pin, art);
            Log.e("mob, pin, art[0]", mob+" "+ pin+" "+ art);

            try {
                getNomenclature.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getNomenclature.getResult();
            try {
                dataModeladdrzArticles = new ArrayList<>();
                /*

                {
                    "empty": "ok",
                    "res": [{
                        "nomen": "Р—Р°С‰РёС‚РЅС‹Р№ РєРѕР»РїР°С‡РѕРє С„РёРєСЃРёСЂСѓСЋС‰РµРіРѕ Р±РѕР»С‚Р° С‚РѕСЂРјРѕР·РЅРѕРіРѕ СЃСѓРїРѕСЂС‚Р° РњР’",
                        "price": "1В 000",
                        "org": "CAR CITY",
                        "quantity": "1",
                        "unit": "С€С‚."


                */

                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(addrzArticle.this);
                            builder.setTitle("Внимание!")
                                    .setMessage("Данные по артикулу "+art+" отсвутвуют!")
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //ifLabel.setVisibility(View.INVISIBLE);

                        }
                    });
                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    int j = 1;
                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        String nomen = jsonArray.getJSONObject(i).getString("nomen");
                        if (nomen.isEmpty()){
                            nomen="Пусто";
                        }
                        Log.e("nomen ",nomen);

                        String price = jsonArray.getJSONObject(i).getString("price");
                        if (price.isEmpty()){
                            price="Пусто";
                        }
                        Log.e("price ",price);
                        String org = jsonArray.getJSONObject(i).getString("org");
                        if (org.isEmpty()){
                            org="Пусто";
                        }
                        Log.e("org ",org);

                        String quantity = jsonArray.getJSONObject(i).getString("quantity");
                        if (quantity.isEmpty()){
                            quantity="Пусто";
                        }
                        Log.e("quantity ",quantity);

                        String unit = jsonArray.getJSONObject(i).getString("unit");
                        if (unit.isEmpty()){
                            unit="Пусто";
                        }
                        Log.e("unit ",unit);

                        final String count = String.valueOf(j);

                        final String finalNomen = nomen;
                        final String finalPrice = price;
                        final String finalOrg = org;
                        final String finalQuantity = quantity + " ";
                        final String finalUnit = unit;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataModeladdrzArticles.add(new DataModeladdrzArticle(count, finalNomen, finalPrice, finalOrg, finalQuantity + finalUnit,finalQuantity,finalUnit));
                                adapter= new CustomAdapteraddrzArticle(dataModeladdrzArticles,getApplicationContext());

                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView kol = (TextView)view.findViewById(R.id.add_rz_article_needS);
                                        TextView unit = (TextView)view.findViewById(R.id.add_rz_article_needI);
                                        TextView org = (TextView)view.findViewById(R.id.add_rz_article_org);
                                        TextView price = (TextView)view.findViewById(R.id.add_rz_article_price);
                                        TextView nomen = (TextView)view.findViewById(R.id.add_rz_article_nomen);
                                        Intent ope = new Intent(addrzArticle.this, addrzSelectedArticle.class);
                                        ope.putExtra("num", num);
                                        ope.putExtra("kol", kol.getText().toString());
                                        ope.putExtra("unit", unit.getText().toString());
                                        ope.putExtra("org", org.getText().toString());
                                        ope.putExtra("price", price.getText().toString());
                                        ope.putExtra("nomen", nomen.getText().toString());
                                        startActivity(ope);
                                    }
                                });
                            }
                        });



                        j=j+1;
                    }
                    //
                }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }

    public class GetNomenclature extends Thread {

        String MobS;
        String PinS,artS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("art", artS));
//getNomenclature
            Log.e("artS",artS);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getNomenclature.php");
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

        public void start(String mobp, String pinp, String artp) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.artS = artp;
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
