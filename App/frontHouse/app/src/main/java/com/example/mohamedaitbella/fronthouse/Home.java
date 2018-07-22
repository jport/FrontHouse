package com.example.mohamedaitbella.fronthouse;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Log.d("Frag", "Starts toolbar");
/*
        if (getIntent().getExtras() == null)
            Log.d("Frag", "DIDN'T WORK");
        else {
            Log.d("Frag", "WORK!");
            Log.d("Frag", getIntent().getExtras().toString());
            Log.d("Frag", getIntent().getExtras().getString("action"));
        }
        */
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
        if(savedInstanceState==null) {
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
            super.onBackPressed();
        }
    }
}
