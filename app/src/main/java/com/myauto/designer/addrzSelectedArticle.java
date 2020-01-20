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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class addrzSelectedArticle extends AppCompatActivity {

    TextView nomenT,orgT,priceT,colT,colEDT;
    EditText editCol;
    Button go;
    String colsGO;
    String kol,unit,num,org,nomen,price;

    private CreateNomen createNomen;

    ProgressDialog progressDialog;


    String IPSTRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrz_selected_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        nomenT = (TextView)findViewById(R.id.selected_add_article_nomen);
        orgT = (TextView)findViewById(R.id.selected_add_article_org);
        priceT = (TextView)findViewById(R.id.selected_add_article_price);
        colT = (TextView)findViewById(R.id.selected_add_article_col);
        colEDT = (TextView)findViewById(R.id.selected_add_article_colED);

        editCol = (EditText) findViewById(R.id.selected_add_article_edit_col);

        go = (Button) findViewById(R.id.selected_add_article_go);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        kol = intent.getStringExtra("kol");
        unit = intent.getStringExtra("unit");
        org = intent.getStringExtra("org");
        nomen = intent.getStringExtra("nomen");
        price = intent.getStringExtra("price");
        Log.e("num",num);Log.e("unit",unit);Log.e("kol",kol);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTitle(num);
                nomenT.setText(nomen);
                orgT.setText(org);
                priceT.setText(price);
                kol = kol.substring(0, kol.length()-1);
                colT.setText(kol);
                colEDT.setText(unit);
            }
        });

        editCol.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Считалочка
                String nyZachem = editCol.getText().toString();
                if(editCol.getText().toString().equals("")){
                    priceT.setText(price);
                }else if(nyZachem.equals("0")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(addrzSelectedArticle.this);
                            builder.setTitle("Внимание!")
                                    .setMessage("Вы не можете добавить 0 запчастей!")
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    editCol.setText("");
                                                    dialog.dismiss();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }else{
                    String summa = price;
                    String which = editCol.getText().toString();
                    String stockInt = colT.getText().toString();
                    stockInt = stockInt.replaceAll("\\s+","");
                    final int finallyS;

                    if(Integer.parseInt(which)>Integer.parseInt(stockInt)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(addrzSelectedArticle.this);
                                builder.setTitle("Внимание!")
                                        .setMessage("Нельзя выбрать количество больше чем есть на складе!")
                                        .setCancelable(false)
                                        .setNegativeButton("ок",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        editCol.setText("");
                                                        dialog.dismiss();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }else{
                        summa = summa.replaceAll("\\s+","");


                        finallyS = Integer.parseInt(summa) * Integer.parseInt(which);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                priceT.setText(String.valueOf(finallyS));
                            }
                        });
                    }


                }

            }
        });


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editCol.getText().toString().equals("")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(addrzSelectedArticle.this);
                            builder.setTitle("Внимание!")
                                    .setMessage("Необходимо ввести количество!")
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
                }else if(editCol.getText().toString()=="0"){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(addrzSelectedArticle.this);
                            builder.setTitle("Внимание!")
                                    .setMessage("Вы не можете добавить 0 запчастей!")
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(addrzSelectedArticle.this);
                            builder.setTitle("Окно предупреждения!")
                                    .setMessage(Html.fromHtml("Вы действительно уверены что хотите вынести запчасть на данный ремотный заказ? <b>"+num+"</b>" +
                                            "<br> Номенклатура: <b>"+nomenT.getText().toString()+"</b>" +
                                            "<br> Организация: <b>"+orgT.getText().toString()+"</b>" +
                                            "<br> Кол-во на складе: <b>"+colT.getText().toString()+" "+colEDT.getText().toString()+"</b>" +
                                            "<br> Цена: <b>"+price+"</b>" +
                                            "<br><br> Итог " +
                                            "<br> Итоговая цена: <b>"+priceT.getText().toString()+"</b>" +
                                            "<br> Выбрано количество: <b>"+editCol.getText().toString()+"</b>"))
                                    .setCancelable(false)
                                    .setPositiveButton("Да",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                    /*runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            colsGO=editCol.getText().toString();
                                                        }
                                                    });

                                                    create lettask = new create();
                                                    lettask.execute();*/

                                                }
                                            })
                                    .setNegativeButton("Отмена",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }

            }
        });

    }

    class create extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(addrzSelectedArticle.this);
            progressDialog.setMessage("Вынос запчастей пожалуйста подождите...");
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
            /*  $arr_params['mob'] 		= $mob;
                $arr_params['pin'] 			= $_POST['pin'];
                $arr_params['nomenc'] 		= $_POST['nomenc'];
                $arr_params['ed'] 			= $_POST['ed'];
                $arr_params['quantity'] 		= $_POST['quantity'];
                $arr_params['amount'] 		= $_POST['amount'];
                $arr_params['num'] 		= $_POST['num'];*/
            createNomen = new CreateNomen();
            createNomen.start(mob, pin,nomen,unit,colsGO,price,num);

            try {
                createNomen.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=createNomen.getResult();
            try {
                JSONObject jsonObject = new JSONObject(result);
                final String res = jsonObject.getString("res");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(addrzSelectedArticle.this);
                        builder.setTitle("Окно предупреждения!")
                                .setMessage(res)
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
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }
    }


    public class CreateNomen extends Thread {

        String MobS;
        String PinS,NumS;
        String NomenS;
        String UnitS;
        String ColsGOS;
        String PriceS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            /*    	$arr_params['mob'] 		= $mob;
                    $arr_params['pin'] 			= $_POST['pin'];
                    $arr_params['nomenc'] 		= $_POST['nomenc'];
                    $arr_params['ed'] 			= $_POST['ed'];
                    $arr_params['quantity'] 		= $_POST['quantity'];
                    $arr_params['amount'] 		= $_POST['amount'];
                    $arr_params['num'] 		= $_POST['num'];*/

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("nomenc", NomenS));
            NameValuerPair.add(new BasicNameValuePair("ed", UnitS));
            NameValuerPair.add(new BasicNameValuePair("quantity", ColsGOS));
            NameValuerPair.add(new BasicNameValuePair("amount", PriceS));
            NameValuerPair.add(new BasicNameValuePair("num", NumS));
//getNomenclature
            Log.e("nomenc", NomenS);
            Log.e("ed", UnitS);
            Log.e("quantity", ColsGOS);
            Log.e("amount", PriceS);
            Log.e("num", NumS);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getProductAdd.php");
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



        public void start(String mobp, String pinp, String nomenp, String unitp, String colsGOp, String pricep, String nump) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.NomenS = nomenp;
            this.UnitS = unitp;
            this.ColsGOS = colsGOp;
            this.PriceS = pricep;
            this.NumS = nump;
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
