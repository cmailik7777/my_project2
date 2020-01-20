package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Profile extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
        String login = prefs.getString("Login", null);
        String password = prefs.getString("Password", null);
        String name = prefs.getString("Name", null);
        String id = prefs.getString("Id", null);

        if(login!=null){
            setTitle(login);
            TextView imya = (TextView) findViewById(R.id.ProfImya);
            TextView tel = (TextView) findViewById(R.id.ProfTel);
            TextView iden = (TextView) findViewById(R.id.ProfId);
            tel.setText(login);
            imya.setText(name);
            iden.setText(id);
            Toast.makeText(getApplicationContext(), "Ты уже тут *"+ login+"*",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----Profile-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Profile-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Profile-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Profile-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----Profile-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Profile-----", "onRestart");
    }


    public void onDelProf(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("Login", null);
        editor.putString("Password", null);
        editor.putString("Name", null);
        editor.putString("Id", null);
        editor.apply();
        finish();
    }
}
