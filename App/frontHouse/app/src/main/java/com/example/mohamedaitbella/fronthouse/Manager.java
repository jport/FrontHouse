package com.example.mohamedaitbella.fronthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

public class Manager extends AppCompatActivity {


    TextView name1, name2, shift1, shift2;
    Button accept, deny;
    String requestID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);


        for(Object i : getIntent().getExtras().keySet())
            Log.d("EXTRAS", i.toString() +" -> " +getIntent().getExtras().get(i.toString()).toString());

        Log.d("EXTRAS", getIntent().getStringExtra("RequestID"));

        name1 = findViewById(R.id.Name_1);
        name1.setText(getIntent().getStringExtra("Employee1"));
        shift1 = findViewById(R.id.Shift_1);
        shift1.setVisibility(View.INVISIBLE);

        if(getIntent().getStringExtra("Shift") != null) {
            shift1.setText(getIntent().getStringExtra("Shift"));
            shift1.setVisibility(View.VISIBLE);
        }

        name2 = findViewById(R.id.Name_2);
        shift2 = findViewById(R.id.Shift_2);
        if(getIntent().getStringExtra("Name2") != null) {
            Log.d("NAME", "HERE");
            name2.setText(getIntent().getStringExtra("Employee2"));
            shift2.setText(getIntent().getStringExtra("Shift2"));
        }
        else{
            Log.d("NAME", "DAMMIT!!");
            name2.setVisibility(View.INVISIBLE);
            shift2.setVisibility(View.INVISIBLE);
        }

        requestID = getIntent().getStringExtra("RequestID");
        //Log.d("MANAGE", "RequestID: " + getIntent().getIntExtra("RequestID", -1) );
        //Log.d("MANAGE", "RequestID: " + requestID );



        accept = findViewById(R.id.button3);
        accept.setText("APPROVE");
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://knightfinder.com/WEBAPI/SetRequestStatus.aspx";

                String payload = "{\"RequestID\": \""+requestID+"\", \"RequestStatus\": \"1\"}";

                APICall apiCall = new APICall();
                apiCall.execute(url, payload);

                FirebaseMessaging.getInstance().unsubscribeFromTopic(requestID);
                Send.respond(1, requestID);
                leave();
            }
        });

        deny = findViewById(R.id.button2);
        deny.setText("DENY");
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://knightfinder.com/WEBAPI/SetRequestStatus.aspx";

                String payload = "{\"RequestID\": \""+requestID+"\", \"RequestStatus\": \"2\"}";

                APICall apiCall = new APICall();
                apiCall.execute(url, payload);

                FirebaseMessaging.getInstance().unsubscribeFromTopic(requestID);
                Send.respond(2, requestID);
                leave();
            }
        });
    }

    private void leave(){
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("action", "Schedule");
        finish();
        startActivity(intent);
    }

    @Override
    public void onStop(){
        super.onStop();
        finish();
    }
}
