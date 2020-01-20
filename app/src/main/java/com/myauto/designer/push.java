package com.myauto.designer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class push extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


                    /*calendar1 = Calendar.getInstance();
                int day2 = calendar1.get(Calendar.DAY_OF_MONTH);
                int month2 = calendar1.get(Calendar.MONTH);
                int year2 = calendar1.get(Calendar.YEAR);

                dpd = new DatePickerDialog(push.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        Log.e("calendar1","mYear "+ mYear+" mMonth "+ mMonth+" mDay "+ mDay);

                        calendar2 = Calendar.getInstance();
                        int day = calendar2.get(Calendar.DAY_OF_MONTH);
                        int month = calendar2.get(Calendar.MONTH);
                        int year = calendar2.get(Calendar.YEAR);

                        dpd2 = new DatePickerDialog(push.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                                Log.e("calendar2","mYear "+ mYear+" mMonth "+ mMonth+" mDay "+ mDay);
                            }
                        }, year,month,day);
                        dpd2.show();

                    }
                }, year2,month2,day2);
                dpd.show();*/


    ArrayList<DataModelPush> dataModelPushes;
    ListView listView;
    private static CustomAdapterPush adapter;


    private String MobS;
    private String PinS;

    ProgressDialog progressDialog;
    String checkprog;
    String textpush;
    String datapush;

    int Numboftabs = 2;
    Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    View holderView, contentView;

    private zapros Zapros1;
    private getArchiv GetArchiv;
    MenuItem target;
    ProgressBar loadingPush;

    TextView textIfLabel;

    DatePickerDialog dpd,dpd2;

    Calendar calendar1,calendar2;
    Button archiv,doneS,donePO;

    String monthPls;
    String dayMePls;
    String periodS,periodPo;
    TextView txtDateS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        listView=(ListView)findViewById(R.id.ListPush);

        textIfLabel=(TextView)findViewById(R.id.textIfLabel);
        txtDateS=(TextView)findViewById(R.id.txtDateS);
        archiv = (Button)findViewById(R.id.button7);

        doneS = (Button)findViewById(R.id.doneS);

        donePO = (Button)findViewById(R.id.donePO);



        final DatePicker datePickerS = (DatePicker) findViewById(R.id.datePickerS);
        final DatePicker datePickerPo = (DatePicker) findViewById(R.id.datePickerPo);

        archiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        datePickerS.setVisibility(View.VISIBLE);
                        txtDateS.setVisibility(View.VISIBLE);
                        archiv.setVisibility(View.INVISIBLE);
                        doneS.setVisibility(View.VISIBLE);
                        textIfLabel.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                        txtDateS.setText("Выберите дату С");
                    }
                });


            }
        });


        doneS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(push.this, datePickerS.getDayOfMonth()+"."+datePickerS.getMonth()+"."+datePickerS.getYear(),Toast.LENGTH_LONG).show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        datePickerPo.setVisibility(View.VISIBLE);
                        datePickerS.setVisibility(View.INVISIBLE);

                        txtDateS.setVisibility(View.VISIBLE);

                        doneS.setVisibility(View.INVISIBLE);
                        donePO.setVisibility(View.VISIBLE);
                        txtDateS.setText("Выбранная дата с "+datePickerS.getDayOfMonth()+"."+datePickerS.getMonth()+"."+datePickerS.getYear()+" . По");
                    }
                });


            }
        });

        donePO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(push.this, datePickerPo.getDayOfMonth()+"."+datePickerPo.getMonth()+"."+datePickerPo.getYear(),Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        datePickerPo.setVisibility(View.INVISIBLE);

                        txtDateS.setVisibility(View.INVISIBLE);

                        donePO.setVisibility(View.INVISIBLE);
                        archiv.setVisibility(View.VISIBLE);
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingPush = (ProgressBar)findViewById(R.id.loadingPush);
                        loadingPush.setVisibility(View.VISIBLE);
                        textIfLabel.setVisibility(View.INVISIBLE);
                    }
                });

                Date d = new Date(datePickerS.getYear(), datePickerS.getMonth(), datePickerS.getDayOfMonth());
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM", Locale.getDefault());
                final String doneS = dateFormatter.format(d)+"."+datePickerS.getYear();


                Date d2 = new Date(datePickerPo.getYear(), datePickerPo.getMonth(), datePickerPo.getDayOfMonth());
                SimpleDateFormat dateFormatter2 = new SimpleDateFormat("dd.MM", Locale.getDefault());
                final String donePo = dateFormatter2.format(d2)+"."+datePickerPo.getYear();

                //Toast.makeText(push.this, "doneS " + doneS + " donePo " + donePo,Toast.LENGTH_LONG).show();



                //final String doneS = datePickerS.getDayOfMonth()+"."+datePickerS.getMonth()+"."+datePickerS.getYear();
                //final String donePo = datePickerPo.getDayOfMonth()+"."+datePickerPo.getMonth()+"."+datePickerPo.getYear();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        String mob = prefs.getString("mob", null);
                        String pin = prefs.getString("pin", null);

                        //-------------------------------------------------------------------------------------------------------------------------------------------
                        GetArchiv = new getArchiv();
                        GetArchiv.start(mob, pin,doneS,donePo);

                        try {
                            GetArchiv.join();
                        } catch (InterruptedException ie) {
                            Log.e("pass 0", ie.getMessage());
                        }
                        //--------------------------------------------------------------------------------------------------------------------------------------------

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String result=GetArchiv.getResult();
                                try {
                                    dataModelPushes = new ArrayList<>();


                                    JSONObject jsonObject = new JSONObject(result);
                                    String empty = jsonObject.getString("empty");
                                    if (empty.equals("no")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                textIfLabel.setVisibility(View.VISIBLE);
                                                textIfLabel.setText("Уведомления за период с "+doneS+" по "+donePo+ " отсутсвуют");
                                                listView.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                textIfLabel.setVisibility(View.INVISIBLE);
                                                listView.setVisibility(View.VISIBLE);
                                            }
                                        });

                                        JSONArray jsonArray = jsonObject.getJSONArray("res");

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
                                        }
                                    }
                                    Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
                                } catch (Exception e) {
                                    Log.e("Fail 3 GetArchiv", e.toString());
                                }
                                loadingPush.setVisibility(View.INVISIBLE);

                            }
                        });



                    }
                }).start();
            }
        });











        /*AlertDialog.Builder builderSingle = new AlertDialog.Builder(push.this);
        builderSingle.setTitle("Выберите организацию");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(push.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Hardik");
        arrayAdapter.add("Archit");
        arrayAdapter.add("Jignesh");
        arrayAdapter.add("Umang");
        arrayAdapter.add("Gatti");
        arrayAdapter.add("Hardik");
        arrayAdapter.add("Archit");
        arrayAdapter.add("Jignesh");
        arrayAdapter.add("Umang");
        arrayAdapter.add("Gatti");
        arrayAdapter.add("Hardik");
        arrayAdapter.add("Archit");
        arrayAdapter.add("Jignesh");
        arrayAdapter.add("Umang");
        arrayAdapter.add("Gatti");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(push.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();*/





//старый drawer
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        holderView = findViewById(R.id.holderPush);
        contentView = findViewById(R.id.contentPush);
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


        archiv();
    }

    private void archiv() {
        loadingPush = (ProgressBar)findViewById(R.id.loadingPush);
        loadingPush.setVisibility(View.VISIBLE);
        textIfLabel.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                String pin = prefs.getString("pin", null);

                //-------------------------------------------------------------------------------------------------------------------------------------------
                Zapros1 = new zapros();
                Zapros1.start(mob, pin);

                try {
                    Zapros1.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
                //--------------------------------------------------------------------------------------------------------------------------------------------

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String result=Zapros1.getResult();
                        try {
                            dataModelPushes = new ArrayList<>();


                            JSONObject jsonObject = new JSONObject(result);
                            String empty = jsonObject.getString("empty");
                            if (empty.equals("no")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textIfLabel.setVisibility(View.VISIBLE);
                                        textIfLabel.setText("Уведомления за период с "+periodS+" по "+periodPo+ " отсутсвуют");
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textIfLabel.setVisibility(View.INVISIBLE);
                                    }
                                });

                                JSONArray jsonArray = jsonObject.getJSONArray("res");

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
                                }
                            }
                            Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
                        } catch (Exception e) {
                            Log.e("Fail 3", e.toString());
                        }
                        loadingPush.setVisibility(View.INVISIBLE);

                    }
                });



            }
        }).start();
    }





    public class zapros extends Thread {

        String MobS;
        String PinS;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            /*
                $arr_params['mob'] = $mob;
                $arr_params['pin'] = $_POST['pin'];
                $arr_params['PeriodS'] = $_POST['PeriodS'];
                $arr_params['PeriodDo'] = $_POST['PeriodDo'];
            */

            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            calendar.set(year, month, 1);
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Log.e("calendar","Текущий год "+year+" Текущий месяц "+month+" Количество дней " +maxDay);


            dayMePls = String.valueOf(maxDay);
            if (dayMePls.equals("1")){
                dayMePls = "01";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("2")){
                dayMePls = "02";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("3")){
                dayMePls = "03";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("4")){
                dayMePls = "04";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("5")){
                dayMePls = "05";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("6")){
                dayMePls = "06";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("7")){
                dayMePls = "07";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("8")){
                dayMePls = "08";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("9")){
                dayMePls = "09";
                Log.e("monthPls", dayMePls);
            }


            month = month+1;
            monthPls = String.valueOf(month);
            if (monthPls.equals("1")){
                monthPls = "01";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("2")){
                monthPls = "02";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("3")){
                monthPls = "03";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("4")){
                monthPls = "04";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("5")){
                monthPls = "05";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("6")){
                monthPls = "06";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("7")){
                monthPls = "07";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("8")){
                monthPls = "08";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("9")){
                monthPls = "09";
                Log.e("monthPls", monthPls);
            }


            periodS = String.valueOf("01"+"."+monthPls+"."+year);
            periodPo = String.valueOf(maxDay+"."+monthPls+"."+year);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("PeriodS", periodS));
            NameValuerPair.add(new BasicNameValuePair("PeriodDo", periodPo));

            Log.e("PeriodS",periodS);
            Log.e("PeriodDo",periodPo);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getMyPush.php");
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



    public class getArchiv extends Thread {

        String MobS;
        String PinS,PerS,PerDo;

        InputStream is = null;
        String result = null;
        String line = null;

        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(2);

            /*
                $arr_params['mob'] = $mob;
                $arr_params['pin'] = $_POST['pin'];
                $arr_params['PeriodS'] = $_POST['PeriodS'];
                $arr_params['PeriodDo'] = $_POST['PeriodDo'];
            */

            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            calendar.set(year, month, 1);
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Log.e("calendar","Текущий год "+year+" Текущий месяц "+month+" Количество дней " +maxDay);


            dayMePls = String.valueOf(maxDay);
            if (dayMePls.equals("1")){
                dayMePls = "01";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("2")){
                dayMePls = "02";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("3")){
                dayMePls = "03";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("4")){
                dayMePls = "04";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("5")){
                dayMePls = "05";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("6")){
                dayMePls = "06";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("7")){
                dayMePls = "07";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("8")){
                dayMePls = "08";
                Log.e("monthPls", dayMePls);
            }else if (dayMePls.equals("9")){
                dayMePls = "09";
                Log.e("monthPls", dayMePls);
            }


            month = month+1;
            monthPls = String.valueOf(month);
            if (monthPls.equals("1")){
                monthPls = "01";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("2")){
                monthPls = "02";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("3")){
                monthPls = "03";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("4")){
                monthPls = "04";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("5")){
                monthPls = "05";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("6")){
                monthPls = "06";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("7")){
                monthPls = "07";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("8")){
                monthPls = "08";
                Log.e("monthPls", monthPls);
            }else if (monthPls.equals("9")){
                monthPls = "09";
                Log.e("monthPls", monthPls);
            }


            periodS = String.valueOf("01"+"."+monthPls+"."+year);
            periodPo = String.valueOf(maxDay+"."+monthPls+"."+year);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("PeriodS", PerS));
            NameValuerPair.add(new BasicNameValuePair("PeriodDo", PerDo));

            Log.e("PeriodS",PerS);
            Log.e("PeriodDo",PerDo);

            //mob=+7%20777%20534%207677&pin=6020&PeriodS=01.08.2018&PeriodDo=17.09.2018

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/getMyPush.php");
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

        public void start(String mobp, String pinp, String perS, String perPo) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.PerS = perS;
            this.PerDo = perPo;
            this.start();
        }

        public String getResult() {
            return result;
        }
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
        getMenuInflater().inflate(R.menu.push, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



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

        } else if (id == R.id.nav_profil) {

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String mob = prefs.getString("mob", null);
            if (mob != null){
                Intent intentV = new Intent(this, Profile2.class);
                startActivity(intentV);
            }else{
                Intent intentV = new Intent(this, Login.class);
                startActivity(intentV);
            }

        } else if (id == R.id.nav_service) {
            Intent intentP = new Intent(this, MyServiceActivity.class);
            startActivity(intentP);
            finish();

        } else if (id == R.id.nav_prog) {
            Intent intentP = new Intent(this, Info.class);
            startActivity(intentP);

        } else if (id == R.id.nav_manager) {
            Intent intentP = new Intent(this, WorkTable.class);
            startActivity(intentP);
            finish();

        } else if (id == R.id.nav_shop) {
            Intent intentP = new Intent(this, market.class);
            startActivity(intentP);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----Push-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Push-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Push-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Push-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
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

        Log.e("-----Push-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Push-----", "onRestart");
    }
}
