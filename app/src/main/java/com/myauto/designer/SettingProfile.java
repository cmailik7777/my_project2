package com.myauto.designer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;


public class SettingProfile extends AppCompatActivity {

    Switch Switch1;
    // Идентификатор уведомления
    protected static final int NOTIFY_ID = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Switch1 = (Switch) findViewById(R.id.switch1);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
        String login = prefs.getString("Login", null);
        String password = prefs.getString("Password", null);
        String name = prefs.getString("Name", null);
        String id = prefs.getString("Id", null);
        if(login!=null){
            setTitle("Настройки");
            toolbar.setSubtitle(login);

        }


    }



    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----Setting-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Setting-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Setting-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Setting-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----Setting-----", "onStart");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Setting-----", "onRestart");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void enterSetting(View view) {
        if (Switch1.isChecked()){
            Toast.makeText(getApplicationContext(), "Включена", Toast.LENGTH_SHORT).show();
            Intent notificationIntent = new Intent(this, Home.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Resources res = this.getResources();

            // до версии Android 8.0 API 26
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setContentIntent(contentIntent)
                    // обязательные настройки
                    .setSmallIcon(R.drawable.af1)
                    //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                    .setContentTitle("Напоминание")
                    //.setContentText(res.getString(R.string.notifytext))
                    .setContentText("Пора менять масло") // Текст уведомления
                    // необязательные настройки
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.af1)) // большая
                    // картинка
                    //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                    .setTicker("Последнее китайское предупреждение!")
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true); // автоматически закрыть уведомление после нажатия

            Notification notification = builder.build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // Альтернативный вариант
            // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFY_ID, notification);
        }else{
            Toast.makeText(getApplicationContext(), "неа", Toast.LENGTH_SHORT).show();
        }
    }
}
