package com.myauto.designer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static com.myauto.designer.Login.MY_PREFS_NAME;
import static com.myauto.designer.R.id.spinnerListRemZKlient;

public class ListRemZ extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private View mLayout;

    String[] data2 = {"z1", "я2", "я3"};
    String[] data3 = {"клиент2", "клиента2", "клиент4"};

    String SotrS;
    String MobS;
    String PinS,Org,OrgS;
    String[] spOrg;
    String[] spManag;
    String[] spClient;
    String Manag;
    String open;
    String closed;
    String dolg;

    SimpleDateFormat simpleDateFormat;
    Calendar mcalendar;

    SimpleDateFormat simpleDateFormat2;
    Calendar mcalendar2;

    EditText ListRemZ,ListRemZDO;

    String checkprog;
    ProgressDialog progressDialog;

    String selectedSpinOrg,selectedSpinManag,selectedSpinClient;

    private Thread t1 = null;
    private Thread t2 = null;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private zapros2 Zapros2;
    private zapros2Corps Zapros2Corps;
    private zapros3 Zapros3;
    private zapros4 Zapros4;
    Spinner spinnerManager,spinnerOrg,spinnerKlient;

    EditText ListRemZGos;

    String GOS=null;
    String IPSTRING;
    ImageView Search_Logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rem_z);

        mLayout = (View)findViewById(R.id.ListRemZLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle("Поиск рем. заказа");
        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);

        SharedPreferences prefs2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String orgLogo = prefs2.getString("orgLogo", null);

        Search_Logo = (ImageView)findViewById(R.id.Search_remz_logo);
        //------------------------------------------------------------------------------------------
        byte[] decodedString = Base64.decode(orgLogo, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        Search_Logo.setImageBitmap(decodedByte);
        //------------------------------------------------------------------------------------------

        ListRemZGos = (EditText) findViewById(R.id.ListRemZGos);
        ListRemZ=(EditText)findViewById(R.id.ListRemZS);
        ListRemZDO=(EditText)findViewById(R.id.ListRemZDo);

        spinnerManager = (Spinner) findViewById(R.id.spinnerListRemZManager);
        spinnerKlient = (Spinner) findViewById(spinnerListRemZKlient);


        Intent intent = getIntent();
        GOS = intent.getStringExtra("GOSCAMREMZ");





        if(GOS==null || GOS=="") {
            //------------------------------------------------------------------------------------------
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Пожалуйста подождите....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //------------------------------------------------------------------------------------------
            Log.e("GOS","null");


            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String org = prefs.getString("org", null);
                    String code = prefs.getString("code", null);
                    //-------------------------------------------------------------------------------------------------------------------------------------------
                    Zapros2 = new zapros2();
                    Zapros2.start(org);
                    try {
                        Zapros2.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    Zapros2Corps = new zapros2Corps();
                    Zapros2Corps.start(code);
                    try {
                        Zapros2Corps.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            Log.e("--Нажал нет?---", " добавил "+ Arrays.toString(spManag)+" массив ");
                            // адаптер
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(com.myauto.designer.ListRemZ.this, android.R.layout.simple_spinner_item, spManag);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinnerManager.setAdapter(adapter2);
                            // заголовок
                            spinnerManager.setPrompt("Title");
                            // устанавливаем обработчик нажатия
                            spinnerManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {

                                    selectedSpinManag = spinnerManager.getSelectedItem().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });

                            Log.e("--Нажал нет?---", " добавил "+ Arrays.toString(spClient)+" массив ");
                            // адаптер
                            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(com.myauto.designer.ListRemZ.this, android.R.layout.simple_spinner_item, spClient);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinnerKlient.setAdapter(adapter3);
                            // заголовок
                            spinnerKlient.setPrompt("Title");
                            // устанавливаем обработчик нажатия
                            spinnerKlient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {

                                    selectedSpinClient = spinnerKlient.getSelectedItem().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
                            spinnerKlient.setPrompt("Выберите корп. клиента");
                            spinnerManager.setPrompt("Выберите менеджера");
                            progressDialog.dismiss();
                        }
                    });
                }
            }).start();

        }
        else {

            //------------------------------------------------------------------------------------------
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Пожалуйста подождите....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //------------------------------------------------------------------------------------------

            if(GOS.equals("BN")){
                ListRemZGos.setText("Не определён");
            }

            ListRemZGos.setText(GOS);

            new Thread(new Runnable() {
                public void run() {

                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String spinnerListRemZOrg = prefs.getString("org", null);
                    EditText ListRemZS = (EditText) findViewById(R.id.ListRemZS);
                    EditText ListRemZDo = (EditText) findViewById(R.id.ListRemZDo);
                    EditText ListRemZNomerD = (EditText) findViewById(R.id.ListRemZNomerD);

                    CheckBox checkBoxOpen = (CheckBox) findViewById(R.id.checkBoxOpen);
                    CheckBox checkBoxClosed = (CheckBox) findViewById(R.id.checkBoxClosed);
                    CheckBox checkBoxDolg = (CheckBox) findViewById(R.id.checkBoxDolg);

                    if(checkBoxOpen.isChecked()){
                        open="open";
                    }else{
                        open="";
                    }
                    if(checkBoxClosed.isChecked()){
                        closed="closed";
                    }else {
                        closed="";
                    }
                    if(checkBoxDolg.isChecked()){
                        dolg="dolg";
                    }else {
                        dolg="";
                    }

                    String spinnerListRemZManager = "";

                    EditText ListRemZVin = (EditText) findViewById(R.id.ListRemZVin);
                    ListRemZGos = (EditText) findViewById(R.id.ListRemZGos);

                    String spinnerListRemZKlient = "";

                    EditText ListRemZKlient = (EditText) findViewById(R.id.ListRemZKlient);
                    EditText ListRemZTel = (EditText) findViewById(R.id.ListRemZTel);
                    EditText ListRemZComent = (EditText) findViewById(R.id.ListRemZComent);
                    //-------------------------------------------------------------------------------------------------------------------------------------------

                    //spinnerListRemZOrg
                    Log.e("Zapros3.START==","spinnerListRemZOrg= "+spinnerListRemZOrg);
                    String ListRemZSS = ListRemZS.getText().toString();
                    Log.e("Zapros3.START==","ListRemZS= "+ListRemZSS);
                    String ListRemZDoS = ListRemZDo.getText().toString();
                    Log.e("Zapros3.START==","ListRemZDoS= "+ListRemZDoS);
                    String ListRemZNomerDS = ListRemZNomerD.getText().toString();
                    Log.e("Zapros3.START==","ListRemZNomerDS= "+ListRemZNomerDS);
                    //open
                    Log.e("Zapros3.START==","open= "+open);
                    //closed
                    Log.e("Zapros3.START==","closed= "+closed);
                    //dolg
                    Log.e("Zapros3.START==","dolg= "+dolg);
                    //spinnerListRemZManager
                    Log.e("Zapros3.START==","spinnerListRemZManager= "+spinnerListRemZManager);
                    String ListRemZVinS = ListRemZVin.getText().toString();
                    Log.e("Zapros3.START==","ListRemZVinS= "+ListRemZVinS);
                    String ListRemZGosS = ListRemZGos.getText().toString();
                    Log.e("Zapros3.START==","ListRemZGosS= "+ListRemZGosS);
                    //spinnerListRemZKlient
                    Log.e("Zapros3.START==","spinnerListRemZKlient= "+spinnerListRemZKlient);
                    String ListRemZKlientS = ListRemZKlient.getText().toString();
                    Log.e("Zapros3.START==","ListRemZKlientS= "+ListRemZKlientS);
                    String ListRemZTelS = ListRemZTel.getText().toString();
                    Log.e("Zapros3.START==","ListRemZTelS= "+ListRemZTelS);
                    String ListRemZComentS = ListRemZComent.getText().toString();
                    Log.e("Zapros3.START==","ListRemZComentS= "+ListRemZComentS);

                    Zapros3 = new zapros3();
                    Zapros3.start(spinnerListRemZOrg, ListRemZSS,ListRemZDoS, ListRemZNomerDS,open,closed,dolg,spinnerListRemZManager,ListRemZVinS,ListRemZGosS,spinnerListRemZKlient,ListRemZKlientS,ListRemZTelS,ListRemZComentS);

                    try {
                        Zapros3.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }
                    String resResult = Zapros3.resResult();

                    try {
                        JSONObject jsonObject = new JSONObject(resResult);
                        JSONArray jsonArray = jsonObject.getJSONArray("rz");

                        if(jsonArray.length() > 0 && jsonArray!=null){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            });
                            Intent intentV = new Intent(com.myauto.designer.ListRemZ.this, FinalListRemZ.class);
                            intentV.putExtra("result", resResult);
                            startActivity(intentV);

                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(com.myauto.designer.ListRemZ.this);
                                    builder.setTitle("Действие отменено!")
                                            .setIcon(R.drawable.iconlogo)
                                            .setMessage("Данные не найдены!")
                                            .setCancelable(false)
                                            .setNegativeButton("ок",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                    //--------------------------------------------------------------------------------------------------------------------------------------------
                    Intent intentV = new Intent(com.myauto.designer.ListRemZ.this, FinalListRemZ.class);
                    intentV.putExtra("result", resResult);
                    startActivity(intentV);

                }
            }).start();
        }


        ListRemZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY", Locale.getDefault());
                mcalendar = Calendar.getInstance();

                int year = mcalendar.get(Calendar.YEAR);
                int month = mcalendar.get(Calendar.MONTH);
                int day = mcalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(ListRemZ.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yearS, int monthS, int dayS) {


                        mcalendar.set(yearS,monthS,dayS);
                        ListRemZ.setText(simpleDateFormat.format(mcalendar.getTime()));

                    }
                }, year, month, day);
                mDatePicker.show();

            }
        });


        ListRemZDO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY", Locale.getDefault());
                    mcalendar = Calendar.getInstance();

                    int year = mcalendar.get(Calendar.YEAR);
                    int month = mcalendar.get(Calendar.MONTH);
                    int day = mcalendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker = new DatePickerDialog(ListRemZ.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int yearS, int monthS, int dayS) {


                            mcalendar.set(yearS,monthS,dayS);
                            ListRemZDO.setText(simpleDateFormat.format(mcalendar.getTime()));

                        }
                    }, year, month, day);
                    mDatePicker.show();
                }catch (final Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(com.myauto.designer.ListRemZ.this);
                            builder.setTitle("Код ошибки")
                                    .setMessage(""+e)
                                    .setCancelable(false)
                                    .setNegativeButton("Отмена",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
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
    public void clear1(View view){
        ListRemZ.setText("");
    }
    public void clear2(View view){
        ListRemZDO.setText("");
    }

    public void CalendarS(View view){
        simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY", Locale.getDefault());
        mcalendar = Calendar.getInstance();
        new DatePickerDialog(this, mDateDataset, mcalendar.get(Calendar.YEAR),mcalendar.get(Calendar.MONTH),mcalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void CalendarDo(View view){
        simpleDateFormat2 = new SimpleDateFormat("dd.MM.YYYY", Locale.getDefault());
        mcalendar2 = Calendar.getInstance();
        new DatePickerDialog(this, mDateDataset2, mcalendar2.get(Calendar.YEAR),mcalendar2.get(Calendar.MONTH),mcalendar2.get(Calendar.DAY_OF_MONTH)).show();
    }

    private final DatePickerDialog.OnDateSetListener mDateDataset2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year2, int month2, int day_of_month2) {
            mcalendar2.set(Calendar.YEAR, year2);
            mcalendar2.set(Calendar.MONTH, month2);
            mcalendar2.set(Calendar.DAY_OF_MONTH, day_of_month2);
            ListRemZDO.setText(simpleDateFormat2.format(mcalendar2.getTime()));
        }
    };

    private final DatePickerDialog.OnDateSetListener mDateDataset = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
            mcalendar.set(Calendar.YEAR, year);
            mcalendar.set(Calendar.MONTH, month);
            mcalendar.set(Calendar.DAY_OF_MONTH, day_of_month);
            ListRemZ.setText(simpleDateFormat.format(mcalendar.getTime()));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void tess(View view){
        //--------------------------------------------------------------------------------------------------------------------------------------------
    }

    public void Search(View view){
        progressDialog = new ProgressDialog(com.myauto.designer.ListRemZ.this);
        progressDialog.setMessage("Поиск ремотного заказа...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        checkprog="1";
        new Thread(new Runnable() {
            public void run() {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String spinnerListRemZOrg = prefs.getString("org", null);


                EditText ListRemZS = (EditText) findViewById(R.id.ListRemZS);
                EditText ListRemZDo = (EditText) findViewById(R.id.ListRemZDo);
                EditText ListRemZNomerD = (EditText) findViewById(R.id.ListRemZNomerD);

                CheckBox checkBoxOpen = (CheckBox) findViewById(R.id.checkBoxOpen);
                CheckBox checkBoxClosed = (CheckBox) findViewById(R.id.checkBoxClosed);
                CheckBox checkBoxDolg = (CheckBox) findViewById(R.id.checkBoxDolg);

                if(checkBoxOpen.isChecked()){
                    open="open";
                }else{
                    open="";
                }
                if(checkBoxClosed.isChecked()){
                    closed="closed";
                }else {
                    closed="";
                }
                if(checkBoxDolg.isChecked()){
                    dolg="dolg";
                }else {
                    dolg="";
                }

                String spinnerListRemZManager = spinnerManager.getSelectedItem().toString();

                EditText ListRemZVin = (EditText) findViewById(R.id.ListRemZVin);
                EditText ListRemZGos = (EditText) findViewById(R.id.ListRemZGos);

                String spinnerListRemZKlient = spinnerKlient.getSelectedItem().toString();

                EditText ListRemZKlient = (EditText) findViewById(R.id.ListRemZKlient);
                EditText ListRemZTel = (EditText) findViewById(R.id.ListRemZTel);
                EditText ListRemZComent = (EditText) findViewById(R.id.ListRemZComent);


                                                                                                       /* SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                                                                                        String login = prefs.getString("Login", null);
                                                                                                        String password = prefs.getString("Password", null);
                                                                                                        String name = prefs.getString("Name", null);
                                                                                                        String id = prefs.getString("Id", null);
                                                                                                        String sotr = prefs.getString("Sotr", null);
                                                                                                        String sotrname = prefs.getString("SotrName", null);*/
                //-------------------------------------------------------------------------------------------------------------------------------------------


                //spinnerListRemZOrg
                Log.e("Zapros3.START==","spinnerListRemZOrg= "+spinnerListRemZOrg);
                String ListRemZSS = ListRemZS.getText().toString();
                Log.e("Zapros3.START==","ListRemZS= "+ListRemZSS);
                String ListRemZDoS = ListRemZDo.getText().toString();
                Log.e("Zapros3.START==","ListRemZDoS= "+ListRemZDoS);
                String ListRemZNomerDS = ListRemZNomerD.getText().toString();
                Log.e("Zapros3.START==","ListRemZNomerDS= "+ListRemZNomerDS);
                //open
                Log.e("Zapros3.START==","open= "+open);
                //closed
                Log.e("Zapros3.START==","closed= "+closed);
                //dolg
                Log.e("Zapros3.START==","dolg= "+dolg);
                //spinnerListRemZManager
                Log.e("Zapros3.START==","spinnerListRemZManager= "+spinnerListRemZManager);
                String ListRemZVinS = ListRemZVin.getText().toString();
                Log.e("Zapros3.START==","ListRemZVinS= "+ListRemZVinS);
                String ListRemZGosS = ListRemZGos.getText().toString();
                Log.e("Zapros3.START==","ListRemZGosS= "+ListRemZGosS);
                //spinnerListRemZKlient
                Log.e("Zapros3.START==","spinnerListRemZKlient= "+spinnerListRemZKlient);
                String ListRemZKlientS = ListRemZKlient.getText().toString();
                Log.e("Zapros3.START==","ListRemZKlientS= "+ListRemZKlientS);
                String ListRemZTelS = ListRemZTel.getText().toString();
                Log.e("Zapros3.START==","ListRemZTelS= "+ListRemZTelS);
                String ListRemZComentS = ListRemZComent.getText().toString();
                Log.e("Zapros3.START==","ListRemZComentS= "+ListRemZComentS);

                Zapros3 = new zapros3();
                Zapros3.start(spinnerListRemZOrg, ListRemZSS,ListRemZDoS, ListRemZNomerDS,open,closed,dolg,spinnerListRemZManager,ListRemZVinS,ListRemZGosS,spinnerListRemZKlient,ListRemZKlientS,ListRemZTelS,ListRemZComentS);

                try {
                    Zapros3.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }


                String resResult = Zapros3.resResult();
                try {
                    JSONObject jsonObject = new JSONObject(resResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("rz");

                    if(jsonArray.length() > 0 && jsonArray!=null){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                        Intent intentV = new Intent(com.myauto.designer.ListRemZ.this, FinalListRemZ.class);
                        intentV.putExtra("result", resResult);
                        startActivity(intentV);

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(com.myauto.designer.ListRemZ.this);
                                builder.setTitle("Действие отменено!")
                                        .setIcon(R.drawable.iconlogo)
                                        .setMessage("Данные не найдены!")
                                        .setCancelable(false)
                                        .setNegativeButton("ок",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

                //--------------------------------------------------------------------------------------------------------------------------------------------



            }
        }).start();

    }




    public class zapros2 extends Thread {

        String OrgS;
        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(1);

            NameValuerPair.add(new BasicNameValuePair("org", OrgS));
            Log.e("===zapros2===RUN","OrgS= "+OrgS);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getManagersService.php");
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
                //{"manager_service":[{"name":"РђРєСЃРµРЅРѕРІ Р®СЂРёР№"}
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("manager_service");

                spManag = new String[jsonArray.length()+1];
                int j = 0;
                spManag[j] = "";
                for(int i = 0; i < jsonArray.length(); i++){
                    String name = jsonArray.getJSONObject(i).getString("name");
                    Log.e("name ",name);
                    j=j+1;
                    spManag[j] = name;
                }

            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }

        public void start(String orgp) {
            this.OrgS = orgp;
            this.start();
            Log.e("===ListRemZ===Start","OrgS= "+OrgS);
        }

    }

    public class zapros2Corps extends Thread {

        String codeS;
        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(1);

            NameValuerPair.add(new BasicNameValuePair("sotr_code", codeS));
            Log.e("===zapros2Corps===RUN","sotr_code= "+codeS);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/getOrgManager.php");
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
                //{"manager_service":[{"name":"РђРєСЃРµРЅРѕРІ Р®СЂРёР№"}
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("corps");

                spClient = new String[jsonArray.length()];
                for(int i = 0; i < jsonArray.length(); i++){
                    String name = jsonArray.getJSONObject(i).getString("name");
                    Log.e("name ",name);
                    spClient[i] = name;
                }

            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }

        public void start(String orgp) {
            this.codeS = orgp;
            this.start();
        }

    }

    public class zapros3 extends Thread {

        String SspinnerListRemZOrg;
        String SlistRemZS;
        String SListRemZDoS;
        String SListRemZNomerDS;
        String Sopen;
        String Sclosed;
        String Sdolg;
        String SspinnerListRemZManager;
        String SlistRemZVinS;
        String SlistRemZGosS;
        String SspinnerListRemZKlient;
        String SlistRemZKlientS;
        String SlistRemZTelS;
        String SlistRemZComentS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(14);

            NameValuerPair.add(new BasicNameValuePair("org", SspinnerListRemZOrg));
            NameValuerPair.add(new BasicNameValuePair("dateFrom", SlistRemZS));
            NameValuerPair.add(new BasicNameValuePair("dateDo", SListRemZDoS));
            NameValuerPair.add(new BasicNameValuePair("numRZ", SListRemZNomerDS));
            NameValuerPair.add(new BasicNameValuePair("open", Sopen));
            NameValuerPair.add(new BasicNameValuePair("closed", Sclosed));
            NameValuerPair.add(new BasicNameValuePair("dolg", Sdolg));
            NameValuerPair.add(new BasicNameValuePair("manager", SspinnerListRemZManager));
            NameValuerPair.add(new BasicNameValuePair("vin", SlistRemZVinS));
            NameValuerPair.add(new BasicNameValuePair("gos", SlistRemZGosS));
            NameValuerPair.add(new BasicNameValuePair("cl_corp", SspinnerListRemZKlient));
            NameValuerPair.add(new BasicNameValuePair("client", SlistRemZKlientS));
            NameValuerPair.add(new BasicNameValuePair("tel", SlistRemZTelS));
            NameValuerPair.add(new BasicNameValuePair("comment", SlistRemZComentS));

            //	$arr_params['org'] 			= $_GET['org'];
            //$arr_params['dateFrom'] 		= $_GET['dateFrom'];
            //$arr_params['dateDo'] 		= $_GET['dateDo'];
            //$arr_params['numRZ'] 		= $_GET['numRZ'];
            //$arr_params['open'] 		= $_GET['open'];
            //$arr_params['closed'] 		= $_GET['closed'];
            //$arr_params['dolg'] 		= $_GET['dolg'];
            // $arr_params['manager'] 		= $_GET['manager'];
            // $arr_params['vin'] 			= $_GET['vin'];
            // $arr_params['gos'] 			= $_GET['gos'];
            // $arr_params['cl_corp'] 		= $_GET['cl_corp'];
            // $arr_params['tel'] 			= $_GET['tel'];
            // $arr_params['comment'] 		= $_GET['comment'];
            //  $arr_params['client']		= $_GET['client'];


            Log.e("Zapros3.run==","spinnerListRemZOrg= "+SspinnerListRemZOrg);
            Log.e("Zapros3.run==","listRemZS= "+SlistRemZS);
            Log.e("Zapros3.run==","ListRemZDoS= "+SListRemZDoS);
            Log.e("Zapros3.run==","ListRemZNomerDS= "+SListRemZNomerDS);
            Log.e("Zapros3.run==","Sopen= "+Sopen);
            Log.e("Zapros3.run==","Sclosed= "+Sclosed);
            Log.e("Zapros3.run==","Sdolg= "+Sdolg);
            Log.e("Zapros3.run==","spinnerListRemZManager= "+SspinnerListRemZManager);
            Log.e("Zapros3.run==","listRemZVinS= "+SlistRemZVinS);
            Log.e("Zapros3.run==","listRemZGosS= "+SlistRemZGosS);
            Log.e("Zapros3.run==","spinnerListRemZKlient= "+SspinnerListRemZKlient);
            Log.e("Zapros3.run==","listRemZKlientS= "+SlistRemZKlientS);
            Log.e("Zapros3.run==","listRemZTelS= "+SlistRemZTelS);
            Log.e("Zapros3.run==","listRemZComentS= "+SlistRemZComentS);


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/searchRZ.php");
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
                Log.e("<><><><><>  Result  ",result+" ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }

        public void start(String spinnerListRemZOrg, String ListRemZSS, String ListRemZDoS, String ListRemZNomerDS, String open, String closed, String dolg, String spinnerListRemZManager, String listRemZVinS, String listRemZGosS, String spinnerListRemZKlient, String listRemZKlientS, String listRemZTelS, String listRemZComentS) {
            this.SspinnerListRemZOrg = spinnerListRemZOrg;
            this.SlistRemZS = ListRemZSS;
            this.SListRemZDoS= ListRemZDoS;
            this.SListRemZNomerDS= ListRemZNomerDS;
            this.Sopen = open;
            this.Sclosed = closed;
            this.Sdolg = dolg;
            this.SspinnerListRemZManager = spinnerListRemZManager;
            this.SlistRemZVinS = listRemZVinS;
            this.SlistRemZGosS = listRemZGosS;
            this.SspinnerListRemZKlient = spinnerListRemZKlient;
            this.SlistRemZKlientS = listRemZKlientS;
            this.SlistRemZTelS = listRemZTelS;
            this.SlistRemZComentS = listRemZComentS;
            this.start();
        }

        public String resResult (){
            return result;
        }
    }

    public class zapros4 extends Thread {

        String SlistRemZGosS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(1);

            NameValuerPair.add(new BasicNameValuePair("listRemZGosS", SlistRemZGosS));

            Log.e("Zapros3.run==","listRemZGosS= "+SlistRemZGosS);


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/androidFormRemzOrgSeach.php");
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
                Log.e("<><><><><>  Result  ",result+" ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }

        public void start(String listRemZGosS) {
            this.SlistRemZGosS = listRemZGosS;
            this.start();
        }

        public String resResult (){
            return result;
        }
    }




    public void camRemZ(View view){
        showCameraPreview();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "Использования камеры теперь доступно",
                        Snackbar.LENGTH_SHORT)
                        .show();
                startCamera();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Разрешение камеры отклонено",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void showCameraPreview() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intentV = new Intent(com.myauto.designer.ListRemZ.this, ShowCameraSearchRemZ.class);
            startActivity(intentV);
            finish();
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
        // END_INCLUDE(startCamera)
    }

    /**
     * Requests the {@link android.Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CAMERA)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(com.myauto.designer.ListRemZ.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для поиска ремотного заказа с помощью фотографии государственного номера, " +
                            "вам необходимо разрешить использовании камеры в приложении My Auto. \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(com.myauto.designer.ListRemZ.this,
                                            new String[]{android.Manifest.permission.CAMERA},
                                            PERMISSION_REQUEST_CAMERA);
                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(com.myauto.designer.ListRemZ.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для поиска ремотного заказа с помощью фотографии государственного номера, " +
                            "вам необходимо разрешить использовании камеры в приложении My Auto. \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(com.myauto.designer.ListRemZ.this,
                                            new String[]{android.Manifest.permission.CAMERA},
                                            PERMISSION_REQUEST_CAMERA);
                                }
                            })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void startCamera() {
        Intent intentV = new Intent(com.myauto.designer.ListRemZ.this, ShowCameraSearchRemZ.class);
        startActivity(intentV);
        finish();
    }







    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----ListRemZ-----", "onPause");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----ListRemZ-----", "onResume");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.e("-----ListRemZ-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----ListRemZ-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----ListRemZ-----", "onStart");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----ListRemZ-----", "onRestart");
    }



}
