package com.myauto.designer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DetailRemz extends AppCompatActivity {

    EditText AcceptAuto, MarkaAuto, Vin, Dvig, GosD, OdometrD, ClientD, TelD;
    String finalListRemzTel,finalListRemzRemz;
    String OtvetClosed,result;
    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 0;
    ProgressDialog progressDialog;

    public zapros1 Zapros1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_remz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        AcceptAuto = (EditText) findViewById(R.id.AcceptAuto);
        MarkaAuto = (EditText) findViewById(R.id.MarkaAuto);
        Vin = (EditText) findViewById(R.id.Vin);
        Dvig = (EditText) findViewById(R.id.Dvig);
        GosD = (EditText) findViewById(R.id.GosD);
        OdometrD = (EditText) findViewById(R.id.OdometrD);
        ClientD = (EditText) findViewById(R.id.ClientD);
        TelD = (EditText) findViewById(R.id.TelD);


        Intent intent = getIntent();
        finalListRemzRemz = intent.getStringExtra("finalListRemzRemz");
        Log.e("finalListRemzRemz",""+finalListRemzRemz);
        String finalListRemzAccept = intent.getStringExtra("finalListAcceptAuto");
        String finalListRemzMarka = intent.getStringExtra("finalListMarkaAuto");
        String finalListRemzVin = intent.getStringExtra("finalListRemzVin");
        String finalListRemzNomerDvig = intent.getStringExtra("finalListRemzNomerDvig");
        String finalListRemzGos = intent.getStringExtra("finalListRemzGos");
        String finalListRemzOdometr = intent.getStringExtra("finalListRemzOdometr");
        String finalListRemzClient = intent.getStringExtra("finalListRemzClient");
        finalListRemzTel = intent.getStringExtra("finalListRemzTel");

        setTitle(finalListRemzRemz);

        AcceptAuto.setText(finalListRemzAccept);
        MarkaAuto.setText(finalListRemzMarka);
        Vin.setText(finalListRemzVin);
        Dvig.setText(finalListRemzNomerDvig);

        GosD.setText(finalListRemzGos);
        OdometrD.setText(finalListRemzOdometr);
        ClientD.setText(finalListRemzClient);
        TelD.setText(finalListRemzTel);



    }

    public void CallClient(View view) {
        comeCall();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_READ_PHONE_STATE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                comeCall();
            } else {

            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void comeCall() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + finalListRemzTel));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
            Log.e("CallClient","tel:" + finalListRemzTel);
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
        // END_INCLUDE(startCamera)
    }

    /**
     * Requests the {@link Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            ActivityCompat.requestPermissions(DetailRemz.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_REQUEST_READ_PHONE_STATE);

        }else{
            ActivityCompat.requestPermissions(DetailRemz.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_REQUEST_READ_PHONE_STATE);
        }
    }




    public void ClosedRemz(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailRemz.this);
        builder.setTitle("Подтвердите действие")
                .setMessage("Вы действительно хотите подтвердить закрытия ремонтного заказа?")
                .setCancelable(false)
                .setPositiveButton("Да",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        progressDialog = new ProgressDialog(DetailRemz.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Пожалуйста подождите...");
                        progressDialog.show();


                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
                                String sotr = prefs.getString("Sotr", null);

                                //-------------------------------------------------------------------------------------------------------------------------------------------
                                Zapros1 = new zapros1();
                                Zapros1.start(sotr,finalListRemzRemz);
                                Log.e("Zapros1====Try ","SotrS= "+sotr);
                                Log.e("Zapros1====Try ","finalListRemzRemz= "+finalListRemzRemz);

                                try {
                                    Zapros1.join();
                                } catch (InterruptedException ie) {
                                    Log.e("pass 0", ie.getMessage());
                                    progressDialog.dismiss();
                                }

                                result = Zapros1.getResult();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            JSONArray json_data = new JSONArray(result); // сам массив
                                            for (int i = 0; i < json_data.length(); i++) { // Цикл где в стринг result пришло от базы
                                                OtvetClosed = json_data.getJSONArray(i).get(0).toString(); // gn string равна данной строке 6
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), OtvetClosed, Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            Log.e("OtvetClosed", OtvetClosed+" ==  ==== ");
                                        } catch (Exception e) {
                                            Log.e("Fail 3", e.toString());
                                            progressDialog.dismiss();
                                        }

                                        progressDialog.dismiss();

                                    }
                                });


                            }
                        }).start();
                        //--------------------------------------------------------------------------------------------------------------------------------------------
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

    public class zapros1 extends Thread {

        String SotrS,finalListRemzRemz;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            NameValuerPair.add(new BasicNameValuePair("sotr", SotrS));
            NameValuerPair.add(new BasicNameValuePair("remZ", finalListRemzRemz));
            Log.e("===zapros1===RUN","SotrS= "+SotrS);
            Log.e("===zapros1===RUN","finalListRemzRemz= "+finalListRemzRemz);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/androidRemzClosed.php"); //Код сотрудника
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
                Log.e("Pass 3", "check result");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }


        public void start(String mobp, String finalListRemzRemz) {
            this.SotrS = mobp;
            this.finalListRemzRemz=finalListRemzRemz;
            this.start();
            Log.e("===ListRemZ===Start1","SotrS= "+SotrS);
        }

        public String getResult() {
            return result;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailremz, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(item.getItemId()==android.R.id.home){
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_create_remz) {
            Intent ope = new Intent(DetailRemz.this, OtkitRemz.class);
            ope.putExtra("finalListRemzRemz", finalListRemzRemz);
            ope.putExtra("finalListRemzVin", Vin.getText().toString());
            ope.putExtra("finalListRemzNomerDvig", Dvig.getText().toString());
            ope.putExtra("finalListRemzGos", GosD.getText().toString());
            ope.putExtra("finalListRemzOdometr", OdometrD.getText().toString());
            ope.putExtra("finalListRemzClient", ClientD.getText().toString());
            ope.putExtra("finalListRemzTel", TelD.getText().toString());
            ope.putExtra("finalListMarkaAuto", MarkaAuto.getText().toString());
            ope.putExtra("finalListAcceptAuto", AcceptAuto.getText().toString());
            startActivity(ope);

        }else if (id == R.id.menu_rab_remz) {
            Intent ope = new Intent(DetailRemz.this, rab_oper.class);
            ope.putExtra("num", finalListRemzRemz);
            startActivity(ope);

        }else if (id == R.id.menu_product_remz) {
            Intent ope = new Intent(DetailRemz.this, rzArticle.class);
            ope.putExtra("num", finalListRemzRemz);
            startActivity(ope);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----DetailRemZ-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----DetailRemZ-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----DetailRemZ-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----DetailRemZ-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----DetailRemZ-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----DetailRemZ-----", "onRestart");
    }


}
