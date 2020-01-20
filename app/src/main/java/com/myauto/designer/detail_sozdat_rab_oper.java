package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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

public class detail_sozdat_rab_oper extends AppCompatActivity {

    Spinner brigada,vid1,vid2,group,category;
    CheckBox fix;
    EditText name,value,time;
    Button sozdat;

    String[] spBrig;
    String[] spVid1;
    String[] spVid2;
    String[] spGroup;
    String[] spCategory;

    private getBrigada getBrigada;
    private getForma getForma;
    private Sozdat_rab sozdat_rab;
    private getFormaWithName getFormaWithName;

    private createRabotmehanic createRabotmehanic;

    String first,vid1S,vid2S,brS,categS;
    String num;
    String nameI;

    ProgressDialog progressDialog;

    String nameForever,valueForever,timeForever;
    String categSF,groupSF;

    String IPSTRING,nameOper,namePS, timePS,numPS,categPS;

    String who;

    TextView titleBrig;
    TextInputLayout valueTIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sozdat_rab_oper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        nameI = intent.getStringExtra("name");
        who = intent.getStringExtra("who");
        Log.e("num",num);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //-------------------------Spinners------------------------------------------------------
        brigada = (Spinner)findViewById(R.id.detail_sozdat_rab_brigada);
        vid1 = (Spinner)findViewById(R.id.detail_sozdat_rab_vid1);
        vid2 = (Spinner)findViewById(R.id.detail_sozdat_rab_vid2);
        group = (Spinner)findViewById(R.id.detail_sozdat_rab_group);
        category = (Spinner)findViewById(R.id.detail_sozdat_rab_category);
        //-------------------------//Spinners------------------------------------------------------

        titleBrig = (TextView)findViewById(R.id.textView89);
        valueTIL = (TextInputLayout)findViewById(R.id.textInputLayout16);

        //-------------------------Checkbox------------------------------------------------------
        fix = (CheckBox) findViewById(R.id.checkboxFix);
        //-------------------------//Checkbox------------------------------------------------------

        //-------------------------EditText------------------------------------------------------
        name = (EditText) findViewById(R.id.detail_sozdat_rab_name);
        value = (EditText) findViewById(R.id.detail_sozdat_rab_value);
        time = (EditText) findViewById(R.id.detail_sozdat_rab_time);
        //-------------------------//EditText------------------------------------------------------
        sozdat=(Button)findViewById(R.id.detail_sozdat_rab_btn_sozdat);

        nameForever = name.getText().toString();
        valueForever = value.getText().toString();
        timeForever = time.getText().toString();

        if(who.equals("mehanic")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fix.setVisibility(View.INVISIBLE);
                    value.setVisibility(View.INVISIBLE);
                    brigada.setVisibility(View.INVISIBLE);
                    value.setVisibility(View.INVISIBLE);
                    titleBrig.setVisibility(View.INVISIBLE);
                    valueTIL.setVisibility(View.INVISIBLE);
                }
            });
        }




        sozdat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(who.equals("mehanic")){
                            categS = category.getSelectedItem().toString();
                            if (name.getText().toString().equals("")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                                        builder.setTitle("Внимание!")
                                                .setMessage("Заполните пожалуйста наименование работы!")
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
                            }else if (categS.equals("")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                                        builder.setTitle("Внимание!")
                                                .setMessage("Выберите пожалуйста категорию работы!")
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                                builder.setTitle("Окно предупреждения!")
                                        .setMessage(Html.fromHtml("Вы действительно уверены что хотите создать рабочую операцию на данный ремотный заказ? <b>" + num + "</b>" +
                                                "<br> Работа: <b>" + name.getText().toString() + "</b>" +
                                                "<br><br> Категория ремонта: <b>" + category.getSelectedItem().toString() + "</b>" +
                                                "<br> Норма времени: <b>" + time.getText().toString() + "</b>"))
                                        .setCancelable(false)
                                        .setNegativeButton("Отмена",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                        .setPositiveButton("Да",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                        namePS = name.getText().toString();
                                                        categS = category.getSelectedItem().toString();
                                                        timePS = time.getText().toString();
                                                        mehanicCreateRabot letTask = new mehanicCreateRabot();
                                                        letTask.execute();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }else if (who.equals("manager")){
                            String testFix;
                            if(fix.isChecked()){
                                testFix="Да";
                            }else {
                                testFix="Нет";
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                            builder.setTitle("Окно предупреждения!")
                                    .setMessage(Html.fromHtml("Вы действительно уверены что хотите создать рабочую операцию на данный ремотный заказ? <b>"+num+"</b>" +
                                            "<br> Бригада: <b>"+brS+"</b>" +
                                            "<br> Работа: <b>"+name.getText().toString()+"</b>" +
                                            "<br> Категория ремонта: <b>"+categS+"</b>" +
                                            "<br> Стоимость: <b>"+value.getText().toString()+"</b>" +
                                            "<br> Норма времени: <b>"+time.getText().toString()+"</b>" +
                                            "<br> Фиксированная цена: <b>"+testFix+"</b>"))
                                    .setCancelable(false)
                                    .setNegativeButton("Отмена",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            })
                                    .setPositiveButton("Да",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();

                                                    // CreateRab();
                                                    createRabPLS();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }


                    }
                });






            }
        });

        if(nameI!=null){
            if(who.equals("mehanic")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(nameI);
                        TgetForma letTast = new TgetForma();
                        letTast.execute();
                    }
                });
            }else{
                notNull letTast = new notNull();
                letTast.execute();
            }
        }else{
            TgetBrigada letTast = new TgetBrigada();
            letTast.execute();
        }




    }

    public void createRabPLS(){
        CatTask catTask = new CatTask();
        catTask.execute();
    }

    class notNull extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(detail_sozdat_rab_oper.this);
            progressDialog.setMessage("Пожалуйста подождите...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TgetBrigada letTast = new TgetBrigada();
                    letTast.execute();
                }
            });


        }

        @Override
        protected Void doInBackground(Void... voids) {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                String pin = prefs.getString("pin", null);
                //-------------------------------------------------------------------------------------------------------------------------------------------
                getFormaWithName = new getFormaWithName();
                getFormaWithName.start(mob, pin,nameI);

                try {
                    getFormaWithName.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }

                String result=getFormaWithName.getResult();
                try {
                /*
                    "empty": "ok",
                    "res": [{
                        "text": "Адаптация фары и диагностика электронных систем",
                        "fix": "no",
                        "categ": "Электрические работы",
                        "value": "25 000",
                        "time": "0",
                        "group": ""
                    }]
                }

                */
                    JSONObject jsonObject = new JSONObject(result);
                    String empty = jsonObject.getString("empty");
                    if (empty.equals("no")){

                    }else {
                        JSONArray jsonArray = jsonObject.getJSONArray("res");
                        final String text = jsonArray.getJSONObject(0).getString("text");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name.setText(text);
                            }
                        });

                        final String fixS = jsonArray.getJSONObject(0).getString("fix");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(fixS.equals("yes")){
                                    fix.setChecked(true);
                                }else{
                                    fix.setChecked(false);
                                }
                            }
                        });

                        final String valueS = jsonArray.getJSONObject(0).getString("value");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                value.setText(valueS);
                            }
                        });

                        final String timeS = jsonArray.getJSONObject(0).getString("time");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                time.setText(timeS);
                            }
                        });
                        categSF = jsonArray.getJSONObject(0).getString("categ");
                        groupSF = jsonArray.getJSONObject(0).getString("group");



                        Log.e("<><><><><>  Result  ", result + " ==  ==== )))");

                        //FgetBrigada();



                    }
                } catch (Exception e) {
                    Log.e("Fail 4", e.toString());
                }


            return null;
        }
    }

    class CatTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(detail_sozdat_rab_oper.this);
            progressDialog.setMessage("Создание рабочей операции на рз...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final String[] nameUje = new String[1];
            final String[] valueUje = new String[1];
            final String[] timeUje = new String[1];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    nameUje[0] = name.getText().toString();
                    valueUje[0] = value.getText().toString();
                    timeUje[0] = time.getText().toString();
                }
            });


            if(brS.equals("")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                        builder.setTitle("Внимание!")
                                .setMessage("Выберите пожалуйста бригаду!")
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

            }else if (nameUje[0]==""){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                        builder.setTitle("Внимание!")
                                .setMessage("Заполните пожалуйста наименование работы!")
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
            }else if (categS.equals("")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                        builder.setTitle("Внимание!")
                                .setMessage("Выберите пожалуйста категорию работы!")
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
            }else if (valueUje[0] ==""){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                        builder.setTitle("Внимание!")
                                .setMessage("Заполните пожалуйста стоимость работы!")
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
            }else if (valueUje[0] =="0"){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                        builder.setTitle("Внимание!")
                                .setMessage("Стомость работы не должна быть ранва 0!")
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


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        String mob = prefs.getString("mob", null);
                        String pin = prefs.getString("pin", null);
                        //-------------------------------------------------------------------------------------------------------------------------------------------
                        String fixS;
                        if(fix.isChecked()){
                            fixS="yes";
                        }else{
                            fixS="no";
                        }

                        sozdat_rab = new Sozdat_rab();
                        sozdat_rab.start(mob, pin,name.getText().toString(),time.getText().toString(),value.getText().toString(),fixS,num,brS,categS);

                        try {
                            sozdat_rab.join();
                        } catch (InterruptedException ie) {
                            Log.e("pass 0", ie.getMessage());
                        }

                        String result=sozdat_rab.getResult();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            final String res = jsonObject.getString("res");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
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
                    }
                }).start();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
    }

    class TgetBrigada extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(detail_sozdat_rab_oper.this);
            progressDialog.setMessage("Поиск бригады...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String mob = prefs.getString("mob", null);
            String pin = prefs.getString("pin", null);
            //-------------------------------------------------------------------------------------------------------------------------------------------
            getBrigada = new getBrigada();
            getBrigada.start(mob, pin);

            try {
                getBrigada.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getBrigada.getResult();

            try {
            /*
            "empty": "ok",
                "res": [{
                    "brig": "B0404"

            */
                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){

                }else{
                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    spBrig = new String[jsonArray.length()+1];
                    String vsemi="";
                    spBrig[0] = vsemi;
                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        String brig = jsonArray.getJSONObject(i).getString("brig");
                        spBrig[i+1] = brig;
                        Log.e("brig ",brig);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(detail_sozdat_rab_oper.this, android.R.layout.simple_spinner_dropdown_item, spBrig);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            brigada.setAdapter(adapter);
                            brigada.setPrompt("Выберите бригаду");
                            // устанавливаем обработчик нажатия
                            brigada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    brS = brigada.getSelectedItem().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
                        }
                    });


                }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 4", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });



            if(nameI!=null){
                //FgetFormaWithName(categSF,groupSF);
                String[] myTaskParams = { categSF, groupSF };
                TgetFormaWithName letTast = new TgetFormaWithName();
                letTast.execute(myTaskParams);

            }else{
                TgetForma letTast = new TgetForma();
                letTast.execute();
            }

        }
    }

    class TgetForma extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(detail_sozdat_rab_oper.this);
            progressDialog.setMessage("Создания формы рабочей операции...");
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
            getForma = new getForma();
            getForma.start(mob, pin);

            try {
                getForma.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getForma.getResult();
            try {
                    /*
                        "empty": "ok",
                        "res": [{
                            "vipolnit": "Поменять"

                        "vipolnitdop": [{
                             "vipolnitdop": "С как нибудь"

                        "category": [{
                             "category": "Агрегатный ремонт"

                        "group": [{
                             "group": "Общий обзор автомобиля"

                    */
                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){
                }else{
                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    spVid1 = new String[jsonArray.length()+1];
                    String vsemi="";
                    spVid1[0] = vsemi;
                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        String vipolnit = jsonArray.getJSONObject(i).getString("vipolnit");
                        spVid1[i+1] = vipolnit;
                        Log.e("vipolnit ",vipolnit);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(detail_sozdat_rab_oper.this, android.R.layout.simple_spinner_dropdown_item, spVid1);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            vid1.setAdapter(adapter);
                            vid1.setPrompt("Выберите вид действия");
                            // устанавливаем обработчик нажатия
                            vid1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    vid1S = vid1.getSelectedItem().toString();
                                    vid2S = vid2.getSelectedItem().toString();

                                    if(name.getText().toString().equals("")){
                                        first=null;
                                    }else{
                                        first = name.getText().toString();
                                        Log.e("first",first);
                                        name.setText(first+". "+vid1S+" "+vid2S);
                                    }




                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
                        }
                    });










                    JSONArray jsonArrayvipolnitdop = jsonObject.getJSONArray("vipolnitdop");
                    spVid2 = new String[jsonArrayvipolnitdop.length()+1];
                    String vsemi2="";
                    spVid2[0] = vsemi2;
                    for (int i = 0; i < jsonArrayvipolnitdop.length(); i++) { // Цикл где в стринг result пришло от баз
                        String vipolnitdop = jsonArrayvipolnitdop.getJSONObject(i).getString("vipolnitdop");
                        spVid2[i+1] = vipolnitdop;
                        Log.e("vipolnitdop ",vipolnitdop);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(detail_sozdat_rab_oper.this, android.R.layout.simple_spinner_dropdown_item, spVid2);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            vid2.setAdapter(adapter2);
                            vid2.setPrompt("Выберите вид действия");
                            // устанавливаем обработчик нажатия
                            vid2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    //selectedSpinMarka = spinner2.getSelectedItem().toString();

                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    vid1S = vid1.getSelectedItem().toString();
                                    vid2S = vid2.getSelectedItem().toString();

                                    if(name.getText().toString().equals("")){
                                        first=null;
                                    }else{
                                        name.setText(first+". "+vid1S+" "+vid2S);
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
                        }
                    });










                    JSONArray jsonArraycategory = jsonObject.getJSONArray("category");
                    spCategory = new String[jsonArraycategory.length()+1];
                    String vsemi3="";
                    spCategory[0] = vsemi2;
                    for (int i = 0; i < jsonArraycategory.length(); i++) { // Цикл где в стринг result пришло от баз
                        String category = jsonArraycategory.getJSONObject(i).getString("category");
                        spCategory[i+1] = category;
                        Log.e("category ",category);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(detail_sozdat_rab_oper.this, android.R.layout.simple_spinner_dropdown_item, spCategory);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            category.setAdapter(adapter3);
                            category.setPrompt("Выберите категорию");
                            // устанавливаем обработчик нажатия
                            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    categS = category.getSelectedItem().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
                        }
                    });












                    JSONArray jsonArraygroup = jsonObject.getJSONArray("group");
                    spGroup = new String[jsonArraygroup.length()+1];
                    String vsemi4="";
                    spGroup[0] = vsemi4;
                    for (int i = 0; i < jsonArraygroup.length(); i++) { // Цикл где в стринг result пришло от баз
                        String group = jsonArraygroup.getJSONObject(i).getString("group");
                        spGroup[i+1] = group;
                        Log.e("group ",group);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(detail_sozdat_rab_oper.this, android.R.layout.simple_spinner_dropdown_item, spGroup);
                            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            group.setAdapter(adapter4);
                            group.setPrompt("Выберите группу");
                            // устанавливаем обработчик нажатия
                            group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    //selectedSpinMarka = spinner2.getSelectedItem().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
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

    class TgetFormaWithName extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(detail_sozdat_rab_oper.this);
            progressDialog.setMessage("Перенос формы для создания рабочей операции...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String Fget_categ, Fget_group;
            Fget_categ=strings[0];
            Fget_group=strings[1];

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String mob = prefs.getString("mob", null);
            String pin = prefs.getString("pin", null);
            //-------------------------------------------------------------------------------------------------------------------------------------------
            getForma = new getForma();
            getForma.start(mob, pin);

            try {
                getForma.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=getForma.getResult();
            try {
                    /*
                        "empty": "ok",
                        "res": [{
                            "vipolnit": "Поменять"

                        "vipolnitdop": [{
                             "vipolnitdop": "С как нибудь"

                        "category": [{
                             "category": "Агрегатный ремонт"

                        "group": [{
                             "group": "Общий обзор автомобиля"

                    */
                JSONObject jsonObject = new JSONObject(result);
                String empty = jsonObject.getString("empty");
                if (empty.equals("no")){
                }else{
                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    spVid1 = new String[jsonArray.length()+1];
                    String vsemi="";
                    spVid1[0] = vsemi;
                    for (int i = 0; i < jsonArray.length(); i++) { // Цикл где в стринг result пришло от баз
                        String vipolnit = jsonArray.getJSONObject(i).getString("vipolnit");
                        spVid1[i+1] = vipolnit;
                        Log.e("vipolnit ",vipolnit);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(detail_sozdat_rab_oper.this, android.R.layout.simple_spinner_dropdown_item, spVid1);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            vid1.setAdapter(adapter);
                            vid1.setPrompt("Выберите вид действия");
                            // устанавливаем обработчик нажатия
                            vid1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    vid1S = vid1.getSelectedItem().toString();
                                    vid2S = vid2.getSelectedItem().toString();

                                    if(name.getText().toString().equals("")){
                                        first=null;
                                    }else{
                                        first = name.getText().toString();
                                        Log.e("first",first);
                                        name.setText(first+". "+vid1S+" "+vid2S);
                                    }




                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
                        }
                    });










                    JSONArray jsonArrayvipolnitdop = jsonObject.getJSONArray("vipolnitdop");
                    spVid2 = new String[jsonArrayvipolnitdop.length()+1];
                    String vsemi2="";
                    spVid2[0] = vsemi2;
                    for (int i = 0; i < jsonArrayvipolnitdop.length(); i++) { // Цикл где в стринг result пришло от баз
                        String vipolnitdop = jsonArrayvipolnitdop.getJSONObject(i).getString("vipolnitdop");
                        spVid2[i+1] = vipolnitdop;
                        Log.e("vipolnitdop ",vipolnitdop);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(detail_sozdat_rab_oper.this, android.R.layout.simple_spinner_dropdown_item, spVid2);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            vid2.setAdapter(adapter2);
                            vid2.setPrompt("Выберите вид действия");
                            // устанавливаем обработчик нажатия
                            vid2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    //selectedSpinMarka = spinner2.getSelectedItem().toString();

                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    vid1S = vid1.getSelectedItem().toString();
                                    vid2S = vid2.getSelectedItem().toString();

                                    if(name.getText().toString().equals("")){
                                        first=null;
                                    }else{
                                        name.setText(first+". "+vid1S+" "+vid2S);
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
                        }
                    });









                    JSONArray jsonArraycategory = jsonObject.getJSONArray("category");
                    spCategory = new String[jsonArraycategory.length()+1];
                    String vsemi3="";
                    spCategory[0] = Fget_categ;
                    spCategory[1] = vsemi2;
                    for (int i = 0; i < jsonArraycategory.length(); i++) { // Цикл где в стринг result пришло от баз
                        String category = jsonArraycategory.getJSONObject(i).getString("category");
                        spCategory[i+1] = category;
                        Log.e("category ",category);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(detail_sozdat_rab_oper.this, android.R.layout.simple_spinner_dropdown_item, spCategory);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            category.setAdapter(adapter3);
                            category.setPrompt("Выберите категорию");
                            // устанавливаем обработчик нажатия
                            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    categS = category.getSelectedItem().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
                        }
                    });











                    JSONArray jsonArraygroup = jsonObject.getJSONArray("group");
                    spGroup = new String[jsonArraygroup.length()+1];
                    String vsemi4="";
                    spGroup[0] = Fget_group;
                    spGroup[1] = vsemi4;
                    for (int i = 0; i < jsonArraygroup.length(); i++) { // Цикл где в стринг result пришло от баз
                        String group = jsonArraygroup.getJSONObject(i).getString("group");
                        spGroup[i+1] = group;
                        Log.e("group ",group);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(detail_sozdat_rab_oper.this, android.R.layout.simple_spinner_dropdown_item, spGroup);
                            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            group.setAdapter(adapter4);
                            group.setPrompt("Выберите группу");
                            // устанавливаем обработчик нажатия
                            group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    //Spinner spinner2 = (Spinner) findViewById(R.id.sozdat_rab_spinnerManag);
                                    //selectedSpinMarka = spinner2.getSelectedItem().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
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


    public class getBrigada extends Thread {

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

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            //getBrigada.php?mob=+7%20777%20534%207677&pin=6020

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getBrigada.php");
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

    public class getForma extends Thread {

        /*
            "empty": "ok",
            "res": [{
                "vipolnit": "Поменять"

        "vipolnitdop": [{
                "vipolnitdop": "С как нибудь"

        "category": [{
                "category": "Агрегатный ремонт"

        "group": [{
                "group": "Общий обзор автомобиля"

        */

        String MobS;
        String PinS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            //getRZForma.php?mob=+7%20777%20534%207677&pin=6020

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getRZForma.php");
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

    public class Sozdat_rab extends Thread {

        /*
            "res": "ok",

        */

        String MobS;
        String PinS,NameS,TimeS,ValueS,FixS,NumS,BrS,CategS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(20);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("name", NameS));
            NameValuerPair.add(new BasicNameValuePair("time", TimeS));
            NameValuerPair.add(new BasicNameValuePair("value", ValueS));
            NameValuerPair.add(new BasicNameValuePair("fix", FixS));
            NameValuerPair.add(new BasicNameValuePair("num", NumS));
            NameValuerPair.add(new BasicNameValuePair("br", BrS));
            NameValuerPair.add(new BasicNameValuePair("categ", CategS));


            Log.e("mob", MobS);
            Log.e("pin", PinS);
            Log.e("name", NameS);
            Log.e("time", TimeS);
            Log.e("value", ValueS);
            Log.e("fix", FixS);
            Log.e("num", NumS);
            Log.e("br", BrS);
            Log.e("categ", CategS);

            /*
            *
            *     	$arr_params['mob']  	= $mob;
                    $arr_params['pin'] 		= $_POST['pin'];
                    $arr_params['name'] 	= $_POST['name'];
                    $arr_params['time'] 	= $_POST['time'];
                    $arr_params['value'] 	= $_POST['value'];
                    $arr_params['fix'] 		= $_POST['fix'];
                    $arr_params['num'] 	    = $_POST['num'];
                    $arr_params['br'] 		= $_POST['br'];
                    $arr_params['categ'] 	= $_POST['categ'];
            *
            *       getSozdatOper
            * */

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getSozdatOper.php");
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

        public void start(String mobp, String pinp, String namep, String timep, String valuep, String fixp, String nump, String brp, String categp) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.NameS = namep;
            this.TimeS = timep;
            this.ValueS = valuep;
            this.FixS = fixp;
            this.NumS = nump;
            this.BrS = brp;
            this.CategS = categp;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

    public class getFormaWithName extends Thread {

        String MobS;
        String PinS;
        String NameS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("text", NameS));
            //getRZForma.php?mob=+7%20777%20534%207677&pin=6020

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getInfoOper.php");
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

        public void start(String mobp, String pinp, String namep) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.NameS = namep;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

    private class mehanicCreateRabot extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(detail_sozdat_rab_oper.this);
            progressDialog.setMessage("Идёт вынос работы "+namePS);
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
            createRabotmehanic = new createRabotmehanic();
            createRabotmehanic.start(mob, pin, namePS, timePS,num,categPS);

            try {
                createRabotmehanic.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String result=createRabotmehanic.getResult();
            try {
                JSONObject jsonObject = new JSONObject(result);
                final String res = jsonObject.getString("res");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(detail_sozdat_rab_oper.this);
                        builder.setTitle(" ")
                                .setMessage(Html.fromHtml("<b>"+res+"</b>"))
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
                Log.e("Fail 6", e.toString());
            }
            return null;
        }
    }

    public class createRabotmehanic extends Thread {

        String MobS;
        String PinS,nameS,timeS,numS,categS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("name", nameS));
            NameValuerPair.add(new BasicNameValuePair("time", timeS));
            NameValuerPair.add(new BasicNameValuePair("num", num));
            NameValuerPair.add(new BasicNameValuePair("categ", categS));

            /*    	    	$arr_params['mob'] 	= $mob;
                            $arr_params['pin'] 		= $_POST['pin'];
                            $arr_params['name'] 	= $_POST['name'];
                            $arr_params['time'] 	= $_POST['time'];
                            $arr_params['num'] 	= $_POST['num'];
                            $arr_params['categ'] 	= $_POST['categ'];*/

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getSozdatRabOperMehanik.php");
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

        public void start(String mobp, String pinp, String name, String time, String num, String categ) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.nameS = name;
            this.timeS = time;
            this.numS = num;
            this.categS = categ;
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
