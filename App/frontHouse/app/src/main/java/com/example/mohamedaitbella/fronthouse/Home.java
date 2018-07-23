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

import org.json.JSONObject;

import java.io.File;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    static final String pref = "DEFAULT";
    static protected String token;
    private DrawerLayout drawer;

    @Override
    protected void onStart() {
        super.onStart();

        if(getIntent().getExtras() == null )
            Log.d("MY_EXTRAS", "There were no extras");
        else
            Log.d("MY_EXTRAS", "CONTAINS:" +getIntent().getExtras().keySet());

        if(getApplicationContext().getSharedPreferences(Home.pref, 0).getInt("userId", -1) < 1){
            Intent intent = new Intent(this, Login.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Log.d("Frag", "Starts toolbar");

        //Toolbar
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("Toolbar_Check:", toolbar.toString());

        drawer=findViewById(R.id.drawer_Layout);
        NavigationView navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(getIntent().hasExtra("action")){

            Fragment page;
            int layout;

            Log.d("MY_HOME", "Caught extras");

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
                Toast.makeText(this, "You clicked logout", Toast.LENGTH_SHORT).show();


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

    static protected void authen(String user, String pass, Context context, Activity main, ProgressBar load){

        load.setVisibility(View.VISIBLE);
        Log.d("PROGRESSBAR", "Passed second one");
        Log.d("AUTHEN", "getPath = " + context.getFilesDir().getPath() );
        String filepath = context.getFilesDir().getPath()+ "data/" + context.getPackageName() + "/shared_prefs/"+ Home.pref;
        Log.d("AUTHEN", "Filepath = " + filepath);
        File f = new File(filepath );

        if (f.exists())
            Log.d("AUTHEN", "Preferences_File_Existed");
        else{
            Log.d("AUTHEN", "Preferences_File_Did_Not_Exist");
        }

        SharedPreferences share = context.getSharedPreferences(Home.pref, 0);
        SharedPreferences.Editor editor = share.edit();

        if(user.length()==0 || pass.length()==0){
            editor.putInt("userId", -1);
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


        try {  editor.putInt("userId", result.getInt("EmployeeID")); }
        catch (Exception e){
            Log.d("Debug: Get Emp ID", e.getMessage());
            return;
        }

        if(share.getInt("userId", -1) > 0){
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
}
