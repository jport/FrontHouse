package com.example.mohamedaitbella.fronthouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    static final String pref = "DEFAULT";
    static protected String token;
    static private DrawerLayout drawer;
    static ProgressBar load;
    SharedPreferences share;
    static NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();

        if(getIntent().getExtras() == null )
            Log.d("MY_EXTRAS", "There were no extras");
        else
            Log.d("MY_EXTRAS", "CONTAINS:" +getIntent().getExtras().keySet());


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        share = getApplicationContext().getSharedPreferences(Home.pref, 0);

        // Stops unauthorized access to home
        if(share.getInt("EmployeeID", -1) < 1 || share.getInt("StoreID", -1)< 1){
            Intent intent = new Intent(this, Login.class);
            finish();
            startActivity(intent);
        }

        load = findViewById(R.id.load2);

        Log.d("Frag", "Starts toolbar");
        //Toolbar
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("Toolbar_Check:", toolbar.toString());

        drawer=findViewById(R.id.drawer_Layout);
        navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Redirects if called from notification
        if(getIntent().hasExtra("action")){

            Fragment page;
            int layout;

            switch ((String)getIntent().getExtras().get("action")){

                case "MyAvailability":
                    page = new MyAvailability();
                    layout = R.id.nav_availability;
                    break;
                default:
                    page = new Schedule();
                    layout = R.id.nav_schedule;
                    break;
            }

            // Starting loading
            //startLoading();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    page).commit();
            navigationView.setCheckedItem(layout);
        }
        else if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Schedule()).commit();
            navigationView.setCheckedItem(R.id.nav_schedule);
        }

        Log.d("Frag", "Ends toolbar");
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            //changing to schedule fragment
            case R.id.nav_schedule:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Schedule()).commit();
                break;
            //changing to my availability fragment
            case R.id.nav_availability:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyAvailability()).commit();
                break;
            case R.id.nav_myinfo:
                Toast.makeText(this, "You clicked myinfo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_Settings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_Logout:
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                getApplicationContext().getSharedPreferences(Home.pref, 0).edit().remove("EmployeeID").commit();
                finish();
                startActivity(new Intent(this, Login.class));
                break;
            //

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    // Authorizes sign in to hide results from login page
    static protected void authen(String user, String pass, Context context, Activity main, ProgressBar load){

        load.setVisibility(View.VISIBLE);

        SharedPreferences share = context.getSharedPreferences(Home.pref, 0);
        SharedPreferences.Editor editor = share.edit();

        if(user.length()==0 || pass.length()==0){
            editor.putInt("EmployeeID", -1);
            editor.commit();
            return;
        }

        Log.d("AUTEHN", "Started login");
        String url = "http://knightfinder.com/WEBAPI/Login.aspx";

        APICall apiCall = new APICall();
        JSONObject result;

        try {
            result = apiCall.execute(url, "{login:\""+user+"\",password:\""+pass+"\"}").get().getJSONObject(0);
            Log.d("AUTHEN", "Result = " + result);
        }catch (Exception e){
            Log.d("Debug: API_Call", e.getMessage());
            return;
        }


        try {  editor.putInt("EmployeeID", result.getInt("EmployeeID")); }
        catch (Exception e){
            Log.d("Debug: Get Emp ID", e.getMessage());
            return;
        }

        if(share.getInt("EmployeeID", -1) > 0){
            try {
                editor.putInt("StoreID", result.getInt("StoreID"));
            }catch(Exception e){
                Log.d("SHARE", e.getMessage());
            }
            final Task task =
                    (FirebaseInstanceId.getInstance().getInstanceId());

            task.addOnSuccessListener(main, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    InstanceIdResult result = (InstanceIdResult)task.getResult();
                    token = result.getToken();
                    Log.d("GET_ID", "" +token );
                }
            });
        }

        Log.d("EDITOR:", "Commit returned - " + editor.commit());

    }

    // Stops loading image after page has been loaded
    public static void stopLoading(){

        navigationView.post(new Runnable() {
            @Override
            public void run() {
                Log.d("LOAD", "CLOSING, IS OPEN?: " + (load.getVisibility() == View.VISIBLE));
                load.setVisibility(View.GONE);
            }
        });

    }

    // Starts loading, assumes view given is of Home.
    public static void startLoading(){
        navigationView.post(new Runnable() {
            @Override
            public void run() {
                Log.d("LOAD", "Got here");
                load.setVisibility(View.VISIBLE);
            }
        });
    }

    // Takes in a time(XX:XX - XX:XX) and returns results of conversion attempt,
    // am shift =  shif
    static public String[] Time(JSONObject data, int i) {

        String[] shifts = {"",""};

        try {
            String am_test = data.getString("StartTime").substring(11);
            String pm_test = data.getString("EndTime").substring(11);
            String[] split_array_am = am_test.split(":", 2);
            String str_am = split_array_am[0] + split_array_am[1].substring(0,2);
            int time = Integer.parseInt(str_am);

            Date date = new Date();
            try {
                date = new SimpleDateFormat("hhmm").parse(String.format("%04d", time));
            } catch (Exception e) {
                Log.d("TIME", e.getMessage());
            }

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

            if (time < 1200) {
                shifts[0] = sdf.format(date) + " - 12:00";
                Log.d("AMShift", shifts[0]);
            }
            else {
                int converted = time - 1200;
                shifts[1] = "12:00 - " + pm_test.substring(0,5);
                Log.d("PMShift", shifts[1]);
            }
        }catch(Exception e){
            Log.d("TIME-ERROR", e.getMessage());
        }

        Log.d("TIME", "Returns:" + Arrays.toString(shifts));
        return shifts;
    }
}
