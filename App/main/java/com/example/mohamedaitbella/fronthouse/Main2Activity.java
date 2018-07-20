package com.example.mohamedaitbella.fronthouse;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    JSONArray result;
    APICall apiCall = new APICall();
    String url = "http://knightfinder.com/WEBAPI/GetSchedule.aspx";
    int userId = -1;
    private DrawerLayout drawer;

    @Override
    protected void onStart() {

        if(userId < 1 && getIntent().getIntExtra("userId", 0) < 1){
            Log.d("STOP", "Here");
            startActivity(new Intent(this, MainActivity.class));
        }

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Toolbar
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_Layout);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        userId = getIntent().getIntExtra("userId", 0);

        String payload = "";

        try {
            result = apiCall.execute(url, payload).get();
        }catch (Exception e){
            Log.d("SchedCall", e.getMessage());
        }

        ArrayList<String> am_shifts = new ArrayList<>();
        ArrayList<String> pm_shifts = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();

        //--------------- Add shifts here -----------------------------------
        for(int i = 0; i < result.length(); i++){
            try {
                // -------------- Still need to edit ----------------------
                am_shifts.add(result.getJSONObject(i).getString(""));
                pm_shifts.add(result.getJSONObject(i).getString(""));
                days.add(result.getJSONObject(i).getString(""));
                //---------------------------------------------------------
            }catch(Exception e){
                Log.d("SetSched", e.getMessage());
            }
        }
        //--------------------------------------------------------------------


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Adapter adapter = new Adapter(am_shifts, pm_shifts, days, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
