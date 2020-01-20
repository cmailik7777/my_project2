package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import static com.myauto.designer.Login.MY_PREFS_NAME;

public class newNews extends AppCompatActivity {

    Button add_pic,add_ico,add_news;
    ImageView ico,pic;
    EditText zagolovok,desc;

    String who = "";

    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;

    ProgressDialog progressDialog;

    private goNewNews goNewNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        who = "";

        zagolovok=(EditText)findViewById(R.id.new_news_head);
        desc=(EditText)findViewById(R.id.new_news_desc);

        ico=(ImageView)findViewById(R.id.new_news_ico);
        pic=(ImageView)findViewById(R.id.new_news_pic);

        add_ico=(Button)findViewById(R.id.new_news_add_ico);
        add_pic=(Button)findViewById(R.id.new_news_add_pic);
        add_news=(Button)findViewById(R.id.new_news_add_News);

        add_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                who = "ico";
                showPictureDialog();
            }
        });

        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                who = "pic";
                showPictureDialog();
            }
        });

        add_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(newNews.this);
                        builder.setTitle("Создать новость?")
                                .setIcon(R.drawable.iconlogo)
                                .setMessage(zagolovok.getText().toString())
                                .setCancelable(false)
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        new news().execute();
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
                }); //

            }
        });



        setTitle("Добавить новость");

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("");
        String[] pictureDialogItems = {
                "Выбрать из галереи",
                "Сделать фотографию" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(newNews.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                    if(who.equals("pic")){
                        pic.setImageBitmap(bitmap);
                    }else if(who.equals("ico")){
                        ico.setImageBitmap(bitmap);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(newNews.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            if(who.equals("pic")){
                pic.setImageBitmap(thumbnail);
            }else if(who.equals("ico")){
                ico.setImageBitmap(thumbnail);
            }

            saveImage(thumbnail);
            Toast.makeText(newNews.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    private class news extends AsyncTask<Void, Void, Void>{

        ProgressDialog pdLoading = new ProgressDialog(newNews.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Идёт загрузка...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pdLoading.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            final String mob = prefs.getString("mob", null);
            final String pin = prefs.getString("pin", null);



            //-------------------------------------------------------------------------------------------------------------------------------------------

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ico.buildDrawingCache(true);
                    Bitmap bitmap = ico.getDrawingCache(true);

                    BitmapDrawable drawable = (BitmapDrawable)ico.getDrawable();
                    bitmap = drawable.getBitmap();

                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
                    byte [] arr=baos.toByteArray();

                    String ikonkaBASE= Base64.encodeToString(arr, Base64.DEFAULT);

                    pic.buildDrawingCache(true);
                    Bitmap bitmap2 = pic.getDrawingCache(true);

                    BitmapDrawable drawable2 = (BitmapDrawable)pic.getDrawable();
                    bitmap2 = drawable2.getBitmap();

                    ByteArrayOutputStream baos2 =new  ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG,100, baos2);
                    byte [] arr2 = baos2.toByteArray();

                    String picBASE = Base64.encodeToString(arr, Base64.DEFAULT);


                    goNewNews = new goNewNews();
                    goNewNews.start(mob, pin,desc.getText().toString(),zagolovok.getText().toString(),ikonkaBASE,picBASE);

                    try {
                        goNewNews.join();
                    } catch (InterruptedException ie) {
                        Log.e("pass 0", ie.getMessage());
                    }

                    String result=goNewNews.getResult();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        final String res = jsonObject.getString("res");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(newNews.this);
                                builder.setTitle("Внимание!")
                                        .setIcon(R.drawable.iconlogo)
                                        .setMessage(res)
                                        .setCancelable(false)
                                        .setNegativeButton("Ок",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                        finish();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }); //
                        Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
                    } catch (Exception e) {
                        Log.e("Fail 4", e.toString());
                    }

                }
            });



            return null;
        }
    }

    public class goNewNews extends Thread{

        String MobS;
        String PinS;
        String textS;
        String zagolovokS;
        String fotoS;
        String inkonkaS;

        InputStream is = null;
        String result = null;
        String line = null;

    public void run() {

        ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(15);

        NameValuerPair.add(new BasicNameValuePair("mob", MobS));
        NameValuerPair.add(new BasicNameValuePair("pin", PinS));
        NameValuerPair.add(new BasicNameValuePair("text", textS));
        NameValuerPair.add(new BasicNameValuePair("zagolovok", zagolovokS));
        NameValuerPair.add(new BasicNameValuePair("foto", fotoS));
        NameValuerPair.add(new BasicNameValuePair("ikonka", inkonkaS));


        Log.e("mob", MobS);
        Log.e("PinS", PinS);
        Log.e("text", textS);
        Log.e("zagolovok", zagolovokS);

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/test/newNews.php");
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

    public void start(String mobp, String pinp, String textp, String zagolovokp, String inkonkap, String bannerp) {
        this.MobS = mobp;
        this.PinS = pinp;
        this.textS = textp;
        this.zagolovokS = zagolovokp;
        this.inkonkaS = inkonkap;
        this.fotoS = bannerp;
        this.start();
    }

    public String getResult() {
        return result;
    }
}

























}
