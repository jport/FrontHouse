package com.example.mohamedaitbella.fronthouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;

import java.util.ArrayList;

public class MyAvailability extends AppCompatActivity {

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_availability);


        submit = findViewById(R.id.submit);

        submit.setEnabled(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("submitAvail", "Got here");
            }
        });

        // -----------------------------------------------------------------------
        String url = "http://knightfinder.com/WEBAPI/GetAvailability.aspx";

        APICall apicall = new APICall();
        JSONArray json = null;

        try {
            json = apicall.execute(url, "{EmployeeID:\"3\"}").get();
            Log.d("GET_AVAIL-", "Result: " + json.toString());
        }
        catch (Exception e){
            Log.d("GET AVAILABILITY", e.getMessage());
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
        Adapter2 adapter = new Adapter2(json);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("Activity2", "Recyclerview");


    }


}
