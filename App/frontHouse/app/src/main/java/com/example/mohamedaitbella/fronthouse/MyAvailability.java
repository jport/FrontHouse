package com.example.mohamedaitbella.fronthouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

        RecyclerView recyclerView = findViewById(R.id.recyclerview2);
        Adapter2 adapter = new Adapter2();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("Activity2", "Recyclerview");
    }

}
