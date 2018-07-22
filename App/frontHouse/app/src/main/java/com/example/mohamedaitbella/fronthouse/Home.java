package com.example.mohamedaitbella.fronthouse;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class Home extends AppCompatActivity {
    private DrawerLayout drawer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Log.d("Frag", "Starts toolbar");

        if (getIntent().getExtras() == null)
            Log.d("Frag", "DIDN'T WORK");
        else {
            Log.d("Frag", "WORK!");
            Log.d("Frag", getIntent().getExtras().toString());
            Log.d("Frag", getIntent().getExtras().getString("action"));
        }
        //Toolbar
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("Toolbar_Check:", toolbar.toString());

        drawer=findViewById(R.id.drawer_Layout);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Log.d("Frag", "Ends toolbar");
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
