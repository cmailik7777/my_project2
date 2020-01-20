package com.myauto.designer;

import android.content.Context;
import android.util.Log;

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

/**
 * Created by Designer2 on 03.08.2018.
 */

public class zaprosOpenDriverOrders extends Thread {

    String MobS;
    String PinS;
    String NumS;

    String res;

    InputStream is = null;
    String result = null;
    String line = null;

    private Context context;


    public void run() {
        this.context=context;
        ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

        NameValuerPair.add(new BasicNameValuePair("mob", MobS));
        NameValuerPair.add(new BasicNameValuePair("pin", PinS));
        NameValuerPair.add(new BasicNameValuePair("num", NumS));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/nd/setDriverOrder.php");
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
            JSONObject jsonObject = new JSONObject(result);

            res = jsonObject.getString("res");
            Log.e("json", "Результат: res" + res);

            Log.e("<><><><><>  Result  ", result + " ==  ==== )))");
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
    }


    public void start(String mobp, String pinp, String num) {
        this.MobS = mobp;
        this.PinS = pinp;
        this.NumS = num;
        this.start();
    }

    public String resOrg() {
        return res;
    }
}
