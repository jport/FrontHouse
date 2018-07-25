package com.example.mohamedaitbella.fronthouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Schedule extends Fragment {

    String url = "http://knightfinder.com/WEBAPI/GetSchedule.aspx";
    SharedPreferences share;

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

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        share = getContext().getApplicationContext().getSharedPreferences(Home.pref, 0);

        Home.startLoading();

        String payload = "{StoreID : \""+ share.getInt("StoreID", 0) +"\"}";

        APICall apicall = new APICall();
        JSONObject result = new JSONObject();
        try {
            result = apicall.execute(url, payload).get().getJSONObject(0);
            Log.d("SCHEDULE", result.toString());
        }catch(Exception e){
            Log.d("Schedule", e.getMessage());

        }

        Home.stopLoading();

        Gson gson = new Gson();

        Shift[] json = gson.fromJson(result.toString(), GetSchedule.class).schedules;

        Log.d("JSON", Arrays.toString(json));

        String am_shifts[] = new String[7];
        String pm_shifts[] = new String[7];
        String days[] = new String[7];     // In case dates are passed

        //fakeNews(am_shifts, pm_shifts);


        //--------------- Add shifts here -----------------------------------
        for(int i = 0; i < json.length; i++){

            String shifts[];
            try {
                // For now, use '0' for Schedule and '1' for MyAvailability
                shifts = Home.Time( new JSONObject(gson.toJson(json[i]) ), 0);
                am_shifts[i] = shifts[0];
                pm_shifts[i] = shifts[1];
                days[i] = json[i].StartTime.substring(5,10);
            }catch (Exception e){
                Log.d("SCHEDULE_catch", e.getMessage());
            }
        }
        //--------------------------------------------------------------------


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        Adapter adapter = new Adapter(am_shifts, pm_shifts, days, getContext());
        if(adapter==null){
            Log.d("Activity2", "null = ");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("Activity2", "PAST LAYOUT_MANAGER");
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("Activity2", "Recyclerview. List size = "+ am_shifts.length);


        try {
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter("Schedule", Intent.CATEGORY_DEFAULT));
        }catch(IntentFilter.MalformedMimeTypeException e){
            Log.d("IntentFilter", e.getMessage());
        }

        return view;

    }

    private void fakeNews(String[] am_shifts, String[] pm_shifts){

        Log.d("fakeNews", "Started");
        for(int i = 0; i < 7; i++){
            am_shifts[i] = "9:30 am";
            pm_shifts[i] = "3:30 pm";

        }
        Log.d("fakeNews", "Finished");
    }

}

