package com.example.mohamedaitbella.fronthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    JSONArray result;
    APICall apiCall = new APICall();
    String url = "http://knightfinder.com/WEBAPI/GetSchedule.aspx";
    int userId = -1;

    @Override
    protected void onStart() {

        /*if(userId < 1 && getIntent().getIntExtra("userId", 0) < 1){
            Log.d("STOP", "Here");
            startActivity(new Intent(this, MainActivity.class));
        }
        */

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Activity2", "Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        userId = getIntent().getIntExtra("userId", 0);

        String payload = "";



        ArrayList<String> am_shifts = new ArrayList<>();
        ArrayList<String> pm_shifts = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();     // In case dates are passed

        fakeNews(am_shifts, pm_shifts);
        /*
        try {
            result = apiCall.execute(url, payload).get();
        }catch (Exception e){
            Log.d("SchedCall", e.getMessage());
        }

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
        */

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        Adapter adapter = new Adapter(am_shifts, pm_shifts, days, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("Activity2", "Recyclerview. List size = "+ am_shifts.size());

    }

    private void fakeNews(ArrayList<String> am_shifts, ArrayList<String> pm_shifts){

        Log.d("fakeNews", "Started");
        for(int i = 0; i < 7; i++){
            am_shifts.add("9:30 am");
            pm_shifts.add("3:30 pm");

        }
        Log.d("fakeNews", "Finished");
    }

}
