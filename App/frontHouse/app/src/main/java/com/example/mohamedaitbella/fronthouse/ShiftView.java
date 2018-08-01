package com.example.mohamedaitbella.fronthouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button drop;
    Adapter3 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_view);

        time = findViewById(R.id.Time);
        state = findViewById(R.id.State);
        name = findViewById(R.id.MyShift);
        name.setText(getSharedPreferences(Home.pref,0).getString("Name", "DIDN'T RECEIVE A NAME"));
        drop = findViewById(R.id.Drop);

        // Open AlertDialog on press. Pressing 'YES' send API and Firebase call
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShiftView.this);
                AlertDialog dialog;
                builder.setMessage("Are you sure you would like to DROP this shift?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences share = getApplicationContext().getSharedPreferences(Home.pref,0);

                        int scheID = getIntent().getIntExtra("MyShiftID", -1);

                        // API call
                        APICall apicall = new APICall();
                        String url = "http://knightfinder.com/WEBAPI/SendRequest.aspx",
                                payload = "{\"StoreID\":\""+ share.getInt("StoreID", -1) + "\", " +
                                        "\"EmployeeID\": \""+share.getInt("EmployeeID", -1) + "\", " +
                                        "\"RequestType\":\"1\", "+
                                        "\"ScheduleID1\": \"" +scheID+ "\", " +
                                        "\"RequestText\":\"\"}";
                        apicall.execute(url, payload);

                        int myShiftID = getIntent().getIntExtra("MyShiftID", -1);

                        int requestID = -1;
                        try {
                            Log.d("MILEY", apicall.get().toString());
                            requestID = apicall.get().getJSONObject(0).getInt("RequestID");
                        }catch(Exception e){
                            Log.d("DROP_ERROR", e.getMessage());
                        }

                        //Log.d("MANAGE", "requestID = " + requestID);

                        // Firebase request(new function needed): sendRequest(Employee1, Employee2, Shift, ScheduleID).
                        String accepter = share.getString("Name", "DefaultVaue"), shift = time.getText().toString();
                        Send.drop(accepter, shift, requestID, share.getInt("StoreID", -1));

                        FirebaseMessaging.getInstance().subscribeToTopic(Integer.toString(requestID) );
                        drop.setEnabled(false);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });

        // Grab your shift
        String yours = getIntent().getStringExtra("MyShift");

        // Disable 'drop' conditions
        if(getIntent().getIntExtra("ShiftStatus",-1) == 0 || yours.equals(""))
            drop.setEnabled(false);

        // Picking 'state' value
        if(yours.length()>11) {
            state.setText("AM/\nPM");
            yours = yours.substring(0,6) + yours.substring(17);
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

        // Picking whether or not to show a shift from 'Others'
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
        adapter = new Adapter3(send, state.getText().toString(), this, getIntent().getIntExtra("MyShiftID", -1), yours);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
