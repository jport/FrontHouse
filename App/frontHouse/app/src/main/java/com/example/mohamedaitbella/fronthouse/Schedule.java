package com.example.mohamedaitbella.fronthouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.ArrayList;

public class Schedule extends Fragment {
    JSONArray result;
    APICall apiCall = new APICall();
    String url = "http://knightfinder.com/WEBAPI/GetSchedule.aspx";
    int userId = -1;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Schedule", "Received Notification");
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule, container, false);

        Log.d("Activity2", "Started");

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        Adapter adapter = new Adapter(am_shifts, pm_shifts, days, getContext());
        if(adapter==null){
            Log.d("Activity2", "null = ");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("Activity2", "PAST LAYOUT_MANAGER");
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("Activity2", "Recyclerview. List size = "+ am_shifts.size());


        try {
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter("Schedule", Intent.CATEGORY_DEFAULT));
        }catch(IntentFilter.MalformedMimeTypeException e){
            Log.d("IntentFilter", e.getMessage());
        }


        return view;

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

