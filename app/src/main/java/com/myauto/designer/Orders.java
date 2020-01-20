package com.myauto.designer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.ArrayList;

import static com.myauto.designer.Drivers.mobDr;
import static com.myauto.designer.Drivers.pinDr;

/**
 * Created by Designer2 on 26.07.2018.
 */

public class Orders extends Fragment {

    ArrayList<DataModelDriverOrders> dataModelDriverOrderses;
    ListView listView;
    private static CustomAdapterDriverOrders adapter;

    private Zapros Zapros1;

    String result;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_orders,
                container, false);

        listView = (ListView) rootView.findViewById(R.id.listview_driver_order);
        progressBar = (ProgressBar)rootView.findViewById(R.id.driver_order_progresbar);




        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listview(); // your code
                pullToRefresh.setRefreshing(false);
                listView.setAdapter(null);

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }catch (Exception e){
                    Log.e("progressBar 0", e.getMessage());
                }
            }
        });




        return rootView;
    }

    public void listview(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Zapros1 = new Zapros();
                    Zapros1.start(mobDr, pinDr);
                    Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                    Zapros1.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }catch (Exception e){
                    Log.e("progressBar 0", e.getMessage());
                }

            }
        }).start();
    }


    public void onResume() {
        super.onResume();
        listView.setAdapter(null);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Zapros1 = new Zapros();
                    Zapros1.start(mobDr, pinDr);
                    Log.e("mob + pin ---ORDERS---",mobDr+pinDr);
                    Zapros1.join();
                } catch (InterruptedException ie) {
                    Log.e("pass 0", ie.getMessage());
                }

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }catch (Exception e){
                    Log.e("progressBar 0", e.getMessage());
                }

            }
        }).start();
    }

    public class Zapros extends Thread {

        String MobS;
        String PinS;
        String Vin;

        InputStream is = null;
        String result = null;
        String line = null;


        public void run() {

            ArrayList<NameValuePair> NameValuerPair = new ArrayList<NameValuePair>(10);

            NameValuerPair.add(new BasicNameValuePair("mob", MobS));
            NameValuerPair.add(new BasicNameValuePair("pin", PinS));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://gps-monitor.kz/oleg/mobile/nd/getDriverOrders.php");
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
                dataModelDriverOrderses = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);

                String res = jsonObject.getString("res");
                if(res.equals("no")){
                    Log.e("empty","res");
                }else{
                    JSONArray jsonArray = jsonObject.getJSONArray("res");

            /*"num": "000000002",
              "data": "27.07.2018 10:54:24",*/

            /*{
                "res": {
                    "num": "ALM000000001",
                    "data": "01.08.2018 11:23:05",
                    "header": [{
                        "prefix": "AVG",
                        "logo": ""
                    }, {
                        "prefix": "CCM",
                        "logo"
                    }, {
                        "prefix": "other",
                        "logo": ""
                    }]
                }
            }*/

                    String  pref1 = "";
                    String icon1 = "";

                    String pref2 = "";
                    String icon2 = "";

                    String pref3 = "";
                    String icon3 = "";

                    String pref4 = "";
                    String icon4 = "";

                    String pref5 = "";
                    String icon5 = "";

                    String pref6 = "";
                    String icon6 = "";

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);
                        final String num = json.getString("num");
                        final String data = json.getString("data");
                        Log.e("num ",num);
                        Log.e("data ",data);

                        JSONArray ingArray = json.getJSONArray("header"); // here you are going   to get ingredients
                        for(int j=0;j<ingArray.length();j++){
                            JSONObject ingredObject = ingArray.getJSONObject(j);
                            if(j==0){
                                Log.e("j==0","");

                                pref1 = ingredObject.getString("prefix");//so you are going to get   ingredient name
                                Log.e("prefix",pref1); // you will get
                                icon1 = ingredObject.getString("logo");//so you are going to get   ingredient name
                            }else if(j==1){
                                Log.e("j==1","");

                                pref2 = ingredObject.getString("prefix");//so you are going to get   ingredient name
                                Log.e("prefix",pref2); // you will get
                                icon2 = ingredObject.getString("logo");//so you are going to get   ingredient name
                            }else if(j==2){
                                Log.e("j==2","");

                                pref3 = ingredObject.getString("prefix");//so you are going to get   ingredient name
                                Log.e("prefix",pref3); // you will get
                                icon3 = ingredObject.getString("logo");//so you are going to get   ingredient name
                            }else if(j==3){
                                Log.e("j==3","");

                                pref4 = ingredObject.getString("prefix");//so you are going to get   ingredient name
                                Log.e("prefix",pref4); // you will get
                                icon4 = ingredObject.getString("logo");//so you are going to get   ingredient name
                            }else if(j==4){
                                Log.e("j==4","");

                                pref5 = ingredObject.getString("prefix");//so you are going to get   ingredient name
                                Log.e("prefix",pref4); // you will get
                                icon5 = ingredObject.getString("logo");//so you are going to get   ingredient name
                            }else if(j==5){
                                Log.e("j==5","");

                                pref6 = ingredObject.getString("prefix");//so you are going to get   ingredient name
                                Log.e("prefix",pref6); // you will get
                                icon6 = ingredObject.getString("logo");//so you are going to get   ingredient name
                            }
                        }

                        final String finalPref = pref1;
                        final String finalIcon = icon1;
                        final String finalPref1 = pref2;
                        final String finalIcon1 = icon2;
                        final String finalPref2 = pref3;
                        final String finalIcon2 = icon3;
                        final String finalPref3 = pref4;
                        final String finalIcon3 = icon4;
                        final String finalPref4 = pref5;
                        final String finalIcon4 = icon5;
                        final String finalPref5 = pref6;
                        final String finalIcon5 = icon6;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataModelDriverOrderses.add(new DataModelDriverOrders(finalPref, finalIcon, finalPref1, finalIcon1, finalPref2, finalIcon2, finalPref3, finalIcon3, finalPref4, finalIcon4, finalPref5, finalIcon5,data,num));
                                adapter= new CustomAdapterDriverOrders(dataModelDriverOrderses,getActivity().getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView tv = (TextView) view.findViewById(R.id.driver_order_race);
                                        //Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                                        Intent ope = new Intent(getActivity(), OrderDetail.class);
                                        ope.putExtra("numOrder", tv.getText().toString());
                                        startActivity(ope);
                                    }
                                });
                            }
                        });

                    }
                }

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

        public String resOrg() {
            return result;
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e("Orders", "Fragment1 onAttach");
    }

    public void onStart() {
        super.onStart();
        Log.e("Orders", "Fragment1 onStart");
    }


    public void onPause() {
        super.onPause();
        Log.e("Orders", "Fragment1 onPause");
    }

    public void onStop() {
        super.onStop();
        Log.e("Orders", "Fragment1 onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Orders", "Fragment1 onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e("Orders", "Fragment1 onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        Log.e("Orders", "Fragment1 onDetach");
    }


}
