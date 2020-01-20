package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

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

public class Login extends AppCompatActivity {


    private EditText editMobP;
    private EditText editPP;

    Button button3;

    private EditText PostIDP;
    private EditText PostLOGINP;
    private EditText PostMOBP;
    private EditText PostPINP;

    private String MobS;
    private String PinS;

    private String PostIDS,PostLOGINS,PostCodeS,PostFS,PostIS,PostOS,PostAvaS,PostdrS,PostRoleS,PostDriverS,PostErrorS,PostMehanicS,PostModeratorS;

    private  String login;
    private  String password;
    private  String name;
    private  String id;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static String dont;

    ProgressDialog progressDialog;


    private zapros Zapros1;

    String checkprog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle("Авторизация");


        editMobP = (EditText) findViewById(R.id.editMob);
        editPP = (EditText) findViewById(R.id.editP);

        PostIDP = (EditText) findViewById(R.id.PostID);
        PostLOGINP = (EditText) findViewById(R.id.PostLOGIN);
        PostMOBP = (EditText) findViewById(R.id.PostMOB);
        PostPINP = (EditText) findViewById(R.id.PostP);

        /*if(login==""){
            Toast.makeText(getApplicationContext(), "Нету  "+login,
                    Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(), "Приветствую вас"+login,
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, EnterActivity.class);
            intent.putExtra("fname", loginEdit.getText().toString());
            intent.putExtra("lname", passwordEdit.getText().toString());
            startActivity(intent);
        }*/


        final EditText editText = (EditText) findViewById(R.id.editMob);
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

    public void register(View view){
        Intent intentP = new Intent(this, Registration.class);
        startActivity(intentP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void OnClick(View view) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Пожалуйста подождите....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String token = FirebaseInstanceId.getInstance().getToken();
                Log.e("Firebase", "token "+ token);
                MobS = editMobP.getText().toString();
                PinS = editPP.getText().toString();
                Zapros1 = new zapros();
                Zapros1.start(MobS, PinS,token);


                try {
                    Zapros1.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }
                PostErrorS = Zapros1.resPostErrorP();

                if (PostErrorS.equals("empty")){

                }else{
                    PostIDS = Zapros1.resID();
                    PostLOGINS = Zapros1.resLOGIN();
                    PostCodeS = Zapros1.resSotr();
                    PostFS = Zapros1.resPostF();
                    PostIS = Zapros1.resPostI();
                    PostOS = Zapros1.resPostO();

                    PostAvaS = Zapros1.resPostAva();
                    PostdrS = Zapros1.resPostdr();

                    PostRoleS = Zapros1.resPostRoleP();
                    PostDriverS = Zapros1.resPostDriverP();
                    PostDriverS = Zapros1.resPostDriverP();
                    PostMehanicS = Zapros1.getPostMehanicP();

                    PostModeratorS = Zapros1.getPostModeratorS();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("id", PostIDS);
                            editor.putString("name", PostLOGINS);

                            editor.putString("mob", editMobP.getText().toString());
                            editor.putString("pin", editPP.getText().toString());

                            editor.putString("f", PostFS);
                            editor.putString("i", PostIS);
                            editor.putString("o", PostOS);
                            editor.putString("dr", PostdrS);

                            editor.putString("ava", PostAvaS);
                            editor.putString("manager_service", PostRoleS);
                            editor.putString("driver", PostDriverS);
                            editor.putString("mehanic", PostMehanicS);

                            editor.putString("code", PostCodeS);

                            editor.putString("moderator", PostModeratorS);
                            editor.apply();



                        /*editor.putString("Login", login);
                        editor.putString("Password", password);
                        editor.putString("Name", name);
                        editor.putString("Id", id);
                        editor.putString("SotrName", PostSotrName);
                        editor.putString("Sotr", PostSotr);
                        editor.putString("Dont", dont);*/


                            //SharedPreferences settings = getPreferences(MODE_PRIVATE);
                            //SharedPreferences.Editor editor = settings.edit();

                    /* Сохраняем данные. Если этого не сделать – ничего не будет сохранено */
                            //editor.commit();

                            progressDialog.dismiss();
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle("")
                                    .setMessage("Добро пожаловать, \n"+PostLOGINS)
                                    .setCancelable(false)
                                    .setNegativeButton("ок",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                                    String manager_service = prefs.getString("manager_service", null);

                                                    if (manager_service!=null){
                                                        dialog.cancel();
                                                        Intent intentP = new Intent(Login.this, WorkTable.class);
                                                        startActivity(intentP);
                                                        finish();
                                                    }else{
                                                        dialog.cancel();
                                                        Intent intentP = new Intent(Login.this, Profile2.class);
                                                        startActivity(intentP);
                                                        finish();
                                                    }
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });

                }




            }
        }).start();






    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("-----Login-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----Login-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----Login-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----Login-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----Login-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----Login-----", "onRestart");
    }


    public void OnDel(View view) {

    }


    public class zapros extends Thread {


        String MobS;
        String PinS;
        String TokenS;

        String PostIDP = "empty";
        String PostLOGINP = "empty";
        String PostMOBP = "empty";
        String PostPP = "empty";
        String PostCodeP = "empty";
        String PostFP = "empty";
        String PostIP = "empty";
        String PostOP = "empty";
        String PostAvaP = "empty";

        String PostdrP = "empty";
        String PostRoleP = "empty";
        String PostDriverP = "empty";
        String PostMehanicP = "empty";
        String PostModeratorS = "empty";
        String PostErrorP = "";

        InputStream is = null;
        String result = null;
        String line = null;


        public void run(){

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(6);

            NameValuerPair.add(new BasicNameValuePair("mob",MobS));
            NameValuerPair.add(new BasicNameValuePair("pin",PinS));
            NameValuerPair.add(new BasicNameValuePair("tokenAPNS",TokenS));

            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/logon.php");
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
                	"id": "000000001",
                    "name": "РќРёРєРёС‚РёРЅ Р”РјРёС‚СЂРёР№",
                    "code": "000000001",
                    "f": "РќРёРєРёС‚РёРЅ",
                    "i": "Р”РјРёС‚СЂРёР№",
                    "o": "",
                    "dr": "01.01.0001 0:00:00",
                    "ava": "/9
                */

                PostErrorP = jsonObject.getString("ERROR");
                if (PostErrorP.equals("empty")){ // не правильный пин ИЛИ моб
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle("Окно предупреждения!")
                                    .setIcon(R.drawable.iconlogo)
                                    .setMessage("Введен неправильный мобильный телефон или пин код." +
                                            "\nПожалуйста повторите ввод.")
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
                    PostIDP = jsonObject.getString("id");
                    Log.e("json", "Результат: " + PostIDP);

                    PostLOGINP = jsonObject.getString("name");
                    Log.e("json", "Результат: " + PostLOGINP);

                    PostCodeP = jsonObject.getString("code");
                    Log.e("json", "Результат: " + PostCodeP);

                    PostFP = jsonObject.getString("f");
                    if (PostFP.equals("")){
                        PostFP = "не указано";
                    }
                    Log.e("json", "Результат: " + PostFP);

                    PostIP = jsonObject.getString("i");
                    if (PostIP.equals("")){
                        PostIP = "не указано";
                    }
                    Log.e("json", "Результат: " + PostFP);

                    PostOP = jsonObject.getString("o");
                    if (PostOP.equals("")){
                        PostOP = "не указано";
                    }
                    Log.e("json", "Результат: " + PostOP);

                    PostdrP = jsonObject.getString("dr");
                    if (PostdrP.equals("01.01.0001 0:00:00")){
                        PostdrP = "не указано";
                    }
                    Log.e("json", "Результат: " + PostdrP);

                    PostAvaP = jsonObject.getString("ava");
                    if (PostAvaP.equals("")){
                        PostAvaP = "нет фото";
                    }
                    Log.e("json", "Результат: " + PostAvaP);


                    /*"roles": [{
                        "role": "manager_service"
                    }],*/

                    JSONArray jsonArray = jsonObject.getJSONArray("roles");
                    Log.e("jsonArray", "Результат: jsonArray " + jsonArray);


                    if (jsonArray.length() > 0 && jsonArray!=null){
                        Log.e("lol", "if (jsonArray.length() > 0 && jsonArray!=null)");
                        for(int i = 0; i < jsonArray.length(); i++){
                            String role = jsonArray.getJSONObject(i).getString("role");
                            Log.e("lol", "Результат: role " + role);

                            if(role.equals("manager_service")){
                                PostRoleP = role;
                                Log.e("lol", "Результат: PostRoleP " + PostRoleP);
                            }else{
                                if(PostRoleP.equals("manager_service")){

                                }else {
                                    PostRoleP = null;
                                }

                            }

                            if(role.equals("driver")){
                                PostDriverP = role;
                                Log.e("lol", "Результат: PostDriverP " + PostDriverP);
                            }else{
                                if(PostDriverP.equals("driver")){

                                }else {
                                    PostDriverP = null;
                                }
                            }

                            if(role.equals("mehanic")){
                                PostMehanicP = role;
                                Log.e("lol", "Результат: PostMehanicP " + PostMehanicP);
                            }else{
                                if(PostMehanicP.equals("mehanic")){

                                }else {
                                    PostMehanicP = null;
                                }
                            }

                            if(role.equals("moderator")){
                                PostModeratorS = role;
                                Log.e("lol", "Результат: PostModeratorS " + PostModeratorS);
                            }else{
                                if(PostModeratorS.equals("moderator")){

                                }else {
                                    PostModeratorS = null;
                                }
                            }

                        }
                    }else {
                        PostRoleP = null;
                        PostDriverP = null;

                        Log.e("lol", "if (jsonArray.length() < 0 && jsonArray=null)");
                        Log.e("lol", "Результат: PostDriverP " + PostDriverP);
                        Log.e("lol", "Результат: PostRoleP " + PostRoleP);
                    }




                }

               /* PostIDP = json_data.getJSONObject("name");
                PostLOGINP = json_data.getJSONArray(0).get(1).toString();
                PostMOBP = json_data.getJSONArray(0).get(2).toString();
                PostPP = json_data.getJSONArray(0).get(3).toString();
                PostSotrName = json_data.getJSONArray(0).get(4).toString();
                PostSotr = json_data.getJSONArray(0).get(5).toString();
                Log.e("pass 3", PostSotr);*/
            } catch (Exception e){
                Log.e("Fail 3", e.toString());
            }





        }


        public void start(String mobp, String pinp, String token){

            this.MobS = mobp;
            this.PinS = pinp;
            this.TokenS = token;
            this.start();


        }
        public String resID (){
            return PostIDP;
        }
        public String resLOGIN (){
            return PostLOGINP;
        }
        public String resSotr (){
            return PostCodeP;
        }
        public String resPostF (){
            return PostFP;
        }
        public String resPostI (){
            return PostIP;
        }
        public String resPostO (){
            return PostOP;
        }
        public String resPostAva (){
            return PostAvaP;
        }
        public String resPostdr (){
            return PostdrP;
        }
        public String resPostRoleP (){
            return PostRoleP;
        }
        public String resPostDriverP (){
            return PostDriverP;
        }
        public String resPostErrorP (){
            return PostErrorP;
        }

        public String getPostMehanicP() {
            return PostMehanicP;
        }

        public String getPostModeratorS() {
            return PostModeratorS;
        }
    }


}
