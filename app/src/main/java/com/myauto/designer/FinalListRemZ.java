package com.myauto.designer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FinalListRemZ extends AppCompatActivity {

    ArrayList<DataModelFinalRemz> dataModelFinalRemzs;
    ListView listView;
    private static CustomAdapterFinalRemz adapter;


    String remzRemz,predvarRemz,dataORemz,dataCRemz,VincodeRemz,NomerDvigRemz,GosRemz,OdomentrRemz,ClientRemz,TelRemz,NachRemz,OplachRemz,BalancRemz,KomentRemz,KomentManagRemz,Marka,Accept;

    TextView finalListRemzRemz,finalListRemzDatao,finalListRemzDataZ,finalListRemzVin,finalListRemzNomerDvig,finalListRemzGos,finalListRemzOdometr,finalListRemzClient,finalListRemzTel,finalListRemzNachis,finalListRemzOplach,finalListRemzBalanc,finalListRemzKomment,finalListRemzKommentManag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_list_rem_z);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Список рем. заказов");

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        finalListRemzRemz=(TextView)findViewById(R.id.FinalListRemzRemz);
        finalListRemzDatao=(TextView)findViewById(R.id.FinalListRemzDatao);
        finalListRemzDataZ=(TextView)findViewById(R.id.FinalListRemzDataZ);
        finalListRemzVin=(TextView)findViewById(R.id.FinalListRemzVin);
        finalListRemzNomerDvig=(TextView)findViewById(R.id.FinalListRemzNomerDvig);
        finalListRemzGos=(TextView)findViewById(R.id.FinalListRemzGos);
        finalListRemzOdometr=(TextView)findViewById(R.id.FinalListRemzOdometr);
        finalListRemzClient=(TextView)findViewById(R.id.FinalListRemzClient);
        finalListRemzTel=(TextView)findViewById(R.id.FinalListRemzTel);
        finalListRemzNachis=(TextView)findViewById(R.id.FinalListRemzNachis);
        finalListRemzOplach=(TextView)findViewById(R.id.FinalListRemzOplach);
        finalListRemzBalanc=(TextView)findViewById(R.id.FinalListRemzBalanc);
        finalListRemzKomment=(TextView)findViewById(R.id.FinalListRemzKomment);
        finalListRemzKommentManag=(TextView)findViewById(R.id.FinalListRemzKommentManag);

        Intent intent = getIntent();

        String resResult = intent.getStringExtra("result");

        Log.e("--Проверка массива---", " добавил "+ resResult +" массив ");
        listView=(ListView)findViewById(R.id.listFinalRemZ);

//{"rz":[
// {"num":"ALM0093023",
// "date_open":"21.10.2016 12:13:03",
// "date_close":"24.10.2016 10:31:50",
// "vin":"JTNBE40K403068220",
// "dvig":"2AZ2455917",
// "gos":"602DNA02",
// "km":"124В 387",
// "client":"РќРёРєРёС‚РёРЅ Р”РјРёС‚СЂРёР№",
// "tel":"+7 777 534 7677",
// "sum_opl":"9В 035",
// "sum_k_opl":"9В 035",
// "balance":"0",
// comment":"",
// "commrnt_manager":"",
// "mark":"Toyota",
// "manager":"РР·РјР°Р№Р»РѕРІ Р®СЂРёР№"}


        try {
            dataModelFinalRemzs = new ArrayList<>();


            JSONObject jsonObject = new JSONObject(resResult);
            JSONArray jsonArray = jsonObject.getJSONArray("rz");


            for(int i = 0; i < jsonArray.length(); i++){
                String num = jsonArray.getJSONObject(i).getString("num");
                Log.e("num ",num);

                String date_open = jsonArray.getJSONObject(i).getString("date_open");
                Log.e("date_open ",date_open);

                String date_close = jsonArray.getJSONObject(i).getString("date_close");
                Log.e("date_close ",date_close);
                if (date_close.equals("01.01.0001 0:00:00")){
                    date_close = "Открыт";
                }

                String vin = jsonArray.getJSONObject(i).getString("vin");
                Log.e("vin ",vin);

                String gos = jsonArray.getJSONObject(i).getString("gos");
                Log.e("gos ",gos);

                String dvig = jsonArray.getJSONObject(i).getString("dvig");
                Log.e("dvig ",dvig);

                String km = jsonArray.getJSONObject(i).getString("km");
                Log.e("km ",km);

                String client = jsonArray.getJSONObject(i).getString("client");
                Log.e("client ",client);

                String tel = jsonArray.getJSONObject(i).getString("tel");
                Log.e("tel ",tel);

                String sum_opl = jsonArray.getJSONObject(i).getString("sum_opl");
                Log.e("sum_opl ",sum_opl);

                String sum_k_opl = jsonArray.getJSONObject(i).getString("sum_k_opl");
                Log.e("sum_k_opl ",sum_k_opl);

                String balance = jsonArray.getJSONObject(i).getString("balance");
                Log.e("balance ",balance);

                String comment = jsonArray.getJSONObject(i).getString("comment");
                Log.e("comment ",comment);

                String commrnt_manager = jsonArray.getJSONObject(i).getString("commrnt_manager");
                Log.e("commrnt_manager ",commrnt_manager);

                String mark = jsonArray.getJSONObject(i).getString("mark");
                Log.e("mark ",mark);

                String manager = jsonArray.getJSONObject(i).getString("manager");
                Log.e("manager ",manager);

                dataModelFinalRemzs.add(new DataModelFinalRemz(num,date_open,date_close,vin,dvig,gos,km,client,tel,sum_opl,sum_k_opl,balance,comment,commrnt_manager,mark,manager));


                adapter= new CustomAdapterFinalRemz(dataModelFinalRemzs,getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view.findViewById(R.id.FinalListRemzRemz);
                        TextView tv2 = (TextView) view.findViewById(R.id.FinalListRemzVin);
                        TextView tv3 = (TextView) view.findViewById(R.id.FinalListRemzNomerDvig);
                        TextView tv4 = (TextView) view.findViewById(R.id.FinalListRemzGos);
                        TextView tv5 = (TextView) view.findViewById(R.id.FinalListRemzOdometr);
                        TextView tv6 = (TextView) view.findViewById(R.id.FinalListRemzClient);
                        TextView tv7 = (TextView) view.findViewById(R.id.FinalListRemzTel);

                        TextView tv8 = (TextView) view.findViewById(R.id.FinalListRemzMarka);
                        TextView tv9 = (TextView) view.findViewById(R.id.FinalListRemzAccept);

                        Intent ope = new Intent(FinalListRemZ.this, DetailRemz.class);
                        ope.putExtra("finalListRemzRemz", tv.getText().toString());
                        ope.putExtra("finalListRemzVin", tv2.getText().toString());
                        ope.putExtra("finalListRemzNomerDvig", tv3.getText().toString());
                        ope.putExtra("finalListRemzGos", tv4.getText().toString());
                        ope.putExtra("finalListRemzOdometr", tv5.getText().toString());
                        ope.putExtra("finalListRemzClient", tv6.getText().toString());
                        ope.putExtra("finalListRemzTel", tv7.getText().toString());

                        ope.putExtra("finalListMarkaAuto", tv8.getText().toString());
                        ope.putExtra("finalListAcceptAuto", tv9.getText().toString());
                        startActivity(ope);

                        Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();




                        //Intent ope = new Intent(mmrpit.this, Gos.class);
                        //ope.putExtra("result", result);
                        //ope.putExtra("GosNomer", tv.getText().toString());
                        //startActivity(ope);


                        //TextView tv = (TextView)findViewById(R.id.FinalListRemzRemz);
                        //Toast.makeText(getApplicationContext(), finalListRemzRemz.getText().toString(), Toast.LENGTH_SHORT).show();
                            /*Intent ope = new Intent(FinalListRemZ.this, Gos.class);
                                ope.putExtra("finalListRemzRemz", finalListRemzRemz.getText().toString());
                                ope.putExtra("finalListRemzVin", finalListRemzVin.getText().toString());
                                ope.putExtra("finalListRemzNomerDvig", finalListRemzNomerDvig.getText().toString());
                                ope.putExtra("finalListRemzGos", finalListRemzGos.getText().toString());
                                ope.putExtra("finalListRemzOdometr", finalListRemzOdometr.getText().toString());
                                ope.putExtra("finalListRemzClient", finalListRemzClient.getText().toString());
                                ope.putExtra("finalListRemzTel", finalListRemzTel.getText().toString());
                            startActivity(ope);*/
                    }
                });
            }
            Log.e("<><><><><>  Result  ", resResult+ " ==  ==== )))");
        } catch (Exception e){
            Log.e("Fail 3333", e.toString());
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
        Log.e("-----FinalListRemZ-----", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("-----FinalListRemZ-----", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.e("-----FinalListRemZ-----", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("-----FinalListRemZ-----", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.e("-----FinalListRemZ-----", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("-----ListRemZ-----", "onRestart");
    }

}
