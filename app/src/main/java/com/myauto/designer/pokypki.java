package com.myauto.designer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;


public class pokypki extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progressDialog;
    String checkprog;

    int Numboftabs = 2;
    Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    View holderView, contentView;
    MenuItem target;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokypki);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//старый drawer
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        holderView = findViewById(R.id.holderPokupki);
        contentView = findViewById(R.id.contentPokupki);
        toolbar.setNavigationIcon(new DrawerArrowDrawable(this));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (drawer.isDrawerOpen(navigationView)) {
                                                         drawer.closeDrawer(navigationView);
                                                     } else {
                                                         drawer.openDrawer(navigationView);
                                                     }
                                                 }
                                             }
        );


        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawer, float slideOffset) {


                                         contentView.setX(navigationView.getWidth() * slideOffset);
                                         RelativeLayout.LayoutParams lp =
                                                 (RelativeLayout.LayoutParams) contentView.getLayoutParams();
                                         lp.height = drawer.getHeight() -
                                                 (int) (drawer.getHeight() * slideOffset * 0.3f);
                                         lp.topMargin = (drawer.getHeight() - lp.height) / 2;
                                         contentView.setLayoutParams(lp);
                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                     }
                                 }
        );
        Menu menu =navigationView.getMenu();

        target = menu.findItem(R.id.nav_manager);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pokypki, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //   return true;
        // }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_garaj) {

            progressDialog = new ProgressDialog(this, R.style.AppTheme);
            progressDialog.setMessage("Пожалуйста подождите....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            checkprog="1";
            Intent intentP = new Intent(this, garaj.class);
            startActivity(intentP);

            finish();
        } else if (id == R.id.nav_main) {
            progressDialog = new ProgressDialog(this, R.style.AppTheme);
            progressDialog.setMessage("Пожалуйста подождите....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            checkprog="1";
            Intent intentV = new Intent(this, Home.class);
            startActivity(intentV);
            finish();


        } else if (id == R.id.nav_push) {

            progressDialog = new ProgressDialog(this, R.style.AppTheme);
            progressDialog.setMessage("Пожалуйста подождите....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            checkprog="1";
            Intent intentV = new Intent(this, push.class);
            startActivity(intentV);
            finish();
        } else if (id == R.id.nav_profil) {

            progressDialog = new ProgressDialog(this, R.style.AppTheme);
            progressDialog.setMessage("Пожалуйста подождите....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            checkprog="1";
            Intent intentP = new Intent(this, Profile2.class);
            startActivity(intentP);

            finish();
        } else if (id == R.id.nav_manager) {
            Intent intentP = new Intent(this, WorkTable.class);
            startActivity(intentP);
            finish();
        } else if (id == R.id.nav_prog) {

            progressDialog = new ProgressDialog(this, R.style.AppTheme);
            progressDialog.setMessage("Пожалуйста подождите....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            checkprog="1";
            Intent intentP = new Intent(this, About1.class);
            startActivity(intentP);

            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void mmprit(View view) {
        progressDialog = new ProgressDialog(this, R.style.AppTheme);
        progressDialog.setMessage("Пожалуйста подождите....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        checkprog = "1";
        Intent intentP = new Intent(this, mmrpit.class);
        startActivity(intentP);

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("=========Pokypki======", "onPause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("=========Pokypki======", "onResume");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("=========Pokypki======", "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();




        Log.e("=========Pokypki======", "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(Login.MY_PREFS_NAME, MODE_PRIVATE);
        String sotr = prefs.getString("Sotr", null);

        if(sotr!=null){
            if(sotr.equals("Empty")){
                target.setVisible(false);
            }
        } else {
            target.setVisible(false);
        }

        if(checkprog=="1"){
            progressDialog.dismiss();
            checkprog="0";
        }

        Log.e("=========Pokypki======", "onStart");
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e("=========Pokypki======", "onRestart");
    }




    public void silkworm(View view) {
        progressDialog = new ProgressDialog(this, R.style.AppTheme);
        progressDialog.setMessage("Пожалуйста подождите....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        checkprog = "1";
        Intent intentP = new Intent(this, silkworm.class);
        startActivity(intentP);
    }

    public void truck(View view) {
        progressDialog = new ProgressDialog(this, R.style.AppTheme);
        progressDialog.setMessage("Пожалуйста подождите....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        checkprog = "1";
        Intent intentP = new Intent(this, trucksolutions.class);
        startActivity(intentP);
    }
}
