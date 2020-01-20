package com.myauto.designer;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;

public class detail_news extends AppCompatActivity {

    TextView head,desc,otvet,mob;
    ImageView banner;
    Button insta,call,message;
    ProgressBar progressBar;

    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        banner = (ImageView) findViewById(R.id.imageView14);

        head = (TextView)findViewById(R.id.see_ad_head);
        desc = (TextView)findViewById(R.id.see_ad_desc);
        otvet = (TextView)findViewById(R.id.see_ad_otvet);
        mob = (TextView)findViewById(R.id.see_ad_mob);

        insta = (Button)findViewById(R.id.angry_btn);
        call = (Button)findViewById(R.id.btn_call);
        message = (Button)findViewById(R.id.btn_message);

        progressBar = (ProgressBar)findViewById(R.id.see_ad_progressbar);



        setTitle("");

        Intent intent = getIntent();

        String headS = intent.getStringExtra("head");
        String textS = intent.getStringExtra("desc");




        final String imageS = intent.getStringExtra("image");


        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);
        new DownloadImageTask(banner)
                .execute(imageS);



        String otvetS = intent.getStringExtra("otvet");
        final String mobS = intent.getStringExtra("mob");
        final String instaS = intent.getStringExtra("insta");


        byte[] decodedString = Base64.decode(imageS, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        banner.setImageBitmap(decodedByte);

        head.setText(headS);
        desc.setText(textS);
        otvet.setText(otvetS);
        mob.setText(mobS);



        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/"+instaS);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/")));
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comeCall(mobS);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String trim = mobS.replaceAll(" ", "");
                Log.e("trim",trim);

                try {
                    PackageManager packageManager = view.getContext().getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone="+ trim;
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        view.getContext().startActivity(i);
                    }
                } catch (final Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(detail_news.this);
                            builder.setTitle("Ошибка!")
                                    .setMessage("Не удалось открыть Whats'App \n с номером:" +
                                            "\n" +trim+
                                            ""+e)
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
        });

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
                progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_READ_PHONE_STATE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.

            } else {

            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void comeCall(String tel) {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + tel));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_PHONE_STATE)) {
            ActivityCompat.requestPermissions(detail_news.this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_REQUEST_READ_PHONE_STATE);

        }else{
            ActivityCompat.requestPermissions(detail_news.this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_REQUEST_READ_PHONE_STATE);
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
        Log.e("-----SeeAd-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----SeeAd-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("-----SeeAd-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----SeeAd-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----SeeAd-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----SeeAd-----", "onRestart");
    }

}
