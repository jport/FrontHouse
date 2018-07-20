package com.example.mohamedaitbella.fronthouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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


    }
}
