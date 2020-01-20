package com.myauto.designer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile2 extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Button adminbutton;
    String login;

    TextView sotrT,newNews,moderatorS;
    ImageView imgSotr,imgAddNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Личный кабинет");

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        newNews = (TextView) findViewById(R.id.lknewNews);
        moderatorS = (TextView)findViewById(R.id.lkModerator);
        imgAddNews = (ImageView) findViewById(R.id.imageView19);

        SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("name", null);
        String mob = prefs.getString("mob", null);
        String ava = prefs.getString("ava", null);
        String sotr = prefs.getString("ava", null);
        String moderator = prefs.getString("moderator", null);

        if (moderator==null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newNews.setVisibility(View.VISIBLE);
                    imgAddNews.setVisibility(View.VISIBLE);

                    moderatorS.setVisibility(View.VISIBLE);
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newNews.setVisibility(View.INVISIBLE);
                    imgAddNews.setVisibility(View.INVISIBLE);

                    moderatorS.setVisibility(View.INVISIBLE);
                }
            });
        }

        newNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentV = new Intent(Profile2.this, newNews.class);
                startActivity(intentV);
            }
        });

        if (ava==null){}else{
            byte[] decodedString = Base64.decode(ava, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            CircleImageView lkPhoto = (CircleImageView)findViewById(R.id.lkPhoto);
            lkPhoto.setImageBitmap(decodedByte);
        }

        TextView imya = (TextView) findViewById(R.id.lkName);
        TextView tel = (TextView) findViewById(R.id.lkPhone);
        tel.setText(mob);
        imya.setText(name);

        //new AsyncRetrieve().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void goToSotrinfo(View view){
        Intent intentV = new Intent(this, SotrInfo.class);
        startActivity(intentV);
    }

    public void Exit(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile2.this);
        builder.setTitle("Окно предупреждения!")
                .setMessage("Вы действительно хотите выйти из учетной записи?")
                .setCancelable(false)
                .setIcon(R.drawable.iconlogo)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor editor = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("id", null);
                        editor.putString("name", null);

                        editor.putString("mob", null);
                        editor.putString("pin", null);

                        editor.putString("f", null);
                        editor.putString("i", null);
                        editor.putString("o", null);
                        editor.putString("dr", null);

                        editor.putString("ava", null);
                        editor.putString("manager_service", null);
                        editor.putString("driver", null);
                        editor.putString("mehanic", null);
                        editor.putString("moderator", null);

                        editor.putString("org", null);
                        editor.putString("orgLogo", null);
                        editor.putString("orgSize", null);

                        editor.apply();
                        finish();
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


    public void InviteNewUser(View view){
        Intent intentV = new Intent(this, InviteNewUser.class);
        startActivity(intentV);
    }

    public void Wheels(View view){
        Intent intentV = new Intent(this, wheels.class);
        startActivity(intentV);
    }

    public void Zapchasti(View view){
        Intent intentV = new Intent(this, MyAd.class);
        startActivity(intentV);
    }

    /**SharedPreferences.Editor editor = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE).edit();
     editor.putString("Login", null);
     editor.putString("Password", null);
     editor.putString("Name", null);
     editor.putString("Id", null);
     editor.putString("Sotr", null);
     editor.putString("Dont", "1");
     editor.apply();*/



    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----Profile2-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Profile2-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Profile2-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Profile2-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----Profile2-----", "onStart");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Profile2-----", "onRestart");
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

                }else{
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
            if (result.equals("1.13")) {
                Log.e("Version check"," Yes 1.13 version");
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Profile2.this);
                        builder.setTitle("Обновление")
                                .setIcon(R.drawable.ic_launcher_round)
                                .setMessage("Вышло новое обновление приложения!\n \nТекущая версия 1.13\nНовая версия приложения "+result)
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
