package com.example.mohamedaitbella.fronthouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONObject;

import java.util.ArrayList;

// Receives current Employee's shift and other employees of that day
// as json in extras of intent
public class ShiftView extends AppCompatActivity {

    TextView time, state, name;
    RecyclerView recyclerView;
    Adapter3 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_view);

        time = findViewById(R.id.Time);
        state = findViewById(R.id.State);
        name = findViewById(R.id.MyShift);

        name.setText(getSharedPreferences(Home.pref,0).getString("Name", "DIDN'T RECEIVE A NAME"));

        // Grab your shift
        String yours = getIntent().getStringExtra("MyShift");

        if(yours.length()>11) {
            state.setText("AM/\nPM");
            yours = yours.substring(0,6) + yours.substring(12);
        }
        else {
            state.setText(getIntent().getStringExtra("State"));
        }

        time.setText(yours);

        // Deserialize everyone else's shift; 'temp' entire list of shifts,
        // 'everyone' (arraylist)list of shifts at same time
        Gson gson = new Gson();
        Shift[] temp = gson.fromJson(getIntent().getStringExtra("Others"), Shift[].class);
        ArrayList<Shift> everyone = new ArrayList<>();


        for(int i = 0; i < temp.length; i++) {
            try {
                String shifts[] = Home.Time(new JSONObject(gson.toJson(temp[i])), 0);
                Log.d("ShiftView", "yours: " + yours + ", theirs: " +shifts[0] + ", " + shifts[1]);
                if (yours.equals("") || (!shifts[0].equals(yours) && !shifts[1].equals(yours))) {
                    Log.d("IFs", "Came in");
                    if (shifts[0].length() == 0 || shifts[1].length() == 0 || !(shifts[0].substring(0, 6) + shifts[1].substring(12)).equals(yours))
                        everyone.add(temp[i]);
                }
            }catch(Exception e){
                Log.d("ShiftView_ERROR", e.getMessage());
            }
        }

        recyclerView = findViewById(R.id.recyclerview3);
        Shift[] send = new Shift[everyone.size()];
        for(int i = 0; i < send.length; i++)
            send[i] = everyone.get(i);
        adapter = new Adapter3(send, state.getText().toString(), this, getIntent().getIntExtra("MyShiftID", -1));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseMessaging.getInstance().subscribeToTopic("Bernardin");

        getIncomingIntentAndSet();


    }

    private void getIncomingIntentAndSet(){

        Intent intent = getIntent();
        String str = intent.getStringExtra("am_shifts");

        if(str=="am_shifts"){
            TextView Time = findViewById(R.id.Time);
            Time.setText(str);
        }
    }

}
