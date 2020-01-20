package com.myauto.designer;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class OtkitRemz extends AppCompatActivity {

    String[] data2 = {"Частное лицо", "Юридическое лицо", "Корпоративный клиент"};

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private View mLayout;

    String MobS;
    String PinS;
    String selectedSpinLico;
    String selectedSpinMarka;
    String SelectedSpinLico;
    String SelectedSpinMarka;

    String remZVincode;
    String remZNomerDvig;
    String remZGos;
    String remZPokOd;
    String remZKlient;
    String remZTel;
    String[] spMarka;

    String Otvet,SotrS,Org;


    String[] spOrg;

    String checkprog,selectedSpinOrg;
    ProgressDialog progressDialog;

    private zapros Zapros1;
    private Marka marka;
    public zapros4 Zapros4;

    public Invite invite;

    CheckBox checkBox;
    ImageView otkritLogo;

    String IPSTRING;


    Spinner spinnerOrgOtkrit;
    String finalListRemzMarka, GosCam;
    EditText RemZVincode,RemZNomerDvig,RemZGos,RemZPokOd,RemZKlient,RemZTel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otkit_remz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mLayout = findViewById(R.id.main_layout);
        checkBox = (CheckBox) findViewById(R.id.checkBoxOtkrit);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle("Открыть рем. заказ");

        SharedPreferences prefsIP = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        IPSTRING = prefsIP.getString("ip", null);


        otkritLogo = (ImageView)findViewById(R.id.Otkrit_remz);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String orgLogo = prefs.getString("orgLogo", null);

        byte[] decodedString = Base64.decode(orgLogo, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        otkritLogo.setImageBitmap(decodedByte);

        //selectedSpinMarka
         RemZVincode = (EditText) findViewById(R.id.RemZVincode);
         RemZNomerDvig = (EditText) findViewById(R.id.RemZNomerDvig);
         RemZGos = (EditText) findViewById(R.id.RemZGos);
         RemZGos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                        builder.setTitle("Поиск по номеру")
                                .setMessage("Начать поиск по государственному номеру \n "+RemZGos.getText().toString()+"?")
                                .setCancelable(false)
                                .setPositiveButton("Да",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                searchGos();
                                            }
                                        })
                                .setNegativeButton("Отменить",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();


                        handled = true;
                    }
                    return handled;
                }
            });
         RemZPokOd = (EditText) findViewById(R.id.RemZPokOd);
        //selectedSpinLico
         RemZKlient = (EditText) findViewById(R.id.RemZKlient);
         RemZTel = (EditText) findViewById(R.id.RemZTel);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Пожалуйста подождите....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String code = prefs.getString("code", null);
                String otvet = "";
                //-------------------------------------------------------------------------------------------------------------------------------------------
                marka = new Marka();
                marka.start(code,otvet);

                try {
                    marka.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
                //--------------------------------------------------------------------------------------------------------------------------------------------

            }
        }).start();


        // адаптер
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter2);


        spinner2.setPrompt("Выберите тип клиента");
        // устанавливаем обработчик нажатия
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
                selectedSpinLico = spinner2.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        String GOS=null;
        Intent intent = getIntent();
        String finalListRemzRemz = intent.getStringExtra("finalListRemzRemz");

        GOS = intent.getStringExtra("GOSCAM");


        if(GOS!=null){
            if(GOS.equals("BN") && GOS.equals("")){
                RemZGos.setText("Не определён");
            }

            //-------------------------------------------------------------------------------------------------------------------------------------------
            GosCam = GOS;
            Zapros4 = new zapros4();
            Zapros4.start(GosCam);
            Log.e("Zapros44444====Try ","GosCam= "+GosCam);

            try {
                Zapros4.join();
            } catch (InterruptedException ie) {
                Log.e("pass 0", ie.getMessage());
            }

            String ResResult = Zapros4.getResult();
            try {
                JSONArray json_data = new JSONArray(ResResult);
                final String Otvet1 = json_data.getJSONArray(0).get(0).toString(); // vin code
                if (Otvet1.equals("empty")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                            builder.setTitle("Окно предупреждения!")
                                    .setMessage("Данные по государственному номер "+GosCam+" Ничего не найдено!")
                                    .setCancelable(false)
                                    .setPositiveButton("ок",
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
                    final String Otvet2 = json_data.getJSONArray(0).get(1).toString(); // HY номер двигателя
                    final String Otvet3 = json_data.getJSONArray(0).get(2).toString(); // BMW
                    final String Otvet4 = json_data.getJSONArray(0).get(3).toString(); // ИМЯ
                    final String Otvet5 = json_data.getJSONArray(0).get(4).toString(); // ТЕЛЕФОН

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RemZVincode.setText(Otvet1);
                            RemZNomerDvig.setText(Otvet2);
                            RemZGos.setText(GosCam);
                            RemZKlient.setText(Otvet4);
                            RemZTel.setText(Otvet5);
                            finalListRemzMarka = Otvet3;
                        }
                    });


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            String code = prefs.getString("code", null);

                            //-------------------------------------------------------------------------------------------------------------------------------------------
                            marka = new Marka();
                            marka.start(code,Otvet3);

                            try {
                                marka.join();
                            } catch (InterruptedException ie) {
                                Log.e("pass 0", ie.getMessage());
                            }
                            //--------------------------------------------------------------------------------------------------------------------------------------------

                        }
                    }).start();
                }

            } catch (Exception e) {
                Log.e("Fail", e.toString());
            }
            RemZGos.setText(GOS);
            //--------------------------------------------------------------------------------------------------------------------------------------------

        }

        if(finalListRemzRemz==null){
            Log.e("finalListRemzRemz","null");
        }else {
            finalListRemzMarka = intent.getStringExtra("finalListMarkaAuto");
            Log.e("finalListRemzMarka","OtkritRemz "+finalListRemzMarka);
            String finalListRemzVin = intent.getStringExtra("finalListRemzVin");
            String finalListRemzNomerDvig = intent.getStringExtra("finalListRemzNomerDvig");
            String finalListRemzGos = intent.getStringExtra("finalListRemzGos");
            String finalListRemzOdometr = intent.getStringExtra("finalListRemzOdometr");
            String finalListRemzClient = intent.getStringExtra("finalListRemzClient");
            String finalListRemzTel = intent.getStringExtra("finalListRemzTel");

            RemZVincode.setText(finalListRemzVin);
            RemZNomerDvig.setText(finalListRemzNomerDvig);
            RemZGos.setText(finalListRemzGos);

            RemZKlient.setText(finalListRemzClient);
            RemZTel.setText(finalListRemzTel);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String code = prefs.getString("code", null);

                    //-------------------------------------------------------------------------------------------------------------------------------------------
                    marka = new Marka();
                    marka.start(code,finalListRemzMarka);

                    try {
                        marka.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }
                    //--------------------------------------------------------------------------------------------------------------------------------------------

                }
            }).start();

        }

        RemZTel.addTextChangedListener(new TextWatcher() {
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
                String text = RemZTel.getText().toString();
                int  textLength = RemZTel.getText().length();
                if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" "))
                    return;
                if (textLength == 1) {
                    if (!text.contains("+"))
                    {
                        RemZTel.setText(new StringBuilder(text).insert(text.length() - 1, "+").toString());
                        RemZTel.setSelection(RemZTel.getText().length());
                    }
                }
                else if (textLength == 2)
                {
                    RemZTel.setText(new StringBuilder(text).insert(text.length() - 1, "7").toString());
                    RemZTel.setSelection(RemZTel.getText().length());
                }
                else if (textLength == 3)
                {
                    RemZTel.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    RemZTel.setSelection(RemZTel.getText().length());
                }
                else if (textLength == 7)
                {
                    RemZTel.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    RemZTel.setSelection(RemZTel.getText().length());
                }
                else if (textLength == 11)
                {
                    RemZTel.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    RemZTel.setSelection(RemZTel.getText().length());
                }
            }
        });


    }

    private void searchGos() {
        progressDialog = new ProgressDialog(OtkitRemz.this);
        progressDialog.setMessage("Пожалуйста подождите...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //-------------------------------------------------------------------------------------------------------------------------------------------
                final String gos = RemZGos.getText().toString();
                Zapros4 = new zapros4();
                Zapros4.start(gos);
                Log.e("Zapros44444====Try ","GosCam= "+gos);

                try {
                    Zapros4.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }

                String ResResult = Zapros4.getResult();



                try {
                    JSONArray json_data = new JSONArray(ResResult);
                    final String Otvet1 = json_data.getJSONArray(0).get(0).toString(); // vin code

                    if (Otvet1.equals("empty")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                                builder.setTitle("Окно предупреждения!")
                                        .setMessage("Данные по государственному номер "+gos+" Ничего не найдено!")
                                        .setCancelable(false)
                                        .setPositiveButton("ок",
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
                        final String Otvet2 = json_data.getJSONArray(0).get(1).toString(); // HY номер двигателя
                        final String Otvet3 = json_data.getJSONArray(0).get(2).toString(); // BMW
                        final String Otvet4 = json_data.getJSONArray(0).get(3).toString(); // ИМЯ
                        final String Otvet5 = json_data.getJSONArray(0).get(4).toString(); // ТЕЛЕФОН

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RemZVincode.setText(Otvet1);
                                RemZNomerDvig.setText(Otvet2);
                                RemZGos.setText(gos);
                                RemZKlient.setText(Otvet4);
                                RemZTel.setText(Otvet5);
                                finalListRemzMarka = Otvet3;
                            }
                        });

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                String code = prefs.getString("code", null);

                                //-------------------------------------------------------------------------------------------------------------------------------------------
                                marka = new Marka();
                                marka.start(code,Otvet3);

                                try {
                                    marka.join();
                                } catch (InterruptedException ie) {
                                    Log.e("pass 0", ie.getMessage());
                                }
                                //--------------------------------------------------------------------------------------------------------------------------------------------

                            }
                        }).start();



                    }
                } catch (Exception e) {
                    Log.e("Fail", e.toString());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        RemZGos.setText(gos);
                    }
                });

                //--------------------------------------------------------------------------------------------------------------------------------------------
            }
        }).start();
    }

    public void GoToCamera(View view){
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
                Snackbar.make(mLayout, "Разрешение отклонено",
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
            Intent intentV = new Intent(OtkitRemz.this, ShowCamera.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для открытия ремотного заказа с помощью фотографии государственного номера, " +
                            "вам необходимо разрешить использовании камеры в приложении My Auto. \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(OtkitRemz.this,
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
            AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для открытия ремотного заказа с помощью фотографии государственного номера, вам необходимо разрешить использовании камеры в приложении My Auto. \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(OtkitRemz.this,
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
        Intent intentV = new Intent(OtkitRemz.this, ShowCamera.class);
        startActivity(intentV);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    public void OtpravitRemZ(View view) {
        String remZKlient2 = RemZKlient.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
        builder.setTitle("Подтверждение")
                .setMessage("Вы уверены что хотите открыть данный ремонтный заказ? " +
                        "Имя клиента: "+remZKlient2)
                .setCancelable(false)
                .setPositiveButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                otpravit();
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
        //--------------------------------------------------------------------------------------------------------------------------------------------
    }

    private void otpravit() {
        progressDialog = new ProgressDialog(OtkitRemz.this);
        progressDialog.setMessage("Пожалуйста подождите....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        checkprog="1";

        new Thread(new Runnable() {
            public void run() {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String code = prefs.getString("code", null);
                String org = prefs.getString("org", null);



                MobS = code;
                remZVincode = RemZVincode.getText().toString();
                remZNomerDvig = RemZNomerDvig.getText().toString();
                remZGos = RemZGos.getText().toString();
                remZPokOd = RemZPokOd.getText().toString();
                remZKlient = RemZKlient.getText().toString();
                remZTel = RemZTel.getText().toString();



                if(remZVincode.equals("")|| remZNomerDvig.equals("") || remZGos.equals("") || remZPokOd.equals("") || remZKlient.equals("") || remZTel.equals("")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Заполните все поля!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    progressDialog.dismiss();
                }else {

                    Zapros1 = new zapros();
                    Zapros1.start(MobS,selectedSpinMarka,remZVincode,remZNomerDvig,remZGos,remZPokOd,selectedSpinLico,remZKlient,remZTel,org);
                    Log.e("Zapros1start====", " -sotr- "+MobS+"/ vincode- "+remZVincode+"/ nomerdvigatelya- "+remZNomerDvig+"/ gos nomer- "+remZGos+"/ pokOD- "+remZPokOd+"/ klient- "+remZKlient+"/ tele- "+remZTel+"/ marka- "+SelectedSpinMarka+"/ lico- "+SelectedSpinLico+"/ org"+ selectedSpinOrg);


                    progressDialog.setMessage("Создание ремотного заказа....");
                    try {
                        Zapros1.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    progressDialog.dismiss();
                }




            }
        }).start();
    }

    public class zapros extends Thread {

        String MobS;
        String SelectedSpinLico;
        String SelectedSpinMarka;
        String remZVincode;
        String remZNomerDvig;
        String remZGos;
        String remZPokOd;
        String remZKlient;
        String remZTel,selectedSpinOrg;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("sotr_code", MobS));
            NameValuerPair.add(new BasicNameValuePair("vid_cl", SelectedSpinLico));
            NameValuerPair.add(new BasicNameValuePair("marka", SelectedSpinMarka));
            NameValuerPair.add(new BasicNameValuePair("vin", remZVincode));
            NameValuerPair.add(new BasicNameValuePair("dvig", remZNomerDvig));
            NameValuerPair.add(new BasicNameValuePair("gos", remZGos));
            NameValuerPair.add(new BasicNameValuePair("km", remZPokOd));
            NameValuerPair.add(new BasicNameValuePair("client", remZKlient));
            NameValuerPair.add(new BasicNameValuePair("tel", remZTel));
            NameValuerPair.add(new BasicNameValuePair("org", selectedSpinOrg));
            Log.e("RUN====", " -sotr- "+MobS+"/ vincode- "+remZVincode+"/ nomerdvigatelya- "+remZNomerDvig+"/ gos nomer- "+remZGos+"/ pokOD- "+remZPokOd+"/ klient- "+remZKlient+"/ tele- "+remZTel+"/ marka- "+SelectedSpinMarka+"/ lico- "+SelectedSpinLico+"/ org" +selectedSpinOrg);


            /*	$arr_params['sotr_code'] 	= $_POST['sotr_code'];
                $arr_params['vid_cl'] 		= $_POST['vid_cl'];
                $arr_params['marka'] 		= $_POST['marka'];
                $arr_params['vin'] 			= $_POST['vin'];
                $arr_params['dvig'] 		= $_POST['dvig'];
                $arr_params['gos'] 			= $_POST['gos'];
                $arr_params['km'] 			= $_POST['km'];
                $arr_params['client'] 		= $_POST['client'];
                $arr_params['tel'] 			= $_POST['tel'];
                $arr_params['org'] 			= $_POST['org'];
            */


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/createRZ.php");
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

                JSONObject jsonObject = new JSONObject(result);

                final String num = jsonObject.getString("num");
                Log.e("json", "Результат: num" + num);

                if(num.equals("0")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                            builder.setTitle("Ошибка!")
                                    .setMessage("Ошибка создание ремотного заказа.\nПожалуйста проверьте данные")
                                    .setCancelable(false)
                                    .setPositiveButton("ок",
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
                    if(checkBox.isChecked()){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setMessage("Отправления приглашения...");
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                String mob = prefs.getString("mob", null);
                                String pin = prefs.getString("pin", null);
                                //-------------------------------------------------------------------------------------------------------------------------------------------
                                invite = new Invite();
                                invite.start(mob,pin,num);

                                try {
                                    invite.join();
                                } catch (InterruptedException ie) {
                                    Log.e("pass 0", ie.getMessage());
                                }

                            }
                        });
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }).start();
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                                builder.setTitle("Успешно!")
                                        .setMessage("Ремотный заказ успешно создан!\nНомер: "+num)
                                        .setCancelable(false)
                                        .setPositiveButton("ок",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        finish();
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }


                }
                Log.e("<><><><><>  Result  ", Otvet + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp, String selectedSpinMarka, String remZVincode, String remZNomerDvig, String remZGos, String remZPokOd, String selectedSpinLico, String remZKlient, String remZTel, String selectedSpinOrg) {
            this.MobS = mobp;
            this.SelectedSpinMarka = selectedSpinMarka;
            this.remZVincode = remZVincode;
            this.remZNomerDvig = remZNomerDvig;
            this.remZGos = remZGos;
            this.remZPokOd = remZPokOd;
            this.SelectedSpinLico = selectedSpinLico;
            this.remZKlient = remZKlient;
            this.remZTel = remZTel;
            this.selectedSpinOrg=selectedSpinOrg;
            this.start();
            Log.e("start====", " -sotr- "+mobp+"/ vincode- "+ this.remZVincode +"/ nomerdvigatelya- "+ this.remZNomerDvig +"/ gos nomer- "+ this.remZGos +"/ pokOD- "+ this.remZPokOd +"/ klient- "+ this.remZKlient +"/ tele- "+ this.remZTel +"/ marka- "+SelectedSpinMarka+"/ lico- "+SelectedSpinLico);


        }
    }

    public class Invite extends Thread {

        String MobS;
        String PinS;
        String Num;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(3);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("num", Num));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://"+IPSTRING+"/oleg/mobile/test/inviteUser.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection succes");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Произошла ошибка \n" +
                                        "fail 1")
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Произошла ошибка \n" +
                                        "fail 2")
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

                final String rec = jsonObject.getString("rec");
                Log.e("json", "Результат: rec" + rec);

                if (rec.equals("no")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                            builder.setTitle("Ошибка!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Произошла ошибка. Приглашения не было отправлено!")
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
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                            builder.setTitle("Успешно!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Ремотный заказ создан успешно "+rec+"! И отправлено приглашения в приложения MyAuto")
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finish();
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Произошла ошибка \n" +
                                        "fail 3")
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


        public void start(String mob, String pin, String num) {
            this.MobS = mob;
            this.PinS = pin;
            this.Num = num;
            this.start();
        }

        public String getResult() {
            return result;
        }

    } //marka

    public class Marka extends Thread {

        String codeS;
        String MarkS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("sotr_code", codeS));

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Произошла ошибка \n" +
                                        "fail 1")
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Произошла ошибка \n" +
                                        "fail 2")
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
                JSONArray jsonArray = jsonObject.getJSONArray("marks");

                if (MarkS.equals("")){
                    spMarka = new String[jsonArray.length()];
                    for(int i = 0; i < jsonArray.length(); i++){
                        String name = jsonArray.getJSONObject(i).getString("name");
                        spMarka[i] = name;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(OtkitRemz.this, android.R.layout.simple_spinner_dropdown_item, spMarka);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            Spinner spinner = (Spinner) findViewById(R.id.spinner);
                            spinner.setAdapter(adapter);


                            spinner.setPrompt("Выберите марку машины");
                            // устанавливаем обработчик нажатия
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    Spinner spinner2 = (Spinner) findViewById(R.id.spinner);
                                    selectedSpinMarka = spinner2.getSelectedItem().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });
                        }
                    });
                }else{
                    spMarka = new String[jsonArray.length()+1];
                    spMarka[0] = MarkS;
                    for(int i = 0; i < jsonArray.length(); i++){
                        String name = jsonArray.getJSONObject(i).getString("name");
                        spMarka[i+1] = name;
                        Log.e("name ",name);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // адаптер
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(OtkitRemz.this, android.R.layout.simple_spinner_dropdown_item, spMarka);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            Spinner spinner = (Spinner) findViewById(R.id.spinner);
                            spinner.setAdapter(adapter);


                            spinner.setPrompt("Выберите марку машины");
                            // устанавливаем обработчик нажатия
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    Spinner spinner2 = (Spinner) findViewById(R.id.spinner);
                                    selectedSpinMarka = spinner2.getSelectedItem().toString();
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                }
                            });


                        }
                    });
                }






                progressDialog.dismiss();
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(OtkitRemz.this);
                        builder.setTitle("Ошибка!")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage("Произошла ошибка \n" +
                                        "fail 3")
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


        public void start(String code, String otvet3) {
            this.codeS = code;
            this.MarkS = otvet3;
            this.start();
        }

        public String getResult() {
            return result;
        }

    } //marka

    public class zapros4 extends Thread {

        String GosCam;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(1);

            NameValuerPair.add(new BasicNameValuePair("gn", GosCam));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/androidLastGN.php");
                httpPost.setEntity(new UrlEncodedFormEntity(NameValuerPair, "UTF-8"));
                HttpResponse resArr = httpClient.execute(httpPost);
                HttpEntity entity = resArr.getEntity();
                is = entity.getContent();
                Log.e("ZAPROS 4", "connection succes");
            } catch (Exception e) {
                progressDialog.dismiss();
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
                Log.e("ZAPROS 4", "connection succes " + result);
            } catch (Exception e) {
                progressDialog.dismiss();
                Log.e("Fail 2", e.toString());
            }
            try {
                Log.e("ZAPROS 4", result + " ==  ==== )))");
            } catch (Exception e) {
                progressDialog.dismiss();
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String GosCam) {
            this.GosCam = GosCam;
            this.start();
        }

        public String getResult() {
            return result;
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----OtkitRemz-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----OtkitRemz-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----OtkitRemz-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(checkprog=="1"){
            progressDialog.dismiss();
            checkprog="0";
        }
        Log.e("-----OtkitRemz-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----OtkitRemz-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----OtkitRemz-----", "onRestart");
    }

}
