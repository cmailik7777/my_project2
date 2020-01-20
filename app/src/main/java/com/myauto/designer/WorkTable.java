package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class WorkTable extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private View mLayout;
    private ZXingScannerView scannerView;

    ProgressDialog progressDialog;
    String checkprog;
    int Numboftabs = 2;
    Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    View holderView, contentView;
    MenuItem target;

    private Org org;
    private IP ip;
    ImageView manag_Org_img;
    String name,logo;
    TextView Manag_Choise_Org;

    ListView listView;
    ArrayList<DataModelManagMenu> dataModelManagMenus;
    private static CustomAdapterManagMenu adapter;

    String menu1 = "Открыть ремотный заказ";
    String menu2 = "Поиск ремотных заказов";
    String menu3 = "Операция с QR кодом";
    String menu4 = "Начало рабочего дня";
    String menu5 = "Заявки";
    String menu6 = "Конец рабочего дня";
    String menu7 = "Ремонтные заказы";
    String menu8 = "Инвентаризация";

    public static final int CONNECTION_TIMEOUT = 100000;
    public static final int READ_TIMEOUT = 150000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_table);

        mLayout = (View)findViewById(R.id.Manag_Main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("name", null);
        setTitle(name);

        manag_Org_img = (ImageView)findViewById(R.id.manag_Org_img);
        Manag_Choise_Org = (TextView)findViewById(R.id.Manag_Choise_Org);


        SharedPreferences prefs2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String org2 = prefs2.getString("org", null);
        String orgLogo2 = prefs2.getString("orgLogo", null);
        String orgSize = prefs2.getString("orgSize", null);

        if (orgSize==null){
        }else{
            if(orgSize.equals("0")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Manag_Choise_Org.setVisibility(View.INVISIBLE);
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Manag_Choise_Org.setVisibility(View.VISIBLE);
                    }
                });
            }
        }




        if(org2==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Пожалуйста подождите....");
            progressDialog.setCancelable(false);
            progressDialog.show();
//{"res":"88.204.153.118:8080"}


            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String code = prefs.getString("code", null);
                    org = new Org();
                    org.start(code);

                    try {
                        org.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }
                }
            }).start();
        }else{
            byte[] decodedString = Base64.decode(orgLogo2, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            manag_Org_img.setImageBitmap(decodedByte);
        }



//старый drawer

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        */

//https://stackoverflow.com/questions/41843485/transition-in-navigation-drawer-android
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        holderView = findViewById(R.id.holderManag);
        contentView = findViewById(R.id.contentManag);
        toolbar.setNavigationIcon(new DrawerArrowDrawable(this));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (drawer.isDrawerOpen(navigationView)) {
                                                         drawer.closeDrawer(navigationView);
                                                     } else {
                                                         drawer.openDrawer(navigationView);
                                                     }
                                                 }
                                             }
        );


        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawer, float slideOffset) {


                                         contentView.setX(navigationView.getWidth() * slideOffset);
                                         RelativeLayout.LayoutParams lp =
                                                 (RelativeLayout.LayoutParams) contentView.getLayoutParams();
                                         lp.height = drawer.getHeight() -
                                                 (int) (drawer.getHeight() * slideOffset * 0.3f);
                                         lp.topMargin = (drawer.getHeight() - lp.height) / 2;
                                         contentView.setLayoutParams(lp);
                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                     }
                                 }
        );
        Menu menu =navigationView.getMenu();

        target = menu.findItem(R.id.nav_manager);


        listView = (ListView)findViewById(R.id.ListView_Manag_Menu);
        listView.setDividerHeight(0);
        dataModelManagMenus = new ArrayList<>();

        SharedPreferences prefs3 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String manager = prefs3.getString("manager_service", null);
        String driver = prefs3.getString("driver", null);
        String mehanic = prefs3.getString("mehanic", null);

        if (manager!=null){
            dataModelManagMenus.add(new DataModelManagMenu(menu1));
            dataModelManagMenus.add(new DataModelManagMenu(menu2));
            dataModelManagMenus.add(new DataModelManagMenu(menu3));
            dataModelManagMenus.add(new DataModelManagMenu(menu8));
        }

        if(driver!=null){
            dataModelManagMenus.add(new DataModelManagMenu(menu4));
            dataModelManagMenus.add(new DataModelManagMenu(menu5));
            dataModelManagMenus.add(new DataModelManagMenu(menu6));
        }

        if(mehanic!=null){
            dataModelManagMenus.add(new DataModelManagMenu(menu7));
        }





        adapter= new CustomAdapterManagMenu(dataModelManagMenus,getApplicationContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvMenu = (TextView)view.findViewById(R.id.Menu);
                String menu = tvMenu.getText().toString();

                if(menu.equals(menu1)){
                    Intent intentV = new Intent(WorkTable.this, OtkitRemz.class);
                    startActivity(intentV);
                }

                if(menu.equals(menu2)){
                    Intent intentV = new Intent(WorkTable.this, ListRemZ.class);
                    startActivity(intentV);
                }

                if(menu.equals(menu3)){
                    showCameraPreview();
                }

                if(menu.equals(menu5)){
                    Intent intentV = new Intent(WorkTable.this, Drivers.class);
                    startActivity(intentV);
                }

                if(menu.equals(menu4)){
                    Intent intentV = new Intent(WorkTable.this, drBeginning.class);
                    startActivity(intentV);
                }


                if(menu.equals(menu6)){
                    Intent intentV = new Intent(WorkTable.this, drEndBeginning.class);
                    startActivity(intentV);
                }

                if(menu.equals(menu7)){
                    Intent intentV = new Intent(WorkTable.this, rzMehanic.class);
                    startActivity(intentV);
                }

                if (menu.equals(menu8)){
                    showCameraPreview();
                }


            }
        });



    }


    public void openRemZ(View view) {
        Intent intentV = new Intent(this, OtkitRemz.class);
        startActivity(intentV);
    }

    public void remz(View view) {
        Intent intentV = new Intent(this, ListRemZ.class);
        startActivity(intentV);
    }

    public void drBeginning(View view) {
        Intent intentV = new Intent(this, drBeginning.class);
        startActivity(intentV);
    }

    public void drOrders(View view) {
        Intent intentV = new Intent(this, Drivers.class);
        startActivity(intentV);
    }

    public void drEnd(View view) {
        Intent intentV = new Intent(this, ListRemZ.class);
        startActivity(intentV);
    }

    public void dai(View view) {
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
                //startsCamera();
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
                Intent intentV = new Intent(WorkTable.this, QRCode.class);
                startActivity(intentV);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для исполнения операции с QR кодом, " +
                            "вам необходимо разрешить использовании камеры в приложении My Auto. \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(WorkTable.this,
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
            AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
            builder.setTitle("Необходимо подтверждение!")
                    .setMessage("Для исполнения операции с QR кодом, " +
                            "вам необходимо разрешить использовании камеры в приложении My Auto. \n Разрешить?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ActivityCompat.requestPermissions(WorkTable.this,
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
        Intent intentV = new Intent(this, QRCode.class);
        Intent intentVs = new Intent(this, Invent.class);
        startActivity(intentV);
        startActivity(intentVs);
    }



    public void change(View view){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Пожалуйста подождите....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String code = prefs.getString("code", null);
                org = new Org();
                org.start(code);

                try {
                    org.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_garaj) {
            Intent intentV = new Intent(this, garaj.class);
            startActivity(intentV);
            finish();

        } else if (id == R.id.nav_main) {
            Intent intentV = new Intent(this, Home.class);
            startActivity(intentV);
            finish();

        } else if (id == R.id.nav_push) {
            Intent intentV = new Intent(this, push.class);
            startActivity(intentV);
            finish();

        } else if (id == R.id.nav_profil) {

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String mob = prefs.getString("mob", null);
            if (mob != null){
                Intent intentV = new Intent(this, Profile2.class);
                startActivity(intentV);
                finish();
            }else{
                Intent intentV = new Intent(this, Login.class);
                startActivity(intentV);
                finish();
            }

        } else if (id == R.id.nav_service) {
            Intent intentV = new Intent(this, MyServiceActivity.class);
            startActivity(intentV);
            finish();

        } else if (id == R.id.nav_prog) {
            Intent intentP = new Intent(this, Info.class);
            startActivity(intentP);

        } else if (id == R.id.nav_manager) {


        } else if (id == R.id.nav_shop) {
            Intent intentP = new Intent(this, market.class);
            startActivity(intentP);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    public class Org extends Thread {

        String CodeS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("sotr_code", CodeS));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getOrgManager.php");
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
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

            } catch (final Exception e) {
                Log.e("Fail 2", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
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
                // getOrgManager
                // {"orgs":[{"name":"AVANGARD", "logo"
                // marks":[{"name":"!Universal"},
                // "corps": [{"name":""}

                JSONObject jsonObject = new JSONObject(result);
                final JSONArray jsonArray = jsonObject.getJSONArray("orgs");

                int j = 0;

                for(int i = 0; i < jsonArray.length(); i++){
                    name = jsonArray.getJSONObject(i).getString("name");
                    Log.e("name ",name);
                    logo = jsonArray.getJSONObject(i).getString("logo");
                    Log.e("logo ",logo);
                    j = j+1;
                }

                if (j>1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(WorkTable.this);
                            builderSingle.setTitle("Выберите организацию");

                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkTable.this, android.R.layout.select_dialog_singlechoice);


                            try{
                                for(int i = 0; i < jsonArray.length(); i++){
                                    String name = jsonArray.getJSONObject(i).getString("name");
                                    arrayAdapter.add(name);
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }


                            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String strName = arrayAdapter.getItem(which);
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        JSONArray jsonArray = jsonObject.getJSONArray("orgs");
                                        for(int i = 0; i < jsonArray.length(); i++){
                                            String name = jsonArray.getJSONObject(i).getString("name");
                                            Log.e("name ",name);
                                            if(strName.equals(name)){
                                                Log.e("name ",name+strName);
                                                final String logo = jsonArray.getJSONObject(i).getString("logo");
                                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                editor.putString("org", name);
                                                editor.putString("orgLogo", logo);
                                                editor.putString("orgSize", "1");
                                                editor.apply();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        byte[] decodedString = Base64.decode(logo, Base64.DEFAULT);
                                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                                                        manag_Org_img.setImageBitmap(decodedByte);
                                                    }
                                                });
                                                progressDialog = new ProgressDialog(WorkTable.this);
                                                progressDialog.setMessage("Пожалуйста подождите....");
                                                progressDialog.setCancelable(false);
                                                progressDialog.show();
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                                        String org = prefs.getString("org", null);
                                                        ip = new IP();
                                                        ip.start(org);

                                                        try {
                                                            ip.join();
                                                        } catch (InterruptedException ie) {
                                                            Log.e("pass 0", ie.getMessage());
                                                        }
                                                    }
                                                }).start();
                                                return;
                                            }
                                        }
                                        //{"res":"88.204.153.118:8080"}

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            builderSingle.show();
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Manag_Choise_Org.setVisibility(View.VISIBLE);
                        }
                    });


                }else{
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("org", name);
                    editor.putString("orgLogo", logo);
                    editor.putString("orgSize", "0");
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            byte[] decodedString = Base64.decode(logo, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                            manag_Org_img.setImageBitmap(decodedByte);
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Manag_Choise_Org.setVisibility(View.INVISIBLE);
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog = new ProgressDialog(WorkTable.this);
                            progressDialog.setMessage("Пожалуйста подождите....");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                        }
                    });

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            String org = prefs.getString("org", null);
                            ip = new IP();
                            ip.start(org);

                            try {
                                ip.join();
                            } catch (InterruptedException ie) {
                                Log.e("pass 0", ie.getMessage());
                            }
                        }
                    }).start();

                }

                //manag_Org_img
                progressDialog.dismiss();
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
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


        public void start(String mobp) {
            this.CodeS = mobp;
            this.start();
        }

        public String resOrg() {
            return result;
        }

    }

    public class IP extends Thread {

        String OrgS;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("org", OrgS));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/nd/getIPOrg.php");
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
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

            } catch (final Exception e) {
                Log.e("Fail 2", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
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
                //{"wheels":[{"date":"10.04.2018", "gos":"602DNA02", "description":"Р—єР°", "brand":"Yokohama", "size":"215/60/R16", "count":"4", "count_month":"2", "sum":"1В 000"} ]}

                JSONObject jsonObject = new JSONObject(result);
                String ip = jsonObject.getString("res");
                Log.e("json", "Результат ip: " + ip);

                if (ip.equals("")){
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("ip", "gps-monitor.kz");
                    editor.apply();
                    Log.e("json", "Результат ip: gps-monitor.kz" + ip);
                }else {
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("ip", ip);
                    editor.apply();
                    Log.e("json", "Результат ip: " + ip);
                }

                progressDialog.dismiss();
                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
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


        public void start(String mobp) {
            this.OrgS = mobp;
            this.start();
        }

        public String resOrg() {
            return result;
        }

    }








    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----Manag-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Manag-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.e("-----Manag-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(checkprog=="1"){
            progressDialog.dismiss();
            checkprog="0";
        }
        Log.e("-----Manag-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();

        //new AsyncRetrieve().execute();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String manager_service = prefs.getString("manager_service", null);
                String driver = prefs.getString("driver", null);
                String mehanic = prefs.getString("mehanic", null);
                String mob = prefs.getString("mob", null);

                if (manager_service==null && driver==null && mehanic==null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_manager).setVisible(false);
                }else if (manager_service!=null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_manager).setVisible(true);
                }else if (driver!=null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_manager).setVisible(true);
                }else if (mehanic!=null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_manager).setVisible(true);
                }

                if(mob==null){
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_garaj).setVisible(false);

                    Menu nav_Menunav_service = navigationView.getMenu();
                    nav_Menunav_service.findItem(R.id.nav_service).setVisible(false);

                    Menu nav_Menunav_shop = navigationView.getMenu();
                    nav_Menunav_shop.findItem(R.id.nav_shop).setVisible(false);

                    Menu nav_Menunav_push = navigationView.getMenu();
                    nav_Menunav_push.findItem(R.id.nav_push).setVisible(false);
                }else {
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.nav_garaj).setVisible(true);

                    Menu nav_Menunav_service = navigationView.getMenu();
                    nav_Menunav_service.findItem(R.id.nav_service).setVisible(true);

                    Menu nav_Menunav_shop = navigationView.getMenu();
                    nav_Menunav_shop.findItem(R.id.nav_shop).setVisible(true);

                    Menu nav_Menunav_push = navigationView.getMenu();
                    nav_Menunav_push.findItem(R.id.nav_shop).setVisible(true);
                }
            }
        });

        if(isInternetOn()){
            Log.e("==Manag===", "=======Connection");
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
            builder.setTitle("Нет соединения!")
                    .setMessage("У вас нет соединения с интернетом, пожалуйста попробуйте позже!")
                    .setIcon(R.mipmap.ic_launcher)
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
            Log.e("==Manag===", "=======No connection");
        }


        SharedPreferences prefs2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String org2 = prefs2.getString("org", null);

        if(org2==null){

        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(WorkTable.this);
                    progressDialog.setMessage("Пожалуйста подождите....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String org = prefs.getString("org", null);
                    ip = new IP();
                    ip.start(org);

                    try {
                        ip.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }
                }
            }).start();
        }


        Log.e("-----Manag-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Manag-----", "onRestart");
    }


    public final boolean isInternetOn() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

        }
        return false;
    }


    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        // This method does not interact with UI, You need to pass result to onPostExecute to display
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://gps-monitor.kz/oleg/androidVersion.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        // this method will interact with UI, display result sent from doInBackground method
        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("1.15")) {
                Log.e("Version check"," Yes 1.15 version");
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WorkTable.this);
                        builder.setTitle("Обновление")
                                .setIcon(R.drawable.ic_launcher_round)
                                .setMessage("Вышло новое обновление приложения!\n \nТекущая версия 1.15\nНовая версия приложения "+result)
                                .setCancelable(false)
                                .setNegativeButton("Отмена",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        })
                                .setPositiveButton("Обновить",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse("market://details?id=com.myauto.designer"));
                                                startActivity(intent);
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        }
    }
}
