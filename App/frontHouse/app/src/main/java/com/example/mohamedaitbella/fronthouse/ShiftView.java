package com.example.mohamedaitbella.fronthouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

public class ShiftView extends AppCompatActivity {

    TextView time, job, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_view);

        time = findViewById(R.id.Time);
        time.setText(getIntent().getStringExtra("Time"));
        job = findViewById(R.id.Title);
        job.setText(getIntent().getStringExtra("job"));
        state = findViewById(R.id.State);
        state.setText(getIntent().getStringExtra("state"));

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
