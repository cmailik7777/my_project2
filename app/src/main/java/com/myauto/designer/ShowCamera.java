package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ShowCamera extends ActionBarActivity {

    private ImageSurfaceView mImageSurfaceView;

    private FrameLayout cameraPreviewLayout;
    private ImageView capturedImageHolder;
    Bitmap bitmap;
    BitmapDrawable drawable;

    private zapros3 Zapros3;
    String encoded;
    Camera camera; // camera class variable

    Button cancelCam,goodCam;
    ProgressDialog progressdialog,progressdialog1;
    String resResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cameraPreviewLayout = (FrameLayout)findViewById(R.id.camera_preview);
        capturedImageHolder = (ImageView)findViewById(R.id.captured_image);

        cancelCam = (Button) findViewById(R.id.cancelCam);
        goodCam = (Button)findViewById(R.id.goodCam);

        camera = checkDeviceCamera();
        mImageSurfaceView = new ImageSurfaceView(ShowCamera.this, camera);
        cameraPreviewLayout.addView(mImageSurfaceView);



        Button captureButton = (Button)findViewById(R.id.button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodCam.setVisibility(View.VISIBLE);
                cancelCam.setVisibility(View.VISIBLE);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        camera.takePicture(null, null, pictureCallback);
                    }
                }).start();

            }
        });

        goodCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressdialog = new ProgressDialog(ShowCamera.this);
                        progressdialog.setMessage("Пожалуйста подождите...");
                        progressdialog.setCancelable(false);
                        progressdialog.show();
                    }
                });


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        capturedImageHolder.buildDrawingCache(true);
                        bitmap = capturedImageHolder.getDrawingCache(true);

                        drawable = (BitmapDrawable)capturedImageHolder.getDrawable();
                        bitmap = drawable.getBitmap();

                        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
                        byte [] arr=baos.toByteArray();

                        String resultFOTO= Base64.encodeToString(arr, Base64.DEFAULT);

                        Log.e("bitmap","" +resultFOTO);

                        Zapros3 = new zapros3();
                        Zapros3.start(resultFOTO);

                        try {
                            Zapros3.join();
                        } catch (InterruptedException ie) {
                            Log.e("pass 0", ie.getMessage());
                            progressdialog.dismiss();
                        }
                        resResult = Zapros3.resResult();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressdialog.dismiss();
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resResult.contains("BN")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowCamera.this);
                                    builder.setTitle("Окно предупреждения!")
                                            .setMessage("Государственный номер не опознан!")
                                            .setCancelable(false)
                                            .setPositiveButton("Повторить",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    capturedImageHolder.setVisibility(View.INVISIBLE);
                                                                    cancelCam.setVisibility(View.INVISIBLE);
                                                                    goodCam.setVisibility(View.INVISIBLE);
                                                                    camera.startPreview();
                                                                }
                                                            });

                                                        }
                                                    })
                                            .setNegativeButton("Выйти",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            finish();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowCamera.this);
                                    builder.setTitle("Окно предупреждения!")
                                            .setMessage("Государственный номер опознан! Верно? "+resResult)
                                            .setCancelable(false)
                                            .setPositiveButton("Повторить",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    capturedImageHolder.setVisibility(View.INVISIBLE);
                                                                    cancelCam.setVisibility(View.INVISIBLE);
                                                                    goodCam.setVisibility(View.INVISIBLE);
                                                                    camera.startPreview();
                                                                }
                                                            });
                                                        }
                                                    })
                                            .setNegativeButton("да",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Intent intentV = new Intent(ShowCamera.this, OtkitRemz.class);
                                                            intentV.putExtra("GOSCAM", resResult);
                                                            startActivity(intentV);
                                                            finish();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        });
                        //--------------------------------------------------------------------------------------------------------------------------------------------
                    }
                }).start();


            }
        });

        cancelCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    public class zapros3 extends Thread {

        String resultFOTO;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(1);

            NameValuerPair.add(new BasicNameValuePair("image", resultFOTO));


            Log.e("Zapros3.run==","image= "+resultFOTO);


            try {
                HttpClient httpClient = new DefaultHttpClient();
                //HttpPost httpPost = new HttpPost("http://gps-monitor.kz/getimgbyte.php");
                HttpPost httpPost = new HttpPost("http://api.megamotors.kz/cgi-bin/gosnomer.php");
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
                Log.e("<><><><><>  Result  ",result+" ==  ==== )))");
            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
        }

        public void start(String resultFOTO) {
            this.resultFOTO = resultFOTO;
            this.start();
        }

        public String resResult (){
            return result;
        }
    }

    private Camera checkDeviceCamera(){
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {


             Bitmap bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

               String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //capturedImageHolder.setImageBitmap(bitmap1);
            capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap1, 480, 640));




           /* bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);


            if(bitmap==null){
                Toast.makeText(ShowCamera.this, "EMPTY", Toast.LENGTH_LONG).show();
                return;
            }*/


            //capturedImageHolder.setImageBitmap(scaleDownBitmapImage(bitmap, 1024, 800));


            //Camera.Parameters parameters = camera.getParameters();
            //int width = parameters.getPreviewSize().width;
            //int height = parameters.getPreviewSize().height;

            //YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);
            // ByteArrayOutputStream out = new ByteArrayOutputStream();
            // yuv.compressToJpeg(new Rect(0, 0, width, height), 90, out);

            //byte[] bytes = out.toByteArray();

            //Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            //final Bitmap bitmap = bitmap1;

            //capturedImageHolder.setImageBitmap(bitmap);
        }
    };

    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }


}
