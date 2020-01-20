package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;

public class InviteNewUser extends AppCompatActivity {

    private invite Invite;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_new_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final EditText editText = (EditText) findViewById(R.id.Invite_Mob);
        editText.addTextChangedListener(new TextWatcher()
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
                String text = editText.getText().toString();
                int  textLength = editText.getText().length();
                if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" "))
                    return;
                if (textLength == 1) {
                    if (!text.contains("+"))
                    {
                        editText.setText(new StringBuilder(text).insert(text.length() - 1, "+").toString());
                        editText.setSelection(editText.getText().length());
                    }
                }
                else if (textLength == 2)
                {
                    editText.setText(new StringBuilder(text).insert(text.length() - 1, "7").toString());
                    editText.setSelection(editText.getText().length());
                }
                else if (textLength == 3)
                {
                    editText.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    editText.setSelection(editText.getText().length());
                }
                else if (textLength == 7)
                {
                    editText.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    editText.setSelection(editText.getText().length());
                }
                else if (textLength == 11)
                {
                    editText.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    editText.setSelection(editText.getText().length());
                }
            }
        });

    }


    public void Invite (View view){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Пожалуйста подождите....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
                String mob = prefs.getString("mob", null);
                String pin = prefs.getString("pin", null);

                EditText tv = (EditText) findViewById(R.id.Invite_Mob);

                String inviteMob = tv.getText().toString();
                Invite = new invite();
                Invite.start(mob, pin,inviteMob);

                try {
                    Invite.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
            }
        }).start();
    }

    public class invite extends Thread {

        String MobS;
        String PinS;
        String InviteMob;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            NameValuerPair.add(new BasicNameValuePair("mobInvite", InviteMob));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/inviteNewUser.php");
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(InviteNewUser.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(InviteNewUser.this);
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
                String res = jsonObject.getString("res");
                Log.e("res", "Результат: res " + res);

                if (res.equals("ok")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(InviteNewUser.this);
                            builder.setTitle("Успешно!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Приглашение успешно отправлено!")
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InviteNewUser.this);
                            builder.setTitle("Ошибка!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Произошла ошибка отправки приглашения!\n" +
                                            "Пожалуйста повторите ввод позже!")
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

                Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(InviteNewUser.this);
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


        public void start(String mobp, String pinp, String inviteMob) {
            this.MobS = mobp;
            this.PinS = pinp;
            this.InviteMob = inviteMob;
            this.start();
        }

        public String resOrg() {
            return result;
        }

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
        Log.e("-----InviteNewUser-----", "onPause");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----InviteNewUser-----", "onResume");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----InviteNewUser-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----InviteNewUser-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----InviteNewUser-----", "onStart");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----InviteNewUser-----", "onRestart");
    }



}
