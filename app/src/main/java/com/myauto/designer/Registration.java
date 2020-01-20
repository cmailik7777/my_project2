package com.myauto.designer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Registration extends AppCompatActivity {

    EditText name,f,o,tel,dr;
    ProgressDialog progressDialog;
    public zapros Zapros1;

    SimpleDateFormat simpleDateFormat;
    Calendar mcalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        name = (EditText)findViewById(R.id.Reg_Name);
        f = (EditText)findViewById(R.id.Reg_F);
        o = (EditText)findViewById(R.id.Reg_O);
        tel = (EditText)findViewById(R.id.Reg_Tel);
        dr = (EditText)findViewById(R.id.Reg_Dr);

        tel.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s)
            {
                String text = tel.getText().toString();
                int  textLength = tel.getText().length();
                if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" "))
                    return;
                if (textLength == 1) {
                    if (!text.contains("+"))
                    {
                        tel.setText(new StringBuilder(text).insert(text.length() - 1, "+").toString());
                        tel.setSelection(tel.getText().length());
                    }
                }
                else if (textLength == 2)
                {
                    tel.setText(new StringBuilder(text).insert(text.length() - 1, "7").toString());
                    tel.setSelection(tel.getText().length());
                }
                else if (textLength == 3)
                {
                    tel.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    tel.setSelection(tel.getText().length());
                }
                else if (textLength == 7)
                {
                    tel.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    tel.setSelection(tel.getText().length());
                }
                else if (textLength == 11)
                {
                    tel.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    tel.setSelection(tel.getText().length());
                }
            }
        });


        dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleDateFormat = new SimpleDateFormat("dd.MM.YYYY", Locale.getDefault());
                mcalendar = Calendar.getInstance();

                int year = mcalendar.get(Calendar.YEAR);
                int month = mcalendar.get(Calendar.MONTH);
                int day = mcalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Registration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yearS, int monthS, int dayS) {


                        mcalendar.set(yearS,monthS,dayS);
                        dr.setText(simpleDateFormat.format(mcalendar.getTime()));

                    }
                }, year, month, day);
                mDatePicker.show();
            }
        });


    }

    public void Regis(View view){

        String nameS = name.getText().toString();
        String fS = f.getText().toString();
        String oS = o.getText().toString();
        String telS = tel.getText().toString();
        String drS = dr.getText().toString();


        if(TextUtils.isEmpty(nameS)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                    builder.setTitle("Окно предупреждения!")
                            .setIcon(R.drawable.iconlogo)
                            .setMessage("Необходимо заполнить \n- Имя*")
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
        }else if (TextUtils.isEmpty(fS)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                    builder.setTitle("Окно предупреждения!")
                            .setIcon(R.drawable.iconlogo)
                            .setMessage("Необходимо заполнить \n- Фамилию*")
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
        }else if (TextUtils.isEmpty(telS)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                    builder.setTitle("Окно предупреждения!")
                            .setIcon(R.drawable.iconlogo)
                            .setMessage("Необходимо заполнить \n- Телефон*")
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
        }else if (tel.length() !=15){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                    builder.setTitle("Окно предупреждения!")
                            .setIcon(R.drawable.iconlogo)
                            .setMessage("Телефон не должен содержать меньше или больше 12 символов! \n Пример: +7 705 000 0000")
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
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(Registration.this);
                    progressDialog.setMessage("Пожалуйста подождите....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {

                    String nameS = name.getText().toString();
                    String fS = f.getText().toString();
                    String oS = o.getText().toString();
                    String telS = tel.getText().toString();
                    String drS = dr.getText().toString();
                    Zapros1 = new zapros();
                    Zapros1.start(nameS, fS,oS,telS,drS);


                    try {
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                }
            }).start();
        }




    }

    public class zapros extends Thread {


        String nameS;
        String fS;
        String oS;
        String telS;
        String drS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run(){

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(6);
            //f=oleg&o=&i=&mob=+7%20111%20111%201111&dr=01.12.1988
            NameValuerPair.add(new BasicNameValuePair("i",nameS));
            Log.e("BasicNameValuePair Reg", "i "+nameS);
            NameValuerPair.add(new BasicNameValuePair("f",fS));
            Log.e("BasicNameValuePair Reg", "f "+fS);
            NameValuerPair.add(new BasicNameValuePair("o",oS));
            Log.e("BasicNameValuePair Reg", "o "+oS);
            NameValuerPair.add(new BasicNameValuePair("mob",telS));
            Log.e("BasicNameValuePair Reg", "mob "+telS);
            NameValuerPair.add(new BasicNameValuePair("dr",drS));
            Log.e("BasicNameValuePair Reg", "dr "+drS);
            String pin = "";
            NameValuerPair.add(new BasicNameValuePair("pin",pin));
            Log.e("BasicNameValuePair Reg", "pin "+pin);

            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/Registration.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection succes");
            } catch (Exception e){
                Log.e("Fail 1", e.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Ошибка подключения! Пожалуйста попробуйте позже.")
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

            try{

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "\n");
                }
                while ((line = reader.readLine()) !=null) {
                    sb.append(line + "[}][,][{]");
                }
                is.close();
                result = sb.toString();

                Log.e("pass 2", "connection succes" + result);
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Ошибка подключения! " +
                                        "Пожалуйста попробуйте позже.")
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
            try {
                JSONObject jsonObject = new JSONObject(result);

                /*
                    {"res":"РќРµС‚ С‚Р°РєРѕРіРѕ РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ!"}
                */

                final String res = jsonObject.getString("res");

                if (res.equals("ok")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                            builder.setTitle("Окно предупреждения!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Регистрация прошла успешно!")
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    info();
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
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                            builder.setTitle("Окно предупреждения!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage(res)
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
            } catch (Exception e){
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Ошибка подключения! Пожалуйста попробуйте позже.")
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




        }


        public void start(String mobp, String pinp, String oS, String telS, String drS){
            this.nameS = mobp;
            this.fS = pinp;
            this.oS = oS;
            this.telS = telS;
            this.drS = drS;
            this.start();


        }
        public String resID (){
            return result;
        }

    }

    public void info (){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                builder.setTitle("MyAuto")
                        .setIcon(R.drawable.iconlogo)
                        .setMessage("В течении 5-10 минут ожидайте SMS на ваш номер телефона с пин кодом. Далее с указанным пин кодом авторизуйтесь в личном кабинете.")
                        .setCancelable(false)
                        .setNegativeButton("ок",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----Registration-----", "onPause");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Registration-----", "onResume");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Registration-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Registration-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----Registration-----", "onStart");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Registration-----", "onRestart");
    }

}
